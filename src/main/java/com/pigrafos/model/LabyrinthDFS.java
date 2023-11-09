package com.pigrafos.model;

import java.util.*;

public class LabyrinthDFS {
    private LabyrinthGraph graph;
    private Set<Integer> visited;

    public LabyrinthDFS(LabyrinthGraph graph) {
        this.graph = graph;
        this.visited = new HashSet<>();
    }

    public List<Integer> findExitPath(int startVertex, int endVertex) {
        List<Integer> path = new ArrayList<>();
        dfs(startVertex, endVertex, path);
        return path;
    }

    private void dfs(int currentVertex, int endVertex, List<Integer> path) {
        visited.add(currentVertex);
        path.add(currentVertex);

        if (currentVertex == endVertex) {
            return;
        }

        for (int neighbor : graph.getNeighbors(currentVertex)) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, endVertex, path);
            }
        }
    }
}
