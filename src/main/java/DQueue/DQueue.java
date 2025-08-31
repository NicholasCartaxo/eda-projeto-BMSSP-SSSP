package main.java.DQueue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import main.java.DQueue.util.IntroSelect;
import main.java.commom.dataStructures.NodeDist;
import main.java.commom.graph.Node;

public class DQueue {
    
    private final int blockSize;

    private BatchList batchList;
    private InsertTree insertTree;

    private HashMap<Node,NodeDistStored> coordinates;

    public DQueue(int blockSize, int upperBound){
        this.blockSize = blockSize;
        this.batchList = new BatchList(this.blockSize);
        this.insertTree = new InsertTree(this.blockSize, upperBound);

        this.coordinates = new HashMap<Node,NodeDistStored>();
    }

    public void insert(NodeDist element){
        NodeDistStored elementToAdd = elementToAdd(element);
        elementToAdd.blockCollection = insertTree;

        if(elementToAdd != null){
            insertTree.insertElement(elementToAdd);
        }
    }

    public void batchPrepend(HashSet<NodeDist> elements){
        HashSet<NodeDistStored> elementsToAdd = new HashSet<NodeDistStored>();

        for(NodeDist element : elements){
            NodeDistStored elementToAdd = elementToAdd(element);
            elementToAdd.blockCollection = batchList;


            if(elementToAdd != null){
                elementsToAdd.add(elementToAdd);
            }
        }

        batchList.batchPrepend(elementsToAdd);
    }

    public HashSet<Node> pull(){
        HashSet<NodeDistStored> possibleSmallests = new HashSet<NodeDistStored>();
        possibleSmallests.addAll(batchList.pull());
        possibleSmallests.addAll(insertTree.pull());

        NodeDistStored blockSizeSmallest = IntroSelect.select(possibleSmallests, blockSize-1);
        HashSet<Node> ret = new HashSet<Node>();
        for(NodeDistStored element : possibleSmallests){
            if(element.compareTo(blockSizeSmallest) <= 0){
                ret.add(element.node);
                delete(element);
            }
        }
        return ret;
    }

    private NodeDistStored elementToAdd(NodeDist element){
        if(!coordinates.containsKey(element.node)){
            NodeDistStored newCordinate = new NodeDistStored(element);
            coordinates.put(element.node, newCordinate);
            return newCordinate;
        }

        if(element.dist > coordinates.get(element.node).dist){
            delete(coordinates.get(element.node));
            NodeDistStored newCordinate = new NodeDistStored(element);
            coordinates.put(element.node, newCordinate);
            return newCordinate;
        }

        return null;
    }

    private void delete(NodeDistStored element){
        element.blockContainer.delete(element.blockNode);
        if(element.blockContainer.isEmpty()){
            element.blockCollection.delete(element.blockContainer);
        }
    }

}