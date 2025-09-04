package DQueue.Block;

import DQueue.NodeDistStored;

public class BlockNode{
    public BlockNode next,prev;
    public final NodeDistStored value;

    public BlockNode(NodeDistStored value){
        this.value = value;
    }
}