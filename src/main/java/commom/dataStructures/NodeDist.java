package main.java.commom.dataStructures;

import main.java.commom.graph.Edge;
import main.java.commom.graph.Node;

public class NodeDist implements Comparable<NodeDist> {
    public final Node node;
    public final long dist;
    public final int numNodes;
    public final NodeDist prev;

    public NodeDist(Node node, long dist, int numNodes, NodeDist prev){
        this.node = node;
        this.dist = dist;
        this.numNodes = numNodes;
        this.prev = prev;
    }

    public NodeDist addEdge(Edge edge){
        return new NodeDist(edge.nodeTo, dist+edge.weight, numNodes+1, this);
    }

    @Override
    public String toString() {
        return "NodeDist [node=" + (node == null ? -1 : node.id) + ", dist=" + dist + "]";
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
        if(this == o) return 0;

        if(dist != o.dist){
            return Long.compare(dist, o.dist);
        }

        if(numNodes != o.numNodes){
            return Integer.compare(numNodes, o.numNodes);
        }
        
        NodeDist currentThis = this;
        NodeDist currentOther = o;
        while (currentThis != null && currentOther != null) {
            if(currentThis == currentOther) return 0;

            if(currentThis.node.id != currentOther.node.id){
                return currentThis.node.compareTo(currentOther.node);
            }

            currentThis = currentThis.prev;
            currentOther = currentOther.prev;
        }

        if (currentThis == null && currentOther != null) {
            return -1;
        }else{
            return 1;
        }

    }
    
}