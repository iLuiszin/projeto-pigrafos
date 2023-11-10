package com.pigrafos;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.pigrafos.client.Client;
import com.pigrafos.service.Solver;

public class Main {

    public static void main(String[] args) {

        Solver solver = null;
        try {
            solver = new Solver(new Client());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            return;
        }

        long inicioTempoBfs = System.currentTimeMillis();
        try {
            String user = "Grupo L";
            String labyrinth = solver.getRandomLabyrinth();

            System.out.println(labyrinth);

            List<Integer> shortestPath = solver.bfs(user, labyrinth);

            System.out.println(shortestPath);
            System.out.println(solver.validatePath(user, labyrinth, shortestPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        long fimTempoBfs = System.currentTimeMillis();
        long execucaoTempoBfs = fimTempoBfs - inicioTempoBfs;

        System.out.println("Tempo de execução bfs: " + execucaoTempoBfs + " ms");
    }
}