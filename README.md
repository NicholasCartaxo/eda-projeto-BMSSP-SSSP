# EDA Projeto BMSSP para SSSP
  Implementação, documentação e experimentação do **Bounded Multi-Source Shortest Path** (**BMSSP**) como algoritmo que resolve o problema de **Single Source Shortest Path** (**SSSP**) em comparação com **Dijkstra**.

  Esse repositório utiliza o artigo [Breaking the Sorting Barrier for Directed Single-Source Shortest Paths](https://arxiv.org/abs/2504.17033) como base para implementar e documentar o **BMSSP** resolvendo o problema de menor caminho para uma única fonte. Além disso, é feita uma comparação experimental com **Dijkstra**, a fim de analisar a superação da barreira de tempo para **SSSP** prometida.

## Introdução
  Algoritmos de menor caminho identificam o caminho mais curto entre pontos em um grafo. O caminho mais curto se refere à soma dos pesos das arestas (custo, tempo ou distância). Essa questão é de suma importância para diversas aplicações, e a eficiência com que o menor caminho é encontrado é crucial para o bom funcionamento de redes de fluxos,  as quais se aplicam à logística de transportes de produtos, à escolha de rotas em redes digitais e aos algoritmos de recomendação de redes sociais. O recém-lançado **BMSSP** é um algoritmo para identificar os caminho mais curtos para fonte única que quebra a barreira de tempo de **Dijkstra**, e a falta de implementações e documentações sobre ele motivaram esse estudo. Para essa análise, o grupo implementou o programa em Java, documentou seu funcionamento e realizou testes de comparação de eficiência com o algoritmo de menor caminho de **Dijkstra**. 

## BMSSP
  A busca pelos menores caminho de fonte única em grafos direcionados com pesos não negativos é um problema central na ciência da computação. Nesse contexto, o algoritmo de **Dijkstra** consolidou-se como o método padrão para resolver o problema de caminho mínimo a partir de uma única fonte, devido à sua eficiência. 
  O algoritmo já tradicional de **Dijkstra** possui uma complexidade de tempo $O(m\log n)$, sendo **n** o número de nós e **m** o número de arestas, o que era considerado a barreira para esse tipo de problema. O artigo **Breaking the Sorting Barrier for Directed Single-Source Shortest Paths** introduz um novo algoritmo teórico com complexidade de tempo $O(m\log^{2/3}n)$, quebrando essa barreira. No entanto, a ausência de implementações concretas e documentadas desse algoritmo dificulta sua compreensão e análise prática, limitando o acesso ao conteúdo por parte da comunidade acadêmica e técnica.
  Diante disso, este projeto tem como objetivo realizar a implementação do algoritmo proposto no artigo referenciado, seguindo sua lógica, suas estruturas de dados e suas estratégias de otimização de forma fidedigna. Para isso, o código será feito com Java, utilizando otimizações de implementação e da linguagem quando possível, sem alterar sua complexidade, permitindo a validação de seu funcionamento. Além disso, são realizados testes comparativos entre os algoritmos, utilizando grafos esparsos, já que a diferença de complexidade é mais proeminente com **m** pequeno, e com graus parecidos entre os nós, já que o artigo assume essa configuração que se satisfaça a complexidade.

# Metodologia
  O processo de execução do projeto ocorreu por meio da divisão de tarefas dentro de subgrupos do projeto, com reuniões semanais (sprints) e a especificação dos problemas/soluções para o código. Todas as ferramentas para essa produção acadêmica se deram de maneira consensual entre os participantes, a partir de discussões sobre a revisão do desempenho e da dificuldade no cumprimento das especificações do algoritmo de menor caminho.
  Nesse viés, esse projeto foi ramificado em 4 partes práticas e 2 partes de análise e conclusão, sendo cada uma focada em uma tarefa ou funcionalidade específica. Dentre os  objetivos principais do plano de execução do BMSSP, temos, em ordem de implementação no projeto, o/a: 
  1. #### Prática
      A implementação propriamente dita do algoritmo, seguindo a mesma divisão do artigo em métodos, além da estrutura auxiliar especializada definida para o funcionamento.
        - DQueue()
        - findPivots()
        - baseCase()
        - bmssp()
  2. #### Análise
      Experimentação do **BMSSP** em comparação com **Dijkstra** para verificação de corretude e para análise gráfica dos tempos de execução.
        - Comparação dos resultados dos algoritmos para comprovar a corretude do algoritmo. 
        - Plotagem de gráficos para o tempo de execução tanto para o **BMSSP** quanto para o algoritmo de **Dijkstra** para a análise de eficiência.

  Primeiramente, foram definidas as estruturas iniciais de grafos e as estruturas de dados que seriam usadas para implementar as abstrações do artigo. Depois, foi realizada a implementação das funções separadamente. Essa etapa foi dividida entre os integrantes, que se organizaram de maneira a cada um ficar com uma parte da estrutura, auxiliando na otimização do projeto. 
  Por fim, foi executada uma bateria de testes com o intuito de verificar se o **BMSSP** resolve corretamente os problemas de tamanhos variados e de analisar o tempo de execução, com a plotagem dos gráficos de comparação para a visualização da complexidade de tempo.

# Documentação

## Ideia geral do Algoritmo

  Para conseguir entender as especificidades do algoritmo, primeiramente, é necessário um entendimento mais abstrato de como ele funciona, já que é incomum um algoritmo de menor caminho usar a estratégia de divisão e conquista.

  A ideia geral é dividir o problema em pequenas partes, com base em limites de distâncias, até que a parte seja pequena o bastante para uma simples computação de **Dijkstra**, também limitando, por construção, uma divisão boa o bastante para diminuir a comlexidade de tempo, na qual não há, como ocorre tradicionalmente, uma ordenação completa das distâncias em uma fila de prioridade, mas apenas uma redução das partes, o  que possibilita a quebra da barreira abordada pelo artigo.

  Mais especificadamente, uma chamada do `bmssp` recebe um limite superior, o nível atual da recursão, e os nós já resolvidos. Então, serão achados, entre esses nós, aqueles que chegam a nós não resolvidos, dentro da fronteira especificada, os chamados **pivôs**, também solucionando as distância de alguns nós alcançados por eles. Com esses pivôs, uma nova chamada será feita para resolver o problema para um limite superior menor, até que o nível da recursão indique que isso seja trivial o bastante para que **Dijkstra** seja usado para o pequeno limite. Assim, com os nós sendo completados para esses limites menores, eles podem ser usados como pivôs dos limites maiores, gradualmente resolvendo todo o problema ao pegar os pivôs com as menores distâncias, por meio da estrutura especializada `DQueue`.
  
  Por isso, o retorno da função contém os novos nós completados e o limite superior por eles alcançado, já que para essa estratégia funcionar, é necessário garantir que, dadas as distâncias $b<B$, todos os caminhos com distâncias entre **b** e **B** passam por um nó com distância de até **b**, então, cada chamada recursiva resolve o problema para um certo **b**, possibilitando a chamada anterior a solucionar os nós da fronteira **B** a partir desses caminhos, conquistando de forma mais otimizada o problema.

  Assim, a primeira chamada do `bmssp` contem um limite superior infinito, já que almeja-se solucionar todos os nós, independente da distância, e o único nó completado é a origem, com distância nula.

## Implementação

  A implementação do algoritmo está organizada em três métodos principais: BMSSP, Base Case e Find Pivots. Além desses, foi desenvolvido também o método solve, responsável por receber o nó de origem e o grafo sobre o qual o algoritmo será executado. Esse método realiza a inicialização das constantes e das estruturas de dados necessárias, define as distâncias de cada nó com valor infinito e, em seguida, chama o método BMSSP. Todas as constantes utilizadas, bem como seus respectivos valores, encontram-se descritas no artigo apresentado no início deste documento.

O método BMSSP é responsável por controlar a recursão principal do algoritmo, dividindo o problema geral em subproblemas até que o caso base seja alcançado, ou seja, quando o nível de recursão atinge zero. Esse método recebe inicialmente três parâmetros. O primeiro é o nível de recursão (level), utilizado como condição de parada e definido como o valor de log N/T. O segundo é um valor numérico denominado limite superior (upper bound), que estabelece a região em que os três métodos irão atuar. Por exemplo, se o limite superior for igual a 5, os métodos somente modificarão os nós cuja distância em relação ao nó de referência seja menor ou igual a 5. Inicialmente, esse limite é definido como infinito, garantindo que seja sempre maior do que qualquer distância finita. O terceiro parâmetro consiste em um conjunto de nós com distâncias já conhecidas. Esse conjunto, no início da execução, contém apenas o nó de origem — isto é, o nó a partir do qual serão calculadas as distâncias para todos os demais. A primeira operação realizada pelo método consiste em verificar se o nível de recursão é igual a zero. Nesse caso, o problema já se encontra suficientemente reduzido e o método retorna a chamada para o Base Case. Caso contrário, o algoritmo recorre ao método Find Pivots para identificar os pivôs da recursão atual, definidos com base nos nós já conhecidos e no limite superior vigente. Em seguida, é inicializada a DQueue, uma estrutura de fila projetada para recuperar, de forma otimizada, os M menores elementos em uma única consulta. A DQueue é instanciada com dois parâmetros: o limite superior atual e o tamanho do bloco (block size), definido como T multiplicado por 2 elevado ao (nível - 1). O uso de (nível - 1) deve-se ao fato de que os resultados produzidos na próxima chamada recursiva serão utilizados na recursão corrente. Após a inicialização, todos os pivôs identificados são inseridos na DQueue, e o limite superior atual é atualizado, assumindo o valor da menor distância entre os pivôs. Em seguida, é definido um novo conjunto, denominado New Complete Nodes, destinado a armazenar os nós que terão suas distâncias determinadas em relação ao nó de origem durante a execução do laço principal do BMSSP. Também é estabelecido um limite máximo para o tamanho desse conjunto, calculado como K multiplicado por 2 elevado a T. A partir disso, enquanto o tamanho do conjunto não ultrapassar esse limite e a DQueue não estiver vazia, o laço principal do algoritmo é executado. O funcionamento desse laço pode ser descrito da seguinte forma:

1 - A partir da DQueue, são extraídas duas informações: os M menores elementos, denominados Prev Nodes, e o Prev Upper Bound, correspondente à menor distância entre os elementos que permanecem na fila.

2 - Em seguida, são definidas duas novas variáveis, análogas às anteriores: Current Nodes e Current Upper Bound. Esses valores correspondem aos resultados da próxima chamada recursiva, realizada com um nível reduzido, considerando os nós presentes na DQueue e o respectivo limite superior. Dessa forma, o problema é progressivamente diminuído até que o nível atinja zero, momento em que o caso base é acionado com um único nó inicial. Em todos esses cenários, o retorno consiste no conjunto de nós completos (Current Nodes) e no novo limite superior obtido;

3 - Os novos nós completos são então incorporados ao conjunto New Complete Nodes, enquanto um novo conjunto, denominado New Node Dists, é criado para armazenar todos os nós cuja distância seja maior que o Current Upper Bound e menor que o Prev Upper Bound. Esses nós são posteriormente adicionados de forma simultânea à DQueue, garantindo maior eficiência na execução do código. Em seguida, é iniciado um laço que percorre todos os nós do conjunto Current Nodes, bem como os nós a eles conectados. Para cada nó analisado, verifica-se se o novo caminho encontrado até ele é menor do que o previamente armazenado; caso positivo, esse novo valor é atualizado no conjunto final. Além disso, se o nó satisfizer a condição de pertencimento ao conjunto New Node Dists, ele também é incluído nesse conjunto;

4 - É executado um laço sobre os nós do conjunto Prev Nodes. Para cada nó, verifica-se se a distância até ele é maior que o Current Upper Bound e menor que o Prev Upper Bound. Quando essa condição é satisfeita, o nó é adicionado ao conjunto New Node Dists;

5 - O conjunto New Node Dists é então adicionado à DQueue por meio da operação batchPrepend, o que garante maior eficiência em comparação à inserção individual de múltiplos elementos. Com isso, conclui-se uma iteração do laço principal, o qual se repete até que o nível da recursão atinja zero.

Por fim, o menor valor entre o Upper Bound recebido como parâmetro no início do método e o Current Upper Bound é armazenado em uma variável auxiliar. Em seguida, para cada nó completo retornado pelo Find Pivots, verifica-se se sua distância é inferior a esse menor Upper Bound; caso positivo, o nó é adicionado ao conjunto New Complete Nodes. Dessa forma, o retorno final do método é composto pelo menor Upper Bound e pelo conjunto New Complete Nodes.

Quando o nível de recursão atinge zero, é acionado o Base Case. Esse método recebe como parâmetros um limite superior e um pivô, uma vez que o problema já se encontra suficientemente reduzido para conter apenas um pivô, a partir do qual serão calculadas as distâncias até os nós vizinhos. Inicialmente, o método define um conjunto de nós completos (Complete Nodes) e uma fila de prioridade, ambos contendo apenas o pivô. Além disso, é estabelecido um novo limite superior, correspondente à distância até o pivô. Em seguida, enquanto a fila de prioridade não estiver vazia e o número de elementos em Complete Nodes for menor que k + 1, o Base Case executa um laço que:

1 - Remove um nó (Current Node) da fila de prioridade e compara o novo caminho até ele com a distância previamente armazenada no conjunto final. Se o novo caminho for maior, a iteração é encerrada e passa-se para o próximo nó; caso contrário, a execução do laço continua.

2 - verifica se a distância até o Current Node é maior que o novo Upper Bound. Se essa condição for atendida, o limite superior é atualizado com o valor da distância.

3 - Executa um laço sobre todos os nós conectados ao Current Node. Para cada nó, calcula-se a distância até ele como a soma da distância até o Current Node com o peso da aresta que os conecta. Caso a nova distância seja menor do que a previamente conhecida, ela é atualizada no conjunto final e o nó é adicionado à fila de prioridade.
Dessa forma, ao final da execução do Base Case, se o número de nós no conjunto Complete Nodes for menor ou igual a k, o método retorna esse conjunto juntamente com o limite superior recebido como parâmetro no início. Caso contrário, são retornados apenas os nós cuja distância seja inferior ao novo Upper Bound calculado, juntamente com esse valor atualizado do limite superior.

O método Find Pivots é responsável por identificar os nós pivôs que serão utilizados para particionar o problema e reduzir o nível de recursão. Ele recebe como parâmetros um limite superior e um conjunto inicial de nós, denominado Border. O método possui um laço principal e define três conjuntos de nós: o conjunto de todos os nós completados a serem retornados ao final do método (Complete Nodes), os nós completados na iteração anterior do laço (Prev Nodes) e os nós que estão sendo completados na iteração atual do laço (Current Nodes). Os conjuntos Prev Nodes e Complete Nodes são inicialmente preenchidos com os nós recebidos como parâmetro, enquanto Current Nodes sem nenhum nó. Enquanto o número de iterações for menor que K, o método executa as operações a seguir:

1 - O método percorre todos os nós do conjunto Prev Nodes e, para cada um deles, itera sobre os nós a eles conectados. Para cada nó conectado, calcula-se a distância como a soma da distância até o nó conhecido (pertencente a Prev Nodes) e o peso da aresta que os conecta.

2 - Se a nova distância calculada for menor do que a distância previamente conhecida para o nó, esse valor é atualizado. Além disso, caso a nova distância seja inferior ao limite superior, o nó é adicionado ao conjunto Current Nodes.

3 - Todos os nós do conjunto Current Nodes são adicionados ao conjunto Complete Nodes, e Prev Nodes é atualizado para conter os nós do conjunto Current Nodes. Além disso, antes da próxima iteração do laço, verifica-se se o tamanho do conjunto Complete Nodes excede K multiplicado pelo número de nós em Border; caso isso ocorra, o método retorna todo o conjunto Border, garantindo que todos os nós nele contidos sejam utilizados como pivôs, juntamente com o conjunto Complete Nodes.

Após a conclusão do laço, caso o tamanho do conjunto Complete Nodes seja inferior ao limite, os pivôs são selecionados. Para cada nó completado, verifica-se se ele é raiz de uma árvore formada no grafo com tamanho maior ou igual a K; caso positivo, o nó é adicionado ao conjunto de pivôs. Por fim, o método Find Pivots retorna todos os pivôs identificados, bem como todos os nós completados durante o processo.


# Experimentação

## Metodologia

  Para a experimentação entre os 2 algoritmos, foram criados em tempo de execução grafos esparsos aleatórios. Todos os grafos foram criados com `n` nós e `2n` arestas de pesos não negativos aleatórios, primeiramente garantiu-se que havia um caminho para todos os nós adicionados e, depois, as arestas restantes foram adicionadas de forma aleatória, assim, a quantidade de arestas que entram e saem dos nós se mantém na mesma ordem de magnitude ao longo de todo o grafo. Essas especificações foram necessárias para promover uma entrada extensa, randômica, mas no cenário em que, segundo o artigo, o novo algoritmo melhor funciona, com grafos esparsos de graus constantes entre os nós. Com isso, foi possível analisar empiricamente os dois algoritmos exatamente nas situações propostas pelo artigo.

  Dessa forma, foram criados grafos de tamanho `1.000` a `1.000.000` com intervalos de `1.000` entre eles, sendo cada um executado `20` vezes em cada algoritmo, obtendo-se a média do tempo de execução de cada um, bem como a verificação de incongruências nos resultados do **BMSSP**

## Resultados

  Os dados puros podem ser vistos em [RESULTS](results.csv). Percebe-se a ausência de erros ao longo de toda a experimentação, 

# Considerações Finais

  Portanto, conclui-se que o algoritmo BMSSP não apenas otimiza a resolução de problemas de menor caminho, mas também se apresenta como uma alternativa congruente e eficiente para futuras aplicações acadêmicas e de mercado que exijam alto desempenho no processamento de grafos.
