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

        start = System.nanoTime();
        HashMap<Integer,Long> dS = d.solve(g, origin);
        end = System.nanoTime();

        System.out.println("dijkstra: " + (end - start)/1000000);

        start = System.nanoTime();
        HashMap<Integer,Long> bS = b.solve(g, origin);
        end = System.nanoTime();

        System.out.println("bmssp: " + (end - start)/1000000);

        for(int i=1;i<=n;i++){
            if(!dS.get(i).equals(bS.get(i))) System.out.println("INCORRECT FOR " + i);
        } 

    }
}
