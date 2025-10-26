package com.netbeans.sudoku;

import java.util.Random;

public class Sudoku {
    private int[][] board;
    private static final int SIZE = 9;
    private Random random = new Random();

    public Sudoku() {
        board = new int[SIZE][SIZE];
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    // Validity check for a number in a position
    public boolean isValid(int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num)
                return false;
        }
        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;
        for (int r = boxRow; r < boxRow + 3; r++) {
            for (int c = boxCol; c < boxCol + 3; c++) {
                if (board[r][c] == num)
                    return false;
            }
        }
        return true;
    }

    // Backtracking solver
    public boolean solve() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValid(row, col, num)) {
                            board[row][col] = num;
                            if (solve()) {
                                return true;
                            }
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    // Generate puzzle by solving and removing some numbers
    public void generatePuzzle(int numberOfCellsToRemove) {
        // Fill board completely
        solve();

        int removed = 0;
        while (removed < numberOfCellsToRemove) {
            int r = random.nextInt(SIZE);
            int c = random.nextInt(SIZE);
            if (board[r][c] != 0) {
                board[r][c] = 0;
                removed++;
            }
        }
    }
}
