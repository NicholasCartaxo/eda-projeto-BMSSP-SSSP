package main.java.commom.graph;

public class Edge {
    public final Node nodeFrom;
    public final Node nodeTo;
    public final long weight;

    public Edge(Node nodeFrom, Node nodeTo, long weight){
        this.nodeFrom = nodeFrom;
        this.nodeTo = nodeTo;
        this.weight = weight;
    }
}
