package com.pigrafos.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Graph {
    private Map<Integer, List<Integer>> adjacencyList;
    private Map<Integer, TypeVertex> typeVertex;
    private Map<Integer, Boolean> visitedVertices;

    public Graph() {
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

    public void buildGraph(List<Response> responses) {
        for (Response response : responses) {
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
    public List<Integer> getPath(int source, int destination) {
        List<Integer> path = new ArrayList<>();
        if (!visitedVertices.containsKey(destination)) {
            return path;
        }

        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Integer> parent = new HashMap<>();

        queue.add(source);
        visitedVertices.put(source, true);
        parent.put(source, null);

        while (!queue.isEmpty()) {
            int current = queue.poll();

            if (current == destination) {
                int node = current;
                while (node != source) {
                    path.add(node);
                    node = parent.get(node);
                }
                path.add(source);
                Collections.reverse(path);
                break;
            }

            for (int neighbor : adjacencyList.getOrDefault(current, Collections.emptyList())) {
                if (!visitedVertices.getOrDefault(neighbor, false)) {
                    visitedVertices.put(neighbor, true);
                    parent.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        return path;
    }
}