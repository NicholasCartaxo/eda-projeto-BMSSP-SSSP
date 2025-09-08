package main.java;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import main.java.commom.dataStructures.FibonacciHeap;
import main.java.commom.dataStructures.NodeDist;
import main.java.commom.dataStructures.Pair;
import main.java.commom.graph.Edge;
import main.java.commom.graph.Node;
import main.java.commom.graph.Graph;
import main.java.DQueue.DQueue;

public class BMSSP {
    private Graph graph;
    private HashMap<Node, Double> dists;
    private double k;
    private double t;
    
    public BMSSP(){
    
    }

    public HashMap<Integer, Double> solve(Graph graph, Node origin){
        this.graph = graph;
        dists = new HashMap<Node, Double>();

        for(Node node : graph.nodesById.values()){
            dists.put(node, Double.POSITIVE_INFINITY);
        }
        dists.put(origin,0.0);

        k = Math.floor(Math.pow(log2(graph.numNodes), 1/3));
        t = Math.floor(Math.pow(log2(graph.numNodes), 2/3));

        double level = Math.ceil(log2(graph.numNodes)/t);

        HashSet<Node> nodes = new HashSet<Node>();
        nodes.add(origin);
        bmssp(level, Double.POSITIVE_INFINITY, nodes);

        HashMap<Integer,Double> idDists = new HashMap<Integer,Double>();
        for(Map.Entry<Node,Double> distPair : dists.entrySet()){
            int id = distPair.getKey().id;
            double dist = distPair.getValue();
            idDists.put(id,dist);
        }

        return idDists;
    }


    private Pair<Double,HashSet<Node>> bmssp(double level,double upperBound, HashSet<Node> initialNodes){
        if(level == 0){
            return baseCase(upperBound,initialNodes);
        }
        Pair<HashSet<Node>,HashSet<Node>> pivotsPair = findPivots(upperBound,initialNodes);
        HashSet<Node> pivots = pivotsPair.first;
        HashSet<Node> completeByPivots = pivotsPair.second;
    
        int blockSize = (int)Math.pow(2,(level-1)*t);
        DQueue dQueue = new DQueue(blockSize, upperBound);
        
        double currentUpperBound = Double.MAX_VALUE;
        for(Node pivot : pivots){
            currentUpperBound = Math.min(currentUpperBound, dists.get(pivot));
            dQueue.insert(new NodeDist(pivot,dists.get(pivot)));
        }

        if(pivots.isEmpty()){
            currentUpperBound = upperBound;
        }

        HashSet<Node> newCompleteNodes = new HashSet<Node>();
        double newCompleteMaxUpperBound = k*Math.pow(2,level*t);
        while((newCompleteNodes.size() < newCompleteMaxUpperBound) && (!dQueue.isEmpty())){
            Pair<Double,HashSet<Node>> prevBoundSet = dQueue.pull();
            double prevUpperBound = prevBoundSet.first;
            HashSet<Node> prevNodes = prevBoundSet.second;

            Pair<Double,HashSet<Node>> currentBoundSet = bmssp(level-1,prevUpperBound,prevNodes);
            currentUpperBound = currentBoundSet.first;
            HashSet<Node> currentNodes = currentBoundSet.second;

            newCompleteNodes.addAll(currentNodes);
            HashSet<NodeDist> newNodeDists = new HashSet<NodeDist>();
            
            for(Node node : currentNodes){
                for(Edge edge : node.outEdges){
                    Node nodeTo = edge.nodeTo;
                    double weight = edge.weight;

                    double newDist = dists.get(node) + weight;
                    if(newDist <= dists.get(nodeTo)){
                        dists.put(nodeTo,newDist);
                        if(prevUpperBound <= newDist && newDist < upperBound) dQueue.insert(new NodeDist(nodeTo,newDist));
                        else if(currentUpperBound <= newDist && newDist < prevUpperBound) newNodeDists.add(new NodeDist(nodeTo,newDist));
                    }
                }
            }   
            
            for(Node node : prevNodes){
                double nodeDist = dists.get(node);
                if(currentUpperBound <= nodeDist && nodeDist < prevUpperBound) newNodeDists.add(new NodeDist(node, nodeDist));
            } 
            dQueue.batchPrepend(newNodeDists);
        }   

        double minUpperBound = Math.min(currentUpperBound,upperBound);
        for(Node node : completeByPivots){
            if(dists.get(node) < minUpperBound) newCompleteNodes.add(node);
        }

        return new Pair<Double,HashSet<Node>>(minUpperBound, newCompleteNodes);
    }

    private Pair<Double, HashSet<Node>> baseCase(double upperBound, HashSet<Node> pivotsSingleton ){
        HashSet<Node> completeNodes = new HashSet<Node>(); 
        
        Node pivot = pivotsSingleton.iterator().next();
        
        FibonacciHeap<NodeDist> minHeap = new FibonacciHeap<NodeDist>();
        minHeap.insert(new NodeDist(pivot, dists.get(pivot)), dists.get(pivot));

        double newUpperBound = dists.get(pivot);

        while(!minHeap.isEmpty() && completeNodes.size() < k + 1){
            NodeDist currentNodeDist = minHeap.extractMinValue();
            Node currentNode = currentNodeDist.node;
            double currentDist = currentNodeDist.dist;
            
            completeNodes.add(currentNode);
            newUpperBound = Math.max(newUpperBound, currentDist);

            for(Edge edge : currentNode.outEdges){
                double weight = edge.weight;
                Node secondNode = edge.nodeTo;
                double secondDist = dists.get(secondNode);
                double newDist = currentDist + weight;
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
            return new Pair<Double, HashSet<Node>>(upperBound, completeNodes);
        }else{
            HashSet<Node> newCompleteNodes = new HashSet<Node>();
            for (Node node : completeNodes) {
                if(dists.get(node) < newUpperBound) newCompleteNodes.add(node);
            }
            return new Pair<Double, HashSet<Node>>(newUpperBound, newCompleteNodes);
        }
    }

    private Pair<HashSet<Node>,HashSet<Node>> findPivots(double upperBound, HashSet<Node> border) {
        HashSet<Node> completeNodes = new HashSet<Node>(border);
        HashSet<Node> prevNodes = new HashSet<Node>(border);

        for(double i = 0; i < k; i++){
            HashSet<Node> currentNodes = new HashSet<>();

            for (Node node : prevNodes) {
                for (Edge edge : node.outEdges) {
                    Node nodeTo = edge.nodeTo;
                    double newDistance = dists.get(edge.nodeFrom) + edge.weight;
                    
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
                double weight = edge.weight;

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

    public static double log2(double n){
        return Math.log(n) / Math.log(2);
    }
}

