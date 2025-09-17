package main.java;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import main.java.commom.graph.Graph;

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

        BMSSP b = new BMSSP();
        HashMap<Integer,Long> a = b.solve(g, g.nodesById.get(1));
        for(int i=1;i<=n;i++){
            System.out.println(a.get(i));
        } 

    }
}
