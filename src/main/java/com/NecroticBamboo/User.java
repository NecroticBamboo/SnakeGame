package com.NecroticBamboo;

public class User {
    private String name;
    private int score;

    public User(String nameIn, int scoreIn){
        name=nameIn;
        score=scoreIn;
    }

    public String getName(){return name;}

    public void setName(String newName){name=newName;}

    public int getScore(){return score;}

    public void setScore(int newScore){score=newScore;}
}
