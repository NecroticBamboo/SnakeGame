package com.NecroticBamboo;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

/**
 * This first tutorial, demonstrating setting up a simple {@link Terminal} and performing some basic operations on it.
 *
 * @author Martin
 */
public class Main {

    private static final String[] mainMenuQuestions = {
            "Play game",
            "Show scoreboard",
            "Something",
    };

    private static final String[] gameMenuQuestions = {
            "[1] Display board",
            "[2] Play version 1",
            "[3] Play version 2",
            "[4] Play version 3",
            "[5] BACK",
            "What would you like to do?"
    };

    private static final String[] setNewBoardQuestions = {
            "[1] Set rows",
            "[2] Set columns",
            "[3] BACK"
    };

    private static final DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();

    private static Terminal terminal = null;

    private static Screen screen = null;

    private static WindowBasedTextGUI textGUI = null;
    private static Panel contentPanel = new Panel(new GridLayout(2));

    private static int boardCol = 5;
    private static int boardRow = 5;

    public static void main(String[] args) throws InterruptedException {
        try {
            initTerminal();
            screen.startScreen();

            final Window window = new BasicWindow("Main Window");

            GridLayout gridLayout = (GridLayout) contentPanel.getLayoutManager();
            gridLayout.setHorizontalSpacing(3);
            menu(window);

            window.setComponent(contentPanel);
            textGUI.addWindowAndWait(window);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (terminal != null) {
                try {

                    terminal.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void initTerminal() {
        try {
            terminal = defaultTerminalFactory.createTerminal();
            screen = new TerminalScreen(terminal);
            textGUI = new MultiWindowTextGUI(screen);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void menu(Window window){
        SnakeGame game = new SnakeGame(screen, terminal, boardRow, boardCol);

        contentPanel.addComponent(new Button("" + 1, game::play));
        Label question = new Label("Play game");
        contentPanel.addComponent(question);

        contentPanel.addComponent(new Button("" + 2, () -> MessageDialog.showMessageDialog(textGUI, "MessageBox", ""+game.getScore(), MessageDialogButton.OK)));
        question = new Label("Show scoreboard");
        contentPanel.addComponent(question);

        contentPanel.addComponent(new Button("" + 3));
        question = new Label("Something");
        contentPanel.addComponent(question);

        contentPanel.addComponent(new Button("" + 4, window::close));
        question = new Label("EXIT");
        contentPanel.addComponent(question);

    }

    private static void setNewBoard() {
        try {
            final TextGraphics textGraphics = terminal.newTextGraphics();
            screen.clear();
            printQuestions(setNewBoardQuestions);
            screen.refresh();
            KeyStroke keyStroke = terminal.readInput();
            switch (keyStroke.getCharacter()) {
                case '1':
                    boardRow = setValue();
                    System.out.println(boardRow);
                    break;
                case '2':
                    boardCol = setValue();
                    System.out.println(boardCol);
                    break;
                case '3':
                    break;
                default:
                    textGraphics.putString(0, mainMenuQuestions.length + 2, "Something is wrong! setNewBoard");
                    screen.refresh();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int setValue() {
        int number = 0;
        try {
            StringBuilder numberSt = new StringBuilder();
            KeyStroke keyStroke;
            do {
                keyStroke = terminal.readInput();
                if (keyStroke.getCharacter() <= '9') {
                    numberSt.append(keyStroke.getCharacter());
                } else if (keyStroke.getKeyType() == KeyType.Enter) {
                    return number;
                }
            } while (keyStroke.getKeyType() != KeyType.Escape);

            number = Integer.parseInt(numberSt.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return number;
    }

    private static void printQuestions(String[] questions) {
        for (int i = 0; i < questions.length; i++) {
//            print(questions[i], i);
            contentPanel.addComponent(new Button("" + (i + 1)));
            Label question = new Label(questions[i]);
            contentPanel.addComponent(question);
        }
    }
}

