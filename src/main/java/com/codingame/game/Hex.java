package com.codingame.game;

import java.util.ArrayList;
import java.util.List;

public class Hex {
    int q, r, s;

    public Hex(int q, int r) {
        this.q = q;
        this.r = r;
        this.s = 0 - q - r;
    }

    public Hex(int q, int r, int s) {
        this(q, r);
        if (q + r + s != 0) {
            throw new IllegalArgumentException("Coordinates must add up to zero");
        }
    }

    public int getY(int size) {
        return size * 3 * q / 2;
    }

    public int getX(int size) {
        return (int) Math.round(size * (Math.sqrt(3) / 2 * q + Math.sqrt(3) * r));
    }

    /** returns the clock wise rotated hex by sixths of the circle */
    public Hex rotate(int sixth) {
        int[] new_q = { q, r, s };
        int[] new_r = { r, s, q };
        int sign = sixth % 2 == 0 ? 1 : -1;
        return new Hex(sign * new_q[sixth % 3], sign * new_r[sixth % 3]);
    }

    public List<Hex> getNeighbours() {
        ArrayList<Hex> neighbors = new ArrayList<>(6);
        neighbors.add(new Hex(q - 1, r));
        neighbors.add(new Hex(q + 1, r));
        neighbors.add(new Hex(q, r - 1));
        neighbors.add(new Hex(q, r + 1));
        neighbors.add(new Hex(q + 1, r - 1));
        neighbors.add(new Hex(q - 1, r + 1));
        return neighbors;
    }

    /** returns the neighbor that is opposite of the given neighbor */
    public Hex getOpposite(Hex neighbour) {
        int dq = neighbour.q - q;
        int dr = neighbour.r - r;
        return new Hex(q - dq, r - dr);
    }

    public static Hex fromPoint(int x, int y, int size) {
        double q = (Math.sqrt(3) / 3 * x - 1 / 3 * y) / size;
        double r = (2 / 3 * y) / size;
        return new Hex((int) Math.round(q), (int) Math.round(r));
    }
}