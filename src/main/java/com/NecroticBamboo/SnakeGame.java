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
import java.util.List;
import java.util.Queue;

//TODO: decrease delay when score increases
//TODO: replace magic characters
//TODO: make it colourful
//TODO: blink on loss
//TODO: extract board interface. Remove screen from the game
//TODO: draw updates only
//TODO: write unit tests

public class SnakeGame {

    private final Queue<Coordinates> snakeBody = new LinkedList<>();

    private Coordinates snakeHead;

    private static char[][] board;

    private int score = 0;

    private static Screen screen;
    private final Terminal terminal;
    private static List<User> leaderBoard;
    private static int leaderBoardLength = 0;

    private static int moveRow;
    private static int moveCol;

    public SnakeGame(List<User> leaderBoardIn, Screen screenIn, Terminal terminalIn) {
        leaderBoard = leaderBoardIn;
        screen = screenIn;
        terminal = terminalIn;
    }

    public void play() {
        final TextGraphics textGraphics;
        try {
            fillBoard();
            moveRow = terminal.getTerminalSize().getRows()/3;
            moveCol = terminal.getTerminalSize().getColumns()/4;

            textGraphics = terminal.newTextGraphics();

            screen.setCursorPosition(null);
            screen.clear();
            setStart();
            screen.refresh();

            KeyStroke keyStroke = terminal.readInput();
            KeyStroke previousKeyStroke=keyStroke;
            do {
                if(keyStroke.getKeyType()==KeyType.Escape){
                    return;
                }

                switch (keyStroke.getCharacter()) {
                    case 'w':
                        if (!move(0, -1)) {
                            return;
                        } else {
                            printBoard();
                            textGraphics.putString(0, 0, "Current score: " + score);
                            screen.refresh();
                        }
                        previousKeyStroke = keyStroke;
                        break;
                    case 's':
                        if (!move(0, 1)) {
                            return;
                        } else {
                            printBoard();
                            textGraphics.putString(0, 0, "Current score: " + score);
                            screen.refresh();
                        }
                        previousKeyStroke = keyStroke;
                        break;
                    case 'a':
                        if (!move(-1, 0)) {
                            return;
                        } else {
                            printBoard();
                            textGraphics.putString(0, 0, "Current score: " + score);
                            screen.refresh();
                        }
                        previousKeyStroke = keyStroke;
                        break;
                    case 'd':
                        if (!move(1, 0)) {
                            return;
                        } else {
                            printBoard();
                            textGraphics.putString(0, 0, "Current score: " + score);
                            screen.refresh();
                        }
                        previousKeyStroke = keyStroke;
                        break;
                    default:
                        break;
                }
                Thread.sleep(200);
                keyStroke = terminal.pollInput();

                if (keyStroke == null) {
                    keyStroke = previousKeyStroke;
                }

            } while (keyStroke.getKeyType() != KeyType.Escape);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void fillBoard() throws IOException {
        board = new char[terminal.getTerminalSize().getRows()-1][terminal.getTerminalSize().getColumns()];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = '*';
            }
        }
    }

    private static void printBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                screen.setCharacter(j, i+1, new TextCharacter(board[i][j]));
            }
        }
    }

    public void setStart() {
        score = 0;
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

    private Coordinates selectRandomElementInArray() {
        int totalElement = board.length * board[0].length;
        int indexToSelect = (int) (Math.random() * totalElement);
        int row = indexToSelect % board.length;
        int column = indexToSelect / board[0].length;
        return new Coordinates(row, column);
    }

    private void placePoint(Coordinates head, char element) {
        board[head.getRow()][head.getColumn()] = element;
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
            textGraphics.putString(0, 0, "You have lost with the score of: " + score);
            screen.refresh();

            Thread.sleep(1500);
            screen.clear();
            screen.refresh();

            addUser();

            Thread.sleep(1500);
            screen.clear();
            screen.refresh();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void addUser() {
        final TextGraphics textGraphics;
        try {
            String name = "";

            textGraphics = terminal.newTextGraphics();

            textGraphics.putString(moveCol, moveRow, "Enter your name:");
            screen.refresh();

            KeyStroke keyStroke;
            int limit = 0;
            do {
                keyStroke = terminal.readInput();
                name = name + keyStroke.getCharacter();
                limit++;
            } while (limit < 4);

            textGraphics.putString(moveCol, moveRow, "Your name is: " + name.toUpperCase());
            screen.refresh();

            User user = new User(name.toUpperCase(), score);

            if (leaderBoardLength < 5) {
                checkForDupes(user);
            } else replaceUser(user);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void checkForDupes(User user) {
        if (leaderBoard.isEmpty()) {
            leaderBoard.add(user);
            leaderBoardLength++;
        } else {
            checkForSameName(user);
        }
    }

    private static void checkForSameName(User user) {
        for (User value : leaderBoard) {
            if (value.getName().equals(user.getName())) {
                changeScore(value, user);
                return;
            }
        }
        leaderBoard.add(user);
        leaderBoardLength++;
    }

    private static void changeScore(User oldUser, User newUser) {
        if (newUser.getScore() > oldUser.getScore()) {
            oldUser.setScore(newUser.getScore());
        }
    }

    private static void replaceUser(User user) {
        for (int i = 0; i < leaderBoard.size(); i++) {
            if (leaderBoard.get(i).getScore() < user.getScore()) {
                leaderBoard.remove(i);
                leaderBoard.add(i, user);
                return;
            }
        }
    }
}