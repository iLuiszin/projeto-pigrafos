package com.pigrafos.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.pigrafos.client.Client;


public class DFS {
    private Graph labyrinthGraph;
    private Client labyrinthClient;
    private String user;
    private String lab;

    public DFS(Graph labyrinthGraph, Client labyrinthClient, String user, String lab) {
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
    
        List<Integer> neighbors = new ArrayList<>(labyrinthGraph.getNeighbors(vertex));
    
        for (int neighbor : neighbors) {
            if (!labyrinthGraph.isVisited(neighbor)) {
                Response moveResponse = labyrinthClient.move(user, lab, neighbor);
                labyrinthGraph.buildGraph(List.of(moveResponse));
                dfs(neighbor, path);
            }
        }
    }
}