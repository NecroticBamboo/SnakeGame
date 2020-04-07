package com.NecroticBamboo;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;

import javax.naming.ldap.Control;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ScoreBoard {

    private static Terminal terminal = null;

    private static Screen screen = null;

    private static List<Score> leaderBoard;

    public ScoreBoard(List<Score> scoreIn, Screen screenIn) {
        screen = screenIn;
        leaderBoard = scoreIn;
    }

    public void showScore() {
        try {
            WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
            screen.startScreen();

            final Window window = new BasicWindow("Score Board Window");
            Panel contentPanel = new Panel(new GridLayout(1));

            GridLayout gridLayout = (GridLayout) contentPanel.getLayoutManager();
            gridLayout.setHorizontalSpacing(3);

            sortAndShowScoreBoard(contentPanel);

            contentPanel.addComponent(new Button("BACK",window::close));

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

    private static void sortAndShowScoreBoard(Panel contentPanel) {
        Collections.sort(leaderBoard,(Score a, Score b)->a.getScore()-b.getScore());
        for (Score score : leaderBoard) {
            Label question = new Label("User: "+score.getName() + " has the following score: " + score.getScore());
            contentPanel.addComponent(question);
        }
    }

}
