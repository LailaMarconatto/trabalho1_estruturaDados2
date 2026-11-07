// Conjunto de inteiros sem repeticao usado em todos os experimentos.
public interface Estrutura {

	boolean inserir(int valor);

	boolean buscar(int valor);

	boolean remover(int valor);

	int contarIntervalo(int minimo, int maximo);

	int tamanho();
}
