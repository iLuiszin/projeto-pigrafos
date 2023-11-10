package com.pigrafos.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import com.pigrafos.client.LabyrinthClient;
import com.pigrafos.model.FinalResponse;
import com.pigrafos.model.LabyrinthBFS;
import com.pigrafos.model.LabyrinthDFS;
import com.pigrafos.model.LabyrinthGraph;
import com.pigrafos.model.LabyrinthResponse;
import com.pigrafos.model.TypeVertex;

public class Solver {
    private LabyrinthClient labyrinthClient;
    private LabyrinthGraph labyrinthGraph;
    private Stack<LabyrinthResponse> path;
    private Set<Integer> visited;

    public Solver(LabyrinthClient labyrinthClient) {
        this.labyrinthClient = labyrinthClient;
        this.labyrinthGraph = new LabyrinthGraph();
        this.visited = new HashSet<>();
        this.path = new Stack<>();
    }

    public String getLabyrinth() throws IOException {
        List<String> labyrinths = labyrinthClient.checkLabyrinths();
        Random random = new Random();
        int randomIndex = random.nextInt(labyrinths.size());
        return labyrinths.get(randomIndex);
    }

    public void navigate(String user, String lab, LabyrinthResponse position) throws IOException {
        visited.add(position.getActualPosition());

        if (position.isBegin()) {
            labyrinthGraph.setTypeVertex(position.getActualPosition(), TypeVertex.INICIO);
        } else if (position.isFinal()) {
            labyrinthGraph.setTypeVertex(position.getActualPosition(), TypeVertex.FIM);
        } else {
            labyrinthGraph.setTypeVertex(position.getActualPosition(), TypeVertex.CAMINHO);
        }

        path.push(position);

        for (int move : position.getMoves()) {
            if (!visited.contains(move)) {
                LabyrinthResponse response = labyrinthClient.move(user, lab, move);
                navigate(user, lab, response);
            }
        }

        path.pop();

        if (path.size() - 1 >= 0) {
            labyrinthClient.move(user, lab, path.get(path.size() - 1).getActualPosition());
        }

    }

    public LabyrinthGraph createGraph(String user, String lab) throws IOException {
        LabyrinthResponse response = labyrinthClient.startExploration(user, lab);
        return dfs(user, lab, response);
    }

    public List<Integer> bfs(int start, int end) {
        LabyrinthBFS bfs = new LabyrinthBFS(labyrinthGraph);
        return bfs.findShortestPath(start, end);
    }

    public Stack<LabyrinthResponse> dfs(String user, String lab, LabyrinthResponse position) {
        LabyrinthDFS dfs = new LabyrinthDFS();
        try {
            dfs.navigate(user, lab, position);
        } catch (Exception e) {

        }

        return dfs.getShortestPath();
    }

    public FinalResponse pathValidator(String user, String lab, List<Integer> moves) throws IOException {
        return labyrinthClient.pathValidator(user, lab, moves);
    }

}
