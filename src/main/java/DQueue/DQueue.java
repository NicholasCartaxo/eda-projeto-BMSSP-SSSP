package main.java.DQueue;

import java.util.HashMap;
import java.util.HashSet;

import main.java.DQueue.util.IntroSelect;
import main.java.commom.dataStructures.NodeDist;
import main.java.commom.dataStructures.Pair;
import main.java.commom.graph.Node;

public class DQueue {
    
    private final int blockSize;
    private final long upperBound;

    private BatchList batchList;
    private InsertTree insertTree;

    private HashMap<Node,NodeDistStored> coordinates;

    public DQueue(int blockSize, long upperBound){
        this.blockSize = blockSize;
        this.upperBound = upperBound;

        this.batchList = new BatchList(this.blockSize);
        this.insertTree = new InsertTree(this.blockSize, upperBound);

        this.coordinates = new HashMap<Node,NodeDistStored>();
    }

    public void insert(NodeDist element){
        NodeDistStored elementToAdd = elementToAdd(element);
        
        if(elementToAdd != null){
            elementToAdd.blockCollection = insertTree;
            insertTree.insertElement(elementToAdd);
        }
    }

    public void batchPrepend(HashSet<NodeDist> elements){
        HashSet<NodeDistStored> elementsToAdd = new HashSet<NodeDistStored>();

        for(NodeDist element : elements){
            NodeDistStored elementToAdd = elementToAdd(element);
            if(elementToAdd != null){
                elementToAdd.blockCollection = batchList;
                elementsToAdd.add(elementToAdd);
            }
        }

        batchList.batchPrepend(elementsToAdd);
    }

    public Pair<Long,HashSet<Node>> pull(){
        HashSet<NodeDistStored> possibleSmallests = new HashSet<NodeDistStored>();
        
        possibleSmallests.addAll(batchList.pull());
        possibleSmallests.addAll(insertTree.pull());
        
        HashSet<Node> nodes = new HashSet<Node>();

        if(possibleSmallests.size() <= blockSize){
            for(NodeDistStored element : possibleSmallests){
                nodes.add(element.node);
                delete(element);
            }

            return new Pair<Long,HashSet<Node>>(upperBound, nodes);
        }

        NodeDistStored blockSizeSmallest = IntroSelect.select(possibleSmallests, blockSize-1);
        for(NodeDistStored element : possibleSmallests){
            if(element.compareTo(blockSizeSmallest) <= 0){
                nodes.add(element.node);
                delete(element);
            }
        }
        
        long upperBoundOfPull = IntroSelect.select(possibleSmallests, blockSize).dist;
        return new Pair<Long,HashSet<Node>>(upperBoundOfPull, nodes);
    }

    public boolean isEmpty(){
        return coordinates.size() == 0;
    }

    private NodeDistStored elementToAdd(NodeDist element){
        if(!coordinates.containsKey(element.node)){
            NodeDistStored newCordinate = new NodeDistStored(element);
            coordinates.put(element.node, newCordinate);
            return newCordinate;
        }

        if(element.dist < coordinates.get(element.node).dist){
            delete(coordinates.get(element.node));
            NodeDistStored newCordinate = new NodeDistStored(element);
            coordinates.put(element.node, newCordinate);
            return newCordinate;
        }

        return null;
    }

    private void delete(NodeDistStored element){
        coordinates.remove(element.node);

        element.blockContainer.delete(element.blockNode);
        if(element.blockContainer.isEmpty()){
            element.blockCollection.delete(element.blockContainer);
        }
    }

}