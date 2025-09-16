package main.java.DQueue;

import java.util.HashSet;
import main.java.DQueue.Block.Block;
import main.java.DQueue.Block.BlockCollection;
import main.java.DQueue.Block.BlockContainer;
import main.java.DQueue.Block.BlockNode;
import main.java.DQueue.util.IntroSelect;

class BatchList implements BlockCollection {

    private final int blockSize;
    private BatchNode head;

    public BatchList(int blockSize){
        this.blockSize = blockSize;
    }

    public void batchPrepend(HashSet<NodeDistCoords> elements){
        if(elements.size() < blockSize){
            addFirst(elements);
        }
        else{
            addPartitioned(elements);
        }
    }

    public HashSet<NodeDistCoords> pull(){
        HashSet<NodeDistCoords> ret = new HashSet<NodeDistCoords>();

        BatchNode aux = head;
        while(aux != null && ret.size() < blockSize){
            for(NodeDistCoords element : aux.value){
                ret.add(element);
            }
            aux = aux.next;
        }
        return ret;
    }
    
    @Override
    public void delete(BlockContainer blockContainer) {
        BatchNode node = (BatchNode)blockContainer;
        if(head == node) head = head.next;

        if(node.prev != null) node.prev.next = node.next;
        if(node.next != null) node.next.prev = node.prev;
    }

    public boolean isEmpty(){
        return head == null;
    }

    private void addFirst(HashSet<NodeDistCoords> elements) {

        BatchNode n = new BatchNode(new Block(blockSize));
        
        for(NodeDistCoords element : elements){
            element.blockContainer = n;
            n.value.addFirst(element);
        }

        if(head == null){
            head = n;
        }else{
            n.next = head;
            head.prev = n;
            head = n;
        }
    }

    private void addPartitioned(HashSet<NodeDistCoords> elements){
        if(elements.size() <= (blockSize+1)/2){
            addFirst(elements);
            return;
        }

        NodeDistCoords median = IntroSelect.select(elements, (elements.size()-1)/2);

        HashSet<NodeDistCoords> left = new HashSet<NodeDistCoords>();
        HashSet<NodeDistCoords> right = new HashSet<NodeDistCoords>();

        for(NodeDistCoords element : elements){
            if(element.compareTo(median) <= 0){
                left.add(element);
            }else{
                right.add(element);
            }
        }

        addPartitioned(right);
        addPartitioned(left);
    }

}

class BatchNode implements BlockContainer {

    Block value;
    BatchNode prev;
    BatchNode next;

    BatchNode(Block value) {
        this.value = value;
    }

    @Override
    public void delete(BlockNode element) {
        value.remove(element);
    }

    @Override
    public boolean isEmpty() {
        return value.isEmpty();
    }

}