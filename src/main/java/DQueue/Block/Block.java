package main.java.DQueue.Block;

import java.util.Iterator;

import main.java.DQueue.NodeDistStored;
import main.java.DQueue.util.IntroSelect;

public class Block implements Iterable<NodeDistStored>, Comparable<Block> {
    private final int blockSize;
    public int upperBound;

    private BlockNode head;
    private int size;
    
    public Block(int blockSize, int upperBound){
        this.blockSize = blockSize;
        this.upperBound = upperBound;
        size = 0;
    }

    public boolean isEmpty(){
        return size() == 0;
    }

    public boolean isFull(){
        return size() == blockSize;
    }

    public void addFirst(NodeDistStored value){
        BlockNode n = new BlockNode(value);
        value.blockNode = n;
        
        if(isEmpty()){
            head = n;
        }else{
            n.next = head;
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
        NodeDistStored median = IntroSelect.select(this, (size()-1)/2);

        Block newBlock = new Block(blockSize, upperBound);
        int newUpperBound = -1;
        
        BlockNode aux = head;
        while(aux != null){
            if(aux.value.compareTo(median) <= 0){
                size--;
                newBlock.addFirst(aux.value);
                newUpperBound = Math.max(newUpperBound,aux.value.dist);
                remove(aux);
            }

            aux = aux.next;
        }

        newBlock.upperBound = newUpperBound;
        return newBlock;
    }

    public BlockNode getHead(){
        return head;
    }

    @Override
    public Iterator<NodeDistStored> iterator() {
        return new BlockIterator(this);
    }
    
    @Override
    public int compareTo(Block o) {
        return Integer.valueOf(upperBound).compareTo(Integer.valueOf(o.upperBound));
    }

    public int size(){
        return size;
    }

}