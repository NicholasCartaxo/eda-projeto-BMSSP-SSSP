package main.java;

import java.util.LinkedList;
import java.util.PriorityQueue;

import main.java.commom.dataStructures.Pair;
import main.java.commom.graph.Graph;
import main.java.commom.graph.NodeDist;

public class Dijkstra {
   
   private static final long INFINITY = 100000000000000000L;

    private static LinkedList<Pair<Integer,Long>>[] adjacent;
    private static long[] dists;
    private static int[] parents;
    private static boolean[] visited;
   
   public static long[] solve(Graph graph, int origin){
         
      adjacent = graph.adjacent;
      dists = new long[graph.numNodes];
      parents = new int[graph.numNodes];
      visited = new boolean[graph.numNodes];

      for(int i=0;i<graph.numNodes;i++){
         dists[i] = INFINITY;
         parents[i] = -1;
         visited[i] = false;
      }
      dists[origin] = 0;
      dijkstra(origin);

      return dists;
    }

   private static void dijkstra(int origin) {
      PriorityQueue<NodeDist> queue = new PriorityQueue<NodeDist>();
      queue.add(new NodeDist(origin, dists[origin]));
      
      while(!queue.isEmpty()){
         NodeDist currentNodeDist = queue.remove();
         int currentNode = currentNodeDist.node;
         long currentDist = currentNodeDist.dist;

         if (visited[currentNode]) continue;

         visited[currentNode] = true;

         for(Pair<Integer,Long> edge: adjacent[currentNode]){
            int nodeTo = edge.first;
            long weight = edge.second;
            if(visited[nodeTo]) continue;
            
            long newDist = currentDist + weight;
            if(newDist < dists[nodeTo]){
               dists[nodeTo] = newDist;
               parents[nodeTo] = currentNode;
               queue.add(new NodeDist(nodeTo, newDist));
            }  
         }
      }
   }  
   
}
