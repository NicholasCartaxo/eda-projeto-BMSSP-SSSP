package main.java.DQueue;

import main.java.DQueue.Block.BlockCollection;
import main.java.DQueue.Block.BlockContainer;
import main.java.DQueue.Block.BlockNode;
import main.java.commom.dataStructures.NodeDist;
import main.java.commom.graph.Node;

public class NodeDistStored extends NodeDist {

    public BlockCollection blockCollection;
    public BlockContainer blockContainer;
    public BlockNode blockNode;

    public NodeDistStored(Node node, double dist) {
        super(node, dist);
    }

    public NodeDistStored(NodeDist nodeDist){
        super(nodeDist.node, nodeDist.dist);
    }
    
}
