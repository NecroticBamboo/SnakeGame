package com.NecroticBamboo;

public class Coordinates {

    private int m_row;
    private int m_column;

    public Coordinates(int row,int column){
        m_row=row;
        m_column=column;
    }

    public int getRow(){
        return m_row;
    }

    public int getColumn(){
        return m_column;
    }
}
