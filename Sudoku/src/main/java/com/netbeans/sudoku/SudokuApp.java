package com.netbeans.sudoku;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class SudokuApp extends Application {

    private static final int SIZE = 9;
    private TextField[][] cells = new TextField[SIZE][SIZE];
    private Sudoku sudoku = new Sudoku();
    private int[][] initialBoard;

    @Override
    public void start(Stage stage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);

        UnaryOperator<TextFormatter.Change> digitFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty() || newText.matches("[1-9]")) {
                return change;
            }
            return null;
        };

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                TextField tf = new TextField();
                tf.setPrefWidth(40);
                tf.setPrefHeight(40);
                tf.setAlignment(Pos.CENTER);
                tf.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), null, digitFilter));
                cells[r][c] = tf;
                grid.add(tf, c, r);
            }
        }

        Button newPuzzleBtn = new Button("New Puzzle");
        Button solveBtn = new Button("Solve");
        Button resetBtn = new Button("Reset");
        Label statusLabel = new Label("");
        statusLabel.setWrapText(true);
        statusLabel.setTextAlignment(TextAlignment.CENTER);

        newPuzzleBtn.setOnAction(e -> {
            sudoku.generatePuzzle(40);
            initialBoard = deepCopy(sudoku.getBoard());
            updateUIFromBoard(initialBoard);
            statusLabel.setText("New puzzle generated!");
        });

        solveBtn.setOnAction(e -> {
            int[][] board = getBoardFromUI();
            sudoku.setBoard(board);
            if (sudoku.solve()) {
                updateUIFromBoard(sudoku.getBoard());
                statusLabel.setText("Puzzle solved!");
            } else {
                statusLabel.setText("No solution found.");
            }
        });

        resetBtn.setOnAction(e -> {
            if (initialBoard != null) {
                updateUIFromBoard(initialBoard);
                statusLabel.setText("Puzzle reset.");
            }
        });

        HBox buttons = new HBox(10, newPuzzleBtn, solveBtn, resetBtn);
        buttons.setAlignment(Pos.CENTER);

        VBox root = new VBox(10, grid, buttons, statusLabel);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(400, 450);

        Scene scene = new Scene(root);
        stage.setTitle("Sudoku Solver");
        stage.setScene(scene);
        stage.show();

        // Create first puzzle automatically at start
        newPuzzleBtn.fire();
    }

    private int[][] getBoardFromUI() {
        int[][] board = new int[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                String val = cells[r][c].getText();
                if (val == null || val.isEmpty()) {
                    board[r][c] = 0;
                } else {
                    try {
                        board[r][c] = Integer.parseInt(val);
                    } catch (NumberFormatException e) {
                        board[r][c] = 0;
                    }
                }
            }
        }
        return board;
    }

    private void updateUIFromBoard(int[][] board) {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                cells[r][c].setText(board[r][c] == 0 ? "" : Integer.toString(board[r][c]));
            }
        }
    }

    private int[][] deepCopy(int[][] src) {
        int[][] copy = new int[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            System.arraycopy(src[r], 0, copy[r], 0, SIZE);
        }
        return copy;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
