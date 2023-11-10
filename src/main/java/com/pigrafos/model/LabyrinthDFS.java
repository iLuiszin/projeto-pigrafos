package com.pigrafos.model;

import java.util.ArrayList;
import java.util.List;

public class LabyrinthDFS {
    private LabyrinthGraph labyrinthGraph;
    private boolean[] visited;

    public LabyrinthDFS(LabyrinthGraph labyrinthGraph) {
        this.labyrinthGraph = labyrinthGraph;
        this.visited = new boolean[labyrinthGraph.getAdjacencyList().size()];
    }

    public List<Integer> findPath(int start) {
        List<Integer> path = new ArrayList<>();
        dfs(start, path);
        return path.isEmpty() ? null : path;
    }

    private void dfs(int vertex, List<Integer> path) {
        System.out.println(visited);
        System.out.println(path);
        System.out.println(vertex);
        visited[vertex] = true;
        path.add(vertex);

        List<Integer> neighbors = labyrinthGraph.getNeighbors(vertex);
        for (int neighbor : neighbors) {
            if (!visited[neighbor]) {
                dfs(neighbor, path);
            }
        }
    }
}