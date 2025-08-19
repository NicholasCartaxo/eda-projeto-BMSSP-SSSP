package main.java.shared.Interfaces;

import main.java.shared.Graph;

public interface SingleShortestPath{
    int getShortestDistance(Graph graph, int sourceId, int destinationId);
}