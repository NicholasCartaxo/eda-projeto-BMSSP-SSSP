package main.java.commom.dataStructures;

import main.java.commom.graph.Edge;
import main.java.commom.graph.Node;

public class NodeDist implements Comparable<NodeDist> {
    public final Node node;
    public final long dist;
    public final NodeDist prev;

    public NodeDist(Node node, long dist, NodeDist prev){
        this.node = node;
        this.dist = dist;
        this.prev = prev;
    }

    public NodeDist addEdge(Edge edge){
        return new NodeDist(edge.nodeTo, dist+edge.weight, this);
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
    public int compareTo(NodeDist o){
        if(dist != o.dist){
            return Long.compare(dist, o.dist);
        }
        
        return node.compareTo(o.node);
    }
    
}