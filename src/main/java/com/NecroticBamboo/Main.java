package com.NecroticBamboo;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This first tutorial, demonstrating setting up a simple {@link Terminal} and performing some basic operations on it.
 *
 * @author Martin
 */
public class Main {

    private final static Options options = new Options(false, false,false);

    private static final DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();

    private static Terminal terminal = null;

    private static Screen screen = null;

    private static WindowBasedTextGUI textGUI = null;
    private final static Panel contentPanel = new Panel(new GridLayout(2));

    public static void main(String[] args) {
        try {
            initTerminal();
            screen.startScreen();

            showLogo();

            final Window window = new BasicWindow("Main Window");

            GridLayout gridLayout = (GridLayout) contentPanel.getLayoutManager();
            gridLayout.setHorizontalSpacing(3);

            OptionsMenu settings = new OptionsMenu(screen, options);

            ILeaderBoard leaderBoard = new LeaderBoard();
            ScoreBoard scoreBoard = new ScoreBoard(leaderBoard, screen);

            SnakeScreen snakeScreen=new SnakeScreen(terminal,screen);
            SnakeMovement movement=new SnakeMovement(terminal);
            Board board=new Board(snakeScreen);

            SnakeGame game = new SnakeGame(options, snakeScreen, movement,board);

            menu(window, snakeScreen, game, scoreBoard, settings,leaderBoard);

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

    private static void showLogo() {
        try {
            Panel logoPanel = new Panel(new GridLayout(1));

            Window logoWindow = new BasicWindow("");
            Label logo = new Label(
                    "   ▄████████ ███▄▄▄▄      ▄████████    ▄█   ▄█▄    ▄████████     \n" +
                            "  ███    ███ ███▀▀▀██▄   ███    ███   ███ ▄███▀   ███    ███          \n" +
                            "  ███    █▀  ███   ███   ███    ███   ███▐██▀     ███    █▀           \n" +
                            "  ███        ███   ███   ███    ███  ▄█████▀     ▄███▄▄▄              \n" +
                            "▀███████████ ███   ███ ▀███████████ ▀▀█████▄    ▀▀███▀▀▀              \n" +
                            "         ███ ███   ███   ███    ███   ███▐██▄     ███    █▄           \n" +
                            "   ▄█    ███ ███   ███   ███    ███   ███ ▀███▄   ███    ███          \n" +
                            " ▄████████▀   ▀█   █▀    ███    █▀    ███   ▀█▀   ██████████          \n" +
                            "                                      ▀                               \n" +
                            "                  ▄██████▄     ▄████████   ▄▄▄▄███▄▄▄▄      ▄████████ \n" +
                            "                 ███    ███   ███    ███ ▄██▀▀▀███▀▀▀██▄   ███    ███ \n" +
                            "                 ███    █▀    ███    ███ ███   ███   ███   ███    █▀  \n" +
                            "                ▄███          ███    ███ ███   ███   ███  ▄███▄▄▄     \n" +
                            "               ▀▀███ ████▄  ▀███████████ ███   ███   ███ ▀▀███▀▀▀     \n" +
                            "                 ███    ███   ███    ███ ███   ███   ███   ███    █▄  \n" +
                            "                 ███    ███   ███    ███ ███   ███   ███   ███    ███ \n" +
                            "                 ████████▀    ███    █▀   ▀█   ███   █▀    ██████████ \n" +
                            "                                                                      \n" +
                            "\n");
            logoPanel.addComponent(logo);

            logoWindow.setComponent(logoPanel);
            textGUI.addWindow(logoWindow);
            textGUI.updateScreen();

            Thread.sleep(1500);
            logoWindow.close();

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

    }

    public static void menu(Window window, ISnakeScreen screen, SnakeGame game, ScoreBoard board, OptionsMenu settings, ILeaderBoard leaderBoard) {

        contentPanel.addComponent(new Button("1", ()->play(game,screen,leaderBoard)));
        Label question = new Label("Play game");
        contentPanel.addComponent(question);

        contentPanel.addComponent(new Button("2", board::showScore));
        question = new Label("Show scoreboard");
        contentPanel.addComponent(question);

        contentPanel.addComponent(new Button("3", settings::selectOptions));
        question = new Label("Options");
        contentPanel.addComponent(question);

        contentPanel.addComponent(new Button("4", window::close));
        question = new Label("EXIT");
        contentPanel.addComponent(question);

    }

    private static void play(SnakeGame game,ISnakeScreen screen, ILeaderBoard leaderBoard){
        int score=game.play();
        String name=screen.query("Enter your name (4 character max): ");
        leaderBoard.addUser(name,score);
    }
}

