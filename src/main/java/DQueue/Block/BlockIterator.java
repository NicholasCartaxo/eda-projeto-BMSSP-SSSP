package main.java.DQueue.Block;

import java.util.Iterator;

import main.java.DQueue.NodeDistStored;

public class BlockIterator implements Iterator<NodeDistStored> {

    private BlockNode curr;

    public BlockIterator(Block block){
        curr = block.getHead();
    }

    @Override
    public boolean hasNext() {
        return curr.next != null;
    }

    @Override
    public NodeDistStored next() {
        NodeDistStored ret = curr.value;
        curr = curr.next;
        return ret;
    }
    
}
