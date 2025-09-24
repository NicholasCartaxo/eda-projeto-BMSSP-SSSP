package main.java.DQueue.Block;

import java.util.Iterator;

import main.java.DQueue.NodeDistCoords;

public class BlockIterator implements Iterator<NodeDistCoords> {

    private BlockNode curr;

    public BlockIterator(Block block, BlockNode head){
        curr = head;
    }

    @Override
    public boolean hasNext() {
        return curr != null;
    }

    @Override
    public NodeDistCoords next() {
        NodeDistCoords ret = curr.value;
        curr = curr.next;
        return ret;
    }
    
}
