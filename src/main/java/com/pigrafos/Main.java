package com.pigrafos;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import com.pigrafos.client.LabyrinthClient;
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
        long startTime = System.currentTimeMillis();

        try {
            String user = "luis";
            String labyrinth = solver.getLabyrinth();

            System.out.println(solver.dfs(user, labyrinth));

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        long endTime = System.currentTimeMillis();
        long tempoTotal = endTime - startTime;
        System.out.println("Tempo total: " + tempoTotal + "ms");

    }

}
