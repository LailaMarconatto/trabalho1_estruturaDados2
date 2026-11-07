import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

// Gera os mesmos lotes para todas as estruturas e mede somente as operacoes.
public final class Experimento {

	static final String[] NOMES = { "array_linear", "array_ordenado", "lista_dupla", "hash", "bst", "avl",
			"rubro_negra" };
	private static final String[] TESTES = { "construcao", "busca_presente", "busca_ausente", "insercao", "remocao",
			"intervalo" };
	private static final String[] ORDENS = { "crescente", "decrescente", "aleatoria" };
	// Resultado observavel, consumido apos parar o cronometro.
	private static volatile long resultadoConsumido;

	static Estrutura criar(int tipo, int n) {
		switch (tipo) {
		case 0:
			return new ArrayLinear(n + n / 10);
		case 1:
			return new ArrayOrdenado(n + n / 10);
		case 2:
			return new ListaDuplamenteEncadeada();
		case 3:
			return new TabelaHash();
		case 4:
			return new ArvoreBinariaBusca();
		case 5:
			return new ArvoreAVL();
		case 6:
			return new ArvoreRubroNegra();
		default:
			throw new IllegalArgumentException("Estrutura desconhecida");
		}
	}

	static void embaralhar(int[] valores, Random random) {
		for (int i = valores.length - 1; i > 0; i--) {
			int j = random.nextInt(i + 1), aux = valores[i];

			valores[i] = valores[j];
			valores[j] = aux;
		}
	}

	private static final class Dados {

		final int n;
		final int[] entrada, presentes = new int[2000], ausentes = new int[2000];
		final int[] inserir, remover, minimos = new int[200];
		final long esperadoIntervalos;

		Dados(int n, int ordem) {
			this.n = n;
			entrada = new int[n];
			inserir = new int[n / 10];
			remover = new int[n / 10];
			for (int i = 0; i < n; i++)
				entrada[i] = 2 * (i + 1);
			if (ordem == 1) {
				for (int i = 0; i < n / 2; i++) {
					int aux = entrada[i];

					entrada[i] = entrada[n - 1 - i];
					entrada[n - 1 - i] = aux;
				}
			} else if (ordem == 2)
				embaralhar(entrada, new Random(42));
			Random random = new Random(42);

			for (int i = 0; i < presentes.length; i++)
				presentes[i] = 2 * (random.nextInt(n) + 1);
			for (int i = 0; i < ausentes.length; i++)
				ausentes[i] = 2 * random.nextInt(n) + 1;
			int[] impares = new int[n], pares = new int[n];

			for (int i = 0; i < n; i++) {
				impares[i] = 2 * i + 1;
				pares[i] = 2 * (i + 1);
			}
			embaralhar(impares, random);
			embaralhar(pares, random);
			System.arraycopy(impares, 0, inserir, 0, inserir.length);
			System.arraycopy(pares, 0, remover, 0, remover.length);
			long total = 0;

			for (int i = 0; i < minimos.length; i++) {
				minimos[i] = 1 + random.nextInt(2 * n - 100);
				total += (minimos[i] + 100) / 2 - (minimos[i] - 1) / 2;
			}
			esperadoIntervalos = total;
		}

		int operacoes(int teste) {
			switch (teste) {
			case 0:
				return n;
			case 1:
			case 2:
				return 2000;
			case 3:
			case 4:
				return n / 10;
			default:
				return 200;
			}
		}
	}

	private static Estrutura construir(int tipo, Dados dados) {
		Estrutura estrutura = criar(tipo, dados.n);

		for (int valor : dados.entrada)
			estrutura.inserir(valor);
		return estrutura;
	}

	private static long executar(Estrutura estrutura, Dados dados, int teste) {
		long total = 0;

		switch (teste) {
		case 0:
			for (int valor : dados.entrada)
				if (estrutura.inserir(valor))
					total++;
			break;
		case 1:
			for (int valor : dados.presentes)
				if (estrutura.buscar(valor))
					total++;
			break;
		case 2:
			for (int valor : dados.ausentes)
				if (estrutura.buscar(valor))
					total++;
			break;
		case 3:
			for (int valor : dados.inserir)
				if (estrutura.inserir(valor))
					total++;
			break;
		case 4:
			for (int valor : dados.remover)
				if (estrutura.remover(valor))
					total++;
			break;
		case 5:
			for (int minimo : dados.minimos)
				total += estrutura.contarIntervalo(minimo, minimo + 100);
			break;
		default:
			throw new IllegalArgumentException();
		}
		return total;
	}

