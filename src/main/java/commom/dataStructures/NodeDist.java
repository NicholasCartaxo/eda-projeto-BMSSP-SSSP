package main.java.commom.dataStructures;

import java.util.Objects;

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

        if(numNodes != o.numNodes){
            return Integer.compare(numNodes, o.numNodes);
        }

        return compareNode(o);
    }
    
    private int compareNode(NodeDist o){
        NodeDist currentThis = this;
        NodeDist currentOther = o;

        while (currentThis != null && currentOther != null) {
            int nodeComparison = Objects.compare(
                currentThis.node, 
                currentOther.node, 
                java.util.Comparator.nullsFirst(Node::compareTo)
            );

            if (nodeComparison != 0) {
                return nodeComparison;
            }

            currentThis = currentThis.prev;
            currentOther = currentOther.prev;
        }

        if (currentThis == null && currentOther != null) {
            return -1;
        }
        if (currentThis != null && currentOther == null) {
            return 1;
        }
        
        return 0;
    }

    
}