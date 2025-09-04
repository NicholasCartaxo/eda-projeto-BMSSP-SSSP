package main.java;

import java.util.HashMap;
import java.util.HashSet;

import main.java.commom.dataStructures.Pair;
import main.java.commom.graph.Edge;
import main.java.commom.graph.Graph;
import main.java.commom.graph.Node;

public class BMSSP {
    private int k;
    private HashMap<Node, Integer> dists;

    public BMSSP(){

    }
    
    public Pair<HashSet<Node>,HashSet<Node>> findPivots(int upperBound, HashSet<Node> border) {
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
                pivots.add(root);
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
