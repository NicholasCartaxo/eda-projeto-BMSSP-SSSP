package main.java.commom.graph;

import java.util.HashMap;

public class Graph {
    public int numNodes;
    public int numEdges;

    public HashMap<Integer,Node> nodesById;

    public void addNode(int id){
        createNode(id);
    }

    public void addEdge(int idFrom, int idTo, int weight){
        Node nodeFrom = nodesById.computeIfAbsent(idFrom, (Integer id)->createNode(id));
        Node nodeTo = nodesById.computeIfAbsent(idTo, (Integer id)->createNode(id));

        numEdges++;
        nodeFrom.addEdge(new Edge(nodeFrom, nodeTo, weight));        
    }

    private Node createNode(int id){
        nodesById.put(id,new Node(id));
        numNodes++;
        return null;
    }
}
