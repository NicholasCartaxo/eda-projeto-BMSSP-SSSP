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

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Node other = (Node) obj;
        if (id != other.id)
            return false;
        return true;
    }

}