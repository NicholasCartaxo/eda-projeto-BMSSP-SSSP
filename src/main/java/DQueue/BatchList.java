package main.java.DQueue;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import main.java.DQueue.util.EntryNodeIntComparator;
import main.java.DQueue.util.IntroSelect;
import main.java.commom.graph.Node;

class BatchList {

    private final int blockSize;
    private BlockNode head;

    public BatchList(int blockSize){
        this.blockSize = blockSize;
    }

    public void batchPrepend(LinkedHashMap<Node,Integer> elements){
        if(elements.size() < blockSize){
            addFirst(elements);
        }
        else{
            addPartitioned(elements);
        }
    }

    public LinkedHashMap<Node,Integer> pull(){
        LinkedHashMap<Node,Integer> ret = new LinkedHashMap<Node,Integer>();

        BlockNode aux = head;
        while(aux != null && ret.size() < blockSize){
            ret.putAll(aux.value);
            aux = aux.next;
        }
        return ret;
    }

    private void addFirst(LinkedHashMap<Node,Integer> elements) {
        BlockNode n = new BlockNode(elements);
        if(head == null){
            head = n;
        }else{
            n.next = head;
            head.prev = n;
            head = n;
        }
    }

    private void addPartitioned(LinkedHashMap<Node,Integer> elements){
        if(elements.size() <= (blockSize+1)/2){
            addFirst(elements);
            return;
        }

        Entry<Node,Integer> median = IntroSelect.select(elements, elements.size()/2);

        LinkedHashMap<Node,Integer> left = new LinkedHashMap<Node,Integer>();
        LinkedHashMap<Node,Integer> right = new LinkedHashMap<Node,Integer>();

        EntryNodeIntComparator comparator = new EntryNodeIntComparator(); 
        for(Entry<Node,Integer> entry : elements.entrySet()){
            if(comparator.compare(entry, median) <= 0){
                left.put(entry.getKey(),entry.getValue());
            }else{
                right.put(entry.getKey(),entry.getValue());
            }
        }

        addPartitioned(right);
        addPartitioned(left);
    }

    public void remove(BlockNode node) {
        if(head == node) head = head.next;

        if(node.prev != null) node.prev.next = node.next;
        if(node.next != null) node.next.prev = node.prev;
    }


}

class BlockNode {

    LinkedHashMap<Node,Integer> value;
    BlockNode prev;
    BlockNode next;

    BlockNode(LinkedHashMap<Node,Integer> v) {
        this.value = v;
    }

}