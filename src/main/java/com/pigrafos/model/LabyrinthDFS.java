package com.pigrafos.model;

import java.util.List;
import java.util.Stack;

public class LabyrinthDFS {

    private LabyrinthGraph graph;

    public LabyrinthDFS(LabyrinthGraph graph) {
        this.graph = graph;
    }

    public List<Integer> findPath(int startVertex) {
        boolean[] visited = new boolean[graph.getAdjacencyList().size()];
        Stack<Integer> stack = new Stack<>();
        Stack<Integer> pathStack = new Stack<>();

        stack.push(startVertex);
        visited[startVertex] = true;

        while (!stack.isEmpty()) {
            int currentVertex = stack.pop();
            pathStack.push(currentVertex);

            if (graph.getTypeVertex().get(currentVertex) == TypeVertex.FIM) {
                return pathStack;
            }

            List<Integer> neighbors = graph.getNeighbors(currentVertex);
            for (int neighbor : neighbors) {
                if (!visited[neighbor]) {
                    stack.push(neighbor);
                    visited[neighbor] = true;
                }
            }
        }

        return null; // No path found
    }
}