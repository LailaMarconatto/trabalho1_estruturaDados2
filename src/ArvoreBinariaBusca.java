// BST das aulas adaptada para percursos iterativos, sem balanceamento.
final class ArvoreBinariaBusca implements Estrutura {

	private static final class No {

		int valor;
		No esq, dir;

		No(int valor) {
			this.valor = valor;
		}
	}

	private No raiz;
	private int quantidade;

	public int tamanho() {
		return quantidade;
	}

	public boolean buscar(int valor) {
		No atual = raiz;

		while (atual != null) {
			if (atual.valor == valor)
				return true;
			atual = valor < atual.valor ? atual.esq : atual.dir;
		}
		return false;
	}

	public boolean inserir(int valor) {
		No pai = null, atual = raiz;

		while (atual != null) {
			if (valor == atual.valor)
				return false;
			pai = atual;
			atual = valor < atual.valor ? atual.esq : atual.dir;
		}
		No novo = new No(valor);

		if (pai == null)
			raiz = novo;
		else if (valor < pai.valor)
			pai.esq = novo;
		else
			pai.dir = novo;
		quantidade++;
		return true;
	}

	public boolean remover(int valor) {
		No pai = null, atual = raiz;

		while (atual != null && atual.valor != valor) {
			pai = atual;
			atual = valor < atual.valor ? atual.esq : atual.dir;
		}
		if (atual == null)
			return false;
		if (atual.esq != null && atual.dir != null) {
			No paiSucessor = atual, sucessor = atual.dir;

			while (sucessor.esq != null) {
				paiSucessor = sucessor;
				sucessor = sucessor.esq;
			}
			atual.valor = sucessor.valor;
			pai = paiSucessor;
			atual = sucessor;
		}
		No filho = atual.esq != null ? atual.esq : atual.dir;

		if (pai == null)
			raiz = filho;
		else if (pai.esq == atual)
			pai.esq = filho;
		else
			pai.dir = filho;
		quantidade--;
		return true;
	}

	// Pilha propria, com poda pela propriedade de ordenacao da BST.
	public int contarIntervalo(int minimo, int maximo) {
		if (minimo > maximo || raiz == null)
			return 0;
		No[] pilha = new No[32];
		int topo = 0, total = 0;

		pilha[topo++] = raiz;
		while (topo > 0) {
			No atual = pilha[--topo];

			if (topo + 2 > pilha.length)
				pilha = java.util.Arrays.copyOf(pilha, pilha.length * 2);
			if (atual.valor >= minimo && atual.valor <= maximo)
				total++;
			if (atual.esq != null && atual.valor > minimo)
				pilha[topo++] = atual.esq;
			if (atual.dir != null && atual.valor < maximo)
				pilha[topo++] = atual.dir;
		}
		return total;
	}
}
