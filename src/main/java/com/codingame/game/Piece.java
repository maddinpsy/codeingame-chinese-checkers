package com.codingame.game;

public class Piece {
    public Hex pos;
    public int playerID;

    public Piece(Hex pos, int playerID) {
        this.pos = pos;
        this.playerID = playerID;
    }

    @Override
    public String toString() {
        return "Piece [pos=" + pos + ", playerID=" + playerID + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pos == null) ? 0 : pos.hashCode());
        result = prime * result + playerID;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Piece other = (Piece) obj;
        if (pos == null) {
            if (other.pos != null)
                return false;
        } else if (!pos.equals(other.pos))
            return false;
        if (playerID != other.playerID)
            return false;
        return true;
    }
}
