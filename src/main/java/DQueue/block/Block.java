package main.java.DQueue.block;

import main.java.commom.graph.Node;
import main.java.commom.util.Pair;

class Block {

    private final int blockSize;
    private BlockNode head;
    private BlockNode tail;
    private int size;
    
    public Block(int blockSize) {
        this.blockSize = blockSize;
        size = 0;
    }
   
    public boolean isEmpty() {
        return size() == 0;
    }

    public void add(Pair<Node,Integer> value) {
        BlockNode n = new BlockNode(value);
        if(isEmpty()){
            head = n;
            tail = n;
        }else{
            n.prev = tail;
            tail.next = n;
            tail = n;
        }
        size++;
    }

    public Pair<Node,Integer> getPair(Node n){
        BlockNode aux = head;
        while(aux != null){
            if(aux.value.getKey().equals(n)){
                return aux.value;
            }
            aux = aux.next;
        }
        return null;
    }
    
    public int size() {
        return size;
    }

}



