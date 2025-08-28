package main.java.DQueue;

public class DQueue {
    
    private final int blockSize;

    private BatchList batchList;
    private InsertTree insertTree;

    public DQueue(int blockSize, int initialBound){
        this.blockSize = blockSize;
    }


}
