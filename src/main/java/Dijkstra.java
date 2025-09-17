package main.java;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import main.java.commom.dataStructures.NodeDist;
import main.java.commom.graph.Edge;
import main.java.commom.graph.Graph;
import main.java.commom.graph.Node;

public class Dijkstra {
   
   private static final long INFINITY = 100000000000000000L;
   private static final NodeDist INFINITY_NODEDIST = new NodeDist(null, INFINITY, 0, null);

   private HashMap<Node, NodeDist> dists;
   private HashMap<Node, Boolean> isNodeComplete;
   
   public HashMap<Integer, Long> solve(Graph graph, Node origin){
        dists = new HashMap<Node, NodeDist>();
        isNodeComplete = new HashMap<Node, Boolean>();

        for(Node node : graph.nodesById.values()){
            dists.put(node, INFINITY_NODEDIST);
            isNodeComplete.put(node, false);
        }
        dists.put(origin,new NodeDist(origin, 0, 1, null));

        dijkstra(origin);

        HashMap<Integer,Long> idDists = new HashMap<Integer,Long>();
        for(Map.Entry<Node,NodeDist> distPair : dists.entrySet()){
            int id = distPair.getKey().id;
            long dist = distPair.getValue().dist;
            idDists.put(id,dist);
        }

        return idDists;
    }

   private void dijkstra(Node pivot) {
      PriorityQueue<NodeDist> queue = new PriorityQueue<NodeDist>();
      queue.add(dists.get(pivot));
      
      while(!queue.isEmpty()){
         NodeDist currentNodeDist = queue.remove();
         Node currentNode = currentNodeDist.node;

         if (isNodeComplete.get(currentNode)) {
            continue;
        }

         isNodeComplete.put(currentNode, true);

         for(Edge edge: currentNode.outEdges){
            if(isNodeComplete.get(edge.nodeTo)) continue;
            
            Node nodeTo = edge.nodeTo;
            NodeDist newDist = currentNodeDist.addEdge(edge);
            if(newDist.compareTo(dists.get(nodeTo)) < 0){
               dists.put(nodeTo, newDist);
               queue.add(newDist);
            }  
         }
      }
   }  
   
}
