/* Author: Elton Shumka */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private final WeightedQuickUnionUF myUF;
    private final boolean[][] theGrid;
    private final int s;          // The size of the grid


    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Grid size negative or zero: " + n + " !!");
        }
        theGrid = new boolean[n][n];
        s = n;
        myUF = new WeightedQuickUnionUF(s * s + 2);

        for (int i = 0; i < s; i++) {
            getMyUF().union(s * s, i);
        }
        for (int j = 0; j < s; j++) {
            getMyUF().union(s * s + 1, s * (s - 1) + j);
        }
        int i, j;
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                getTheGrid()[i][j] = false;
            }
        }
    }

    public void open(int row, int col) {
        if (row <= 0 || row > s) {
            throw new IllegalArgumentException("Index " + row + " out of range");
        }
        if (col <= 0 || col > s) {
            throw new IllegalArgumentException("Index " + col + " out of range");
        }
        getTheGrid()[row - 1][col - 1] = true;
        if (row >= 2 && col >= 2 && row <= s - 1 && col <= s - 1) {
            if (isOpen(row - 1, col)) {
                getMyUF().union((row - 2) * s + col - 1, (row - 1) * s + col - 1);
            }
            if (isOpen(row, col - 1)) {
                getMyUF().union((row - 1) * s + col - 2, (row - 1) * s + col - 1);
            }
            if (isOpen(row, col + 1)) {
                getMyUF().union((row - 1) * s + col, (row - 1) * s + col - 1);
            }
            if (isOpen(row + 1, col)) {
                getMyUF().union(row * s + col - 1, (row - 1) * s + col - 1);
            }
        }
        else if (row == 1 && col != 1 && col != s) {
            if (isOpen(1, col - 1)) {
                getMyUF().union(col - 2, col - 1);
            }
            if (isOpen(1, col + 1)) {
                getMyUF().union(col, col - 1);
            }
            if (isOpen(2, col)) {
                getMyUF().union(s + col - 1, col - 1);
            }
        }
        else if (row == s && col != 1 && col != s) {
            if (isOpen(s - 1, col)) {
                getMyUF().union((s - 2) * s + col - 1, (s - 1) * s + col - 1);
            }
            if (isOpen(s, col - 1)) {
                getMyUF().union((s - 1) * s + col - 2, (s - 1) * s + col - 1);
            }
            if (isOpen(s, col + 1)) {
                getMyUF().union((s - 1) * s + col, (s - 1) * s + col - 1);
            }
        }
        else if (col == 1 && row != 1 && row != s) {
            if (isOpen(row - 1, 1)) {
                getMyUF().union((row - 2) * s, (row - 1) * s);
            }
            if (isOpen(row, 2)) {
                getMyUF().union((row - 1) * s + 1, (row - 1) * s);
            }
            if (isOpen(row + 1, 1)) {
                getMyUF().union(row * s, (row - 1) * s);
            }
        }
        else if (col == s && row != 1 && row != s) {
            if (isOpen(row - 1, s)) {
                getMyUF().union(row * s - s - 1, row * s - 1);
            }
            if (isOpen(row, s - 1)) {
                getMyUF().union(row * s - 2, row * s - 1);
            }
            if (isOpen(row + 1, s)) {
                getMyUF().union(row * s + s - 1, row * s - 1);
            }
        }
        else if (row == 1 && col == 1) {
            if (isOpen(1, 2)) {
                getMyUF().union(1, 0);
            }
            if (isOpen(2, 1)) {
                getMyUF().union(s, 0);
            }
        }
        else if (row == 1) {
            if (isOpen(1, s - 1)) {
                getMyUF().union(s - 2, s - 1);
            }
            if (isOpen(2, s)) {
                getMyUF().union(2 * s - 1, s - 1);
            }
        }
        else if (row == s && col == 1) {
            if (isOpen(s, 2)) {
                getMyUF().union((s - 1) * s + 1, (s - 1) * s);
            }
            if (isOpen(s - 1, 1)) {
                getMyUF().union((s - 2) * s, (s - 1) * s);
            }
        }
        else if (row == s) {
            if (isOpen(s - 1, s)) {
                getMyUF().union((s - 2) * s + s - 1, s * s - 1);
            }
            if (isOpen(s, s - 1)) {
                getMyUF().union((s - 1) * s + s - 2, s * s - 1);
            }
        }
    }

    public boolean isOpen(int row, int col) {
        if (row <= 0 || row > s) {
            throw new IllegalArgumentException("Index " + row + " out of range");
        }
        if (col <= 0 || col > s) {
            throw new IllegalArgumentException("Index " + col + " out of range");
        }
        return getTheGrid()[row - 1][col - 1];
    }

    public boolean isFull(int irow, int icol) {
        if (irow <= 0 || irow > s) {
            throw new IllegalArgumentException("Index " + irow + " out of range");
        }
        if (icol <= 0 || icol > s) {
            throw new IllegalArgumentException("Index " + icol + " out of range");
        }
        return isOpen(irow, icol) && getMyUF().find((irow - 1) * s + icol - 1) == getMyUF()
                .find(s * s);

    }

    public int numberOfOpenSites() {
        // int num = Arrays.deepToString(getTheGrid()).replaceAll("[^t]", "").length();
        int num = 0;
        for (int k = 0; k < s; k++) {
            for (int m = 0; m < s; m++) {
                if (getTheGrid()[k][m]) {
                    num = num + 1;
                }
            }
        }
        return num;
    }

    public boolean percolates() {
        boolean val = (getMyUF().find(s * s) == getMyUF().find(s * s + 1));
        return val;
    }

    public static void main(String[] args) {
        // empty main
    }

    private WeightedQuickUnionUF getMyUF() {
        return myUF;
    }

    private boolean[][] getTheGrid() {
        return theGrid;
    }
}
