// Adaptada da lista e do NodoDuplo das aulas; insercao ao final.
final class ListaDuplamenteEncadeada implements Estrutura {

	private static final class NodoDuplo {

		int dado;
		NodoDuplo ant, prox;

		NodoDuplo(int dado) {
			this.dado = dado;
		}
	}

	private NodoDuplo inicio, fim;
	private int quantidade;

	public int tamanho() {
		return quantidade;
	}

	private NodoDuplo localizar(int valor) {
		for (NodoDuplo atual = inicio; atual != null; atual = atual.prox)
			if (atual.dado == valor)
				return atual;
		return null;
	}

	public boolean buscar(int valor) {
		return localizar(valor) != null;
	}

	public boolean inserir(int valor) {
		// Conferencia faz parte do custo de insercao, conforme o enunciado.
		if (buscar(valor))
			return false;
		NodoDuplo novo = new NodoDuplo(valor);

		novo.ant = fim;
		if (fim == null)
			inicio = novo;
		else
			fim.prox = novo;
		fim = novo;
		quantidade++;
		return true;
	}

	public boolean remover(int valor) {
		NodoDuplo no = localizar(valor);

		if (no == null)
			return false;
		if (no.ant == null)
			inicio = no.prox;
		else
			no.ant.prox = no.prox;
		if (no.prox == null)
			fim = no.ant;
		else
			no.prox.ant = no.ant;
		quantidade--;
		return true;
	}

	public int contarIntervalo(int minimo, int maximo) {
		int total = 0;

		for (NodoDuplo no = inicio; no != null; no = no.prox)
			if (no.dado >= minimo && no.dado <= maximo)
				total++;
		return total;
	}
}
