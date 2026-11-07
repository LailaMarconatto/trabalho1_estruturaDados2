# Comparação de estruturas de dados — ED2

Repositório: [https://github.com/LailaMarconatto/trabalho1_estruturaDados2](https://github.com/LailaMarconatto/trabalho1_estruturaDados2).

Relatório: [Comparação entre estruturas de dados](relatorio/Analise_Comparacao_Estruturas_de_Dados.pdf).

Projeto Java baseado nos códigos das aulas enviados pela aluna. Os originais do Eclipse não foram alterados e estão preservados em `referencia-aulas/`. As versões adaptadas estão em `src/`. Somente `src/` participa da compilação; as versões originais são material de referência.

## Começar pelo Eclipse

1. Use **File > Import > General > Existing Projects into Workspace**.
2. Selecione a pasta baixada ou clonada deste repositório. O projeto importado se chama `ComparacaoEstruturasED2`.
3. Configure um JDK 11 ou superior nas propriedades do projeto (a validação usou o Java 21 incluído no Eclipse).
4. Execute `Testes` como Java Application para conferir as estruturas.
5. Para conferir o experimento, abra **Run > Run Configurations > Java Application**, selecione a classe `Experimento` e coloque `--rapido` em **Arguments > Program arguments**. Use a pasta do projeto como **Working directory**. Execute com **Run**, sem depurador. Os novos resultados ficam em `resultados-validacao/`.
6. Para realizar outra rodada completa, execute `Experimento` sem argumentos. Essa opção sobrescreve os arquivos de `resultados/`: preserve-os ou use outra cópia do projeto para não substituir a rodada usada no relatório.

O Java padrão encontrado no sistema é 8; ele não suporta os métodos de arquivos utilizados neste projeto. Escolha o Java 21 do Eclipse tanto para compilar quanto para executar.

## Como o experimento funciona

- Valores iniciais: pares de 2 até 2n; tamanhos oficiais 5.000, 10.000 e 100.000.
- Três ordens: crescente, decrescente e aleatória. Embaralhamento Fisher–Yates com semente 42.
- Para cada tamanho/ordem, os lotes são preparados uma vez e reutilizados por todas as estruturas, aquecimento e repetições. As sequências de operações são as mesmas também entre as três ordens, isolando o efeito da construção.
- Buscas: 2.000 presentes e 2.000 ausentes; os ausentes são ímpares.
- Inserções e remoções: n/10 valores distintos, obtidos embaralhando o domínio e selecionando os primeiros n/10.
- Intervalos: 200 mínimos uniformes entre 1 e 2n−100, com máximo = mínimo + 100, limites inclusivos.
- Cada combinação recebe um aquecimento e cinco medições. Toda execução recebe uma estrutura nova.
- Na construção, o cronômetro inclui criar a estrutura e inserir os elementos. Nos outros testes, a reconstrução fica fora do cronômetro.
- Sorteios, conferências, mensagens e escrita dos CSVs ficam fora da medição. O resultado é usado e exibido após medir.
- CSVs separados por ponto e vírgula, sem separadores de milhares, com ponto decimal.
- A mediana é o terceiro dos cinco tempos ordenados; o tempo por operação divide essa mediana pelo número de operações do lote.

O modo completo gera `resultados/resultados_brutos.csv` (1.890 linhas de dados), `resultados/resumo.csv` (378 linhas de dados) e `resultados/ambiente.txt`. O aquecimento não entra nos CSVs.

O modo rápido usa somente n=200 para validar o fluxo e grava em `resultados-validacao/`. Seus tempos **não são os resultados oficiais do trabalho**.

## Conferência e limitações

`Testes` executa 350.000 operações aleatórias no total, em dez sementes por estrutura, comparadas com uma referência em `boolean[]`. Também verifica intervalos, duplicatas, buscas por todo o domínio, esvaziamento, valores inteiros extremos e entradas ordenadas. As propriedades da AVL e da rubro-negra são verificadas após cada operação.

O experimento confere as somas das operações, tamanhos finais, presença/ausência dos elementos alterados, contagem total e invariantes das árvores balanceadas. Uma falha interrompe a execução. A bateria de testes é separada dos tempos experimentais.

A BST com entrada ordenada continua tendo custo quadrático de construção; arrays lineares e listas também têm construção quadrática por verificarem duplicatas. Os testes completos podem levar bastante tempo, pois há muitas reconstruções. Essa característica faz parte da comparação e não é contornada por uma construção especial.

Use a mesma máquina e o mesmo Java em toda a rodada. Registre no relatório o modelo do processador, RAM física e versão do Java. O arquivo `ambiente.txt` ajuda nessa coleta; o limite do heap não é a RAM física.

A rodada oficial foi executada integralmente. Os arquivos em `resultados/` contêm 1.890 medições e 378 resumos, cobrindo os três tamanhos, as três ordens, as sete estruturas e os seis testes. Todas as conferências das operações passaram. Uma validação independente confirmou cobertura, cinco repetições por combinação, quantidades de operações, medianas e conversões de unidades.

O ambiente utilizado foi Windows 11, processador Intel Core i7-8550U e Java OpenJDK 21.0.10. A memória física utilizável informada pelo Windows foi 15,89 GiB; os detalhes estão em `resultados/ambiente.txt`.

O histórico da rodada analisada está em `resultados/conferencia-execucao.txt`. O arquivo `resultados/validacao.txt` contém a conferência de cobertura e medianas. Para repeti-la, execute `python validar_resultados.py` na pasta do repositório, com Python 3. O validador lê os CSVs e a saída preservada, sem modificá-los. A pasta `bin/` é recriada automaticamente ao compilar e não é publicada.

Os gráficos e a análise estão em `relatorio/`. O PDF contém seis figuras, as 378 medianas e os menores e maiores resultados nos nove cenários. Os gráficos em PNG e SVG ficam em `relatorio/graficos/`. O texto em Markdown, o ZIP dos gráficos, os scripts de produção e suas dependências foram preservados na pasta de apoio e não são necessários para executar o projeto Java.

O relatório usa Times New Roman, títulos de 13 pt em caixa alta e numerados e texto de 12 pt, com capa de Laila Vitória Marconatto, Instituto Federal Catarinense, Bacharelado em Ciência da Computação, Campus Rio do Sul, 2026, e inclui a URL completa deste repositório.

## Reprodução no Eclipse e preservação dos dados

Uma cópia limpa foi baixada do GitHub, importada e executada no Eclipse. A classe `Testes` passou nas sete estruturas; `Experimento --rapido` gerou 630 medições e 126 resumos, com as 126 conferências corretas. A saída do modo rápido está em `resultados/verificacao-eclipse.txt`. Esses resultados reduzidos não foram usados na análise e seus CSVs não substituem os da rodada completa.

Os CSVs de `resultados/` são os mesmos da rodada completa analisada, originalmente iniciada pela JVM fora da IDE. Executar e validar o projeto no Eclipse confirma sua reprodução nesse ambiente, mas não altera retroativamente a origem das medições preservadas.

As explicações das adaptações estão em `ALTERACOES.md`.
