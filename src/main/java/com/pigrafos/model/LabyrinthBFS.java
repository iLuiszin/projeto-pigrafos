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
        Queue<Pair<Integer, List<Integer>>> queue = new LinkedList<>();
        List<Integer> rootNeighbors = new ArrayList<>(labyrinthGraph.getNeighbors(start));
        queue.add(new Pair<>(start, rootNeighbors));

        while (!queue.isEmpty()) {
            Pair<Integer, List<Integer>> pair = queue.poll();
            int vertex = pair.getFirst();
            List<Integer> neighbors = pair.getSecond();

            System.out.println("BFS Vertex: " + vertex);
            path.add(vertex);

            labyrinthGraph.markVisited(vertex);

            for (int neighbor : neighbors) {
                LabyrinthResponse moveResponse = labyrinthClient.move(user, lab, neighbor);
                labyrinthGraph.buildGraph(List.of(moveResponse));
                queue.add(new Pair<>(neighbor, new ArrayList<>(labyrinthGraph.getNeighbors(neighbor))));
                labyrinthClient.move(user, lab, vertex);
            }
        }
    }

    private static class Pair<T, U> {
        private final T first;
        private final U second;

        public Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }

        public T getFirst() {
            return first;
        }

        public U getSecond() {
            return second;
        }
    }
}
