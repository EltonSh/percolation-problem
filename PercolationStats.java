/* *****************************************************************************
 *  Name:              Elton Shumka
 *  Last modified:     4/4/2021
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double[] thresh;
    private int T;

    public PercolationStats(int n, int trials) {
        if (n <= 0) {
            throw new IllegalArgumentException("Grid size negative or zero: " + n + " !!");
        }
        if (trials <= 0) {
            throw new IllegalArgumentException(
                    "Number of trials negative or zero: " + trials + " !!");
        }
        setT(trials);
        thresh = new double[T];
        for (int j = 0; j < T; j++) {
            Percolation perc = new Percolation(n);
            double num;
            double size = n * n;
            while (!perc.percolates()) {
                int nOne = StdRandom.uniform(1, n + 1);
                int nTwo = StdRandom.uniform(1, n + 1);
                if (!perc.isOpen(nOne, nTwo)) {
                    perc.open(nOne, nTwo);
                }
            }
            // StdOut.println("Number of open sites at trial " + j + " : " + perc.numberOfOpenSites());
            num = perc.numberOfOpenSites();
            getThresh()[j] = num / size;
        }
    }

    public double mean() {
        return StdStats.mean(thresh);
    }

    public double stddev() {
        return StdStats.stddev(thresh);
    }

    public double confidenceLo() {
        double confL = mean() - (CONFIDENCE_95 * stddev()) / Math.sqrt(T);
        return confL;
    }

    public double confidenceHi() {
        double confH = mean() + (CONFIDENCE_95 * stddev()) / Math.sqrt(T);
        return confH;
    }

    public static void main(String[] args) {
        int nR = Integer.parseInt(args[0]);
        int nTrials = Integer.parseInt(args[1]);
        PercolationStats percStat = new PercolationStats(nR, nTrials);
        StdOut.println("mean                    = " + percStat.mean());
        StdOut.println("stddev                  = " + percStat.stddev());
        StdOut.println("95% confidence interval = [" + percStat.confidenceLo() + "," + percStat
                .confidenceHi() + "]");
    }

    private double[] getThresh() {
        return thresh;
    }

    private void setT(int t) {
        T = t;
    }
}
