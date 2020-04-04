package com.NecroticBamboo;


import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class SnakeGame {

    private final Queue<Coordinates> snakeBody = new LinkedList<>();

    private Coordinates snakeHead;

    private static char[][] board;

    private int score = 0;

    private static Screen screen;
    private Terminal terminal;
    private int row;
    private int column;
    private static int moveRow;
    private static int moveCol;

    public SnakeGame(Screen screenIn, Terminal terminalIn, int rowIn, int columnIn) {
        screen = screenIn;
        terminal = terminalIn;
        row = rowIn;
        column = columnIn;
    }

    public void play() {
        fillBoard();
        try {
            moveRow =(terminal.getTerminalSize().getColumns()/10)-1;
            moveCol =terminal.getTerminalSize().getColumns()/4;

            screen.setCursorPosition(null);
            screen.clear();
            setStart();
            screen.refresh();

            final TextGraphics textGraphics = terminal.newTextGraphics();
            KeyStroke keyStroke = terminal.readInput();
            KeyStroke previousKeyStroke;
            do {
                textGraphics.putString(moveCol, moveRow +7, keyStroke.toString());
                switch (keyStroke.getCharacter()) {
                    case 'w':
                        if (!move(0, -1)) {
                            return;
                        } else {
                            printBoard();
                            screen.refresh();
                        }
                        previousKeyStroke = keyStroke;
                        break;
                    case 's':
                        if (!move(0, 1)) {
                            return;
                        } else {
                            printBoard();
                            screen.refresh();
                        }
                        previousKeyStroke = keyStroke;
                        break;
                    case 'a':
                        if (!move(-1, 0)) {
                            return;
                        } else {
                            printBoard();
                            screen.refresh();
                        }
                        previousKeyStroke = keyStroke;
                        break;
                    case 'd':
                        if (!move(1, 0)) {
                            return;
                        } else {
                            printBoard();
                            screen.refresh();
                        }
                        previousKeyStroke = keyStroke;
                        break;
                    default:
                        previousKeyStroke = null;
//                            System.out.println("Something is wrong!  SnakeGame");
                        break;
                }
                Thread.sleep(1000);
                keyStroke = terminal.pollInput();

                if (keyStroke == null) {
                     keyStroke = previousKeyStroke;
                }

            } while (keyStroke.getKeyType() != KeyType.Escape);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void fillBoard() {
        board = new char[row][column];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = '*';
            }
        }
    }

    private static void printBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                screen.setCharacter(j+ moveCol +10, i+ moveRow, new TextCharacter(board[i][j]));
            }
        }
    }

    public void setStart() {
        score=0;
        clearBoard();
        snakeBody.clear();
        snakeHead = selectRandomElementInArray();
        placePoint(snakeHead, 'A');
        makeAndPlacePointToCollect();
        printBoard();
    }

    private void makeAndPlacePointToCollect() {
        Coordinates point = selectRandomElementInArray();
        while (board[point.getRow()][point.getColumn()] == 'A') {
            point = selectRandomElementInArray();
        }
        placePoint(point, '0');
    }

    private void placePoint(Coordinates head, char element) {
        board[head.getRow()][head.getColumn()] = element;
    }

    private Coordinates selectRandomElementInArray() {
        int totalElement = board.length * board[0].length;
        int indexToSelect = (int) (Math.random() * totalElement);
        int xIndex = indexToSelect / board.length;
        int yIndex = indexToSelect % board.length;
        return new Coordinates(xIndex, yIndex);
    }

    private void clearBoard() {
        for (char[] chars : board) {
            Arrays.fill(chars, '*');
        }
    }


    private boolean move(int columnChange, int rowChange) {
        int newRow = snakeHead.getRow() + rowChange;
        int newColumn = snakeHead.getColumn() + columnChange;
        if (newRow >= board.length || newRow < 0 || newColumn >= board[0].length || newColumn < 0) {
            printEndGameMessage();
            return false;
        } else if (board[newRow][newColumn] == 'A') {
            printEndGameMessage();
            return false;
        } else if (board[newRow][newColumn] == '0') {
            snakeBody.add(snakeHead);
            snakeHead = new Coordinates(newRow, newColumn);
            placePoint(snakeHead, 'A');
            makeAndPlacePointToCollect();
            score++;
        } else {
            snakeBody.add(snakeHead);
            snakeHead = new Coordinates(newRow, newColumn);
            Coordinates tail = snakeBody.poll();
            placePoint(snakeHead, 'A');
            placePoint(tail, '*');
        }
        return true;
    }

    private void printEndGameMessage() {
        final TextGraphics textGraphics;
        try {
            textGraphics = terminal.newTextGraphics();
            textGraphics.putString(moveCol, board[0].length+ moveRow, "You have lost!");
            textGraphics.putString(moveCol, board[0].length + moveRow + 1, "Your score is " + score);
            screen.refresh();
            Thread.sleep(1000);
            screen.clear();
            screen.refresh();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
//        System.out.println("Lost message triggered");
    }

    public int getScore(){return score;}
}