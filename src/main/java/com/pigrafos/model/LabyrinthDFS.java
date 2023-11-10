package com.pigrafos.model;

import java.io.IOException;
import java.util.*;

import com.pigrafos.client.LabyrinthClient;

public class LabyrinthDFS {
    private LabyrinthGraph labyrinthGraph;
    private LabyrinthClient labyrinthClient;
    private HashSet<Integer> visited;
    private Stack<LabyrinthResponse> path;
    private Stack<LabyrinthResponse> shortestPath;

    public LabyrinthDFS() {
        visited = new HashSet<>();
        path = new Stack<>();
        shortestPath = new Stack<>();
    }

    public void navigate(String user, String lab, LabyrinthResponse position) throws IOException {
        visited.add(position.getActualPosition());

        if (position.isBegin()) {
            labyrinthGraph.setTypeVertex(position.getActualPosition(), TypeVertex.INICIO);
        } else if (position.isFinal()) {
            labyrinthGraph.setTypeVertex(position.getActualPosition(), TypeVertex.FIM);
            // Se chegamos ao final, verificamos se este caminho é mais curto que o
            // atualmente armazenado
            if (shortestPath.isEmpty() || path.size() < shortestPath.size()) {
                shortestPath = new Stack<>();
                shortestPath.addAll(path);
            }
        } else {
            labyrinthGraph.setTypeVertex(position.getActualPosition(), TypeVertex.CAMINHO);
        }

        path.push(position);

        for (int move : position.getMoves()) {
            if (!visited.contains(move)) {
                LabyrinthResponse response = labyrinthClient.move(user, lab, move);
                navigate(user, lab, response);
            }
        }

        path.pop();
        visited.remove(position.getActualPosition());

        if (!path.isEmpty()) {
            labyrinthClient.move(user, lab, path.peek().getActualPosition());
        }
    }

    public Stack<LabyrinthResponse> getShortestPath() {
        return shortestPath;
    }

    // public List<Integer> findExitPath(int startVertex, int endVertex) {
    // List<Integer> path = new ArrayList<>();
    // dfs(startVertex, endVertex, path);
    // return path;
    // }

    // private void dfs(int currentVertex, int endVertex, List<Integer> path) {
    // visited.add(currentVertex);
    // path.add(currentVertex);

    // if (currentVertex == endVertex) {
    // return;
    // }

    // for (int neighbor : graph.getNeighbors(currentVertex)) {
    // if (!visited.contains(neighbor)) {
    // dfs(neighbor, endVertex, path);
    // }
    // }
    // }
}
