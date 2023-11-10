package com.pigrafos.service;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import com.pigrafos.client.LabyrinthClient;
import com.pigrafos.model.FinalResponse;
import com.pigrafos.model.LabyrinthDFS;
import com.pigrafos.model.LabyrinthGraph;
import com.pigrafos.model.LabyrinthResponse;

public class Solver {
    private LabyrinthClient labyrinthClient;
    private LabyrinthGraph labyrinthGraph;

    public Solver(LabyrinthClient labyrinthClient) {
        this.labyrinthClient = labyrinthClient;
        this.labyrinthGraph = new LabyrinthGraph();
    }

    public String getLabyrinth() throws IOException {
        List<String> labyrinths = labyrinthClient.checkLabyrinths();
        Random random = new Random();
        int randomIndex = random.nextInt(labyrinths.size());
        return labyrinths.get(randomIndex);
    }

    public FinalResponse pathValidator(String user, String lab, List<Integer> moves) throws IOException {
        return labyrinthClient.pathValidator(user, lab, moves);
    }

    public FinalResponse dfs(String user, String lab) throws IOException {
        LabyrinthResponse response = labyrinthClient.startExploration(user, lab);
        LabyrinthDFS labyrinthDFS = new LabyrinthDFS(labyrinthGraph, labyrinthClient, user, lab);

        labyrinthGraph.buildGraph(List.of(response));

        List<Integer> path = labyrinthDFS.findPath(response.getActualPosition());

        if (path != null) {
            System.out.println("Path found: " + path);
            List<Integer> movements = convertPathToMovements(path);

            return pathValidator(user, lab, movements);
        } else {
            System.out.println("No path found.");
            return new FinalResponse(false, 0);
        }
    }

    private List<Integer> convertPathToMovements(List<Integer> path) {
        return path.subList(1, path.size());
    }
}
