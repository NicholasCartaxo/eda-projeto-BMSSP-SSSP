package main.java.commom;

import java.util.Set;

import main.java.commom.graph.Node;

public class SetTuple {
    public Set<Node> P;
    public Set<Node> W;
    
    public SetTuple(Set<Node> P, Set<Node> W) {
        this.P = P;
        this.W = W;
    }
}
