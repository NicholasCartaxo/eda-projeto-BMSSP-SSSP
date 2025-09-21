package main.java.DQueue.DQueue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import main.java.DQueue.DQueueInterface;
import main.java.DQueue.DQueue.util.IntroSelect;
import main.java.commom.dataStructures.Pair;
import main.java.commom.graph.NodeDist;

public class DQueue implements DQueueInterface {
    
    private final int blockSize;
    private final NodeDist upperBound;

    private BatchList batchList;
    private InsertTree insertTree;

    private HashMap<Integer,NodeDistCoords> coordinates;

    public DQueue(int blockSize, NodeDist upperBound){
        this.blockSize = blockSize;
        this.upperBound = upperBound;

        this.batchList = new BatchList(this.blockSize);
        this.insertTree = new InsertTree(this.blockSize, upperBound);

        this.coordinates = new HashMap<Integer,NodeDistCoords>();
    }

    public void insert(NodeDist element){
        NodeDistCoords elementToAdd = elementToAdd(element);
        
        if(elementToAdd != null){
            insertTree.insertElement(elementToAdd);
        }
    }

    public void batchPrepend(LinkedList<NodeDist> elements){
        HashSet<NodeDistCoords> elementsToAdd = new HashSet<NodeDistCoords>();

        for(NodeDist element : elements){
            NodeDistCoords elementToAdd = elementToAdd(element);
            if(elementToAdd != null){
                elementsToAdd.add(elementToAdd);
            }
        }

        batchList.batchPrepend(elementsToAdd);
    }

    public Pair<NodeDist,HashSet<Integer>> pull(){
        HashSet<NodeDistCoords> possibleSmallests = new HashSet<NodeDistCoords>();
        
        possibleSmallests.addAll(batchList.pull());
        possibleSmallests.addAll(insertTree.pull());
        
        HashSet<Integer> nodes = new HashSet<Integer>();

        if(possibleSmallests.size() <= blockSize){
            for(NodeDistCoords element : possibleSmallests){
                nodes.add(element.nodeDist.node);
                delete(element);
            }

            return new Pair<NodeDist,HashSet<Integer>>(upperBound, nodes);
        }

        NodeDistCoords blockSizeSmallest = IntroSelect.select(possibleSmallests, blockSize-1);
        for(NodeDistCoords element : possibleSmallests){
            if(element.compareTo(blockSizeSmallest) <= 0){
                nodes.add(element.nodeDist.node);
                delete(element);
            }
        }
        
        NodeDist upperBoundOfPull = IntroSelect.select(possibleSmallests, blockSize).nodeDist;
        return new Pair<NodeDist,HashSet<Integer>>(upperBoundOfPull, nodes);
    }

    public boolean isEmpty(){
        return coordinates.size() == 0;
    }

    private NodeDistCoords elementToAdd(NodeDist element){
        if(!coordinates.containsKey(element.node)){
            NodeDistCoords newCordinate = new NodeDistCoords(element);
            coordinates.put(element.node, newCordinate);
            return newCordinate;
        }

        if(element.compareTo(coordinates.get(element.node).nodeDist) < 0){
            delete(coordinates.get(element.node));
            NodeDistCoords newCordinate = new NodeDistCoords(element);
            coordinates.put(element.node, newCordinate);
            return newCordinate;
        }

        return null;
    }

    private void delete(NodeDistCoords element){
        coordinates.remove(element.nodeDist.node);

        element.blockContainer.delete(element.blockNode);
        if(element.blockContainer.isEmpty()){
            element.blockContainer.blockCollection().delete(element.blockContainer);
        }
    }

}