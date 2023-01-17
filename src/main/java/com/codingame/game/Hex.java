package com.codingame.game;

import java.util.HashSet;
import java.util.Set;

public class Hex {
    public int q, r, s;

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
        while (sixth < 0)
            sixth += 6;
        int[] new_q = { q, r, s };
        int[] new_r = { r, s, q };
        int sign = sixth % 2 == 0 ? 1 : -1;
        return new Hex(sign * new_q[sixth % 3], sign * new_r[sixth % 3]);
    }

    public Set<Hex> getNeighbours() {
        Set<Hex> neighbors = new HashSet<>();
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

    public int distance(Hex b) {
        return (Math.abs(this.q - b.q) + Math.abs(this.r - b.r) + Math.abs(this.s - b.s)) / 2;
    }

    public static Hex fromPoint(int x, int y, int size) {
        double q = (Math.sqrt(3) / 3 * x - 1 / 3 * y) / size;
        double r = (2 / 3 * y) / size;
        return new Hex((int) Math.round(q), (int) Math.round(r));
    }

    @Override
    public String toString() {
        return "Hex [q=" + q + ", r=" + r + ", s=" + s + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + q;
        result = prime * result + r;
        result = prime * result + s;
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
        Hex other = (Hex) obj;
        if (q != other.q)
            return false;
        if (r != other.r)
            return false;
        if (s != other.s)
            return false;
        return true;
    }
}