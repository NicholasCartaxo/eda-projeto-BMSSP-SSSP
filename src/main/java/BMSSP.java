package main.java;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import main.java.commom.dataStructures.Pair;
import main.java.commom.SetMapTuple;
import main.java.commom.graph.Edge;
import main.java.commom.graph.Node;

public class BMSSP {
    private int k;
    private Map<Node, Integer> dists;

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

        SetMapTuple tupleForest = buildForest(completeNodes);
        Set<Node> roots = tupleForest.roots;
        Map<Node, Integer> treeSizes = tupleForest.treeSizes;

        // Select pivots
        HashSet<Node> pivots = new HashSet<>();
        for (Node root : roots) {
            if (border.contains(root) && treeSizes.get(root) >= k) {
                pivots.add(root);
            }
        }
        
        return new Pair<HashSet<Node>,HashSet<Node>>(pivots, completeNodes);
        
        
    }

    private SetMapTuple buildForest(Set<Node> nodes){
        Map<Node, Node> parent = new HashMap<>(); 
        Map<Node, Set<Node>> children = new HashMap<>();
        
        for (Node node : nodes) {
            List<Edge> edges = node.outEdges;
            if (edges == null) continue;
            
            for (Edge edge : edges) {
                Node nodeTo = edge.nodeTo;
                if (nodes.contains(nodeTo) && dists.get(nodeTo) == dists.get(node) + edge.weight) {
                    parent.put(nodeTo, node);

                    if(!children.containsKey(node)){
                        children.put(node, new HashSet<>());
                    }

                    Set<Node> setNodes = children.get(node);
                    setNodes.add(nodeTo);
                }
            }
        }
    
        Set<Node> roots = new HashSet<>();
        for (Node node : nodes) {
            if (!parent.containsKey(node)) {
                roots.add(node);
            }
        }

        Map<Node, Integer> treeSizes = new HashMap<>();
        for (Node root : roots) {
            int size = countTreeSize(root, children);
            treeSizes.put(root, size);
        }

        return new SetMapTuple(roots, treeSizes);
    }

    private int countTreeSize(Node node, Map<Node, Set<Node>> children) {
        int count = 1;
        if (children.containsKey(node)) {
            for (Node child : children.get(node)) {
                count += countTreeSize(child, children);
            }
        }
        return count;
    }
}
