package main.java.DQueue.Block;

import java.util.Iterator;

import main.java.DQueue.NodeDistCoords;
import main.java.DQueue.util.IntroSelect;
import main.java.commom.graph.NodeDist;

public class Block implements Iterable<NodeDistCoords>, Comparable<Block>, BlockContainer {
    private final int blockSize;
    public NodeDist upperBound;

    private BlockNode head;
    private int size;
    
    public Block(int blockSize, NodeDist upperBound){
        this.blockSize = blockSize;
        this.upperBound = upperBound;
        size = 0;
    }

    public Block(int blockSize){
        this(blockSize, null);
    }

    public boolean isEmpty(){
        return size() == 0;
    }

    public boolean isFull(){
        return size() > blockSize;
    }

    public void addFirst(NodeDistCoords value){
        BlockNode n = new BlockNode(value);
        value.blockNode = n;
        
        if(isEmpty()){
            head = n;
        }else{
            n.next = head;
            head.prev = n;
            head = n;
        }
        size++;
    }

    public void remove(BlockNode node){
        if(head == node) head = head.next;

        if(node.prev != null) node.prev.next = node.next;
        if(node.next != null) node.next.prev = node.prev;
        size--;
    }

    public Block split(){
        NodeDistCoords median = IntroSelect.select(this, (size()-1)/2);

        Block newBlock = new Block(blockSize, upperBound);

        BlockNode aux = head;
        while(aux != null){
            if(aux.value.compareTo(median) <= 0){
                newBlock.addFirst(aux.value);
                remove(aux);
            }

            aux = aux.next;
        }

        newBlock.upperBound = median.nodeDist;
        return newBlock;
    }

    @Override
    public Iterator<NodeDistCoords> iterator() {
        return new BlockIterator(this,head);
    }
    
    @Override
    public int compareTo(Block o) {
        return upperBound.compareTo(o.upperBound);
    }

    public int size(){
        return size;
    }

    @Override
    public void delete(BlockNode element) {
        remove(element);
    }

}