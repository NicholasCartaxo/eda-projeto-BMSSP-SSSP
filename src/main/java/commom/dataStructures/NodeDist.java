package main.java.commom.dataStructures;

import main.java.commom.graph.Node;

public class NodeDist implements Comparable<NodeDist> {
    public final Node node;
    public final long dist;

    public NodeDist(Node node, long dist){
        this.node = node;
        this.dist = dist;
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
        if(dist == o.dist)
            return node.compareTo(o.node);
        return Long.valueOf(dist).compareTo(Long.valueOf(o.dist));
    }

    
}