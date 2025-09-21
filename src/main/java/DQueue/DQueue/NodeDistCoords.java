package main.java.DQueue.DQueue;

import main.java.DQueue.DQueue.Block.BlockContainer;
import main.java.DQueue.DQueue.Block.BlockNode;
import main.java.commom.graph.NodeDist;

public class NodeDistCoords implements Comparable<NodeDistCoords> {

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
