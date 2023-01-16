package com.codingame.game;

public class Piece {
    Hex pos;
    int playerID;

    public Piece(Hex pos, int playerID) {
        this.pos = pos;
        this.playerID = playerID;
    }
}
