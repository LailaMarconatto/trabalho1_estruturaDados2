package listalineares;

public class ListaDuplamenteEncadeada {
	private NodoDuplo inicio;
	private NodoDuplo fim;
	
	public ListaDuplamenteEncadeada() {
		this.inicio = null;
		this.fim = null;
	}

	public NodoDuplo getInicio() {
		return inicio;
	}

	
	public void setInicio(NodoDuplo inicio) {
		this.inicio = inicio;
	}

	public NodoDuplo getFim() {
		return fim;
	}

	public void setFim(NodoDuplo fim) {
		this.fim = fim;
	}
	
	public boolean vazia() {
		return inicio == null;
	}
	
	public void inserirInicio(int dado) {
		NodoDuplo novoNodo = new NodoDuplo(dado);
		if(vazia()) {
			inicio = novoNodo;
			fim = novoNodo;
			return;
		}
		
		novoNodo.setProx(inicio);
		inicio.setAnt(novoNodo);
		inicio = novoNodo;
	}
	
	public void inserirFinal(int dado) {
		if(vazia()) {
			inserirInicio(dado);
			return;
		}
		
		NodoDuplo novoNodo = new NodoDuplo(dado);
		
		fim.setProx(novoNodo);
		novoNodo.setAnt(fim);
		fim = novoNodo;
	}
	
	public void mostrarLista() {
		if (vazia()) {
			System.out.println("Lista Vazia");
			return;
		}
		
		NodoDuplo aux = inicio;
		
		while(aux != null) {
			System.out.println(aux.getDado());
			aux = aux.getProx();
		}
	}
	
	public void removerValor(int valor) {
		if(vazia()) return;
		
		//caso 1 : o valor está no inicio
		if(inicio.getDado() == valor) {
			// se for unico elemento
			if(inicio == fim) {
				inicio = null;
				fim = null;
				return;
			}
			
			inicio = inicio.getProx();
			inicio.setAnt(null);
			return;
		}
		
		//caso 2 : o valor está no fim
		if(fim.getDado() == valor) {
			fim = fim.getAnt();
			fim.setProx(null);
			return;
		}
		
		//caso 3 : o valor está no meio
		NodoDuplo aux = inicio.getProx();
		while(aux != null) {
			if(aux.getDado() == valor) {
				aux.getAnt().setProx(aux.getProx());
				aux.getProx().setAnt(aux.getAnt());
				return;
			}
			
			aux = aux.getProx();
		}
	}
	
	public void mostrarListaInversa() {
		if (vazia()) {
			System.out.println("Lista Vazia");
			return;
		}
		
		NodoDuplo aux = fim;
		
		while(aux != null) {
			System.out.println(aux.getDado());
			aux = aux.getAnt();
		}
	}
	
	public void getExtremos() {  
		if (vazia()) {
			System.out.println("Lista Vazia");
			return;
		}
		
		int maior = inicio.getDado();
		int menor = inicio.getDado();
		NodoDuplo aux = inicio;
		
		while(aux.getProx() != null) {
			if(aux.getDado() > maior) {
				maior = aux.getDado();
			}
			
			if(aux.getDado() < menor) {
				menor = aux.getDado();
			}
			
			aux = aux.getProx();
		}
		
		System.out.println("Maior : "+ maior);
		System.out.println("Menor : "+ menor);
		return;
	}
	
	public boolean isPalindromo() {
		if (vazia()) {
			System.out.println("Lista Vazia");
			return true;
		}

		//pagar valores da lista na ordem normal
		NodoDuplo aux = inicio;
		StringBuilder valorlista1 = new StringBuilder();
		while(aux != null) {
			valorlista1.append(aux.getDado());
			aux = aux.getProx();
		}
		
		//pagar valores da lista na ordem inversa
		aux = fim;
		StringBuilder valorlista2 = new StringBuilder();
		while(aux != null) {
			valorlista2.append(aux.getDado());
			aux = aux.getAnt();
		}

		//compara os dois
		if(valorlista1.toString().equals(valorlista2.toString())) {
			return true;
		}else {
			return false;
		}
	}
}
