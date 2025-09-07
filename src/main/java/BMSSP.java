package main.java;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

import main.java.commom.dataStructures.FibonacciHeap;
import main.java.commom.dataStructures.NodeDist;
import main.java.commom.dataStructures.Pair;
import main.java.commom.graph.Edge;
import main.java.commom.graph.Node;

public class BMSSP {

    private HashMap<Node, Integer> dists;
    private int k;

    private Pair<Integer, HashSet<Node>> baseCase(int upperBound, Node pivot ){
        HashSet<Node> completeNodes = new HashSet<Node>(); 
        

        FibonacciHeap<NodeDist> minHeap = new FibonacciHeap<NodeDist>();
        minHeap.insert(new NodeDist(pivot, dists.get(pivot)), dists.get(pivot));

        int newUpperBound = dists.get(pivot);

        while(!minHeap.isEmpty() && completeNodes.size() < k + 1){
            NodeDist currentNodeDist = minHeap.extractMinValue();
            Node currentNode = currentNodeDist.node;
            int currentDist = currentNodeDist.dist;
            
            completeNodes.add(currentNode);
            newUpperBound = Math.max(newUpperBound, currentDist);

            for(Edge edge : currentNode.outEdges){
                int weight = edge.weight;
                Node secondNode = edge.nodeTo;
                int secondDist = dists.get(secondNode);
                int newDist = currentDist + weight;
                if(newDist <= secondDist && newDist < upperBound){
                    dists.put(secondNode, newDist);
                    NodeDist newNodeDist = new NodeDist(secondNode,newDist);
                    if(!minHeap.containsValue(newNodeDist)){
                        minHeap.insert(newNodeDist, newNodeDist.dist);
                    }else{
                        minHeap.decreaseKeyByValue(newNodeDist, newNodeDist.dist);
                    }

                } 

            }
            
        }

        if(completeNodes.size() <= k){
            return new Pair<Integer, HashSet<Node>>(upperBound, completeNodes);
        }else{
            HashSet<Node> newCompleteNodes = new HashSet<Node>();
            for (Node node : completeNodes) {
                if(dists.get(node) < newUpperBound) newCompleteNodes.add(node);
            }
            return new Pair<Integer, HashSet<Node>>(newUpperBound, newCompleteNodes);
        }


    }

}

