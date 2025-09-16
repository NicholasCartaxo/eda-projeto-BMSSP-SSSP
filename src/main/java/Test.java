package main.java;

import java.util.HashMap;
import java.util.Scanner;

import main.java.commom.graph.Graph;

public class Test {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        
        Graph g = new Graph();
        for(int i=0;i<m;i++){
            g.addEdge(sc.nextInt(), sc.nextInt(), sc.nextInt());
        }

        BMSSP b = new BMSSP();
        HashMap<Integer,Long> a = b.solve(g, g.nodesById.get(1));
        for(int i=1;i<=n;i++){
            System.out.println(a.get(i));
        } 

    }
}
