package arvores;

public class ArvoreBinariaBusca {
	private class No{
		int valor;
		No dir;
		No esq;
		
		public No(int valor) {
			this.valor= valor;
			dir = esq = null;
		}
	}
	
	No raiz;
	
	public void inserir(int valor) {
		raiz = inserir(raiz,valor);
	}
	
	private No inserir(No no, int valor) {
		if(no == null) {
			no = new No(valor);
			return no;
		}
		
		if(valor < no.valor) {
			no.esq = inserir(no.esq, valor);
		} else if (valor > no.valor) {
			no.dir = inserir(no.dir,valor);
		} else {
			System.out.println("O valor "+valor+" já existe na árvore");
		}
		
		return no;
	}
	
	public void emOrdem() {
		emOrdem(raiz);
	}
	
	private void emOrdem(No no) {
		if (no != null) {
			emOrdem(no.esq);
			System.out.println(no.valor);
			emOrdem(no.dir);
		}
	}
	
	public boolean buscar(int valor) {
		No atual = raiz;
		
		while(atual != null) {
			if(valor == atual.valor) {
				return true;
			}
			
			if(valor < atual.valor) {
				atual = atual.esq;
			} else {
				atual = atual.dir;
			}
		}
		
		return false;
	}
}

