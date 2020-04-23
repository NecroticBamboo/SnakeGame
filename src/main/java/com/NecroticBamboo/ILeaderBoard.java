package com.NecroticBamboo;

import java.util.List;

public interface ILeaderBoard {
    List<User> getLeaderBoard();
    void addUser(String name,int score);
}
