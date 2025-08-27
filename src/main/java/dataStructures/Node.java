package main.java.dataStructures;

import java.util.LinkedList;
import java.util.List;

public class Node {
    
    public final int id;
    public List<Edge> outEdges;

    public Node(int id){
        this.id = id;
        outEdges = new LinkedList<Edge>();
    }

    public void addEdge(Edge e){
        outEdges.add(e);
    }

}
