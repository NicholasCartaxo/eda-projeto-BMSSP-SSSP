package DQueue;

import DQueue.Block.BlockCollection;
import DQueue.Block.BlockContainer;
import DQueue.Block.BlockNode;
import commom.dataStructures.NodeDist;
import commom.graph.Node;

public class NodeDistStored extends NodeDist {

    public BlockCollection blockCollection;
    public BlockContainer blockContainer;
    public BlockNode blockNode;

    public NodeDistStored(Node node, int dist) {
        super(node, dist);
    }

    public NodeDistStored(NodeDist nodeDist){
        super(nodeDist.node, nodeDist.dist);
    }
    
}
