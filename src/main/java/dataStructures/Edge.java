package main.java.dataStructures;

public class Edge {
    public final Node nodeFrom;
    public final Node nodeTo;
    public final int weight;

    public Edge(Node nodeFrom, Node nodeTo, int weight){
        this.nodeFrom = nodeFrom;
        this.nodeTo = nodeTo;
        this.weight = weight;
    }
}
