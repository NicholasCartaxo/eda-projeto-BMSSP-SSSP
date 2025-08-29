package main.java;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import main.java.commom.graph.Edge;

public class BMSSP {
    private int n;
    private Map<Integer, List<Edge>> graph;
    private int[] dHat;

    public BMSSP(){
        graph = new HashMap<Integer, List<Edge>>();
        n = graph.size();
        dHat = new int[n];
    }

    public static class Result {
        public Set<Integer> P;
        public Set<Integer> W;
        
        public Result(Set<Integer> P, Set<Integer> W) {
            this.P = P;
            this.W = W;
        }
    }
    
    public Result findPivots(int B, Set<Integer> S) {
        Set<Integer> W = new HashSet<>(S);
        Set<Integer> W_prev = new HashSet<>(S);
        
        int k = computeK();

        for(int i = 0; i < k; i++){
            Set<Integer> W_i = new HashSet<>();

            for (int u : W_prev) {
                List<Edge> edges = graph.get(u);
                if (edges == null) continue;
                
                for (Edge edge : edges) {
                    int v = edge.nodeTo.id;
                    int newDistance = dHat[u] + edge.weight;
                    
                    if (newDistance <= dHat[v]) {
                        dHat[v] = newDistance;
                        
                        if (newDistance < B) {
                            W_i.add(v);
                        }
                    }
                }
            }

            W.addAll(W_i);
            W_prev = W_i;

            if (W.size() > k * S.size()) {
                return new Result(new HashSet<>(S), W);
            }
        }

        // Build forest F 
        Map<Integer, Integer> parent = new HashMap<>(); 
        Map<Integer, Set<Integer>> children = new HashMap<>();
        
        for (int u : W) {
            List<Edge> edges = graph.get(u);
            if (edges == null) continue;
            
            for (Edge edge : edges) {
                int v = edge.nodeTo.id;
                if (W.contains(v) && dHat[v] == dHat[u] + edge.weight) {
                    parent.put(v, u);
                    children.computeIfAbsent(u, z -> new HashSet<>()).add(v);
                }
            }
        }
    
        // Find roots 
        Set<Integer> roots = new HashSet<>();
        for (int u : W) {
            if (!parent.containsKey(u)) {
                roots.add(u);
            }
        }
        
        // For each root, traverse its tree to count vertices
        Map<Integer, Integer> treeSizes = new HashMap<>();
        for (int root : roots) {
            int size = countTreeSize(root, children);
            treeSizes.put(root, size);
        }
        
        // Select pivots
        Set<Integer> P = new HashSet<>();
        for (int root : roots) {
            if (S.contains(root) && treeSizes.get(root) >= k) {
                P.add(root);
            }
        }
        
        return new Result(P, W);
        
        
    }

    private int countTreeSize(int node, Map<Integer, Set<Integer>> children) {
        int count = 1; // count this node
        if (children.containsKey(node)) {
            for (int child : children.get(node)) {
                count += countTreeSize(child, children);
            }
        }
        return count;
    }
        
    private int computeK() {
        // k = log^c(n) for some const c
        // Example: c = 2 → k = (log n)²
        double logN = Math.log(n) / Math.log(2); // log2(n)
        int k = (int) Math.pow(logN, 2); // k = (log n)²
        return Math.max(k, 1); 
    }
}
