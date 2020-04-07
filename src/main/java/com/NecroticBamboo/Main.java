package com.NecroticBamboo;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This first tutorial, demonstrating setting up a simple {@link Terminal} and performing some basic operations on it.
 *
 * @author Martin
 */
public class Main {

    private static List<Score> test=new ArrayList<>();

    private static final DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();

    private static Terminal terminal = null;

    private static Screen screen = null;

    private static WindowBasedTextGUI textGUI = null;
    private static Panel contentPanel = new Panel(new GridLayout(2));

    public static void main(String[] args){
        try {
            initTerminal();
            screen.startScreen();

            final Window window = new BasicWindow("Main Window");

            GridLayout gridLayout = (GridLayout) contentPanel.getLayoutManager();
            gridLayout.setHorizontalSpacing(3);

            SnakeGame game = new SnakeGame(test,screen,terminal);
            ScoreBoard board=new ScoreBoard(test,screen);

            menu(window,game,board);

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

    public static void menu(Window window,SnakeGame game,ScoreBoard board){

        contentPanel.addComponent(new Button("1", game::play));
        Label question = new Label("Play game");
        contentPanel.addComponent(question);

        contentPanel.addComponent(new Button("2", board::showScore));
        question = new Label("Show scoreboard");
        contentPanel.addComponent(question);

        contentPanel.addComponent(new Button("3"));
        question = new Label("Something");
        contentPanel.addComponent(question);

        contentPanel.addComponent(new Button("4", window::close));
        question = new Label("EXIT");
        contentPanel.addComponent(question);

    }

}

