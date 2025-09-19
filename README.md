# EDA Projeto BMSSP para SSSP
Implementação, documentação e experimentação do Bounded Multi-Source Shortest Path (BMSSP) como algoritmo que resolve o problema de Single Source Shortest Path (SSSP) em comparação com Dijkstra.

Esse repositório utiliza o artigo [Breaking the Sorting Barrier for Directed Single-Source Shortest Paths](https://arxiv.org/abs/2504.17033) como base para implementar e documentar o BMSSP resolvendo o problema de menor caminho para uma única fonte. Além disso, é feita uma comparação experimental com Dijkstra, a fim de analisar a superação da barreira de tempo para SSSP prometida.

#ANALISE PRIMARIA
  Algoritmos de menor caminho identificam o caminho mais curto entre pontos em um grafo. O caminho mais curto se refere à soma dos pesos das arestas (custo, tempo ou distância). Essa questão é de suma importância para diversas aplicações, e a eficiência com que o menor caminho é encontrado é crucial para o bom funcionamento de redes de fluxos,  as quais se aplicam à logística de transportes de produtos, à escolha de rotas em redes digitais e aos algoritmos de recomendação de redes sociais. O recém-lançado Bounded Multi-Source Shortest Path Algorithm (BMSSP) é um algoritmo para identificar o caminho mais curto que quebra a barreira de tempo de Dijkstra, e a falta de implementações e documentações sobre ele motivaram esse estudo. Para essa análise, o grupo implementou o algoritmo BMSSP em Java, documentou o código e realizou testes de comparação de eficiência com o algoritmo de menor caminho de Dijkstra. 

#BMSSP
  A busca pelo menor caminho em grafos direcionados com pesos não negativos é um problema central na ciência da computação e possui aplicações diretas em diversas áreas, como logística de transportes, redes de comunicação e sistemas de recomendação. Nesse contexto, o algoritmo de Dijkstra consolidou-se como o método padrão para resolver o problema de caminho mínimo a partir de uma única fonte, devido à sua eficiência e simplicidade. 
  Apesar do bom desempenho em casos práticos, o algoritmo de Dijkstra possui uma complexidade assintótica O(mlog n), sendo ‘‘n’’ o número de nós e “m” o número de arestas. Essa complexidade apresenta um desafio para grafos densos ou com grande volume de dados. O artigo Breaking the Sorting Barrier for Directed Single-Source Shortest Paths, que introduz um novo algoritmo teórico com complexidade de tempo O(mlog2/3n), supera essa limitação. No entanto, a ausência de implementações concretas e documentadas desse algoritmo dificulta sua compreensão e análise prática, limitando o acesso ao conteúdo por parte da comunidade acadêmica e técnica.
  Diante desse cenário, este projeto tem como objetivo realizar um estudo aprofundado do algoritmo proposto no artigo referenciado, abordando sua lógica, suas estruturas de dados e suas estratégias de otimização em comparação com o algoritmo de Dijkstra. Para isso, será desenvolvida uma implementação, com ênfase na clareza e na documentação, permitindo a validação de seu funcionamento. Além disso, serão realizados testes com grafos densos para os dois algoritmos, com o objetivo de análise pŕatica de eficiência.

  O processo de execução do projeto ocorreu por meio da divisão de tarefas dentro de subgrupos do projeto, com reuniões semanais (sprints) e a especificação dos problemas/soluções para o código. Todas as ferramentas para essa produção acadêmica se deram de maneira consensual entre os participantes, a partir de discussões sobre a revisão do desempenho e da dificuldade no cumprimento das especificações do algoritmo de menor caminho.
  Nesse viés, esse projeto foi ramificado em 3 partes práticas e 2 partes de análise e conclusão, sendo cada uma focada em uma tarefa ou funcionalidade específica. Dentre os  objetivos principais do plano de execução do BMSSP, temos, em ordem de implementação no projeto, o/a: 
  1. Prática
    Criação da estrutura base para a execução do algoritmo, incluindo Árvore PV modificada, FibonacciHeap, DQueue (fila de prioridade) , BatchList (Lista de Lotes de blocos para os nós), etc;
    Implementação do algoritmo de menor caminho BMSP, dividido nos métodos primários:	
      - Find Pivots();
      - Base Case();
      - BMSSP();
      - Produção de uma bateria de testes, que simula casos da vida real;
  2. Análise
      - Criação(Plot) de gráficos para os casos de testes tanto para o BMSSP quanto para o algoritmo de Dijkstra;
      - Análise de comparação entre os dois algoritmos com base nos gráficos gerados, resultando em uma classificação de eficiência;

  Em primeiro lugar, foi realizada a implementação das estruturas básicas. Essa etapa foi dividida entre os integrantes, que se organizaram de maneira a cada um ficar com uma parte da estrutura, auxiliando na otimização do projeto. 
  Após a codificação das estruturas, o próximo passo se deu a partir da verificação do paper¹ do algoritmo e a elaboração dos métodos base para a aplicação correta do código.
  Por último, foi executada a bateria de testes de carga com o intuito de verificar se o BMSSP cumpre com o seu objetivo para casos em que o valor da quantidade máxima de nós (Qn)  A aresta de maior distância (Amax), não exceda o espaço amostral de 10¹⁶. 
  No fim, houve a plotagem dos gráficos de comparação  com o Dijkstra,  e a análise comparativa em questão de eficiência.


