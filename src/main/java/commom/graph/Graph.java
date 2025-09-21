package main.java.commom.graph;

import java.util.LinkedList;

public class Graph {
    public final int numNodes;
    public final LinkedList<NodeDist> adjacent[];

    @SuppressWarnings("unchecked")
    public Graph(int numNodes){
        this.numNodes = numNodes;
        adjacent = new LinkedList[this.numNodes];
        for(int i=0;i<numNodes;i++){
            adjacent[i] = new LinkedList<NodeDist>();
        }
    }

    public void addEdge(int idFrom, int idTo, long weight){
        adjacent[idFrom].add(new NodeDist(idTo,weight));     
    }

}