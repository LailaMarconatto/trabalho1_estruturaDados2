# O que mudou em relação às aulas

## Padronização visual

Os arquivos de `src/` foram formatados no estilo do Eclipse usado nas aulas: indentação com tabulações, comentários somente com `//`, métodos escritos em várias linhas e linhas em branco separando métodos e os blocos de declarações de variáveis das operações. As cópias das aulas foram preservadas em `referencia-aulas/` e não participam da compilação. Essa revisão alterou apenas comentários e espaços; a compilação sem informações de depuração produziu classes idênticas às anteriores. Os experimentos e seus resultados não foram alterados.

## Organização comum

Foi criada a interface `Estrutura`: `inserir`, `buscar`, `remover`, `contarIntervalo` e `tamanho`. Inserção e remoção retornam `boolean` para conferir se houve mudança. Nenhuma estrutura permite duplicatas. Os nomes dos métodos foram padronizados para que o programa de experimentos trate todas igualmente.

Nós internos passaram a ser classes estáticas, sem referência implícita ao objeto externo. Foram retiradas impressões das operações usadas nas medições. As classes de demonstração das aulas não foram incluídas no programa experimental.

## Lista duplamente encadeada

Base: `ListaDuplamenteEncadeada.java` e `NodoDuplo.java` das aulas.

- Preservados início, fim e ligações anterior/próximo; inserção ao final.
- `NodoDuplo` foi colocado dentro da classe e os acessores foram simplificados para acesso interno.
- Adicionada busca sequencial, utilizada também para ignorar duplicatas antes de inserir.
- Remoção reunida em um único tratamento das duas ligações, contemplando início, fim e elemento único.
- Adicionados tamanho e contagem por intervalo percorrendo a lista.
- Métodos de impressão, palíndromo e extremos não entram na versão experimental.

Consequência: embora ligar um novo nó ao fim custe O(1), a inserção completa custa O(n) porque precisa procurar duplicatas.

## Tabela hash

Base: `HashTable2.java`. A `HashTable1` não foi usada porque sobrescreve entradas nas colisões.

- Preservados sondagem linear e crescimento para o dobro da capacidade, com limite de carga 0,7.
- Capacidade inicial fixa em 16 para todas as execuções. O crescimento é medido quando ocorre dentro do lote.
- Strings e objetos chave/valor foram substituídos por inteiros, pois o enunciado pede conjunto de inteiros.
- A função de espalhamento passou de soma dos bytes do texto para `floorMod(valor, capacidade)`, apropriada para inteiros e segura para negativos. Essa política deve ser informada na análise: os pares podem colidir conforme a capacidade.
- Duplicatas são ignoradas antes de considerar crescimento. O teste de carga considera a próxima ocupação, em vez de esperar uma inserção posterior.
- Adicionados estados vazio/ocupado/removido. Uma remoção não pode simplesmente tornar a posição vazia: isso interromperia buscas por elementos que sofreram colisões.
- A ocupação que dispara crescimento inclui posições marcadas como removidas; o redimensionamento limpa essas marcações. A quantidade de elementos é contada separadamente.
- Busca limitada à capacidade para evitar laço infinito; intervalo percorre as posições ocupadas.

## BST sem balanceamento

Base: `ArvoreBinariaBusca.java`, não a árvore binária genérica.

- Preservada a propriedade menores à esquerda e maiores à direita e a busca iterativa.
- Inserção convertida de recursiva para iterativa para suportar árvores degeneradas grandes.
- Remoção adicionada com os três casos: folha, um filho e dois filhos; no último, usa o sucessor da subárvore direita.
- Consulta por intervalo usa pilha própria em array, ampliada quando necessário, e poda ramos que não podem conter valores válidos. Não usa recursão.
- Não foi adicionado balanceamento. A perda de desempenho com entradas ordenadas deve aparecer nos resultados.

## AVL

Base: `ArvoreAVL.java`.

- Preservadas a inserção como BST e as quatro situações de rotação.
- As duas alturas de filhos foram substituídas por uma altura do próprio nó; a informação é equivalente, mas mais simples de manter.
- Adicionada busca iterativa. O trecho original de remoção fazia referência a métodos ausentes no arquivo fornecido.
- Na remoção de nó com dois filhos, encontra o menor valor da subárvore direita.
- A remoção agora atualiza alturas e rebalanceia ao retornar da recursão. O trecho original não fazia isso.
- Consulta por intervalo poda ramos pela ordenação. A recursão é mantida porque a altura é balanceada.
- Um validador, usado fora da medição, verifica ordenação, alturas armazenadas e diferença máxima de um entre alturas dos filhos.

## Rubro-negra

Base: `ArvoreRubroNegra.java`; é a adaptação mais extensa porque a aula não incluía remoção.

- Preservados os conceitos de pai, cores, rotações e correção da inserção por recoloração/rotações, inclusive casos simétricos.
- As cores passaram do enum para um booleano interno; raiz e folhas continuam pretas.
- Folhas nulas substituídas por uma sentinela preta compartilhada, permitindo que a correção de remoção trate também um filho vazio com acesso ao seu pai.
- Inserção iterativa detecta duplicatas no mesmo percurso, sem a busca prévia separada.
- Adicionadas remoção por transplante e correção dos casos de irmão vermelho, irmão preto com filhos pretos e irmão preto com filho vermelho.
- Adicionada contagem por intervalo com poda.
- O validador confere raiz/sentinela pretas, vínculos de pais, ordenação, ausência de dois vermelhos consecutivos e mesma altura preta nos caminhos.

## Arrays

As duas estruturas foram implementadas para o trabalho usando os conceitos de busca sequencial e binária das aulas; não são cópias de uma classe completa fornecida.

- Linear: busca sequencial; inserção ao fim após verificar duplicata; remoção substitui pelo último, já que não há ordenação a preservar; intervalo percorre todos os valores.
- Ordenado: busca binária para localizar posição; inserção e remoção deslocam os elementos com `System.arraycopy`. A construção insere um a um mantendo a ordem, sem ordenar o lote depois. Intervalos usam duas buscas binárias e contam pela diferença de posições.
- Ambos reservam n+n/10 posições. Não usam `ArrayList` ou outra coleção pronta para representar dados.

## Programa experimental

`Experimento.java` é novo: prepara dados reproduzíveis, reconstrói estados independentes, mede lotes com `System.nanoTime()`, confere resultados e exporta os dois CSVs pedidos.

`Testes.java` é novo: valida correção antes de comparar desempenho. Bibliotecas Java aparecem apenas em tarefas auxiliares, como geração aleatória, cópias de arrays, estatística e arquivos. As sete estruturas são implementações próprias.
