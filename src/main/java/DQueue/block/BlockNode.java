package main.java.DQueue.block;

import main.java.commom.graph.Node;
import main.java.commom.util.Pair;

class BlockNode {

    Pair<Node,Integer> value;
    BlockNode prev;
    BlockNode next;

    BlockNode(Pair<Node,Integer> v) {
        this.value = v;
    }

}
