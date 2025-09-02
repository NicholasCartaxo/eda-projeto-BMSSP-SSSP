package main.java;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import main.java.commom.SetTuple;
import main.java.commom.SetMapTuple;
import main.java.commom.graph.Edge;
import main.java.commom.graph.Node;

public class BMSSP {
    private int k;
    private Map<Node, Integer> shortestDistance;

    public BMSSP(){

    }
    
    public SetTuple findPivots(int upperBound, Set<Node> border) {
        Set<Node> nodes = new HashSet<>(border);
        Set<Node> prevNodes = new HashSet<>(border);

        for(int i = 0; i < k; i++){
            Set<Node> currentNodes = new HashSet<>();

            for (Node u : prevNodes) {
                List<Edge> edges = u.outEdges;
                
                for (Edge edge : edges) {
                    Node nodeTo = edge.nodeTo;
                    int newDistance = shortestDistance.get(edge.nodeFrom) + edge.weight;
                    
                    if (newDistance <= shortestDistance.get(nodeTo)) {
                        shortestDistance.put(nodeTo, newDistance);
                        
                        if (newDistance < upperBound) {
                            currentNodes.add(nodeTo);
                        }
                    }
                }
            }

            nodes.addAll(currentNodes);
            prevNodes = currentNodes;

            if (nodes.size() > k * border.size()) {
                return new SetTuple(new HashSet<>(border), nodes);
            }
        }

        SetMapTuple tupleForest = buildForest(nodes);
        Set<Node> roots = tupleForest.roots;
        Map<Node, Integer> treeSizes = tupleForest.treeSizes;

        // Select pivots
        Set<Node> pivots = new HashSet<>();
        for (Node root : roots) {
            if (border.contains(root) && treeSizes.get(root) >= k) {
                pivots.add(root);
            }
        }
        
        return new SetTuple(pivots, nodes);
        
        
    }

    private SetMapTuple buildForest(Set<Node> nodes){
        Map<Node, Node> parent = new HashMap<>(); 
        Map<Node, Set<Node>> children = new HashMap<>();
        
        for (Node node : nodes) {
            List<Edge> edges = node.outEdges;
            if (edges == null) continue;
            
            for (Edge edge : edges) {
                Node nodeTo = edge.nodeTo;
                if (nodes.contains(nodeTo) && shortestDistance.get(nodeTo) == shortestDistance.get(node) + edge.weight) {
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
