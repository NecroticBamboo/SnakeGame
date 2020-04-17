package com.NecroticBamboo;


import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.*;


//TODO: make it colourful
//TODO: extract board interface. Remove screen from the game
//TODO: draw updates only
//TODO: write unit tests

public class SnakeGame {

    private final Queue<Coordinates> snakeBody = new LinkedList<>();

    private Coordinates snakeHeadPosition;

    private static char[][] board;

    private int score = 0;
    private int delay = 250;
    private int level = 1;

    private final char snakeSymbol = 'A';
    private final char pointSymbol = '0';
    private final char objectSymbol = '*';


    private final int blinkDelay = 500;
    private final int numberOfBlinks = 3;
    private int objectLimit = 0;
    private int pointLimit = 0;

    private final TextColor colorWhite = TextColor.ANSI.WHITE;
    private final TextColor colorBlack = TextColor.ANSI.BLACK;

    private static List<User> leaderBoard;
    private final Options settings;
    private static Screen screen;
    private final Terminal terminal;

    private static int leaderBoardLength = 0;

    private static int moveRow;
    private static int moveCol;

    public SnakeGame(List<User> leaderBoardIn, Options settingsIn, Screen screenIn, Terminal terminalIn) {
        leaderBoard = leaderBoardIn;
        settings = settingsIn;
        screen = screenIn;
        terminal = terminalIn;
    }

    public void play() {
        final TextGraphics textGraphics;
        try {
            fillBoard();
            moveRow = terminal.getTerminalSize().getRows() / 3;
            moveCol = terminal.getTerminalSize().getColumns() / 4;

            textGraphics = terminal.newTextGraphics();

            screen.setCursorPosition(null);
            screen.clear();
            setStart();
            screen.refresh();

            KeyStroke keyStroke = terminal.readInput();
            KeyStroke previousKeyStroke = keyStroke;
            do {
                if (keyStroke.getKeyType() == KeyType.Escape) {
                    return;
                }

                switch (keyStroke.getCharacter()) {
                    case 'w':
                        if (move(0, -1)) {
                            return;
                        } else {
                            printBoard();
                        }
                        previousKeyStroke = keyStroke;
                        break;
                    case 's':
                        if (move(0, 1)) {
                            return;
                        } else {
                            printBoard();
                        }
                        previousKeyStroke = keyStroke;
                        break;
                    case 'a':
                        if (move(-1, 0)) {
                            return;
                        } else {
                            printBoard();
                        }
                        previousKeyStroke = keyStroke;
                        break;
                    case 'd':
                        if (move(1, 0)) {
                            return;
                        } else {
                            printBoard();
                        }
                        previousKeyStroke = keyStroke;
                        break;
                    default:
                        break;
                }
                textGraphics.setBackgroundColor(colorWhite);
                textGraphics.setForegroundColor(colorBlack);
                textGraphics.putString(0, 0, "Current score: " + score + " Current level: " + level + "  Current delay: " + delay + " ");
                screen.refresh();

                Thread.sleep(delay);
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
        board = new char[terminal.getTerminalSize().getRows() - 1][terminal.getTerminalSize().getColumns()];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = ' ';
            }
        }
    }

