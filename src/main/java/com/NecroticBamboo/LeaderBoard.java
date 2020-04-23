package com.NecroticBamboo;


import java.util.ArrayList;
import java.util.List;

public class LeaderBoard implements ILeaderBoard {

    private List<User> leaderBoard=new ArrayList<>();
    private static int leaderBoardLength = 0;

    @Override
    public List<User> getLeaderBoard() {
        return leaderBoard;
    }

    @Override
    public void addUser(String name,int score) {
        User user;
        if (name.equals(" ")) {
            return;
        } else {
            user = new User(name.toUpperCase(), score);
        }

        if (leaderBoardLength < 5) {
            checkForDupes(user);
        } else replaceUser(user);
    }

    private void checkForDupes(User user) {
        if (leaderBoard.isEmpty()) {
            leaderBoard.add(user);
            leaderBoardLength++;
        } else {
            checkForSameName(user);
        }
    }

    private void checkForSameName(User user) {
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

    private void replaceUser(User user) {
        for (int i = 0; i < leaderBoard.size(); i++) {
            if (leaderBoard.get(i).getScore() < user.getScore()) {
                leaderBoard.remove(i);
                leaderBoard.add(i, user);
                return;
            }
        }
    }
}
