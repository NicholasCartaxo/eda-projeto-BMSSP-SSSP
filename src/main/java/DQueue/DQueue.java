package main.java.DQueue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

import main.java.commom.dataStructures.NodeDist;
import main.java.commom.dataStructures.Pair;
import main.java.commom.graph.Node;

public class DQueue {
    
    private final int blockSize;
    private final NodeDist upperBound;

    private PriorityQueue<NodeDist> heap;

    private HashMap<Node,NodeDist> coordinates;

    public DQueue(int blockSize, NodeDist upperBound){
        this.blockSize = blockSize;
        this.upperBound = upperBound;

        this.heap = new PriorityQueue<NodeDist>();
        this.coordinates = new HashMap<Node,NodeDist>();
    }

    public void insert(NodeDist element){
        if(!coordinates.containsKey(element.node) || element.dist < coordinates.get(element.node).dist){
            heap.remove(coordinates.get(element.node));
            heap.add(element);
            coordinates.put(element.node,element);
        }
    }

    public void batchPrepend(HashSet<NodeDist> elements){

        for(NodeDist element : elements){
            insert(element);
        }

    }

    public Pair<NodeDist,HashSet<Node>> pull(){
        HashSet<Node> ret = new HashSet<Node>();
        while(!heap.isEmpty() && ret.size() < blockSize){
            NodeDist ele = heap.remove();
            coordinates.remove(ele.node);
            ret.add(ele.node);
        }
        if(heap.isEmpty()){
            return new Pair<NodeDist,HashSet<Node>>(upperBound, ret);
        }else{
            return new Pair<NodeDist,HashSet<Node>>(heap.peek(), ret);
        }
    }

    public boolean isEmpty(){
        return coordinates.size() == 0;
    }

}