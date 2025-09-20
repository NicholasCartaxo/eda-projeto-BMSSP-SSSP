package main.java;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

import main.java.DQueue.DQueue;
import main.java.commom.dataStructures.Pair;
import main.java.commom.graph.Graph;
import main.java.commom.graph.NodeDist;

public class BMSSP {

    private static final long INFINITY = 100000000000000000L;
    private static final NodeDist INFINITY_NODE_DIST = new NodeDist(Integer.MAX_VALUE, INFINITY);
    
    private static LinkedList<Pair<Integer,Long>>[] adjacent;
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

        LinkedList<Integer> initialNodes = new LinkedList<Integer>();
        initialNodes.add(origin);
        bmssp(level, INFINITY_NODE_DIST, initialNodes);

        return dists;
    }


    private static Pair<NodeDist,LinkedList<Integer>> bmssp(int level, NodeDist upperBound, LinkedList<Integer> initialNodes){
        if(level == 0){
            return baseCase(upperBound,initialNodes);
        }
        Pair<LinkedList<Integer>,LinkedList<Integer>> pivotsPair = findPivots(upperBound,initialNodes);
        LinkedList<Integer> pivots = pivotsPair.first;
        LinkedList<Integer> completeByPivots = pivotsPair.second;
    
        int blockSize = (int)Math.pow(2,(level-1)*t);
        DQueue dQueue = new DQueue(blockSize, upperBound);
        
        NodeDist currentUpperBound = upperBound;
        for(int pivot : pivots){
            if(currentUpperBound.compareTo(new NodeDist(pivot, dists[pivot])) > 0){
                currentUpperBound = new NodeDist(pivot, dists[pivot]);
            }
            dQueue.insert(new NodeDist(pivot, dists[pivot]));
        }

        LinkedList<Integer> newCompleteNodes = new LinkedList<Integer>();
        int newCompleteNodesMaxSize = k*(int)Math.pow(2,level*t);
        while(newCompleteNodes.size() < newCompleteNodesMaxSize && !dQueue.isEmpty()){
            Pair<NodeDist,LinkedList<Integer>> prevBoundSet = dQueue.pull();
            NodeDist prevUpperBound = prevBoundSet.first;
            LinkedList<Integer> prevNodes = prevBoundSet.second;

            Pair<NodeDist,LinkedList<Integer>> currentBoundSet = level == 1 ? baseCase(prevUpperBound, prevNodes) : bmssp(level-1,prevUpperBound,prevNodes);
            currentUpperBound = currentBoundSet.first;
            LinkedList<Integer> currentNodes = currentBoundSet.second;

            newCompleteNodes.addAll(currentNodes);
            LinkedList<NodeDist> newNodeDists = new LinkedList<NodeDist>();
            
            for(int node : currentNodes){
                for(Pair<Integer,Long> edge : adjacent[node]){
                    int nodeTo = edge.first;
                    long weight = edge.second;

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

        return new Pair<NodeDist,LinkedList<Integer>>(minUpperBound, newCompleteNodes);
    }

    private static Pair<NodeDist, LinkedList<Integer>> baseCase(NodeDist upperBound, LinkedList<Integer> completeNodes ){
        
        int origin = completeNodes.getFirst();
        
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

            for(Pair<Integer,Long> edge : adjacent[currentNode]){
                int nodeTo = edge.first;
                long weight = edge.second;

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
            return new Pair<NodeDist, LinkedList<Integer>>(upperBound, completeNodes);
        }else{
            LinkedList<Integer> newCompleteNodes = new LinkedList<Integer>();
            for (int node : completeNodes) {
                if(newUpperBound.compareTo(new NodeDist(node, dists[node])) > 0) newCompleteNodes.add(node);
            }
            return new Pair<NodeDist, LinkedList<Integer>>(newUpperBound, newCompleteNodes);
        }
    }

    private static Pair<LinkedList<Integer>,LinkedList<Integer>> findPivots(NodeDist upperBound, LinkedList<Integer> border){
        LinkedList<Integer> completeNodes = new LinkedList<Integer>(border);
        LinkedList<Integer> prevNodes = new LinkedList<Integer>(border);

        for(long i = 0; i < k; i++){
            LinkedList<Integer> currentNodes = new LinkedList<Integer>();

            for(int node : prevNodes){
                for(Pair<Integer,Long> edge : adjacent[node]){
                    int nodeTo = edge.first;
                    long weight = edge.second;

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
                return new Pair<LinkedList<Integer>,LinkedList<Integer>>(border, completeNodes);
            }
        }

        HashSet<Integer> completeNodesSet = new HashSet<Integer>(completeNodes);
        LinkedList<Integer> pivots = getPivots(completeNodesSet,border);

        return new Pair<LinkedList<Integer>,LinkedList<Integer>>(pivots, completeNodes);   
    }

    private static LinkedList<Integer> getPivots(HashSet<Integer> nodes, LinkedList<Integer> border){

        HashMap<Integer,LinkedList<Integer>> adjacentInForest = new HashMap<Integer,LinkedList<Integer>>();

        for(int node : nodes){
            int nodeFrom = parents[node];
            if(!nodes.contains(nodeFrom)) continue;

            adjacentInForest.putIfAbsent(nodeFrom, new LinkedList<Integer>());
            adjacentInForest.get(nodeFrom).add(node);
        }

        LinkedList<Integer> pivots = new LinkedList<Integer>();
        for(int root : border){
            if(nodes.contains(parents[root])) continue;
            if(countTreeSize(root, adjacentInForest) >= k){
                pivots.add(root);
            }
        }

        return pivots;
    }

    private static int countTreeSize(int root, HashMap<Integer,LinkedList<Integer>> adjacentInForest) {
        int count = 1;
        LinkedList<Integer> adjacent = adjacentInForest.get(root);
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

