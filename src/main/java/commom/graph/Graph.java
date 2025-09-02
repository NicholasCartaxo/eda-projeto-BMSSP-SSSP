package main.java.commom.graph;

import java.util.Map;

public class Graph {
    public int numNodes;
    public int numEdges;

    public Map<Integer,Node> nodesById;

    public Graph(int numNodes, int numEdges){
        this.numNodes = numNodes;
        this.numEdges = numEdges;
    }

    public void addNode(int id){
        nodesById.put(id, new Node(id));
    }

    public void addEdge(int idFrom, int idTo, int weight){
        Node nodeFrom = nodesById.get(idFrom);
        Node nodeTo = nodesById.get(idTo);
        nodeFrom.addEdge(new Edge(nodeFrom, nodeTo, weight));        
    }
}
