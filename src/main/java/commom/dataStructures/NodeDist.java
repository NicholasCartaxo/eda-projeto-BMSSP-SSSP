package main.java.commom.dataStructures;

import main.java.commom.graph.Edge;
import main.java.commom.graph.Node;

public class NodeDist implements Comparable<NodeDist> {
    public final Node node;
    public final long dist;
    public final int numEdges;
    public final NodeDist prev;

    public NodeDist(Node node, long dist, int numEdges, NodeDist prev){
        this.node = node;
        this.dist = dist;
        this.numEdges = numEdges;
        this.prev = prev;
    }

    public NodeDist addEdge(Edge edge){
        return new NodeDist(edge.nodeTo, dist+edge.weight, numEdges+1, this);
    }

    @Override
    public String toString() {
        return "NodeDist [node=" + node.id + ", dist=" + dist + "]";
    }

    @Override
    public int hashCode() {
        return node.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NodeDist other = (NodeDist) obj;
        if (node == null) {
            if (other.node != null)
                return false;
        } else if (!node.equals(other.node))
            return false;
        return true;
    }

    @Override
    public int compareTo(NodeDist o) {
        if(dist != o.dist){
            return Long.compare(dist, o.dist);
        }

        if(numEdges != o.numEdges){
            return Integer.compare(numEdges, o.numEdges);
        }

        if(!node.equals(o.node)){
            return node.compareTo(o.node);
        }
        if(prev == null && o.prev == null) return 0;
        return prev.compareTo(o.prev);
    }

    
}