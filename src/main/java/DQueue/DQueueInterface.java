package main.java.DQueue;

import java.util.HashSet;
import java.util.LinkedList;

import main.java.commom.dataStructures.Pair;
import main.java.commom.graph.NodeDist;

public interface DQueueInterface {
    public void insert(NodeDist element);
    public void batchPrepend(LinkedList<NodeDist> elements);
    public Pair<NodeDist,HashSet<Integer>> pull();
    public boolean isEmpty();
}
