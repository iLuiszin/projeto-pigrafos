package com.pigrafos;

import com.pigrafos.client.Client;
import com.pigrafos.model.VertexType;
import com.pigrafos.service.Solver;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;

public class Main {
    public static void main(String[] args) {
        Solver solver = null;
        try {
            solver = new Solver(new Client());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            return;
        }

        long startTime = System.currentTimeMillis();
        try {
            String labyrinth = solver.getRandomLabyrinth();
            String user = "Grupo L";

            System.out.println(labyrinth);

            Map<Integer, VertexType> vertexTypes = solver.graphCreator(user, labyrinth).getVertexTypes();
            Map<Integer, List<Integer>> adjacencyList = solver.graphCreator(user, labyrinth).getAdjacencyList();
            AtomicReference<Integer> destination = new AtomicReference<>();
            AtomicReference<Integer> source = new AtomicReference<>();
            System.out.println(vertexTypes);
            System.out.println(adjacencyList);

            vertexTypes.forEach((k, v) -> {
                if (v.equals(VertexType.valueOf("INITIAL"))) {
                    source.set(k);
                } else if (v.equals(VertexType.valueOf("FINAL"))) {
                    destination.set(k);
                }
            });

            System.out.println(solver.bfs(source.get(), destination.get()));
            System.out.println(solver.validatePath(user, labyrinth, solver.bfs(source.get(), destination.get())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        System.out.println("Tempo de execução: " + elapsedTime + " ms");
    }
}
