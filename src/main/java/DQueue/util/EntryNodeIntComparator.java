package main.java.DQueue.util;

import java.util.Comparator;
import java.util.Map.Entry;
import main.java.commom.graph.Node;

public class EntryNodeIntComparator implements Comparator<Entry<Node,Integer>> {

    @Override
    public int compare(Entry<Node, Integer> a, Entry<Node, Integer> b) {
        if(a.getValue().equals(b.getValue()))
            return a.getKey().compareTo(b.getKey());
        return a.getValue().compareTo(b.getValue());
    }
    
}
