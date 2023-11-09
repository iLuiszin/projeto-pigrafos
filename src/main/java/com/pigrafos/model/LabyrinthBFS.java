package com.pigrafos.model;

import java.util.*;

public class LabyrinthBFS {
    private LabyrinthGraph graph;
    private Map<Integer, Integer> parentMap;

    public LabyrinthBFS(LabyrinthGraph graph) {
        this.graph = graph;
        this.parentMap = new HashMap<>();
    }

    public List<Integer> findShortestPath(int startVertex, int endVertex) {
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();

        queue.add(startVertex);
        visited.add(startVertex);
        parentMap.put(startVertex, null);

        while (!queue.isEmpty()) {
            int currentVertex = queue.poll();

            if (currentVertex == endVertex) {
                return constructPath(endVertex);
            }

            for (int neighbor : graph.getNeighbors(currentVertex)) {
                if (!visited.contains(neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                    parentMap.put(neighbor, currentVertex);
                }
            }
        }

        return Collections.emptyList();
    }

    private List<Integer> constructPath(int endVertex) {
        List<Integer> path = new ArrayList<>();
        Integer currentVertex = endVertex;

        while (currentVertex != null) {
            path.add(currentVertex);
            currentVertex = parentMap.get(currentVertex);
        }

        Collections.reverse(path);
        return path;
    }
}
