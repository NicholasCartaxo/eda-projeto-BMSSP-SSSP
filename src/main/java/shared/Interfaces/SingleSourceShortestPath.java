package main.java.shared.Interfaces;

import main.java.shared.Graph;

public interface SingleSourceShortestPath{
    int[] getShortestDistances(Graph graph, int sourceId);
}