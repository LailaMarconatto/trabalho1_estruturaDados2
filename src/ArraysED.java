// Arrays de capacidade fixa: o experimento reserva n + 10%.
final class ArrayLinear implements Estrutura {

	private final int[] dados;
	private int quantidade;

	ArrayLinear(int capacidade) {
		dados = new int[capacidade];
	}

	public int tamanho() {
		return quantidade;
	}

	private int indice(int valor) {
		for (int i = 0; i < quantidade; i++)
			if (dados[i] == valor)
				return i;
		return -1;
	}

	public boolean buscar(int valor) {
		return indice(valor) >= 0;
	}

	public boolean inserir(int valor) {
		if (buscar(valor))
			return false;
		if (quantidade == dados.length)
			throw new IllegalStateException("Array cheio");
		dados[quantidade++] = valor;
		return true;
	}

	public boolean remover(int valor) {
		int i = indice(valor);

		if (i < 0)
			return false;
		// Sem ordem a preservar: substitui pelo ultimo elemento.
		dados[i] = dados[--quantidade];
		return true;
	}

	public int contarIntervalo(int minimo, int maximo) {
		int total = 0;

		for (int i = 0; i < quantidade; i++)
			if (dados[i] >= minimo && dados[i] <= maximo)
				total++;
		return total;
	}
}

final class ArrayOrdenado implements Estrutura {

	private final int[] dados;
	private int quantidade;

	ArrayOrdenado(int capacidade) {
		dados = new int[capacidade];
	}

	public int tamanho() {
		return quantidade;
	}

	// Primeira posicao com valor maior ou igual ao procurado.
	private int limiteInferior(int valor) {
		int inicio = 0, fim = quantidade;

		while (inicio < fim) {
			int meio = inicio + (fim - inicio) / 2;

			if (dados[meio] < valor)
				inicio = meio + 1;
			else
				fim = meio;
		}
		return inicio;
	}

	public boolean buscar(int valor) {
		int i = limiteInferior(valor);

		return i < quantidade && dados[i] == valor;
	}

	public boolean inserir(int valor) {
		int i = limiteInferior(valor);

		if (i < quantidade && dados[i] == valor)
			return false;
		if (quantidade == dados.length)
			throw new IllegalStateException("Array cheio");
		System.arraycopy(dados, i, dados, i + 1, quantidade - i);
		dados[i] = valor;
		quantidade++;
		return true;
	}

	public boolean remover(int valor) {
		int i = limiteInferior(valor);

		if (i == quantidade || dados[i] != valor)
			return false;
		System.arraycopy(dados, i + 1, dados, i, quantidade - i - 1);
		quantidade--;
		return true;
	}

	public int contarIntervalo(int minimo, int maximo) {
		if (minimo > maximo)
			return 0;
		int inicio = limiteInferior(minimo);
		int fim = limiteInferior(maximo);

		if (fim < quantidade && dados[fim] == maximo)
			fim++;
		return fim - inicio;
	}
}
