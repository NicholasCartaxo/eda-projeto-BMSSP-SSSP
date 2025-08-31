package main.java.DQueue.Block;

import main.java.DQueue.NodeDistStored;

public class BlockNode{
    public BlockNode next,prev;
    public final NodeDistStored value;

    public BlockNode(NodeDistStored value){
        this.value = value;
    }
}