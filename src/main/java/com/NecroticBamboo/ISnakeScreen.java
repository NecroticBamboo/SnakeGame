package com.NecroticBamboo;

public interface ISnakeScreen {

    int getWidth();
    int getHeight();

    void drawSnakeHead(int row,int col);
    void eraseSnakeTail(int row,int col);
    void drawMeal(int row,int col);
    void drawObstacle(int row,int col);
    void drawScore(int score,int level,int delay);
    void clear();
    void blink();
    void drawBoard(IBoard board);
    void drawEndGameMessage(int score);
    String query(String message);
}
