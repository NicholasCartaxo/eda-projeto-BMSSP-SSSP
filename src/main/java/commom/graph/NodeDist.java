package main.java.commom.graph;

public class NodeDist implements Comparable<NodeDist>{
    public final int node;
    public final long dist;

    public NodeDist(int node, long dist) {
        this.node = node;
        this.dist = dist;
    }

    @Override
    public int compareTo(NodeDist o) {
        int distCompare = Long.compare(dist, o.dist);
        if(distCompare != 0){
            return distCompare;
        }
        return Integer.compare(node, o.node);
    }
}