package com.pigrafos.model;

import java.io.IOException;
import java.util.*;

import com.pigrafos.client.LabyrinthClient;

public class LabyrinthBFS {
    private LabyrinthGraph labyrinthGraph;
    private LabyrinthClient labyrinthClient;
    private String user;
    private String lab;

    public LabyrinthBFS(LabyrinthGraph labyrinthGraph, LabyrinthClient labyrinthClient, String user, String lab) {
        this.labyrinthGraph = labyrinthGraph;
        this.labyrinthClient = labyrinthClient;
        this.user = user;
        this.lab = lab;
    }

    public List<Integer> findPath(int start) throws IOException {
        List<Integer> path = new ArrayList<>();
        labyrinthGraph.markVisited(start);
        bfs(start, path);
        return path.isEmpty() ? null : path;
    }

    private void bfs(int start, List<Integer> path) throws IOException {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            System.out.println("BFS Vertex: " + vertex);
            path.add(vertex);

            labyrinthGraph.markVisited(vertex);

            List<Integer> neighbors = new ArrayList<>(labyrinthGraph.getNeighbors(vertex));

            for (int neighbor : neighbors) {
                if (!labyrinthGraph.isVisited(neighbor)) {
                    LabyrinthResponse moveResponse = labyrinthClient.move(user, lab, neighbor);
                    labyrinthGraph.buildGraph(List.of(moveResponse));
                    queue.add(neighbor);
                }
            }
        }
    }
}