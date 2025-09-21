package main.java.DQueue.DQueue.util;

import java.util.ArrayList;
import java.util.LinkedList;

import main.java.DQueue.DQueue.NodeDistCoords;


public class IntroSelect {

    public static NodeDistCoords select(Iterable<NodeDistCoords> elements, int index){
        LinkedList<ArrayList<NodeDistCoords>> groups = new LinkedList<ArrayList<NodeDistCoords>>();

        for(NodeDistCoords element : elements){
            if(groups.isEmpty() || groups.getLast().size() == 5){
                groups.addLast(new ArrayList<NodeDistCoords>());
            }
            groups.getLast().add(element);
        }

        LinkedList<NodeDistCoords> medians = new LinkedList<NodeDistCoords>();
        for(ArrayList<NodeDistCoords> group : groups){
            group.sort(null);
            int medianIndex = ((group.size()-1)/2);
            medians.add(group.get(medianIndex));
        }

        NodeDistCoords medianOfMedians;
        if(medians.size() == 1){
            medianOfMedians = medians.getFirst();
        }else{
            medianOfMedians = select(medians,(medians.size()-1)/2);
        }

        LinkedList<NodeDistCoords> left = new LinkedList<NodeDistCoords>();
        LinkedList<NodeDistCoords> right = new LinkedList<NodeDistCoords>();
        for(NodeDistCoords element : elements){
            if(element.compareTo(medianOfMedians) <= 0) left.add(element);
            else right.add(element);
        }

        int medianOfMediansIndex = left.size()-1;
        if(index == medianOfMediansIndex) return medianOfMedians;
        left.remove(medianOfMedians);
        if(index < medianOfMediansIndex) return select(left,index);
        return select(right, index-medianOfMediansIndex-1);
    }

}
