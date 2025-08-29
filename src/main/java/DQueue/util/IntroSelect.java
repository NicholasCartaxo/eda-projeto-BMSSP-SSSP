package main.java.DQueue.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import main.java.commom.graph.Node;

public class IntroSelect {

    public static Entry<Node,Integer> select(LinkedHashMap<Node,Integer> elements, int index){
    
        ArrayList<ArrayList<Entry<Node,Integer>>> groups = new ArrayList<ArrayList<Entry<Node,Integer>>>();

        int groupIdx = 0;
        int insideGroupIdx = 0;
        for(Entry<Node,Integer> entry : elements.entrySet()){
            if(groups.get(groupIdx) == null){
                groups.set(groupIdx, new ArrayList<Entry<Node,Integer>>());
            }
            groups.get(groupIdx).add(entry);

            groupIdx++;
            insideGroupIdx = (insideGroupIdx+1)%5;
        }

        ArrayList<Entry<Node,Integer>> medians = new ArrayList<Entry<Node,Integer>>();
        for(ArrayList<Entry<Node,Integer>> group : groups){
            group.sort(new EntryNodeIntComparator());
            int medianIndex = ((group.size()-1)/2);
            medians.add(group.get(medianIndex));
        }

        

        return null;
    }

}
