package com.pigrafos.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.pigrafos.client.LabyrinthClient;


public class LabyrinthDFS {
    private LabyrinthGraph labyrinthGraph;
    private LabyrinthClient labyrinthClient;
    private String user;
    private String lab;

    public LabyrinthDFS(LabyrinthGraph labyrinthGraph, LabyrinthClient labyrinthClient, String user, String lab) {
        this.labyrinthGraph = labyrinthGraph;
        this.labyrinthClient = labyrinthClient;
        this.user = user;
        this.lab = lab;
    }

    public List<Integer> findPath(int start) throws IOException {
        List<Integer> path = new ArrayList<>();
        labyrinthGraph.markVisited(start);
        dfs(start, path);
        return path.isEmpty() ? null : path;
    }

    private void dfs(int vertex, List<Integer> path) throws IOException {
        System.out.println(path);
        System.out.println(vertex);

        path.add(vertex);

        labyrinthGraph.markVisited(vertex);

        List<Integer> neighbors = labyrinthGraph.getNeighbors(vertex);
        for (int neighbor : neighbors) {
            if (!labyrinthGraph.isVisited(neighbor)) {
                LabyrinthResponse moveResponse = labyrinthClient.move(user, lab, neighbor);
                labyrinthGraph.buildGraph(List.of(moveResponse));
                dfs(neighbor, path);
            }
        }
    }
}