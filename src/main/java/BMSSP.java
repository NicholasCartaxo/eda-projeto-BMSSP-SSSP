package main.java;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

import main.java.DQueue.DQueueInterface;
import main.java.DQueue.SimpleDQueue;
import main.java.DQueue.DQueue.DQueue;
import main.java.commom.dataStructures.Pair;
import main.java.commom.graph.Graph;
import main.java.commom.graph.NodeDist;

public class BMSSP {

    private static final long INFINITY = 100000000000000000L;
    private static final NodeDist INFINITY_NODE_DIST = new NodeDist(Integer.MAX_VALUE, INFINITY);
    
    private static LinkedList<NodeDist>[] adjacent;
    private static long[] dists;
    private static int[] parents;
    private static int k;
    private static int t;
    
    public static long[] solve(Graph graph, int origin){

        adjacent = graph.adjacent;
        dists = new long[graph.numNodes];
        parents = new int[graph.numNodes];

        for(int i=0;i<graph.numNodes;i++){
            dists[i] = INFINITY;
            parents[i] = -1;
        }
        dists[origin] = 0;

        k = (int)Math.floor(Math.pow(log2(graph.numNodes), 1.0/3.0));
        t = (int)Math.floor(Math.pow(log2(graph.numNodes), 2.0/3.0));

        int level = (int)Math.ceil(log2(graph.numNodes)/t);

        HashSet<Integer> initialNodes = new HashSet<Integer>();
        initialNodes.add(origin);
        bmssp(level, INFINITY_NODE_DIST, initialNodes);

        return dists;
    }


    private static Pair<NodeDist,HashSet<Integer>> bmssp(int level, NodeDist upperBound, HashSet<Integer> initialNodes){
        if(level == 0){
            return baseCase(upperBound,initialNodes);
        }
        Pair<HashSet<Integer>,HashSet<Integer>> pivotsPair = findPivots(upperBound,initialNodes);
        HashSet<Integer> pivots = pivotsPair.first;
        HashSet<Integer> completeByPivots = pivotsPair.second;
    
        int blockSize = (int)Math.pow(2,(level-1)*t);
        DQueueInterface dQueue = blockSize == 1 ? new SimpleDQueue(upperBound) : new DQueue(blockSize, upperBound);
        
        NodeDist currentUpperBound = upperBound;
        for(int pivot : pivots){
            if(currentUpperBound.compareTo(new NodeDist(pivot, dists[pivot])) > 0){
                currentUpperBound = new NodeDist(pivot, dists[pivot]);
            }
            dQueue.insert(new NodeDist(pivot, dists[pivot]));
        }

        HashSet<Integer> newCompleteNodes = new HashSet<Integer>();
        int newCompleteNodesMaxSize = k*(int)Math.pow(2,level*t);
        while(newCompleteNodes.size() < newCompleteNodesMaxSize && !dQueue.isEmpty()){
            Pair<NodeDist,HashSet<Integer>> prevBoundSet = dQueue.pull();
            NodeDist prevUpperBound = prevBoundSet.first;
            HashSet<Integer> prevNodes = prevBoundSet.second;

            Pair<NodeDist,HashSet<Integer>> currentBoundSet = level == 1 ? baseCase(prevUpperBound, prevNodes) : bmssp(level-1,prevUpperBound,prevNodes);
            currentUpperBound = currentBoundSet.first;
            HashSet<Integer> currentNodes = currentBoundSet.second;

            newCompleteNodes.addAll(currentNodes);
            LinkedList<NodeDist> newNodeDists = new LinkedList<NodeDist>();
            
            for(int node : currentNodes){
                for(NodeDist edge : adjacent[node]){
                    int nodeTo = edge.node;
                    long weight = edge.dist;

                    long newDist = dists[node]+weight;
                    if(newDist <= dists[nodeTo]){
                        dists[nodeTo] = newDist;
                        parents[nodeTo] = node;
                        
                        NodeDist newNodeDist = new NodeDist(nodeTo, newDist);
                        if(prevUpperBound.compareTo(newNodeDist) <= 0 && newNodeDist.compareTo(upperBound) < 0) dQueue.insert(newNodeDist);
                        else if(currentUpperBound.compareTo(newNodeDist) <= 0 && newNodeDist.compareTo(prevUpperBound) < 0) newNodeDists.add(newNodeDist);
                    }
                }
            }   
            
            for(int node : prevNodes){
                NodeDist nodeDist = new NodeDist(node, dists[node]);
                if(currentUpperBound.compareTo(nodeDist) <= 0 && nodeDist.compareTo(prevUpperBound) < 0) newNodeDists.add(new NodeDist(node, dists[node]));
            } 
            dQueue.batchPrepend(newNodeDists);
        }   

        NodeDist minUpperBound = currentUpperBound.compareTo(upperBound) < 0 ? currentUpperBound : upperBound;


        for(int node : completeByPivots){
            if(minUpperBound.compareTo(new NodeDist(node, dists[node])) > 0) newCompleteNodes.add(node);
        }

        return new Pair<NodeDist,HashSet<Integer>>(minUpperBound, newCompleteNodes);
    }

