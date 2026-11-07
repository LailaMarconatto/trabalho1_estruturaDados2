// Adaptacao da aula: mesmos pais, cores e rotacoes; sentinela preta na remocao.
final class ArvoreRubroNegra implements Estrutura {

	private static final class No {

		int valor;
		boolean vermelho;
		No esq, dir, pai;

		No(int valor, boolean vermelho) {
			this.valor = valor;
			this.vermelho = vermelho;
		}
	}

	private final No nil = new No(0, false);
	private No raiz;
	private int quantidade;

	ArvoreRubroNegra() {
		nil.esq = nil.dir = nil.pai = nil;
		raiz = nil;
	}

	public int tamanho() {
		return quantidade;
	}

	private No localizar(int valor) {
		No no = raiz;

		while (no != nil && no.valor != valor)
			no = valor < no.valor ? no.esq : no.dir;
		return no;
	}

	public boolean buscar(int valor) {
		return localizar(valor) != nil;
	}

	private void rotacaoEsquerda(No no) {
		No nova = no.dir;

		no.dir = nova.esq;
		if (nova.esq != nil)
			nova.esq.pai = no;
		nova.pai = no.pai;
		if (no.pai == nil)
			raiz = nova;
		else if (no == no.pai.esq)
			no.pai.esq = nova;
		else
			no.pai.dir = nova;
		nova.esq = no;
		no.pai = nova;
	}

	private void rotacaoDireita(No no) {
		No nova = no.esq;

		no.esq = nova.dir;
		if (nova.dir != nil)
			nova.dir.pai = no;
		nova.pai = no.pai;
		if (no.pai == nil)
			raiz = nova;
		else if (no == no.pai.dir)
			no.pai.dir = nova;
		else
			no.pai.esq = nova;
		nova.dir = no;
		no.pai = nova;
	}

	public boolean inserir(int valor) {
		No pai = nil, atual = raiz;

		while (atual != nil) {
			if (valor == atual.valor)
				return false;
			pai = atual;
			atual = valor < atual.valor ? atual.esq : atual.dir;
		}
		No novo = new No(valor, true);

		novo.esq = novo.dir = nil;
		novo.pai = pai;
		if (pai == nil)
			raiz = novo;
		else if (valor < pai.valor)
			pai.esq = novo;
		else
			pai.dir = novo;
		corrigirInsercao(novo);
		quantidade++;
		return true;
	}

	private void corrigirInsercao(No no) {
		while (no.pai.vermelho) {
			if (no.pai == no.pai.pai.esq) {
				No tio = no.pai.pai.dir;

				if (tio.vermelho) {
					no.pai.vermelho = tio.vermelho = false;
					no.pai.pai.vermelho = true;
					no = no.pai.pai;
				} else {
					if (no == no.pai.dir) {
						no = no.pai;
						rotacaoEsquerda(no);
					}
					no.pai.vermelho = false;
					no.pai.pai.vermelho = true;
					rotacaoDireita(no.pai.pai);
				}
			} else {
				No tio = no.pai.pai.esq;

				if (tio.vermelho) {
					no.pai.vermelho = tio.vermelho = false;
					no.pai.pai.vermelho = true;
					no = no.pai.pai;
				} else {
					if (no == no.pai.esq) {
						no = no.pai;
						rotacaoDireita(no);
					}
					no.pai.vermelho = false;
					no.pai.pai.vermelho = true;
					rotacaoEsquerda(no.pai.pai);
				}
			}
		}
		raiz.vermelho = false;
	}

	private void transplantar(No antigo, No novo) {
		if (antigo.pai == nil)
			raiz = novo;
		else if (antigo == antigo.pai.esq)
			antigo.pai.esq = novo;
		else
			antigo.pai.dir = novo;
		novo.pai = antigo.pai;
	}

