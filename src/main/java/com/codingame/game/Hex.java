package com.codingame.game;

public class Hex {
    int q, r, s;

    public Hex(int q, int r) {
        this.q = q;
        this.r = r;
        this.s = 0 - q - r;
    }

    public int getY(int size) {
        return size * 3 * q / 2;
    }

    public int getX(int size) {
        return (int) Math.round(size * (Math.sqrt(3) / 2 * q + Math.sqrt(3) * r));
    }

    public static Hex fromPoint(int x, int y, int size) {
        double q = (Math.sqrt(3) / 3 * x - 1 / 3 * y) / size;
        double r = (2 / 3 * y) / size;
        return new Hex((int) Math.round(q), (int) Math.round(r));
    }
}