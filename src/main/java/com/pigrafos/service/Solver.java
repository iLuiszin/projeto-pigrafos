package com.pigrafos.service;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import com.pigrafos.client.LabyrinthClient;
import com.pigrafos.model.FinalResponse;
import com.pigrafos.model.LabyrinthBFS;
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

    public List<Integer> bfs(int start, int end) {
        LabyrinthBFS bfs = new LabyrinthBFS(labyrinthGraph);
        return bfs.findShortestPath(start, end);
    }

    public Stack<LabyrinthResponse> dfs(String user, String lab) throws IOException {
        LabyrinthResponse response = labyrinthClient.startExploration(user, lab);

        LabyrinthDFS dfs = new LabyrinthDFS();
        dfs.navigate(user, lab, response);

        Stack<LabyrinthResponse> path = dfs.getShortestPath();

        return path;
    }

    public FinalResponse pathValidator(String user, String lab, List<Integer> moves) throws IOException {
        return labyrinthClient.pathValidator(user, lab, moves);
    }

}
