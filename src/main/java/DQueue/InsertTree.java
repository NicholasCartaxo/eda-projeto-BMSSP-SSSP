package main.java.DQueue;

import java.util.HashSet;
import java.util.TreeMap;

import main.java.DQueue.Block.Block;
import main.java.DQueue.Block.BlockCollection;
import main.java.DQueue.Block.BlockContainer;
import main.java.commom.graph.NodeDist;

class InsertTree implements BlockCollection{

    private final int blockSize;
    private final NodeDist upperBound;

    private TreeMap<NodeDist,Block> blockTree;

    public InsertTree(int blockSize, NodeDist upperBound){
        this.blockSize = blockSize;
        this.upperBound = upperBound;

        blockTree = new TreeMap<NodeDist,Block>();
        insertBlock(new Block(this.blockSize,this.upperBound));
    }

    public void insertElement(NodeDistCoords element) {
        Block blockToAdd = blockTree.ceilingEntry(element.nodeDist).getValue();
        element.blockContainer = blockToAdd;
        blockToAdd.addFirst(element);
        if(blockToAdd.isFull()){
            insertBlock(blockToAdd.split());
        }
    }

    @Override
    public void delete(BlockContainer blockContainer){
        if(((Block)blockContainer).upperBound != upperBound){
            blockTree.remove(((Block)blockContainer).upperBound);
        }
    }

    public HashSet<NodeDistCoords> pull(){
        HashSet<NodeDistCoords> ret = new HashSet<NodeDistCoords>();
        for(Block block : blockTree.values()){
            for(NodeDistCoords element : block){
                ret.add(element);
            }
            if(ret.size() > blockSize) break;
        }
        return ret;
    }

    private void insertBlock(Block element) {
        for(NodeDistCoords nodeDist : element){
            nodeDist.blockContainer = element;
        }
        blockTree.put(element.upperBound,element);
    }
    

}