package com.NecroticBamboo;

import java.util.Arrays;
import java.util.Random;

public class Board implements IBoard{

    private final char[][] board;
    private final ISnakeScreen screen;

    public Board(ISnakeScreen snakeScreenIn){
        screen=snakeScreenIn;
        board = new char[getHeight()][getWidth()];
    }

    @Override
    public int getWidth() {
        return screen.getWidth();
    }

    @Override
    public int getHeight() {
        return screen.getHeight();
    }

    @Override
    public void clearBoard() {
        for (char[] chars : board) {
            Arrays.fill(chars, ' ');
        }
    }

    @Override
    public void initialise(char symbol,int limit) {
        clearObjectsOrPointFromBoard(symbol);
        for (int i = 0; i < limit; i++) {
            Coordinates objectPosition = selectRandomElementInArray();
            while (
                    board[objectPosition.getRow()][objectPosition.getColumn()] == SnakeScreen.snakeSymbol
                            || board[objectPosition.getRow()][objectPosition.getColumn()] == SnakeScreen.obstacleSymbol
                            || board[objectPosition.getRow()][objectPosition.getColumn()] == SnakeScreen.mealSymbol) {
                objectPosition = selectRandomElementInArray();
            }
            placePoint(objectPosition, symbol);
        }
    }

    private void clearObjectsOrPointFromBoard(char symbol) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == symbol) {
                    board[i][j] = ' ';
                }
            }
        }
    }

    @Override
    public void placePoint(Coordinates pointCoords, char element) {
        board[pointCoords.getRow()][pointCoords.getColumn()] = element;
    }

    @Override
    public char getPoint(Coordinates coords) {
        return board[coords.getRow()][coords.getColumn()];
    }

    @Override
    public Coordinates selectRandomElementInArray() {
        int row = new Random().nextInt(board.length);
        int column = new Random().nextInt(board[0].length);
        return new Coordinates(row, column);
    }

}
