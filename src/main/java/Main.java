import com.pigrafos.client.Client;
import com.pigrafos.Solver;

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
            String user = "Grupo L";
            String lab = solver.getRandomLabyrinth();

            System.out.println(lab);

            solver.start(user, lab);
            solver.findShortestPath();

        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        System.out.println("Tempo de execução: " + elapsedTime + " ms");
    }
}
