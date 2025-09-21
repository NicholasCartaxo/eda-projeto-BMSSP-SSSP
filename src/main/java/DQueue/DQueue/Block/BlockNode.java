package main.java.DQueue.DQueue.Block;

import main.java.DQueue.DQueue.NodeDistCoords;

public class BlockNode{
    public BlockNode next,prev;
    public final NodeDistCoords value;

    public BlockNode(NodeDistCoords value){
        this.value = value;
    }
}