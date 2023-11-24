import com.pigrafos.client.Client;
import com.pigrafos.Solver;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Scanner;
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
            String user = "Group L";
            List<String> availableLabs = solver.listAvailableLabs();

            System.out.println("Available Labs:");
            for (int i = 0; i < availableLabs.size(); i++) {
                System.out.println((i + 1) + ". " + availableLabs.get(i));
            }

            try (Scanner scanner = new Scanner(System.in)) {
                System.out.print("Choose a lab (1-" + availableLabs.size() + "): ");
                int chosenLabIndex = scanner.nextInt();

                if (chosenLabIndex < 1 || chosenLabIndex > availableLabs.size()) {
                    System.out.println("Invalid index. Exiting...");
                    return;
                }
                String lab = availableLabs.get(chosenLabIndex - 1);

                System.out.println("Chosen lab: " + lab);

                solver.start(user, lab);
            }
            solver.findShortestPath();

        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        double seconds = (double) elapsedTime / 1000.0;

        System.out.println("Execution time: " + seconds + " s");
    }
}