	public boolean remover(int valor) {
		No alvo = localizar(valor);

		if (alvo == nil)
			return false;
		No sucessor = alvo, ajuste;
		boolean eraVermelho = sucessor.vermelho;

		if (alvo.esq == nil) {
			ajuste = alvo.dir;
			transplantar(alvo, alvo.dir);
		} else if (alvo.dir == nil) {
			ajuste = alvo.esq;
			transplantar(alvo, alvo.esq);
		} else {
			sucessor = alvo.dir;
			while (sucessor.esq != nil)
				sucessor = sucessor.esq;
			eraVermelho = sucessor.vermelho;
			ajuste = sucessor.dir;
			if (sucessor.pai == alvo)
				ajuste.pai = sucessor;
			else {
				transplantar(sucessor, sucessor.dir);
				sucessor.dir = alvo.dir;
				sucessor.dir.pai = sucessor;
			}
			transplantar(alvo, sucessor);
			sucessor.esq = alvo.esq;
			sucessor.esq.pai = sucessor;
			sucessor.vermelho = alvo.vermelho;
		}
		if (!eraVermelho)
			corrigirRemocao(ajuste);
		quantidade--;
		nil.pai = nil;
		return true;
	}

	private void corrigirRemocao(No no) {
		while (no != raiz && !no.vermelho) {
			if (no == no.pai.esq) {
				No irmao = no.pai.dir;

				if (irmao.vermelho) {
					irmao.vermelho = false;
					no.pai.vermelho = true;
					rotacaoEsquerda(no.pai);
					irmao = no.pai.dir;
				}
				if (!irmao.esq.vermelho && !irmao.dir.vermelho) {
					irmao.vermelho = true;
					no = no.pai;
				} else {
					if (!irmao.dir.vermelho) {
						irmao.esq.vermelho = false;
						irmao.vermelho = true;
						rotacaoDireita(irmao);
						irmao = no.pai.dir;
					}
					irmao.vermelho = no.pai.vermelho;
					no.pai.vermelho = false;
					irmao.dir.vermelho = false;
					rotacaoEsquerda(no.pai);
					no = raiz;
				}
			} else {
				No irmao = no.pai.esq;

				if (irmao.vermelho) {
					irmao.vermelho = false;
					no.pai.vermelho = true;
					rotacaoDireita(no.pai);
					irmao = no.pai.esq;
				}
				if (!irmao.dir.vermelho && !irmao.esq.vermelho) {
					irmao.vermelho = true;
					no = no.pai;
				} else {
					if (!irmao.esq.vermelho) {
						irmao.dir.vermelho = false;
						irmao.vermelho = true;
						rotacaoEsquerda(irmao);
						irmao = no.pai.esq;
					}
					irmao.vermelho = no.pai.vermelho;
					no.pai.vermelho = false;
					irmao.esq.vermelho = false;
					rotacaoDireita(no.pai);
					no = raiz;
				}
			}
		}
		no.vermelho = false;
	}

	public int contarIntervalo(int minimo, int maximo) {
		return contar(raiz, minimo, maximo);
	}

	private int contar(No no, int minimo, int maximo) {
		if (no == nil || minimo > maximo)
			return 0;
		if (no.valor < minimo)
			return contar(no.dir, minimo, maximo);
		if (no.valor > maximo)
			return contar(no.esq, minimo, maximo);
		return 1 + contar(no.esq, minimo, maximo) + contar(no.dir, minimo, maximo);
	}

	void validar() {
		if (nil.vermelho || raiz.vermelho || raiz.pai != nil)
			throw new AssertionError("Raiz/sentinela RN");
		validar(raiz, Long.MIN_VALUE, Long.MAX_VALUE);
	}

	private int validar(No no, long minimo, long maximo) {
		if (no == nil)
			return 1;
		if (no.valor <= minimo || no.valor >= maximo)
			throw new AssertionError("Ordem RN");
		if (no.esq != nil && no.esq.pai != no || no.dir != nil && no.dir.pai != no)
			throw new AssertionError("Pais RN");
		if (no.vermelho && (no.esq.vermelho || no.dir.vermelho))
			throw new AssertionError("Cores RN");
		int esq = validar(no.esq, minimo, no.valor), dir = validar(no.dir, no.valor, maximo);

		if (esq != dir)
			throw new AssertionError("Altura preta RN");
		return esq + (no.vermelho ? 0 : 1);
	}
}
