package com.NecroticBamboo;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class SnakeScreen implements ISnakeScreen {

    private final Terminal terminal;
    private final Screen screen;

    private int moveRow;
    private int moveCol;

    private final int numberOfBlinks = 3;
    private final int blinkDelay = 500;

    private final TextColor colorWhite = TextColor.ANSI.WHITE;
    private final TextColor colorBlack = TextColor.ANSI.BLACK;

    public static final char snakeSymbol = '☼';
    public static final char mealSymbol = '0';
    public static final char obstacleSymbol = '#';
    public static final char boardSymbol = ' ';

    private final TextCharacter headCharacter = new TextCharacter(snakeSymbol);
    private final TextCharacter mealCharacter = new TextCharacter(mealSymbol);
    private final TextCharacter obstacleCharacter = new TextCharacter(obstacleSymbol);
    private final TextCharacter boardCharacter = new TextCharacter(boardSymbol);

    private int totalRows;
    private int totalColumns;

    public SnakeScreen(Terminal terminalIn, Screen screenIn) {
        terminal = terminalIn;
        screen = screenIn;
        try {
            TerminalSize terminalSize=terminal.getTerminalSize();
            totalRows = terminalSize.getRows() - 1;
            totalColumns = terminalSize.getColumns();

            moveRow = terminalSize.getRows() / 3;
            moveCol = terminalSize.getColumns() / 4;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getWidth() {
        return totalColumns;
    }

    @Override
    public int getHeight() {
        return totalRows;
    }

    @Override
    public void drawSnakeHead(int row, int col) {
        screen.setCharacter(col, row + 1, headCharacter);
        try {
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eraseSnakeTail(int row, int col) {
        screen.setCharacter(col, row + 1, boardCharacter);
        try {
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void drawMeal(int row, int col) {
        screen.setCharacter(col, row + 1, mealCharacter);
        try {
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void drawObstacle(int row, int col) {
        screen.setCharacter(col, row + 1, obstacleCharacter);
        try {
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void drawScore(int score, int level, int delay) {
        try {
            TextGraphics textGraphics = screen.newTextGraphics();
            for (int i = 0; i < terminal.getTerminalSize().getColumns(); i++) {
                textGraphics.setBackgroundColor(colorWhite);
                textGraphics.putString(i, 0, " ");
            }
            textGraphics.setForegroundColor(colorBlack);
            textGraphics.putString(0, 0, "Current score: " + score + " Current level: " + level + "  Current delay: " + delay + " ");
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clear() {
        try {
            fillBackground(colorBlack);
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void blink() {
        try {

            screen.clear();
            screen.refresh();

            for (int i = 0; i < numberOfBlinks; i++) {
                if (i % 2 == 0) {
                    fillBackground(colorWhite);
                } else {
                    fillBackground(colorBlack);
                }
                screen.refresh();
                Thread.sleep(blinkDelay);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void fillBackground(TextColor colour) throws IOException {
        final TextGraphics textGraphics=terminal.newTextGraphics();
        for (int i = 0; i < terminal.getTerminalSize().getRows(); i++) {
            for (int j = 0; j < terminal.getTerminalSize().getColumns(); j++) {
                textGraphics.setCharacter(j, i, boardSymbol);
                textGraphics.setBackgroundColor(colour);
            }
        }
    }

    @Override
    public void drawBoard(IBoard board) {
        try {
            screen.setCursorPosition(null);
            for (int col = 0; col < board.getWidth(); col++) {
                for (int row = 0; row < board.getHeight(); row++) {
                    screen.setCharacter(col, row + 1, new TextCharacter(board.getPoint(new Coordinates(row,col))));
                }
            }
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void drawEndGameMessage(int score){
        try {
            TextGraphics textGraphics;
            screen.clear();

            screen.refresh();

            textGraphics = terminal.newTextGraphics();
            textGraphics.putString(moveCol, moveRow, "You have lost with the score of: " + score);
            screen.refresh();

            Thread.sleep(1500);
            screen.clear();
            screen.refresh();
        }catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String query(String message){
        try {
            String name = "";

            TextGraphics textGraphics = terminal.newTextGraphics();
            KeyStroke keyStroke;
            int limit = 4;
            do {
                textGraphics.putString(moveCol, moveRow, message + " " + name);
                screen.refresh();
                keyStroke = terminal.readInput();

                if (keyStroke.getKeyType() == KeyType.Escape) {
                    return " ";
                } else if (keyStroke.getKeyType() == KeyType.Backspace && limit < 4) {
                    name = name.substring(0, name.length() - 1);

                    textGraphics.putString(moveCol, moveRow, message + " " + name + " ");
                    screen.refresh();
                    limit++;
                } else if (keyStroke.getKeyType() == KeyType.Character) {
                    name = name + keyStroke.getCharacter();
                    limit--;
                }

            } while (limit > 0);

            screen.clear();
            screen.refresh();

            textGraphics.putString(moveCol,moveRow,"Your name is: "+name.toUpperCase());
            screen.refresh();
            Thread.sleep(500);

            return name;
        }catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
