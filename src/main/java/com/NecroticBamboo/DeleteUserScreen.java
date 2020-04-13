package com.NecroticBamboo;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;
import java.util.List;

public class DeleteUserScreen {

    private static Screen screen = null;

    private static List<User> leaderBoard;

    public DeleteUserScreen(List<User> userIn, Screen screenIn) {
        screen = screenIn;
        leaderBoard = userIn;
    }

    public void deleteUser() {
        try {
            WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
            screen.startScreen();

            final Window window = new BasicWindow("Delete User Window");
            Panel contentPanel = new Panel(new GridLayout(2));

            GridLayout gridLayout = (GridLayout) contentPanel.getLayoutManager();
            gridLayout.setHorizontalSpacing(3);

            if (leaderBoard.isEmpty()) {
                Label statement = new Label("Nothing to display");
                contentPanel.addComponent(statement);
            } else displayNamesOfUsers(contentPanel);

            contentPanel.addComponent(new Button("BACK", window::close));

            window.setComponent(contentPanel);
            textGUI.addWindowAndWait(window);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void displayNamesOfUsers(Panel contentPanel) {
        for (int user = 0; user < leaderBoard.size(); user++) {
            int finalUser = user;
            contentPanel.addComponent(new Button("" + (user + 1), () -> deleteSelectedUser(finalUser)));
            Label name = new Label("" + leaderBoard.get(user).getName());
            contentPanel.addComponent(name);
        }
    }

    private static void deleteSelectedUser(int index) {
        leaderBoard.remove(index);
    }
}
