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

    /** returns the clock wise rotated hex by sixths of the circle */
    public Hex rotate(int sixth){
        int[] new_q = {q,r,s};
        int[] new_r = {r,s,q};
        int sign = sixth%2==0?1:-1;
        return new Hex(sign*new_q[sixth%3],sign*new_r[sixth%3]);
    }

    public static Hex fromPoint(int x, int y, int size) {
        double q = (Math.sqrt(3) / 3 * x - 1 / 3 * y) / size;
        double r = (2 / 3 * y) / size;
        return new Hex((int) Math.round(q), (int) Math.round(r));
    }
}