package com.pigrafos.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    private final Map<Integer, List<Integer>> adjacencyList;

    public Graph() {
        this.adjacencyList = new HashMap<>();
    }

    public void addVertex(int vertex) {
        adjacencyList.put(vertex, new ArrayList<>());
    }

    public void addEdge(int source, int destination) {
        adjacencyList.get(source).add(destination);
        adjacencyList.get(destination).add(source);
    }

    public List<Integer> getNeighbors(int vertex) {
        return adjacencyList.get(vertex);
    }

    public Map<Integer, List<Integer>> getAdjacencyList() {
        return adjacencyList;
    }

    public void buildGraph(Response response) {
        int currentPosition = response.getActualPosition();
        List<Integer> possibleMoves = response.getMovimentos();

        if (!adjacencyList.containsKey(currentPosition)) {
            addVertex(currentPosition);
        }

        for (int newPosition : possibleMoves) {
            if (!adjacencyList.containsKey(newPosition)) {
                addVertex(newPosition);
            }
            if (!adjacencyList.get(currentPosition).contains(newPosition)) {
                addEdge(currentPosition, newPosition);
            }
        }
    }
}
