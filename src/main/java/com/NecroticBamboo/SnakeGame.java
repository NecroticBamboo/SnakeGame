package com.NecroticBamboo;

import java.util.*;


//TODO: make it colourful
//TODO: draw updates only
//TODO: write unit tests

public class SnakeGame {

    private final Queue<Coordinates> snakeBody = new LinkedList<>();

    private Coordinates snakeHeadPosition;

    private int score = 0;
    private int delay = 250;
    private int level = 1;

    private int objectLimit = 1;
    private int pointLimit = 0;


    private final Options settings;
    private static ISnakeScreen screen;
    private final ISnakeMovement movement;

    private final IBoard board;



    public SnakeGame(Options settingsIn, ISnakeScreen screenIn, ISnakeMovement movementIn, IBoard boardIn) {
        settings = settingsIn;
        screen = screenIn;
        movement = movementIn;
        board = boardIn;
    }

    public int play() {

        setStart();

        try {
            screen.drawScore(score, level, delay);

            do {

                switch (movement.getDirection()) {
                    case Up:
                        if (move(0, -1)) {
                            return score;
                        } else {
                            screen.drawBoard(board);
                        }
                        break;
                    case Down:
                        if (move(0, 1)) {
                            return score;
                        } else {
                            screen.drawBoard(board);
                        }
                        break;
                    case Left:
                        if (move(-1, 0)) {
                            return score;
                        } else {
                            screen.drawBoard(board);
                        }
                        break;
                    case Right:
                        if (move(1, 0)) {
                            return score;
                        } else {
                            screen.drawBoard(board);
                        }
                        break;
                    default:
                        break;
                }
                screen.drawScore(score, level, delay);

                Thread.sleep(delay);

            } while (movement.getDirection() != null);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return score;
    }

    public void setStart() {
        score = 0;
        delay = 250;
        pointLimit = 1;

        board.clearBoard();
        snakeBody.clear();

        snakeHeadPosition = board.selectRandomElementInArray();
        board.placePoint(snakeHeadPosition, SnakeScreen.snakeSymbol);

        board.initialise(SnakeScreen.mealSymbol, pointLimit);

        screen.drawBoard(board);
    }

    private boolean move(int columnChange, int rowChange) {
        int newRow = snakeHeadPosition.getRow() + rowChange;
        int newColumn = snakeHeadPosition.getColumn() + columnChange;
        Coordinates test = new Coordinates(newRow, newColumn);

        if (newRow >= board.getHeight() && settings.getBorderOption()) {
            newRow = 0;
        }
        if (newRow < 0 && settings.getBorderOption()) {
            newRow = board.getHeight() - 1;
        }
        if (newColumn >= board.getWidth() && settings.getBorderOption()) {
            newColumn = 0;
        }
        if (newColumn < 0 && settings.getBorderOption()) {
            newColumn = board.getWidth() - 1;
        }

        if (newRow >= board.getHeight() ||
                newRow < 0 ||
                (newColumn >= board.getWidth() || (newColumn < 0 && !settings.getBorderOption())) ||
                board.getPoint(test) == SnakeScreen.snakeSymbol ||
                board.getPoint(test) == SnakeScreen.obstacleSymbol) {

            screen.blink();
            printEndGameMessage();
            return true;
        } else if (board.getPoint(test) == SnakeScreen.mealSymbol) {

            snakeBody.add(snakeHeadPosition);
            snakeHeadPosition = new Coordinates(newRow, newColumn);

            board.placePoint(snakeHeadPosition, SnakeScreen.snakeSymbol);

            board.initialise(SnakeScreen.mealSymbol, pointLimit);
            if (!settings.getObjectsOption()) {
                board.initialise(SnakeScreen.obstacleSymbol, objectLimit);
            }

            proceedToNextLevel();

        } else {
            snakeBody.add(snakeHeadPosition);
            snakeHeadPosition = new Coordinates(newRow, newColumn);
            Coordinates tail = snakeBody.poll();
            board.placePoint(snakeHeadPosition, SnakeScreen.snakeSymbol);
            board.placePoint(tail, ' ');
        }
        return false;
    }

    private void proceedToNextLevel() {
        int percentage = (int) (delay * (10.0f / 100.0f));
        delay = delay - percentage;
        if (delay <= 50) {
            delay = 250;
            level++;
            objectLimit = objectLimit * 2;
            pointLimit++;

            if (level >= 5) {
                pointLimit = 5;
            }

            board.initialise(SnakeScreen.mealSymbol, pointLimit);
            if (!settings.getObjectsOption()) {
                board.initialise(SnakeScreen.obstacleSymbol, objectLimit);
            }
        }
        incrementScore(settings.getDoublePointsOption());
    }

    private void incrementScore(boolean doublePointsOption) {
        if (!doublePointsOption) {
            score++;
        } else score = score + 2;
    }

    private void printEndGameMessage() {
        try {
            screen.drawEndGameMessage(score);

            Thread.sleep(1500);
            screen.clear();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}