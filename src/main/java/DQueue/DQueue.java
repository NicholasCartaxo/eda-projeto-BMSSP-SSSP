package main.java.DQueue;

import java.util.LinkedHashMap;
import java.util.Set;
import main.java.commom.graph.Node;

public class DQueue {
    
    private final int blockSize;

    private BatchList batchList;
    private InsertTree insertTree;

    public DQueue(int blockSize, int initialBound){
        this.blockSize = blockSize;

        
    }

    public void insert(){

    }

    public void batchPrepend(LinkedHashMap<Node,Integer> elements){
        batchList.batchPrepend(elements);
    }

    public Set<Node> pull(){
        return null;
    }

}
