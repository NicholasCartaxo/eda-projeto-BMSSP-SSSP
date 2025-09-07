package main.java;

import java.util.HashMap;
import java.util.HashSet;

import main.java.commom.dataStructures.FibonacciHeap;
import main.java.commom.dataStructures.NodeDist;
import main.java.commom.dataStructures.Pair;
import main.java.commom.graph.Edge;
import main.java.commom.graph.Node;
import main.java.commom.graph.Graph;
import main.java.DQueue.DQueue;

public class BMSSP {
    private Graph graph;
    private HashMap<Node, Integer> dists;
    private int k;
    private int t;
    
    public BMSSP(){
    
    }

    private Pair<Integer,HashSet<Node>> bmssp(int level,int upperBound, HashSet<Node> completeNodes){
        if(level == 0){
            return baseCase(upperBound,completeNodes);
        }
        Pair<HashSet<Node>,HashSet<Node>> pivotsPair = findPivots(upperBound,completeNodes);
        HashSet<Node> pivots = pivotsPair.first;
        HashSet<Node> completeByPivots = pivotsPair.second;
    
        int blockSize = (int)Math.pow(2,(level-1)*t);
        DQueue dQueue = new DQueue(blockSize, upperBound);
        
        int currentUpperBound = Integer.MAX_VALUE;
        for(Node pivot : pivots){
            currentUpperBound = Math.min(currentUpperBound, dists.get(pivot));
            dQueue.insert(new NodeDist(pivot,dists.get(pivot)));
        }

        if(pivots.isEmpty()){
            currentUpperBound = upperBound;
        }

        HashSet<Node> newCompleteNodes = new HashSet<Node>();
        int newCompleteMaxUpperBound = k*(int)Math.pow(level,t);
        while(newCompleteNodes.size() < newCompleteMaxUpperBound && !dQueue.isEmpty()){
            Pair<Integer,HashSet<Node>> prevBoundSet = dQueue.pull();
            int prevUpperBound = prevBoundSet.first;
            HashSet<Node> prevNodes = prevBoundSet.second;

            Pair<Integer,HashSet<Node>> currentBoundSet = bmssp(level-1,prevUpperBound,prevNodes);
            currentUpperBound = currentBoundSet.first;
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
        }   

        int minUpperBound = Math.min(currentUpperBound,upperBound);
        for(Node node : completeByPivots){
            if(dists.get(node) < minUpperBound) newCompleteNodes.add(node);
        }

        return new Pair<Integer,HashSet<Node>>(minUpperBound, newCompleteNodes);
    }

    private Pair<Integer, HashSet<Node>> baseCase(int upperBound, HashSet<Node> pivotsSingleton ){
        HashSet<Node> completeNodes = new HashSet<Node>(); 
        
        Node pivot = pivotsSingleton.iterator().next();
        
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

    private Pair<HashSet<Node>,HashSet<Node>> findPivots(int upperBound, HashSet<Node> border) {
        HashSet<Node> completeNodes = new HashSet<Node>(border);
        HashSet<Node> prevNodes = new HashSet<Node>(border);

        for(int i = 0; i < k; i++){
            HashSet<Node> currentNodes = new HashSet<>();

            for (Node node : prevNodes) {
                for (Edge edge : node.outEdges) {
                    Node nodeTo = edge.nodeTo;
                    int newDistance = dists.get(edge.nodeFrom) + edge.weight;
                    
                    if (newDistance <= dists.get(nodeTo)) {
                        dists.put(nodeTo, newDistance);
                        
                        if (newDistance < upperBound) {
                            currentNodes.add(nodeTo);
                        }
                    }
                }
            }

            completeNodes.addAll(currentNodes);
            prevNodes = currentNodes;

            if (completeNodes.size() > k * border.size()) {
                return new Pair<HashSet<Node>,HashSet<Node>>(new HashSet<Node>(border), completeNodes);
            }
        }

        Graph pivotForest = buildForest(prevNodes);
        HashSet<Node> pivots = getPivots(pivotForest);
        
        return new Pair<HashSet<Node>,HashSet<Node>>(pivots, completeNodes);   
    }

    private Graph buildForest(HashSet<Node> nodes){
        Graph forest = new Graph();

        for(Node node : nodes){
            for(Edge edge : node.outEdges){
                Node nodeTo = edge.nodeTo;
                int weight = edge.weight;

                if(dists.get(node)+weight == dists.get(nodeTo) && !forest.nodesById.containsKey(nodeTo.id)){
                    forest.addEdge(node.id, nodeTo.id, weight);
                }
            }
        }

        return forest;
    }

    private HashSet<Node> getPivots(Graph forest){
        HashSet<Node> roots = new HashSet<Node>(forest.nodesById.values());

        for(Node node : forest.nodesById.values()){
            for(Edge edge : node.outEdges){
                roots.remove(edge.nodeTo);
            }
        }

        HashSet<Node> pivots = new HashSet<Node>();
        for(Node root : roots){
            if(countTreeSize(root) >= k){
                Node pivot = graph.nodesById.get(root.id);
                pivots.add(pivot);
            }
        }

        return pivots;
    }

    private int countTreeSize(Node root) {
        int count = 1;
        for (Edge edge : root.outEdges) {
            count += countTreeSize(edge.nodeTo);
        }
        return count;
    }

}