    private static void printBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                screen.setCharacter(j, i + 1, new TextCharacter(board[i][j]));
            }
        }
    }

    public void setStart() {
        score = 0;
        delay = 250;
        pointLimit = 1;

        clearBoard();
        snakeBody.clear();
        snakeHeadPosition = selectRandomElementInArray();
        placePoint(snakeHeadPosition, snakeSymbol);
        placeObjectsAndPoints(pointSymbol, pointLimit);
        printBoard();
    }

    private void clearBoard() {
        for (char[] chars : board) {
            Arrays.fill(chars, ' ');
        }
    }

    private Coordinates selectRandomElementInArray() {
        int row = new Random().nextInt(board.length);
        int column = new Random().nextInt(board[0].length);
        return new Coordinates(row, column);
    }

    private void placeObjectsAndPoints(char symbol, int limit) {
        clearObjectsOrPointFromBoard(symbol);
        for (int i = 0; i < limit; i++) {
            Coordinates objectPosition = selectRandomElementInArray();
            while (board[objectPosition.getRow()][objectPosition.getColumn()] == snakeSymbol || board[objectPosition.getRow()][objectPosition.getColumn()] == objectSymbol || board[objectPosition.getRow()][objectPosition.getColumn()] == pointSymbol) {
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

    private void placePoint(Coordinates pointCoords, char element) {
        board[pointCoords.getRow()][pointCoords.getColumn()] = element;
    }

    private boolean move(int columnChange, int rowChange) {
        int newRow = snakeHeadPosition.getRow() + rowChange;
        int newColumn = snakeHeadPosition.getColumn() + columnChange;

        if (newRow >= board.length && settings.getBorderOption()) {
            newRow = 0;
        }
        if (newRow < 0 && settings.getBorderOption()) {
            newRow = board.length - 1;
        }
        if (newColumn >= board[0].length && settings.getBorderOption()) {
            newColumn = 0;
        }
        if (newColumn < 0 && settings.getBorderOption()) {
            newColumn = board[0].length - 1;
        }


        if (newRow >= board.length || newRow < 0 || newColumn >= board[0].length || newColumn < 0 && !settings.getBorderOption() || board[newRow][newColumn] == snakeSymbol || board[newRow][newColumn] == objectSymbol) {
            blink();
            printEndGameMessage();
            return true;

        } else if (board[newRow][newColumn] == pointSymbol) {
            snakeBody.add(snakeHeadPosition);
            snakeHeadPosition = new Coordinates(newRow, newColumn);
            placePoint(snakeHeadPosition, snakeSymbol);

            placeObjectsAndPoints(pointSymbol, pointLimit);
            if (!settings.getObjectsOption()) {
                placeObjectsAndPoints(objectSymbol, objectLimit);
            }

            proceedToNextLevel();

        } else {
            snakeBody.add(snakeHeadPosition);
            snakeHeadPosition = new Coordinates(newRow, newColumn);
            Coordinates tail = snakeBody.poll();
            placePoint(snakeHeadPosition, snakeSymbol);
            placePoint(tail, ' ');
        }
        return false;
    }

    private void proceedToNextLevel() {
        int percentage = (int) (delay * (10.0f / 100.0f));
        delay = delay - percentage;
        if (delay <= 50) {
            delay = 250;
            level++;
            objectLimit = objectLimit + 2;
            pointLimit++;

            if (level >= 5) {
                pointLimit = 5;
            }

            placeObjectsAndPoints(pointSymbol, pointLimit);
            if (!settings.getObjectsOption()) {
                placeObjectsAndPoints(objectSymbol, objectLimit);
            }
        }
        incrementScore(settings.getDoublePointsOption());
    }

    private void incrementScore(boolean doublePointsOption) {
        if (!doublePointsOption) {
            score++;
        } else score = score + 2;
    }

    private void blink() {
        final TextGraphics textGraphics;
        try {
            textGraphics = terminal.newTextGraphics();

            screen.clear();
            screen.refresh();

            for (int i = 0; i < numberOfBlinks; i++) {
                if (i % 2 == 0) {
                    fillBackground(textGraphics, colorWhite);
                } else {
                    fillBackground(textGraphics, colorBlack);
                }

                Thread.sleep(blinkDelay);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void fillBackground(TextGraphics textGraphics, TextColor colour) throws IOException {
        for (int i = 0; i < terminal.getTerminalSize().getRows(); i++) {
            for (int j = 0; j < terminal.getTerminalSize().getColumns(); j++) {
                textGraphics.putString(j, i, " ");
                textGraphics.setBackgroundColor(colour);
            }
        }
        screen.refresh();
    }

    private void printEndGameMessage() {
        final TextGraphics textGraphics;
        try {
            screen.clear();
            screen.refresh();

            textGraphics = terminal.newTextGraphics();
            textGraphics.putString(moveCol, moveRow, "You have lost with the score of: " + score);
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


            KeyStroke keyStroke;
            int limit = 4;
            do {
                textGraphics.putString(moveCol, moveRow, "Enter your name (only four symbols): " + name);
                screen.refresh();
                keyStroke = terminal.readInput();

                if (keyStroke.getKeyType() == KeyType.Escape) {
                    return;
                } else if (keyStroke.getKeyType() == KeyType.Backspace && limit < 4) {
                    name = name.substring(0, name.length() - 1);

                    textGraphics.putString(moveCol, moveRow, "Enter your name (only four symbols): " + name + " ");
                    screen.refresh();
                    limit++;
                } else if (keyStroke.getKeyType() == KeyType.Character) {
                    name = name + keyStroke.getCharacter();
                    limit--;
                }

            } while (limit > 0);

            screen.clear();
            screen.refresh();

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