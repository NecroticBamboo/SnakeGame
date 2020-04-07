package com.NecroticBamboo;

public class Score {
    private String name;
    private int score;

    public Score(String nameIn,int scoreIn){
        name=nameIn;
        score=scoreIn;
    }

    public String getName(){return name;}

    public int getScore(){return score;}
}
