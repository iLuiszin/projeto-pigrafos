package com.pigrafos;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.pigrafos.client.LabyrinthClient;
import com.pigrafos.model.TypeVertex;
import com.pigrafos.service.Solver;

public class Main {

    public static void main(String[] args) {

        Solver solver = null;
        try {
            solver = new Solver(new LabyrinthClient());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            return;
        }

        List<String> labyrinthsList;
        long startTime = System.currentTimeMillis();

        try {
            String labyrinth = solver.getLabyrinth();
            String user = "luis";

            solver.createGraph(user, labyrinth);
            Map<Integer, List<Integer>> adjacencyList = solver.createGraph(user, labyrinth).getAdjacencyList();
            AtomicReference<Integer> start = new AtomicReference<>();
            AtomicReference<Integer> end = new AtomicReference<>();

            typeVertex.forEach((key, value) -> {
                if (value.equals(TypeVertex.valueOf("INICIO"))) {
                    start.set(key);
                } else if (value.equals(TypeVertex.valueOf("FIM"))) {
                    end.set(key);
                }
            });

            System.out.println(solver.pathValidator(user, labyrinth, solver.bfs(start.get(), end.get())));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        long endTime = System.currentTimeMillis();
        long tempoTotal = endTime - startTime;
        System.out.println("Tempo total: " + tempoTotal + "ms");

    }

}