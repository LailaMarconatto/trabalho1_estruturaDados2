// Adaptacao da HashTable2: sondagem linear, carga 0.7 e capacidade dobrada.
final class TabelaHash implements Estrutura {

	private int[] valores;
	// 0 = vazio, 1 = ocupado, 2 = removido (preserva a cadeia de sondagem).
	private byte[] estados;
	private int quantidade, usados;

	TabelaHash() {
		valores = new int[16];
		estados = new byte[16];
	}

	public int tamanho() {
		return quantidade;
	}

	private int hash(int valor) {
		return Math.floorMod(valor, valores.length);
	}

	private int localizar(int valor) {
		int i = hash(valor);

		for (int passo = 0; passo < valores.length; passo++) {
			if (estados[i] == 0)
				return -1;
			if (estados[i] == 1 && valores[i] == valor)
				return i;
			i = (i + 1) % valores.length;
		}
		return -1;
	}

	public boolean buscar(int valor) {
		return localizar(valor) >= 0;
	}

	public boolean inserir(int valor) {
		if (buscar(valor))
			return false;
		if ((usados + 1) > valores.length * 0.7)
			redimensionar();
		colocar(valor);
		return true;
	}

	private void colocar(int valor) {
		int i = hash(valor);

		while (estados[i] == 1)
			i = (i + 1) % valores.length;
		if (estados[i] == 0)
			usados++;
		estados[i] = 1;
		valores[i] = valor;
		quantidade++;
	}

	private void redimensionar() {
		int[] antigos = valores;
		byte[] estadosAntigos = estados;

		valores = new int[antigos.length * 2];
		estados = new byte[valores.length];
		quantidade = usados = 0;
		for (int i = 0; i < antigos.length; i++)
			if (estadosAntigos[i] == 1)
				colocar(antigos[i]);
	}

	public boolean remover(int valor) {
		int i = localizar(valor);

		if (i < 0)
			return false;
		estados[i] = 2;
		quantidade--;
		return true;
	}

	public int contarIntervalo(int minimo, int maximo) {
		int total = 0;

		for (int i = 0; i < valores.length; i++)
			if (estados[i] == 1 && valores[i] >= minimo && valores[i] <= maximo)
				total++;
		return total;
	}
}
