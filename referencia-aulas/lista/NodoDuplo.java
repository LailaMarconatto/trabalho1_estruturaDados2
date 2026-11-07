package listalineares;

public class NodoDuplo {
	private int dado;
	private NodoDuplo ant;
	private NodoDuplo prox;
	
	
	public NodoDuplo(int dado) {
		this.dado = dado;
		this.ant = null;
		this.prox = null;
	}


	public int getDado() {
		return dado;
	}


	public void setDado(int dado) {
		this.dado = dado;
	}


	public NodoDuplo getAnt() {
		return ant;
	}


	public void setAnt(NodoDuplo ant) {
		this.ant = ant;
	}


	public NodoDuplo getProx() {
		return prox;
	}


	public void setProx(NodoDuplo prox) {
		this.prox = prox;
	}
	
	
	
}
