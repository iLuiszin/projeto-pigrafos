package com.pigrafos;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import com.pigrafos.client.LabyrinthClient;
import com.pigrafos.model.FinalResponse;
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
            FinalResponse finalResponse = solver.dfs(user, labyrinth);

            System.out.println("Final Response: " + finalResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Total time: " + totalTime + "ms");
    }
}