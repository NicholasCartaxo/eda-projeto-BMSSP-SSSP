package main.java.commom.graph;

import java.util.LinkedList;

import main.java.commom.dataStructures.Pair;

public class Graph {
    public final int numNodes;
    public final LinkedList<Pair<Integer,Long>> adjacent[];

    @SuppressWarnings("unchecked")
    public Graph(int numNodes){
        this.numNodes = numNodes;
        adjacent = new LinkedList[this.numNodes];
        for(int i=0;i<numNodes;i++){
            adjacent[i] = new LinkedList<Pair<Integer,Long>>();
        }
    }

    public void addEdge(int idFrom, int idTo, long weight){
        adjacent[idFrom].add(new Pair<Integer,Long>(idTo,weight));     
    }

}