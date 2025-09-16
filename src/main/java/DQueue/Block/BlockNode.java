package main.java.DQueue.Block;

import main.java.DQueue.NodeDistCoords;

public class BlockNode{
    public BlockNode next,prev;
    public final NodeDistCoords value;

    public BlockNode(NodeDistCoords value){
        this.value = value;
    }
}