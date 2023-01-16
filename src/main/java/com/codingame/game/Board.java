package com.codingame.game;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private int size;
    private int numPlayers;

    public void init(int size, int numPlayers) {
        this.size = size;
        this.numPlayers = numPlayers;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public int getSize() {
        return size;
    }

    public List<Hex> getAllFields() {
        ArrayList<Hex> allFields = new ArrayList<>();
        int max_coord = getSize() * 2;
        for (int q = -max_coord; q <= max_coord; q++) {
            for (int r = -max_coord; r <= max_coord; r++) {
                Hex hex = new Hex(q, r);
                boolean qo = Math.abs(hex.q) >= getSize();
                boolean ro = Math.abs(hex.r) >= getSize();
                boolean so = Math.abs(hex.s) >= getSize();
                if ((qo && ro) || (qo && so) || (ro && so)) {
                    // if at least two areover the limit contine
                    continue;
                }
                allFields.add(hex);
            }
        }

        return allFields;
    }

}
