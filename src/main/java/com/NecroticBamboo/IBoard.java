package com.NecroticBamboo;

public interface IBoard {
    int getWidth();
    int getHeight();

    void clearBoard();
    void initialise(char symbol,int limit);
    void placePoint(Coordinates pointCoords,char element);

    char getPoint(Coordinates coords);
    Coordinates selectRandomElementInArray();
}
