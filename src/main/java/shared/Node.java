package main.java.shared;

import java.util.LinkedList;
import java.util.List;

public class Node{

    private final int identifier;
    private List<Edge> outEdges;

    public Node(int identifier){
        this.identifier = identifier;
        this.outEdges = new LinkedList<Edge>();
    }

    public int getIdentifier(){
        return this.identifier;
    }

    public void addEdge(Edge edge){
        this.outEdges.add(edge);
    }

    public List<Edge> getOutEdges(){
        return this.outEdges;
    }
}