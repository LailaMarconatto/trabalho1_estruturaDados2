package tabelasHash;

public class HashTable2 {
	private class Entrada {
		String chave;
		String valor;

		public Entrada(String chave, String valor) {
			this.chave = chave;
			this.valor = valor;
		}
	}

	private final double FATOR_DE_CARGA = 0.7;

	private Entrada[] array;
	private int tamanhoAtual;

	// construtor
	public HashTable2(int capacidadeInicial) {
		this.array = new Entrada[capacidadeInicial];
		this.tamanhoAtual = 0;
	}

	private int hash(String chave) {
		int hashCode = 0;
		for (byte b : chave.getBytes())
			hashCode += b;
		return hashCode % array.length;
	}

	public void inserir(String chave, String valor) {
		if ((double) tamanhoAtual / array.length >= FATOR_DE_CARGA) {
			System.out.println("FAAAHtor de carga excedido e redimensionamento ativo");
			redimensionar();
		}
		int indiceBase = hash(chave);
		int indice = indiceBase;
		int colisao = 0;

		while (array[indice] != null && !array[indice].chave.equals(chave)) {
			colisao++;
			System.out.println("Colisão. Tratamento ativo");
			System.out.println("Tentativa: " + colisao);
			indice = (indiceBase + colisao) % array.length;
		}
		if (array[indice] == null)
			tamanhoAtual++;
		array[indice] = new Entrada(chave, valor);
	}

	public String recuperar(String chave) {
		int indiceBase = hash(chave);
		int indice = indiceBase;
		int colisao = 0;

		while (array[indice] != null) {
			if (array[indice].chave.equals(chave))
				return array[indice].valor;
			colisao++;
			indice = (indiceBase + colisao) % array.length;
		}
		return "Chave não encontrada";
	}

	private void redimensionar() {
		Entrada[] antigoArray = array;
		array = new Entrada[antigoArray.length * 2];
		tamanhoAtual = 0;

		System.out.println("-> Nova capaicidade: " + array.length);
		for (Entrada entrada : antigoArray) {
			if (entrada != null) {
				inserir(entrada.chave, entrada.valor);
			}
		}
	}

	public void mostrarTabela() {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == null)
				System.out.println("Índice " + i + " ->[VAZIO] ");
			else
				System.out.println("Índice "+ i + "-> {CHAVE:" + array[i].chave + ", VALOR: " + array[i].valor + " }");
		}
		System.out.println("----------------------0-------------0------------------");
	}

}