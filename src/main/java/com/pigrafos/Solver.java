package com.pigrafos;

import com.pigrafos.client.Client;
import com.pigrafos.model.Graph;
import com.pigrafos.model.Response;
import com.pigrafos.model.FinalResponse;

import java.io.IOException;
import java.util.*;

public class Solver {
    private final Client client;
    private final Graph graph;
    private final Stack<Response> path;
    private final Set<Integer> visited;

    private String user;
    private String lab;
    private int source;
    private int destination;

    public Solver(Client client) {
        this.client = client;
        this.graph = new Graph();
        this.visited = new HashSet<>();
        this.path = new Stack<>();
        this.source = 0;
        this.destination = 1;
    }

    public List<String> listAvailableLabs() throws IOException {
        return client.checkLabs();
    }

    public void start(String user, String lab) throws IOException {
        Response starting = client.apiStart(user, lab);
        source = starting.getActualPosition();
        this.user = user;
        this.lab = lab;

        dfs(starting);
    }

    private void dfs(Response currentPosition) throws IOException {
        visited.add(currentPosition.getActualPosition());

        if (currentPosition.isFinal()) {
            destination = currentPosition.getActualPosition();
        }

        graph.buildGraph(currentPosition);
        path.push(currentPosition);

        for (int newPosition : currentPosition.getMovimentos()) {
            if (!visited.contains(newPosition)) {
                Response moveResponse = client.apiMove(user, lab, newPosition);
                dfs(moveResponse);
            }
        }

        path.pop();
        if (!path.isEmpty()) {
            client.apiMove(user, lab, path.peek().getActualPosition());
        }
    }

    public void findShortestPath() throws IOException {
        Map<Integer, Integer> distance = new HashMap<>();
        Map<Integer, Integer> parent = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();

        for (int vertex : graph.getAdjacencyList().keySet()) {
            distance.put(vertex, Integer.MAX_VALUE);
            parent.put(vertex, null);
        }

        distance.put(source, 0);
        queue.add(source);

        while (!queue.isEmpty()) {
            int current = queue.poll();

            for (int neighbor : graph.getNeighbors(current)) {
                if (distance.get(neighbor) == Integer.MAX_VALUE) {
                    distance.put(neighbor, distance.get(current) + 1);
                    parent.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        List<Integer> shortestPath = new ArrayList<>();
        int current = destination;

        while (current != source) {
            shortestPath.add(current);
            current = parent.get(current);
        }

        shortestPath.add(source);
        Collections.reverse(shortestPath);
        FinalResponse finalResponse = client.apiValided(user, lab, shortestPath);

        System.out.println("Steps: " + finalResponse.getMoves());
        System.out.println("Path: " + shortestPath);
        System.out.println("IsValid: " + finalResponse.isValid());
    }
}
