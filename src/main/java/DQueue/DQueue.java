package main.java.DQueue;

import java.util.HashMap;
import java.util.HashSet;

import main.java.DQueue.util.IntroSelect;
import main.java.commom.dataStructures.NodeDist;
import main.java.commom.dataStructures.Pair;
import main.java.commom.graph.Node;

public class DQueue {
    
    private final int blockSize;
    private final NodeDist upperBound;

    private BatchList batchList;
    private InsertTree insertTree;

    private HashMap<Node,NodeDistCoords> coordinates;

    public DQueue(int blockSize, NodeDist upperBound){
        this.blockSize = blockSize;
        this.upperBound = upperBound;

        this.batchList = new BatchList(this.blockSize);
        this.insertTree = new InsertTree(this.blockSize, upperBound);

        this.coordinates = new HashMap<Node,NodeDistCoords>();
    }

    public void insert(NodeDist element){
        NodeDistCoords elementToAdd = elementToAdd(element);
        
        if(elementToAdd != null){
            elementToAdd.blockCollection = insertTree;
            insertTree.insertElement(elementToAdd);
        }
    }

    public void batchPrepend(HashSet<NodeDist> elements){
        HashSet<NodeDistCoords> elementsToAdd = new HashSet<NodeDistCoords>();

        for(NodeDist element : elements){
            NodeDistCoords elementToAdd = elementToAdd(element);
            if(elementToAdd != null){
                elementToAdd.blockCollection = batchList;
                elementsToAdd.add(elementToAdd);
            }
        }

        batchList.batchPrepend(elementsToAdd);
    }

    public Pair<NodeDist,HashSet<Node>> pull(){
        HashSet<NodeDistCoords> possibleSmallests = new HashSet<NodeDistCoords>();
        
        possibleSmallests.addAll(batchList.pull());
        possibleSmallests.addAll(insertTree.pull());
        
        HashSet<Node> nodes = new HashSet<Node>();

        if(possibleSmallests.size() <= blockSize){
            for(NodeDistCoords element : possibleSmallests){
                nodes.add(element.nodeDist.node);
                delete(element);
            }

            return new Pair<NodeDist,HashSet<Node>>(upperBound, nodes);
        }

        NodeDistCoords blockSizeSmallest = IntroSelect.select(possibleSmallests, blockSize-1);
        for(NodeDistCoords element : possibleSmallests){
            if(element.compareTo(blockSizeSmallest) <= 0){
                nodes.add(element.nodeDist.node);
                delete(element);
            }
        }
        
        NodeDist upperBoundOfPull = IntroSelect.select(possibleSmallests, blockSize).nodeDist;
        return new Pair<NodeDist,HashSet<Node>>(upperBoundOfPull, nodes);
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
            element.blockCollection.delete(element.blockContainer);
        }
    }

}