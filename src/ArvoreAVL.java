// AVL das aulas: alturas simplificadas para uma altura por no.
final class ArvoreAVL implements Estrutura {

	private static final class No {

		int valor, altura = 1;
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

	private int altura(No no) {
		return no == null ? 0 : no.altura;
	}

	private void atualizar(No no) {
		no.altura = 1 + Math.max(altura(no.esq), altura(no.dir));
	}

	private No rotacaoEsquerda(No no) {
		No nova = no.dir;

		no.dir = nova.esq;
		nova.esq = no;
		atualizar(no);
		atualizar(nova);
		return nova;
	}

	private No rotacaoDireita(No no) {
		No nova = no.esq;

		no.esq = nova.dir;
		nova.dir = no;
		atualizar(no);
		atualizar(nova);
		return nova;
	}

	private No balancear(No no) {
		if (no == null)
			return null;
		atualizar(no);
		int fator = altura(no.dir) - altura(no.esq);

		if (fator > 1) {
			if (altura(no.dir.esq) > altura(no.dir.dir))
				no.dir = rotacaoDireita(no.dir);
			return rotacaoEsquerda(no);
		}
		if (fator < -1) {
			if (altura(no.esq.dir) > altura(no.esq.esq))
				no.esq = rotacaoEsquerda(no.esq);
			return rotacaoDireita(no);
		}
		return no;
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
		int antes = quantidade;

		raiz = inserir(raiz, valor);
		return quantidade != antes;
	}

	private No inserir(No no, int valor) {
		if (no == null) {
			quantidade++;
			return new No(valor);
		}
		if (valor < no.valor)
			no.esq = inserir(no.esq, valor);
		else if (valor > no.valor)
			no.dir = inserir(no.dir, valor);
		else
			return no;
		return balancear(no);
	}

	public boolean remover(int valor) {
		if (!buscar(valor))
			return false;
		raiz = remover(raiz, valor);
		quantidade--;
		return true;
	}

	private No remover(No no, int valor) {
		if (valor < no.valor)
			no.esq = remover(no.esq, valor);
		else if (valor > no.valor)
			no.dir = remover(no.dir, valor);
		else {
			if (no.esq == null)
				return no.dir;
			if (no.dir == null)
				return no.esq;
			No sucessor = no.dir;

			while (sucessor.esq != null)
				sucessor = sucessor.esq;
			no.valor = sucessor.valor;
			no.dir = remover(no.dir, sucessor.valor);
		}
		// Diferenca essencial em relacao a aula: rebalancear tambem ao remover.
		return balancear(no);
	}

	public int contarIntervalo(int minimo, int maximo) {
		return contar(raiz, minimo, maximo);
	}

	private int contar(No no, int minimo, int maximo) {
		if (no == null || minimo > maximo)
			return 0;
		if (no.valor < minimo)
			return contar(no.dir, minimo, maximo);
		if (no.valor > maximo)
			return contar(no.esq, minimo, maximo);
		return 1 + contar(no.esq, minimo, maximo) + contar(no.dir, minimo, maximo);
	}

	void validar() {
		validar(raiz, Long.MIN_VALUE, Long.MAX_VALUE);
	}

	private int validar(No no, long minimo, long maximo) {
		if (no == null)
			return 0;
		if (no.valor <= minimo || no.valor >= maximo)
			throw new AssertionError("Ordem AVL");
		int esq = validar(no.esq, minimo, no.valor), dir = validar(no.dir, no.valor, maximo);

		if (Math.abs(esq - dir) > 1 || no.altura != 1 + Math.max(esq, dir))
			throw new AssertionError("Altura/balanceamento AVL");
		return no.altura;
	}
}
