package main.java;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

import main.java.commom.dataStructures.NodeDist;
import main.java.commom.dataStructures.Pair;
import main.java.commom.graph.Edge;
import main.java.commom.graph.Node;
import main.java.commom.graph.Graph;
import main.java.DQueue.DQueue;

public class BMSSP {

    private static final long INFINITY = 100000000000000000L;
    private static final NodeDist INFINITY_NODEDIST = new NodeDist(null, INFINITY, 0, null);
    
    private HashMap<Node, NodeDist> dists;
    private long k;
    private long t;
    
    public BMSSP(){
    
    }

    public HashMap<Integer, Long> solve(Graph graph, Node origin){
        dists = new HashMap<Node, NodeDist>();

        for(Node node : graph.nodesById.values()){
            dists.put(node, INFINITY_NODEDIST);
        }
        dists.put(origin,new NodeDist(origin, 0, 1, null));

        k = (long)Math.floor(Math.pow(log2(graph.numNodes), 1.0/3.0));
        t = (long)Math.floor(Math.pow(log2(graph.numNodes), 2.0/3.0));

        long level = (long)Math.ceil(log2(graph.numNodes)/t);

        HashSet<Node> nodes = new HashSet<Node>();
        nodes.add(origin);
        bmssp(level, INFINITY_NODEDIST, nodes);

        HashMap<Integer,Long> idDists = new HashMap<Integer,Long>();
        for(Map.Entry<Node,NodeDist> distPair : dists.entrySet()){
            int id = distPair.getKey().id;
            long dist = distPair.getValue().dist;
            idDists.put(id,dist);
        }

        return idDists;
    }


    private Pair<NodeDist,HashSet<Node>> bmssp(long level, NodeDist upperBound, HashSet<Node> initialNodes){
        if(level == 0){
            return baseCase(upperBound,initialNodes);
        }
        Pair<HashSet<Node>,HashSet<Node>> pivotsPair = findPivots(upperBound,initialNodes);
        HashSet<Node> pivots = pivotsPair.first;
        HashSet<Node> completeByPivots = pivotsPair.second;
    
        int blockSize = (int)Math.pow(2,(level-1)*t);
        DQueue dQueue = new DQueue(blockSize, upperBound);
        
        NodeDist currentUpperBound = upperBound;
        for(Node pivot : pivots){
            if(currentUpperBound.compareTo(dists.get(pivot)) > 0){
                currentUpperBound = dists.get(pivot);
            }
            dQueue.insert(dists.get(pivot));
        }

        HashSet<Node> newCompleteNodes = new HashSet<Node>();
        long newCompleteNodesMaxSize = k*(long)Math.pow(2,level*t);
        while((newCompleteNodes.size() < newCompleteNodesMaxSize) && (!dQueue.isEmpty())){
            Pair<NodeDist,HashSet<Node>> prevBoundSet = dQueue.pull();
            NodeDist prevUpperBound = prevBoundSet.first;
            HashSet<Node> prevNodes = prevBoundSet.second;

            Pair<NodeDist,HashSet<Node>> currentBoundSet = bmssp(level-1,prevUpperBound,prevNodes);
            currentUpperBound = currentBoundSet.first;
            HashSet<Node> currentNodes = currentBoundSet.second;

            newCompleteNodes.addAll(currentNodes);
            HashSet<NodeDist> newNodeDists = new HashSet<NodeDist>();
            
            for(Node node : currentNodes){
                for(Edge edge : node.outEdges){
                    Node nodeTo = edge.nodeTo;

                    NodeDist newDist = dists.get(node).addEdge(edge);
                    if(newDist.compareTo(dists.get(nodeTo)) <= 0){
                        dists.put(nodeTo,newDist);
                        if(prevUpperBound.compareTo(newDist) <= 0 && newDist.compareTo(upperBound) < 0) dQueue.insert(newDist);
                        else if(currentUpperBound.compareTo(newDist) <= 0 && newDist.compareTo(prevUpperBound) < 0) newNodeDists.add(newDist);
                    }
                }
            }   
            
            for(Node node : prevNodes){
                NodeDist nodeDist = dists.get(node);
                if(currentUpperBound.compareTo(nodeDist) <= 0 && nodeDist.compareTo(prevUpperBound) < 0) newNodeDists.add(nodeDist);
            } 
            dQueue.batchPrepend(newNodeDists);
        }   

        NodeDist minUpperBound;
        if(currentUpperBound.compareTo(upperBound) <= 0){
            minUpperBound = currentUpperBound;
        }else{
            minUpperBound = upperBound;
        }

        for(Node node : completeByPivots){
            if(dists.get(node).compareTo(minUpperBound) < 0) newCompleteNodes.add(node);
        }

        return new Pair<NodeDist,HashSet<Node>>(minUpperBound, newCompleteNodes);
    }

