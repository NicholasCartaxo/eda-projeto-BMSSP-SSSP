package main.java.commom;

import java.util.Map;
import java.util.Set;
import main.java.commom.graph.Node;

public class SetMapTuple {
    public Set<Node> roots;
    public Map<Node, Integer> treeSizes;
    
    public SetMapTuple(Set<Node> roots, Map<Node, Integer> treeSizes) {
        this.roots = roots;
        this.treeSizes = treeSizes;
    }
}
