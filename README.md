# EDA Projeto BMSSP para SSSP
  Implementação, documentação e experimentação do **Bounded Multi-Source Shortest Path** (**BMSSP**) como algoritmo que resolve o problema de **Single Source Shortest Path** (**SSSP**) em comparação com **Dijkstra**.

  Esse repositório utiliza o artigo [Breaking the Sorting Barrier for Directed Single-Source Shortest Paths](https://arxiv.org/abs/2504.17033) como base para implementar e documentar o **BMSSP** resolvendo o problema de menor caminho para uma única fonte. Além disso, é feita uma comparação experimental com **Dijkstra**, a fim de analisar a superação da barreira de tempo para **SSSP** prometida.

# Documentação

## Introdução
  Algoritmos de menor caminho identificam o caminho mais curto entre pontos em um grafo. O caminho mais curto se refere à soma dos pesos das arestas (custo, tempo ou distância). Essa questão é de suma importância para diversas aplicações, e a eficiência com que o menor caminho é encontrado é crucial para o bom funcionamento de redes de fluxos,  as quais se aplicam à logística de transportes de produtos, à escolha de rotas em redes digitais e aos algoritmos de recomendação de redes sociais. O recém-lançado **BMSSP** é um algoritmo para identificar os caminho mais curtos para fonte única que quebra a barreira de tempo de **Dijkstra**, e a falta de implementações e documentações sobre ele motivaram esse estudo. Para essa análise, o grupo implementou o programa em Java, documentou seu funcionamento e realizou testes de comparação de eficiência com o algoritmo de menor caminho de **Dijkstra**. 

## BMSSP
  A busca pelos menores caminho de fonte única em grafos direcionados com pesos não negativos é um problema central na ciência da computação. Nesse contexto, o algoritmo de **Dijkstra** consolidou-se como o método padrão para resolver o problema de caminho mínimo a partir de uma única fonte, devido à sua eficiência. 
  O algoritmo já tradicional de **Dijkstra** possui uma complexidade de tempo $O(m\log n)$, sendo **n** o número de nós e **m** o número de arestas, o que era considerado a barreira para esse tipo de problema. O artigo **Breaking the Sorting Barrier for Directed Single-Source Shortest Paths** introduz um novo algoritmo teórico com complexidade de tempo $O(m\log^{2/3}n)$, quebrando essa barreira. No entanto, a ausência de implementações concretas e documentadas desse algoritmo dificulta sua compreensão e análise prática, limitando o acesso ao conteúdo por parte da comunidade acadêmica e técnica.
  Diante disso, este projeto tem como objetivo realizar a implementação do algoritmo proposto no artigo referenciado, seguindo sua lógica, suas estruturas de dados e suas estratégias de otimização de forma fidedigna. Para isso, o código será feito com Java, utilizando otimizações de implementação e da linguagem quando possível, sem alterar sua complexidade, permitindo a validação de seu funcionamento. Além disso, são realizados testes comparativos entre os algoritmos, utilizando grafos esparsos, já que a diferença de complexidade é mais proeminente com **m** pequeno, e com graus parecidos entre os nós, já que o artigo assume essa configuração que se satisfaça a complexidade.

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

## Ideia geral do Algoritmo

  Para conseguir entender as especificidades do algoritmo, primeiramente, é necessário um entendimento mais abstrato de como ele funciona, já que é incomum um algoritmo de menor caminho usar a estratégia de divisão e conquista.

  A ideia geral é dividir o problema em pequenas partes, com base em limites de distâncias, até que a parte seja pequena o bastante para uma simples computação de **Dijkstra**, também limitando, por construção, uma divisão boa o bastante para diminuir a comlexidade de tempo.

  Mais especificadamente, uma chamada do `bmssp` recebe um limite superior, o nível atual da recursão, e os nós já resolvidos. Então, serão achados, entre esses nós, aqueles que chegam a nós não resolvidos, dentro da fronteira especificada, os chamados **pivôs**, também solucionando as distância de alguns nós alcançados por eles. Com esses pivôs, uma nova chamada será feita para resolver o problema para um limite superior menor, até que o nível da recursão indique que isso seja trivial o bastante para que **Dijkstra** seja usado para o pequeno limite. Assim, com os nós sendo completados para esses limites menores, eles podem ser usados como pivôs dos limites maiores, gradualmente resolvendo todo o problema.
  
  Por isso, o retorno da função contém os novos nós completados e o limite superior por eles alcançado, já que para essa estratégia funcionar, é necessário garantir que, dadas as distâncias $b<B$, todos os caminhos com distâncias entre **b** e **B** passam por um nó com distância de até **b**, então, cada chamada recursiva resolve o problema para um certo **b**, possibilitando a chamada anterior a solucionar os nós da fronteira **B** a partir desses caminhos, conquistando de forma mais otimizada o problema.

  Assim, a primeira chamada do `bmssp` contem um limite superior infinito, já que almeja-se solucionar todos os nós, independente da distância, e o único nó completado é a origem, com distância nula.

## Implementação

  A implementação do algoritmo é dividida em três métodos principais, o `bmssp`, o `baseCase` e o `findPivots`. O `bmssp` é o método que faz uso dos outros dois métodos e controla a recursão principal do algoritmo, sendo responsável por dividir o problema central em problemas menores até que o caso base seja alcançado, ou seja, o nível da recursão seja igual a zero, no qual é feito algo similar a **Dijkstra**, com um certo limite de distância e nós acessados. Em cada chamada recursiva, esse método acha distâncias menores dentro de um limite superior (**upper bound**), utilizando nós de fronteira de chamadas recursivas ou os pivôs iniciais do `findPivots`. Para, as distâncias para todos os pivôs encontrados são inseridas na `DQueue`, uma fila adaptada que permite, com uma única consulta, retornar os **M** menores elementos armazenados de forma eficiente. No laço principal, ele acessa os **M** menores elementos da `DQueue` e executa recursivamente o `bmssp`, passando como parâmetro o nível de recursão atual decrementado em uma unidade e os `M` menores elementos acessados. Ao final, ele verifica se as novas distâncias encontradas no nível inferior da recursão são, de fato, as menores até o momento. Então, o novo **upper bound** calculado e os novos nós completados na recursão atual são retornados, para que a parte de conquista do algoritmo continue. Isso se repete até a primeira chamada, que tem um limite superior infinito, encontrando, então, a menor distância para todos os nós. 
  
  Assim, quando o nível é zero, o `baseCase` é executado, calculando as distâncias para uma quantidade limitada de nós com base em um nó central e em um limite superior passados como parâmetros. Dessa forma, enquanto o número máximo de nós e iterações não for atingido, esse método calcula a distância para todos os nós ligados ao pivô e, na próxima iteração, calcula a distância para os nós ligados aos novos nós encontrados e assim sucessivamente. Para isso, ele utiliza, semelhante a **Dijkstra** um heap binário mínimo, implementando a mesma estratégia de continuar pelo nó de menor distância ainda não analisado.
  
  O `findPivots`, por sua vez, é o método responsável por encontrar os nós pivôs que serão usados para particionar o problema e reduzir o nível de recursão, também retornando os nós que foram completados nessa sub-rotina. Enquanto o número de iterações e o número de nós completados não atingir o limite máximo, esse método permanece calculando a menor distância dos nós de fronteira (border). Se, no fim, não houver pivôs o suficiente, o método busca por raízes das árvores dos menores caminhos com uma certa quantidade de nós para serem os pivôs. 
  
  Por fim, foi implementado o método `solve`, que recebe o nó de origem e o grafo onde o algoritmo implementado irá operar. Esse método inicializa as estruturas de dados iniciais, define as distâncias para cada nó com um valor infinito e executa o método `bmssp` com os parâmetros iniciais. 

# Experimentação

## Metodologia

## Resultados

# Considerações Finais

  Portanto, conclui-se que o algoritmo BMSSP não apenas otimiza a resolução de problemas de menor caminho, mas também se apresenta como uma alternativa congruente e eficiente para futuras aplicações acadêmicas e de mercado que exijam alto desempenho no processamento de grafos.
