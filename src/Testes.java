import java.util.Random;

// Testes deterministas contra um conjunto de referencia implementado com boolean[].
public final class Testes {

	private static void exigir(boolean condicao, String mensagem) {
		if (!condicao)
			throw new AssertionError(mensagem);
	}

	private static void validar(Estrutura estrutura) {
		if (estrutura instanceof ArvoreAVL)
			((ArvoreAVL) estrutura).validar();
		if (estrutura instanceof ArvoreRubroNegra)
			((ArvoreRubroNegra) estrutura).validar();
	}

	public static void main(String[] args) {
		for (int tipo = 0; tipo < Experimento.NOMES.length; tipo++) {
			for (int semente = 0; semente < 10; semente++) {
				Estrutura estrutura = Experimento.criar(tipo, 1000);
				boolean[] referencia = new boolean[401];
				Random random = new Random(semente);
				int quantidade = 0;

				exigir(!estrutura.remover(0) && !estrutura.buscar(0), "Estrutura vazia");
				for (int passo = 0; passo < 5000; passo++) {
					int indice = random.nextInt(referencia.length), valor = indice - 200;
					int operacao = random.nextInt(3);

					if (operacao == 0) {
						exigir(estrutura.inserir(valor) == !referencia[indice], "Insercao/duplicata");
						if (!referencia[indice])
							quantidade++;
						referencia[indice] = true;
					} else if (operacao == 1) {
						exigir(estrutura.remover(valor) == referencia[indice], "Remocao");
						if (referencia[indice])
							quantidade--;
						referencia[indice] = false;
					} else
						exigir(estrutura.buscar(valor) == referencia[indice], "Busca");
					exigir(estrutura.tamanho() == quantidade, "Tamanho");
					validar(estrutura);
					if (passo % 50 == 0) {
						int minimo = random.nextInt(501) - 250, maximo = random.nextInt(501) - 250;
						int esperado = 0;

						for (int i = 0; i < referencia.length; i++)
							if (referencia[i] && i - 200 >= minimo && i - 200 <= maximo)
								esperado++;
						exigir(estrutura.contarIntervalo(minimo, maximo) == esperado, "Intervalo");
						for (int i = 0; i < referencia.length; i++)
							exigir(estrutura.buscar(i - 200) == referencia[i], "Conteudo");
					}
				}
				for (int i = 0; i < referencia.length; i++) {
					exigir(estrutura.remover(i - 200) == referencia[i], "Esvaziamento");
					validar(estrutura);
				}
				exigir(estrutura.tamanho() == 0, "Tamanho final");
				exigir(estrutura.inserir(Integer.MIN_VALUE), "Minimo inteiro");
				exigir(estrutura.inserir(Integer.MAX_VALUE), "Maximo inteiro");
				exigir(estrutura.contarIntervalo(Integer.MIN_VALUE, Integer.MAX_VALUE) == 2, "Limites inteiros");
				validar(estrutura);
			}
			// Ordens que degeneram BST e exigem rotacoes das arvores balanceadas.
			for (int ordem = 0; ordem < 2; ordem++) {
				Estrutura estrutura = Experimento.criar(tipo, 1000);

				for (int i = 0; i < 1000; i++)
					estrutura.inserir(ordem == 0 ? i : 999 - i);
				for (int i = 0; i < 1000; i++) {
					exigir(estrutura.remover(i), "Remocao ordenada");
					validar(estrutura);
				}
				exigir(estrutura.tamanho() == 0, "Esvaziamento ordenado");
			}
			System.out.println("OK: " + Experimento.NOMES[tipo]);
		}
		System.out.println("Todas as sete estruturas passaram nos testes.");
	}
}
