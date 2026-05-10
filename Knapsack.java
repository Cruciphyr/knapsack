//Tarin Katasema




import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

class Knapsack {

    static int max(int a, int b) { return (a > b) ? a : b; }

    // Brute force
    static int knapSackBrute(int W, int wt[], int val[], int n) {
        if (n == 0 || W == 0)
            return 0;
        if (wt[n - 1] > W)
            return knapSackBrute(W, wt, val, n - 1);
        else
            return max(val[n - 1] + knapSackBrute(W - wt[n - 1], wt, val, n - 1),
                       knapSackBrute(W, wt, val, n - 1));
    }

    // 
    static int knapSack(int W, int wt[], int val[], int n) {
        int[][] dp = new int[n + 1][W + 1];
        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= W; w++) {
                dp[i][w] = dp[i - 1][w];
                if (wt[i - 1] <= w)
                    dp[i][w] = max(dp[i][w], val[i - 1] + dp[i - 1][w - wt[i - 1]]);
            }
        }
        return dp[n][W];
    }

    public static void main(String args[]) {
        try {
            Scanner scanner = new Scanner(new File(args[0]));

            int capacity = scanner.nextInt();
            int n = scanner.nextInt();

            int[] values = new int[n];
            int[] weights = new int[n];

            for (int i = 0; i < n; i++) {
                values[i] = scanner.nextInt();
                weights[i] = scanner.nextInt();
            }
            scanner.close();

            long CUTOFF_NS = 10_000_000_000L; // 10 second cutoff

            // --- Brute Force ---
            System.out.println("Running brute force...");
            long bruteStart = System.nanoTime();
            long bruteEnd;
            String bruteResult;

            Thread bruteThread = new Thread(() -> knapSackBrute(capacity, weights, values, n));
            bruteThread.start();
            bruteThread.join(CUTOFF_NS / 1_000_000); // join uses milliseconds

            bruteEnd = System.nanoTime();
            if (bruteThread.isAlive()) {
                bruteThread.interrupt();
                bruteResult = "TIMED OUT (> 10 seconds)";
            } else {
                bruteResult = String.valueOf(knapSackBrute(capacity, weights, values, n));
            }

            System.out.println("Brute Force Max Value: " + bruteResult);
            System.out.println("Brute Force Time: " + (bruteEnd - bruteStart) / 1e9 + " seconds");

            // --NOn brutforce method 
            System.out.println("\nRunning KnapSack...");
            long dpStart = System.nanoTime();
            int dpResult = knapSack(capacity, weights, values, n);
            long dpEnd = System.nanoTime();

            System.out.println("KnapSack Max Value: " + dpResult);
            System.out.println("KnapSack Time: " + (dpEnd - dpStart) / 1e9 + " seconds");

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted: " + e.getMessage());
        }
    }
}