package main.java.DQueue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

import main.java.commom.dataStructures.Pair;
import main.java.commom.graph.NodeDist;

public class SimpleDQueue implements DQueueInterface {

    private PriorityQueue<NodeDist> heap;
    private HashMap<Integer,NodeDist> coords;
    private final NodeDist upperBound;

    public SimpleDQueue(NodeDist upperBound){
        this.upperBound = upperBound;

        heap = new PriorityQueue<NodeDist>();
        coords = new HashMap<Integer,NodeDist>();
    }

    public void insert(NodeDist element) {
        if(!coords.containsKey(element.node) || element.dist < coords.get(element.node).dist){
            coords.put(element.node,element);
            heap.add(element);
        }
    }

    public void batchPrepend(LinkedList<NodeDist> elements) {
        for(NodeDist element : elements){
            coords.put(element.node,element);
            heap.add(element);
        }
    }

    public Pair<NodeDist, HashSet<Integer>> pull() {
        HashSet<Integer> pull = new HashSet<Integer>();
        while(pull.isEmpty()){
            NodeDist smallest = heap.remove();
            if(coords.containsKey(smallest.node)){
                pull.add(smallest.node);
                coords.remove(smallest.node);
            }
        }
        NodeDist bound = isEmpty() ? upperBound : heap.peek();
        return new Pair<NodeDist,HashSet<Integer>>(bound, pull);
    }

    public boolean isEmpty() {
        return coords.isEmpty();
    }
    
}
