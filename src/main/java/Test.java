package main.java;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import main.java.commom.graph.Graph;
import main.java.commom.graph.Node;

public class Test {
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String[] firstLine = br.readLine().split(" ");
        int n = Integer.parseInt(firstLine[0]);
        int m = Integer.parseInt(firstLine[1]);

        Graph g = new Graph();
        for (int i = 0; i < m; i++) {
            String[] edgeData = br.readLine().split(" ");
            int u = Integer.parseInt(edgeData[0]);
            int v = Integer.parseInt(edgeData[1]);
            int w = Integer.parseInt(edgeData[2]);
            
            g.addEdge(u, v, w);
        }
        Node origin = g.nodesById.get(1);

        Dijkstra d = new Dijkstra();
        BMSSP b = new BMSSP();

        long start, end;
        long dijkstraTime = 0;
        long BMSSPTime = 0;
        long mistakes = 0;

        for(int i=0;i<20;i++){
            start = System.nanoTime();
            HashMap<Integer,Long> dS = d.solve(g, origin);
            end = System.nanoTime();
            dijkstraTime += (end-start)/20;

            start = System.nanoTime();
            HashMap<Integer,Long> bS = b.solve(g, origin);
            end = System.nanoTime();
            BMSSPTime += (end-start)/20;

            long thisMistakes = 0;
            for(int j=1;j<=n;j++){
                if(!dS.get(j).equals(bS.get(j))) thisMistakes++;
            } 
            mistakes += thisMistakes;
        }

        System.out.printf("%d %d %d\n",dijkstraTime,BMSSPTime,mistakes);

    }
}
