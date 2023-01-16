package com.codingame.game;

import java.util.ArrayList;
import java.util.LinkedList;
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

    public boolean isFree(Hex hex) {
        for (Piece p : pieces) {
            if (p.pos.equals(hex)) {
                return false;
            }
        }
        return true;
    }

    public List<Hex> getRoute(Hex start, Hex end) {
        // class to store the parrent
        class Node<T> {
            Node<T> parent;
            T data;

            Node(T data, Node<T> parent) {
                this.parent = parent;
                this.data = data;
            }
        }
        // list with all posible next nodes
        LinkedList<Node<Hex>> openList = new LinkedList<>();
        openList.add(new Node<>(start, null));

        while (!openList.isEmpty()) {
            final Node<Hex> current = openList.pop();
            if (current.data.equals(end)) {
                // reconstruct path, when end is found
                LinkedList<Hex> path = new LinkedList<>();
                Node<Hex> work = current;
                while (work.parent != null) {
                    path.addFirst(current.data);
                    work = work.parent;
                }
                return path;
            }

            // add all free jump positions to the open set
            current.data.getNeighbours()
                    .parallelStream()
                    .filter(n -> !isFree(n))
                    .map(n -> n.getOpposite(current.data))
                    .filter(this::isFree)
                    .forEach(j -> openList.add(new Node<>(j, current)));
        }
        return new LinkedList<>();
    }

    public boolean isOnMap(Hex hex) {
        int aq = Math.abs(hex.q);
        int ar = Math.abs(hex.r);
        int as = Math.abs(hex.s);
        boolean qo = aq >= size;
        boolean ro = ar >= size;
        boolean so = as >= size;
        if ((qo && ro) || (qo && so) || (ro && so)) {
            // if at least two are over the limit contine
            return false;
        }
        if (aq > size * 2 || ar > size * 2 || as > size * 2) {
            // if anyone is far out
            return false;
        }
        return true;
    }

    public void checkMove(int playerId, Move move) throws IllegalArgumentException {
        // check are on map
        if (!isOnMap(move.start)) {
            throw new IllegalArgumentException("Start is not on map");
        }
        if (!isOnMap(move.end)) {
            throw new IllegalArgumentException("End is not on map");
        }
        // check moving own piece
        if (!pieces.parallelStream().anyMatch(p -> p.pos.equals(move.start) && p.playerID == playerId)) {
            throw new IllegalArgumentException("You can only move your own pieces");
        }
        // check end is empty
        if (!isFree(move.end)) {
            throw new IllegalArgumentException("End must be empty");
        }
    }

    public List<Hex> makeMove(int playerId, Move move) throws IllegalArgumentException {
        List<Hex> route;
        checkMove(playerId, move);
        // check is direct move
        if (move.start.getNeighbours().contains(move.end)) {
            route = new ArrayList<>(2);
            route.add(move.start);
            route.add(move.end);
        } else {
            // check jump route exists
            route = getRoute(move.start, move.end);
            if (route.size() == 0) {
                throw new IllegalArgumentException("Thre is no jump route from start to end.");
            }
        }
        // update piece list
        Piece movingPiece = pieces.parallelStream()
                .filter(p -> p.pos.equals(move.start))
                .findAny()
                .orElseThrow(() -> new RuntimeException("move check was incomplete"));
        movingPiece.pos = move.end;
        return route;
    }

}
