package main.java;

import java.util.HashMap;
import java.util.PriorityQueue;

import main.java.commom.dataStructures.NodeDist;
import main.java.commom.graph.Edge;
import main.java.commom.graph.Node;

public class Dijkstra {
   
   private HashMap<Node, Long> dists;
   private HashMap<Node, Boolean> isNodeComplete;
   
   private void dijkstra(Node pivot) {
      PriorityQueue<NodeDist> queue = new PriorityQueue<NodeDist>();
      queue.add(new NodeDist(pivot, dists.get(pivot)));
      
      while(!queue.isEmpty()){
         NodeDist currentNodeDist = queue.remove();
         Node currentNode = currentNodeDist.node;
         long currentDist = currentNodeDist.dist;

         isNodeComplete.put(currentNode, true);

         for(Edge edge: currentNode.outEdges){
            if(isNodeComplete.get(edge.nodeTo)) continue;
            
            Node nodeTo = edge.nodeTo;
            long newDist = currentDist + edge.weight;
            if(newDist < dists.get(nodeTo)){
               dists.put(nodeTo, newDist);

            }  
         }
      }
   }  
   
}
