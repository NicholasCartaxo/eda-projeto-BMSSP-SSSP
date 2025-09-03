package main.java.DQueue.util;

import java.util.ArrayList;
import java.util.LinkedList;

import main.java.DQueue.NodeDistStored;


public class IntroSelect {

    public static NodeDistStored select(Iterable<NodeDistStored> elements, int index){
    
        LinkedList<ArrayList<NodeDistStored>> groups = new LinkedList<ArrayList<NodeDistStored>>();

        for(NodeDistStored element : elements){
            if(groups.isEmpty() || groups.getLast().size() == 5){
                groups.addLast(new ArrayList<NodeDistStored>());
            }
            groups.getLast().add(element);
        }

        LinkedList<NodeDistStored> medians = new LinkedList<NodeDistStored>();
        for(ArrayList<NodeDistStored> group : groups){
            group.sort(null);
            int medianIndex = ((group.size()-1)/2);
            medians.add(group.get(medianIndex));
        }

        NodeDistStored medianOfMedians;
        if(medians.size() == 1){
            medianOfMedians = medians.getFirst();
        }else{
            medianOfMedians = select(medians,(medians.size()-1)/2);
        }

        LinkedList<NodeDistStored> left = new LinkedList<NodeDistStored>();
        LinkedList<NodeDistStored> right = new LinkedList<NodeDistStored>();
        for(NodeDistStored element : elements){
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
