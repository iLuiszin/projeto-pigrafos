package com.pigrafos;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.pigrafos.client.LabyrinthClient;

public class Main {
    public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException {
        try {
            LabyrinthClient client = new LabyrinthClient();

            String user = "Grupo L";
            String labyrinths = "Labirinto";

            List<Integer> shortestPath = client.findShortestPathBFS(user, labyrinths);
            System.out.println("Shortest Path: " + shortestPath);

            List<Integer> exitPath = client.findShortestPathDFS(user, labyrinths);
            System.out.println("Exit Path: " + exitPath);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
