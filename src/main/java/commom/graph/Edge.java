package main.java.commom.graph;

public class Edge {
    public final Node nodeFrom;
    public final Node nodeTo;
    public final double weight;

    public Edge(Node nodeFrom, Node nodeTo, double weight){
        this.nodeFrom = nodeFrom;
        this.nodeTo = nodeTo;
        this.weight = weight;
    }
}
