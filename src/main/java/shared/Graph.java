package main.java.shared;

public class Graph {
    
    private final int numNodes;
    private final int numEdges;
    private final Node[] nodes;

    private int edgesStored;

    public Graph(int numNodes, int numEdges){
        this.numNodes = numNodes;
        this.numEdges = numEdges;

        this.edgesStored = 0;

        this.nodes = new Node[this.numNodes];
        for(int i=0;i<this.numNodes;i++){
            this.nodes[i] = new Node(i);
        }
    }

    public void addEdge(int nodeId1, int nodeId2, int weight){
        if(validIndex(nodeId1) || validIndex(nodeId2)) throw new IndexOutOfBoundsException("Identificador de nó inválido.");
        if(this.edgesStored >= this.numEdges) throw new IllegalStateException("Todas as arestas já foram adicionadas ao grafo.");

        Edge newEdge = new Edge(this.nodes[nodeId1],this.nodes[nodeId2], weight);
        this.nodes[nodeId1].addEdge(newEdge);
        this.edgesStored++;
    }

    public Node getNode(int index){
        if(validIndex(index)) throw new IndexOutOfBoundsException("Identificador de nó inválido.");
        return this.nodes[index];
    }

    private boolean validIndex(int index){
        return index < 0 || index >= this.numNodes;
    }

}
