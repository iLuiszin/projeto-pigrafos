package com.pigrafos.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;

import com.pigrafos.client.Client;
import com.pigrafos.model.FinalResponse;
import com.pigrafos.model.Graph;
import com.pigrafos.model.Response;
import com.pigrafos.model.TypeVertex;

public class Solver {
    private Client client;
    private Graph graph;
    private Stack<Response> path;
    private Set<Integer> visited;

    public Solver(Client client) {
        this.client = client;
        this.graph = new Graph();
        this.visited = new HashSet<>();
        this.path = new Stack<>();
    }

    public String getRandomLabyrinth() throws IOException {
        List<String> labirinthList = client.checkLabyrinths();

        Random random = new Random();
        int labyrinthNumber = random.nextInt(0, labirinthList.size());
        return labirinthList.get(labyrinthNumber);
    }

    public Graph graphCreator(String user, String labyrinth) throws IOException {
        Response starting = client.startExploration(user, labyrinth);

        navigating(user, labyrinth, starting);
        return graph;
    }

    private void navigating(String user, String labirinth, Response currentPosition) throws IOException {
        visited.add(currentPosition.getActualPosition());

        if (currentPosition.isInicio()) {
            graph.setTypeVertex(currentPosition.getActualPosition(), TypeVertex.INICIO);
        } else if (currentPosition.isFinal()) {
            graph.setTypeVertex(currentPosition.getActualPosition(), TypeVertex.FIM);
        } else {
            graph.setTypeVertex(currentPosition.getActualPosition(), TypeVertex.CAMINHO);
        }

        if (graph.getNeighbors(currentPosition.getActualPosition()) == null) {
            graph.buildGraph(List.of(currentPosition));
        }
        path.push(currentPosition);

        for (int newPosition : currentPosition.getMoves()) {
            if (!visited.contains(newPosition)) {
                Response moveResponse = client.move(user, labirinth, newPosition);
                navigating(user, labirinth, moveResponse);
            }
        }

        path.pop();
        if (path.size() - 1 >= 0) {
            client.move(user, labirinth, path.get(path.size() - 1).getActualPosition());
        }

    }

    public List<Integer> bfs(String user, String labyrinth) throws IOException {
        AtomicReference<Integer> source = new AtomicReference<>();
        AtomicReference<Integer> destination = new AtomicReference<>();

        Response starting = client.startExploration(user, labyrinth);
        graph.setTypeVertex(starting.getActualPosition(), TypeVertex.INICIO);
        source.set(starting.getActualPosition());

        performBFS(user, labyrinth, starting, source, destination);

        List<Integer> shortestPath = graph.getPath(source.get(), destination.get());

        return shortestPath;
    }

    private void performBFS(String user, String labirinth, Response start, AtomicReference<Integer> source, AtomicReference<Integer> destination)
            throws IOException {
        Queue<Response> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();

        queue.add(start);
        visited.add(start.getActualPosition());

        while (!queue.isEmpty()) {
            Response current = queue.poll();

            graph.setTypeVertex(current.getActualPosition(), TypeVertex.CAMINHO);

            if (current.isFinal()) {
                destination.set(current.getActualPosition());
                break;
            }

            for (int newPosition : current.getMoves()) {
                if (!visited.contains(newPosition)) {
                    visited.add(newPosition);
                    Response moveResponse = client.move(user, labirinth, newPosition);
                    graph.setTypeVertex(newPosition, TypeVertex.CAMINHO);
                    graph.addEdge(current.getActualPosition(), newPosition);
                    queue.add(moveResponse);
                }
            }
        }
    }


    public FinalResponse validatePath(String user, String labirinth, List<Integer> moves) throws IOException {
        return client.pathValidator(user, labirinth, moves);
    }
}