    private Pair<NodeDist, HashSet<Node>> baseCase(NodeDist upperBound, HashSet<Node> pivotsSingleton ){
        HashSet<Node> completeNodes = new HashSet<Node>(pivotsSingleton); 
        
        Node pivot = pivotsSingleton.iterator().next();
        
        PriorityQueue<NodeDist> minHeap = new PriorityQueue<NodeDist>();
        minHeap.add(dists.get(pivot));

        NodeDist newUpperBound = dists.get(pivot);

        while(!minHeap.isEmpty() && completeNodes.size() < k + 1){
            NodeDist currentNodeDist = minHeap.remove();
            Node currentNode = currentNodeDist.node;
            
            if(currentNodeDist.compareTo(dists.get(currentNode)) > 0) continue;

            completeNodes.add(currentNode);
            if(newUpperBound.compareTo(currentNodeDist) < 0){
                newUpperBound = currentNodeDist;
            }

            for(Edge edge : currentNode.outEdges){
                Node secondNode = edge.nodeTo;
                NodeDist newDist = currentNodeDist.addEdge(edge);                

                if(newDist.compareTo(dists.get(secondNode)) <= 0 && newDist.compareTo(upperBound) < 0){
                    dists.put(secondNode, newDist);
                    minHeap.add(newDist);
                } 

            }
            
        }

        if(completeNodes.size() <= k){
            return new Pair<NodeDist, HashSet<Node>>(upperBound, completeNodes);
        }else{
            HashSet<Node> newCompleteNodes = new HashSet<Node>();
            for (Node node : completeNodes) {
                if(dists.get(node).compareTo(newUpperBound) < 0) newCompleteNodes.add(node);
            }
            return new Pair<NodeDist, HashSet<Node>>(newUpperBound, newCompleteNodes);
        }
    }

    private Pair<HashSet<Node>,HashSet<Node>> findPivots(NodeDist upperBound, HashSet<Node> border) {
        HashSet<Node> completeNodes = new HashSet<Node>(border);
        HashSet<Node> prevNodes = new HashSet<Node>(border);

        for(long i = 0; i < k; i++){
            HashSet<Node> currentNodes = new HashSet<>();

            for (Node node : prevNodes) {
                for (Edge edge : node.outEdges) {
                    Node nodeTo = edge.nodeTo;
                    NodeDist newDistance = dists.get(node).addEdge(edge);

                    if (newDistance.compareTo(dists.get(nodeTo)) <= 0) {
                        dists.put(nodeTo, newDistance);
                        
                        if (newDistance.compareTo(upperBound) < 0) {
                            currentNodes.add(nodeTo);
                        }
                    }
                }
            }

            completeNodes.addAll(currentNodes);
            prevNodes = currentNodes;

            if (completeNodes.size() > k * border.size()) {
                return new Pair<HashSet<Node>,HashSet<Node>>(border, completeNodes);
            }
        }

        HashSet<Node> pivots = getPivots(completeNodes,border);
        
        return new Pair<HashSet<Node>,HashSet<Node>>(pivots, completeNodes);   
    }

    private HashSet<Node> getPivots(HashSet<Node> nodes, HashSet<Node> border){

        HashMap<Node,LinkedList<Node>> adjacentInForest = new HashMap<Node,LinkedList<Node>>();

        for(Node node : nodes){
            Node nodeFrom = dists.get(node).prev.node;
            if(!nodes.contains(nodeFrom)) continue;

            adjacentInForest.putIfAbsent(nodeFrom, new LinkedList<Node>());
            adjacentInForest.get(nodeFrom).add(node);
        }

        HashSet<Node> pivots = new HashSet<Node>();
        for(Node root : border){
            if(nodes.contains(dists.get(root).prev.node) || !adjacentInForest.containsKey(root)) continue;
            if(countTreeSize(root, adjacentInForest) >= k){
                pivots.add(root);
            }
        }

        return pivots;
    }

    private int countTreeSize(Node root, HashMap<Node,LinkedList<Node>> adjacentInForest) {
        int count = 1;
        LinkedList<Node> adjacent = adjacentInForest.get(root);
        if(adjacent == null) return count;

        for (Node node : adjacent) {
            count += countTreeSize(node, adjacentInForest);
        }
        return count;
    }

    public static double log2(long n){
        return Math.log(n) / Math.log(2);
    }
}

