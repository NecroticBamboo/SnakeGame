package com.NecroticBamboo;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ScoreBoard {

    private static Screen screen = null;

    private static List<User> leaderBoard;

    public ScoreBoard(List<User> userIn, Screen screenIn) {
        screen = screenIn;
        leaderBoard = userIn;
    }

    public void showScore() {
        try {
            WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
            screen.startScreen();

            final Window window = new BasicWindow("Score Board Window");
            Panel contentPanel = new Panel(new GridLayout(3));

            GridLayout gridLayout = (GridLayout) contentPanel.getLayoutManager();
            gridLayout.setHorizontalSpacing(3);

            sortAndShowScoreBoard(contentPanel);

            DeleteUserScreen delete=new DeleteUserScreen(leaderBoard,screen);
            contentPanel.addComponent(new Button("BACK",window::close));
            contentPanel.addComponent(new Button("DELETE", delete::deleteUser));
            contentPanel.addComponent(new Button("RESET", ScoreBoard::resetLeaderBoard));

            window.setComponent(contentPanel);
            textGUI.addWindowAndWait(window);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sortAndShowScoreBoard(Panel contentPanel) {
        Collections.sort(leaderBoard,(User a, User b)->a.getScore()-b.getScore());
        for (User user : leaderBoard) {
            Label question = new Label("User: "+ user.getName() + " has the following score: " + user.getScore());
            question.setLayoutData(GridLayout.createLayoutData(GridLayout.Alignment.BEGINNING,GridLayout.Alignment.BEGINNING,true,false,3,1));
            contentPanel.addComponent(question);
        }
    }

    private static void resetLeaderBoard(){
        leaderBoard.clear();
    }

}
