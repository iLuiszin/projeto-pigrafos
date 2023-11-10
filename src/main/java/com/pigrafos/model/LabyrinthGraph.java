package com.pigrafos.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LabyrinthGraph {
    private Map<Integer, List<Integer>> adjacencyList;
    private Map<Integer, TypeVertex> typeVertex;
    private Map<Integer, Boolean> visitedVertices;

    public LabyrinthGraph() {
        this.adjacencyList = new HashMap<>();
        this.typeVertex = new HashMap<>();
        this.visitedVertices = new HashMap<>();
    }

    public Map<Integer, TypeVertex> getTypeVertex() {
        return typeVertex;
    }

    public void setTypeVertex(int vertex, TypeVertex type) {
        typeVertex.put(vertex, type);
    }

    public void addVertex(int vertex) {
        adjacencyList.put(vertex, new ArrayList<>());
        visitedVertices.put(vertex, false);
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

    public boolean isVisited(int vertex) {
        return visitedVertices.get(vertex);
    }

    public void markVisited(int vertex) {
        visitedVertices.put(vertex, true);
    }

    public void buildGraph(List<LabyrinthResponse> responses) {
        for (LabyrinthResponse response : responses) {
            int currentPosition = response.getActualPosition();
            List<Integer> possibleMoves = response.getMoves();

            if (!adjacencyList.containsKey(currentPosition)) {
                addVertex(currentPosition);
            }

            for (int newPosition : possibleMoves) {
                if (!adjacencyList.containsKey(newPosition)) {
                    addVertex(newPosition);
                }
                addEdge(currentPosition, newPosition);
            }
        }
    }
}