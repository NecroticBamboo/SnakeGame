package com.NecroticBamboo;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class SnakeMovement implements ISnakeMovement{

    private MoveDirection previousDirection = MoveDirection.Right;
    private final Terminal terminal;

    public SnakeMovement(Terminal terminalIn){
        terminal=terminalIn;
    }

    @Override
    public MoveDirection getDirection() {
        try {
            KeyStroke keyStroke = terminal.pollInput();

            if(keyStroke==null){
                return previousDirection;
            }

            if (keyStroke.getKeyType() == KeyType.Escape) {
                return null;
            }

            Character c = keyStroke.getCharacter();
            if ( c == null )
                return previousDirection;

            MoveDirection d = ConvertToDirection(c);
            if (d == null ) {
                return previousDirection;
            }
            previousDirection=d;
            return d;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return previousDirection;
    }

    private MoveDirection ConvertToDirection(char c) {
        switch (c) {
            case 'w':
                return MoveDirection.Up;
            case 's':
                return MoveDirection.Down;
            case 'a':
                return MoveDirection.Left;
            case 'd':
                return MoveDirection.Right;
            default:
                break;
        }
        return null;
    }

}
