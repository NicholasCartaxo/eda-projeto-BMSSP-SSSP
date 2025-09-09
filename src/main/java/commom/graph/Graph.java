package main.java.commom.graph;

import java.util.HashMap;

public class Graph {
    public int numNodes;
    public int numEdges;

    public HashMap<Integer,Node> nodesById;

    public Graph(){
        numNodes = 0;
        numEdges = 0;
        nodesById = new HashMap<Integer,Node>();
    }

    public void addNode(int id){
        nodesById.put(id,new Node(id));
        numNodes++;
    }

    public void addEdge(int idFrom, int idTo, long weight){
        if(!nodesById.containsKey(idFrom)){
            addNode(idFrom);
        }
        if(!nodesById.containsKey(idTo)){
            addNode(idTo);
        }
        Node nodeFrom = nodesById.get(idFrom);
        Node nodeTo = nodesById.get(idTo);

        numEdges++;
        nodeFrom.addEdge(new Edge(nodeFrom, nodeTo, weight));        
    }

}