package com.codingame.game;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private int size;
    private int numPlayers;
    private ArrayList<Piece> pieces;

    public void init(int size, int numPlayers) {
        this.size = size;
        this.numPlayers = numPlayers;
        initPices();
    }

    private void initPices() {
        pieces = new ArrayList<>((size * (size - 1)) / 2 * numPlayers);
        final int m = getSize() - 1;
        for (int id = 0; id < getNumPlayers(); id++) {
            for (int r = m; r <= 2 * m; r++) {
                for (int q = -m; q <= m - r; q++) {
                    // skip overlapping corner
                    if (r == m && q == 0) {
                        continue;
                    }
                    Hex hex = new Hex(r, q).rotate(id);
                    pieces.add(new Piece(hex, id));
                }
            }
        }
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

    public List<Piece> getAllPieces() {
        return pieces;
    }

}
