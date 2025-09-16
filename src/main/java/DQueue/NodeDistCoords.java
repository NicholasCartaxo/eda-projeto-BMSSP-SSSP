package main.java.DQueue;

import main.java.DQueue.Block.BlockCollection;
import main.java.DQueue.Block.BlockContainer;
import main.java.DQueue.Block.BlockNode;
import main.java.commom.dataStructures.NodeDist;

public class NodeDistCoords implements Comparable<NodeDistCoords> {

    public BlockCollection blockCollection;
    public BlockContainer blockContainer;
    public BlockNode blockNode;
    public final NodeDist nodeDist;

    public NodeDistCoords(NodeDist nodeDist){
        this.nodeDist = nodeDist;
    }

    @Override
    public int compareTo(NodeDistCoords o) {
        return nodeDist.compareTo(o.nodeDist);
    }
    


}