	private static void conferir(Estrutura estrutura, Dados dados, int teste, long resultado) {
		long esperado = teste == 2 ? 0 : teste == 5 ? dados.esperadoIntervalos : dados.operacoes(teste);
		int tamanho = dados.n + (teste == 3 ? dados.n / 10 : teste == 4 ? -dados.n / 10 : 0);

		if (resultado != esperado || estrutura.tamanho() != tamanho)
			throw new AssertionError("Falha no teste " + TESTES[teste] + ": " + resultado + "/" + esperado);
		// A bateria Testes confere conteudo exato contra boolean[]. Aqui confere
		// todas as alteracoes, tamanho e um intervalo total, fora da medicao.
		if (estrutura.contarIntervalo(1, 2 * dados.n) != tamanho)
			throw new AssertionError("Contagem total");
		if (teste == 3)
			for (int valor : dados.inserir)
				if (!estrutura.buscar(valor))
					throw new AssertionError("Insercao perdida");
		if (teste == 4)
			for (int valor : dados.remover)
				if (estrutura.buscar(valor))
					throw new AssertionError("Remocao perdida");
		if (estrutura instanceof ArvoreAVL)
			((ArvoreAVL) estrutura).validar();
		if (estrutura instanceof ArvoreRubroNegra)
			((ArvoreRubroNegra) estrutura).validar();
		resultadoConsumido = resultado;
	}

	public static void main(String[] args) throws Exception {
		boolean rapido = args.length > 0 && args[0].equals("--rapido");

		Path saida = Path.of(rapido ? "resultados-validacao" : "resultados");
		Files.createDirectories(saida);
		String ambiente = "Java: " + System.getProperty("java.version") + "\nVM: " + System.getProperty("java.vm.name")
				+ "\nSistema: " + System.getProperty("os.name") + " " + System.getProperty("os.arch")
				+ "\nProcessador (Windows): " + System.getenv("PROCESSOR_IDENTIFIER")
				+ "\nProcessadores logicos: " + Runtime.getRuntime().availableProcessors()
				+ "\nLimite de heap JVM (bytes, nao e a RAM fisica): " + Runtime.getRuntime().maxMemory()
				+ "\nRAM fisica: preencher no relatorio\nSemente: 42\nModo: "
				+ (rapido ? "validacao reduzida" : "completo") + "\n";
		Files.writeString(saida.resolve("ambiente.txt"), ambiente, StandardCharsets.UTF_8);
		try (BufferedWriter bruto = Files.newBufferedWriter(saida.resolve("resultados_brutos.csv"),
				StandardCharsets.UTF_8);
				BufferedWriter resumo = Files.newBufferedWriter(saida.resolve("resumo.csv"), StandardCharsets.UTF_8)) {
			bruto.write("tamanho;ordem;estrutura;teste;repeticao;operacoes;tempo_total_ns\n");
			resumo.write("tamanho;ordem;estrutura;teste;mediana_total_ms;mediana_por_operacao_ns\n");
			for (int n : rapido ? new int[] { 200 } : new int[] { 5000, 10000, 100000 }) {
				for (int ordem = 0; ordem < ORDENS.length; ordem++) {
					Dados dados = new Dados(n, ordem);

					for (int tipo = 0; tipo < NOMES.length; tipo++) {
						for (int teste = 0; teste < TESTES.length; teste++) {
							long[] tempos = new long[5];
							String prefixo = n + ";" + ORDENS[ordem] + ";" + NOMES[tipo] + ";" + TESTES[teste];

							for (int repeticao = 0; repeticao <= 5; repeticao++) {
								Estrutura estrutura = teste == 0 ? null : construir(tipo, dados);

								if (estrutura != null && estrutura.tamanho() != n)
									throw new AssertionError("Estado inicial");
								long inicio = System.nanoTime();

								if (teste == 0)
									estrutura = criar(tipo, n);
								long resultado = executar(estrutura, dados, teste);
								long tempo = System.nanoTime() - inicio;

								conferir(estrutura, dados, teste, resultado);
								if (repeticao > 0) {
									tempos[repeticao - 1] = tempo;
									bruto.write(prefixo + ";" + repeticao + ";" + dados.operacoes(teste) + ";" + tempo
											+ "\n");
								}
							}
							Arrays.sort(tempos);
							resumo.write(String.format(Locale.ROOT, "%s;%.6f;%.6f%n", prefixo, tempos[2] / 1_000_000.0,
									(double) tempos[2] / dados.operacoes(teste)));
							System.out.println(prefixo + ";conferencia=OK;resultado=" + resultadoConsumido);
						}
						bruto.flush();
						resumo.flush();
					}
				}
			}
		}
		System.out.println("Concluido: " + saida.toAbsolutePath());
	}
}
