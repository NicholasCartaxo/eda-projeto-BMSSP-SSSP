package main.java;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import main.java.commom.graph.Graph;

public class Test {
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String[] firstLine = br.readLine().split(" ");
        int n = Integer.parseInt(firstLine[0]);
        int m = Integer.parseInt(firstLine[1]);

        Graph g = new Graph(n);
        for (int i = 0; i < m; i++) {
            String[] edgeData = br.readLine().split(" ");
            int u = Integer.parseInt(edgeData[0])-1;
            int v = Integer.parseInt(edgeData[1])-1;
            int w = Integer.parseInt(edgeData[2]);
            
            g.addEdge(u, v, w);
        }

        long start, end;
        long dijkstraTime = 0;
        long BMSSPTime = 0;
        long mistakes = 0;

        for(int i=0;i<20;i++){
            start = System.nanoTime();
            long[] dS = Dijkstra.solve(g, 0);
            end = System.nanoTime();
            dijkstraTime += (end-start)/20;

            start = System.nanoTime();
            long[] bS = BMSSP.solve(g, 0);
            end = System.nanoTime();
            BMSSPTime += (end-start)/20;

            long thisMistakes = 0;
            for(int j=1;j<=n;j++){
                if(dS[i] != bS[i]) thisMistakes++;
            } 
            mistakes += thisMistakes;
        }

        System.out.printf("%d %d %d\n",dijkstraTime,BMSSPTime,mistakes);

    }
}
