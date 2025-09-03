package main.java;

import java.util.HashMap;
import java.util.HashSet;

import main.java.DQueue.DQueue;
import main.java.commom.dataStructures.NodeDist;
import main.java.commom.dataStructures.Pair;
import main.java.commom.graph.Edge;
import main.java.commom.graph.Node;

public class BMSSP {
    private HashMap<Node, Integer> dists;
    private int k;
    private int t;

    private Pair<Integer,HashSet<Node>> bmssp(int level,int upperBound, HashSet<Node> completeNodes){
        if(level == 0){
            return baseCase(upperBound,completeNodes);
        }
        Pair<HashSet<Node>,HashSet<Node>> pivotsPair = findPivots(upperBound,completeNodes);
        HashSet<Node> pivots = pivotsPair.first;
        HashSet<Node> completeByPivots = pivotsPair.second;
    
        int blockSize = (int)Math.pow(2,(level-1)*t);
        DQueue dQueue = new DQueue(blockSize, upperBound);
        
        int minUpperBound = Integer.MAX_VALUE;
        for(Node pivot : pivots){
            minUpperBound = Math.min(minUpperBound, dists.get(pivot));
            dQueue.insert(new NodeDist(pivot,dists.get(pivot)));
        }

        if(pivots.isEmpty()){
            minUpperBound = upperBound;
        }
        int i=0;
        HashSet<Node> newCompleteNodes = new HashSet<Node>();
        int newCompleteMaxUpperBound = k*(int)Math.pow(level,t);
        while(newCompleteNodes.size() < newCompleteMaxUpperBound && !dQueue.isEmpty()){
            i++;
            Pair<Integer,HashSet<Node>> prevBoundSet = dQueue.pull();
            int prevUpperBound = prevBoundSet.first;
            HashSet<Node> prevNodes = prevBoundSet.second;

            Pair<Integer,HashSet<Node>> currentBoundSet = bmssp(level-1,prevUpperBound,prevNodes);
            int currentUpperBound = currentBoundSet.first;
            HashSet<Node> currentNodes = currentBoundSet.second;

            newCompleteNodes.addAll(currentNodes);
            HashSet<NodeDist> newNodeDists = new HashSet<NodeDist>();
            
            for(Node node : currentNodes){
                for(Edge edge : node.outEdges){
                    Node nodeTo = edge.nodeTo;
                    int weight = edge.weight;

                    int newDist = dists.get(node) + weight;
                    if(newDist <= dists.get(nodeTo)){
                        dists.put(nodeTo,newDist);
                        if(prevUpperBound <= newDist && newDist < upperBound) dQueue.insert(new NodeDist(nodeTo,newDist));
                        else if(currentUpperBound <= newDist && newDist < prevUpperBound) newNodeDists.add(new NodeDist(nodeTo,newDist));
                    }
                }
            }   
            
            for(Node node : prevNodes){
                int nodeDist = dists.get(node);
                if(currentUpperBound <= nodeDist && nodeDist < prevUpperBound) newNodeDists.add(new NodeDist(node, nodeDist));
            } 
            dQueue.batchPrepend(newNodeDists);
            minUpperBound = Math.min(minUpperBound, currentUpperBound);
        }   

        minUpperBound = Math.min(minUpperBound,upperBound);
        for(Node node : completeByPivots){
            if(dists.get(node) < minUpperBound) newCompleteNodes.add(node);
        }

        return new Pair<Integer,HashSet<Node>>(minUpperBound, newCompleteNodes);

    }
}
