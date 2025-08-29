package main.java.commom.graph;

import java.util.LinkedList;
import java.util.List;

public class Node implements Comparable<Node> {
    
    public final int id;
    public List<Edge> outEdges;

    public Node(int id){
        this.id = id;
        outEdges = new LinkedList<Edge>();
    }

    public void addEdge(Edge e){
        outEdges.add(e);
    }

    @Override
    public int compareTo(Node n2) {
        return Integer.valueOf(id).compareTo(Integer.valueOf(n2.id));
    }

}