    private static Pair<NodeDist, HashSet<Integer>> baseCase(NodeDist upperBound, HashSet<Integer> completeNodes ){
        
        int origin = completeNodes.iterator().next();
        
        PriorityQueue<NodeDist> minHeap = new PriorityQueue<NodeDist>();
        minHeap.add(new NodeDist(origin, dists[origin]));

        NodeDist newUpperBound = new NodeDist(origin, dists[origin]);

        while(!minHeap.isEmpty() && completeNodes.size() < k + 1){
            NodeDist currentNodeDist = minHeap.remove();
            int currentNode = currentNodeDist.node;
            long currentDist = currentNodeDist.dist;
            
            if(currentDist > dists[currentNode]) continue;

            completeNodes.add(currentNode);
            newUpperBound =  newUpperBound.compareTo(currentNodeDist) > 0 ? newUpperBound : currentNodeDist;

            for(NodeDist edge : adjacent[currentNode]){
                int nodeTo = edge.node;
                long weight = edge.dist;

                long newDist = currentDist + weight;  
                NodeDist newNodeDist = new NodeDist(nodeTo, newDist);              
                if(newDist <= dists[nodeTo] && newNodeDist.compareTo(upperBound) < 0){
                    dists[nodeTo] = newDist;
                    parents[nodeTo] = currentNode;
                    minHeap.add(newNodeDist);
                } 

            }
            
        }

        if(completeNodes.size() <= k){
            return new Pair<NodeDist, HashSet<Integer>>(upperBound, completeNodes);
        }else{
            HashSet<Integer> newCompleteNodes = new HashSet<Integer>();
            for (int node : completeNodes) {
                if(newUpperBound.compareTo(new NodeDist(node, dists[node])) > 0) newCompleteNodes.add(node);
            }
            return new Pair<NodeDist, HashSet<Integer>>(newUpperBound, newCompleteNodes);
        }
    }

    private static Pair<HashSet<Integer>,HashSet<Integer>> findPivots(NodeDist upperBound, HashSet<Integer> border){
        HashSet<Integer> completeNodes = new HashSet<Integer>(border);
        HashSet<Integer> prevNodes = new HashSet<Integer>(border);

        for(long i = 0; i < k; i++){
            HashSet<Integer> currentNodes = new HashSet<Integer>();

            for(int node : prevNodes){
                for(NodeDist edge : adjacent[node]){
                    int nodeTo = edge.node;
                    long weight = edge.dist;

                    long newDistance = dists[node]+weight;
                    if(newDistance <= dists[nodeTo]){
                        dists[nodeTo] = newDistance;
                        parents[nodeTo] = node;

                        if(upperBound.compareTo(new NodeDist(nodeTo, newDistance)) > 0){
                            currentNodes.add(nodeTo);
                        }
                    }
                }
            }

            completeNodes.addAll(currentNodes);
            prevNodes = currentNodes;

            if(completeNodes.size() > k * border.size()){
                return new Pair<HashSet<Integer>,HashSet<Integer>>(border, completeNodes);
            }
        }

        HashSet<Integer> completeNodesSet = new HashSet<Integer>(completeNodes);
        HashSet<Integer> pivots = getPivots(completeNodesSet,border);

        return new Pair<HashSet<Integer>,HashSet<Integer>>(pivots, completeNodes);   
    }

    private static HashSet<Integer> getPivots(HashSet<Integer> nodes, HashSet<Integer> border){

        HashMap<Integer,HashSet<Integer>> adjacentInForest = new HashMap<Integer,HashSet<Integer>>();

        for(int node : nodes){
            int nodeFrom = parents[node];
            if(!nodes.contains(nodeFrom)) continue;

            adjacentInForest.putIfAbsent(nodeFrom, new HashSet<Integer>());
            adjacentInForest.get(nodeFrom).add(node);
        }

        HashSet<Integer> pivots = new HashSet<Integer>();
        for(int root : border){
            if(nodes.contains(parents[root])) continue;
            if(countTreeSize(root, adjacentInForest) >= k){
                pivots.add(root);
            }
        }

        return pivots;
    }

    private static int countTreeSize(int root, HashMap<Integer,HashSet<Integer>> adjacentInForest) {
        int count = 1;
        HashSet<Integer> adjacent = adjacentInForest.get(root);
        if(adjacent == null) return count;

        for (int node : adjacent) {
            count += countTreeSize(node, adjacentInForest);
        }
        return count;
    }

    public static double log2(long n){
        return Math.log(n) / Math.log(2);
    }
}

