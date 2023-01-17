package com.codingame.game;

import java.util.HashMap;
import java.util.List;

import com.codingame.gameengine.module.entities.Circle;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.google.inject.Inject;

public class BasicUI extends AbstractUI {
    @Inject
    private GraphicEntityModule graphicEntityModule;
    @Inject
    private TooltipModule tooltips;

    private static int WIDTH = 1920;
    private static int HEIGHT = 1080;
    /** Color of a not occuped field */
    private static int FIELD_COLOR = 0x888888;
    /** Radius of a not occuped field */
    private static int FIELD_RADIUS = 10;
    private static int PIECE_RADIUS = 15;
    /** Distance of two fileds */
    private static int FIELD_DIST = 40;

    private HashMap<Hex, Circle> pieces = new HashMap<>();
    private Board board;

    public void initBackground() {
        // add triangles
        // add triangles for background of start position
        final int m = board.getSize() - 1;
        for (int id = 0; id < board.getNumPlayers(); id++) {
            Hex one = new Hex(2 * m, -m).rotate(id * 2);
            Hex two = new Hex(m, -m).rotate(id * 2);
            Hex three = new Hex(m, 0).rotate(id * 2);
            graphicEntityModule.createPolygon()
                    .addPoint(one.getX(FIELD_DIST) + WIDTH / 2, one.getY(FIELD_DIST) + HEIGHT / 2)
                    .addPoint(two.getX(FIELD_DIST) + WIDTH / 2, two.getY(FIELD_DIST) + HEIGHT / 2)
                    .addPoint(three.getX(FIELD_DIST) + WIDTH / 2, three.getY(FIELD_DIST) + HEIGHT / 2)
                    .setFillColor(PLAYER_COLORS[id])
                    .setAlpha(0.5);

            // add second darker triangle for end positions
            one = new Hex(-2 * m, m).rotate(id * 2);
            two = new Hex(-m, m).rotate(id * 2);
            three = new Hex(-m, 0).rotate(id * 2);
            graphicEntityModule.createPolygon()
                    .addPoint(one.getX(FIELD_DIST) + WIDTH / 2, one.getY(FIELD_DIST) + HEIGHT / 2)
                    .addPoint(two.getX(FIELD_DIST) + WIDTH / 2, two.getY(FIELD_DIST) + HEIGHT / 2)
                    .addPoint(three.getX(FIELD_DIST) + WIDTH / 2, three.getY(FIELD_DIST) + HEIGHT / 2)
                    .setFillColor(PLAYER_COLORS[id])
                    .setAlpha(0.5);
        }

        // add fileds
        board.getAllFields().forEach(hex -> {
            Circle circle = graphicEntityModule.createCircle()
                    .setRadius(FIELD_RADIUS)
                    .setFillColor(FIELD_COLOR)
                    .setX(hex.getX(FIELD_DIST) + WIDTH / 2)
                    .setY(hex.getY(FIELD_DIST) + HEIGHT / 2);
            tooltips.setTooltipText(circle, String.format("%d %d %d", hex.q, hex.r, hex.s));
        });
    }

    public void initPlayers() {
        // add pieses
        board.getAllPieces().forEach(piece -> {
            Circle circle = graphicEntityModule.createCircle()
                    .setRadius(PIECE_RADIUS)
                    .setFillColor(PLAYER_COLORS[piece.playerID])
                    .setLineColor(0x000000)
                    .setLineWidth(1)
                    .setX(piece.pos.getX(FIELD_DIST) + WIDTH / 2)
                    .setY(piece.pos.getY(FIELD_DIST) + HEIGHT / 2);
            pieces.put(piece.pos, circle);
        });
    }

    @Override
    public void init(Board board) {
        this.board = board;

        initBackground();
        initPlayers();
    }

    @Override
    public void update(List<Hex> hops) {
        // get entity at start position
        Circle movongEntity = pieces.remove(hops.get(0));
        if (movongEntity == null) {
            throw new RuntimeException("Board and UI are out of sync!");
        }
        boolean slideingMove = hops.size() == 2 && hops.get(0).getNeighbours().contains(hops.get(1));
        if (slideingMove) {
            movongEntity.setX(hops.get(1).getX(FIELD_DIST) + WIDTH / 2);
            movongEntity.setY(hops.get(1).getY(FIELD_DIST) + HEIGHT / 2);
        } else {
            // move from hop to hop
            int lastx = movongEntity.getX();
            int lasty = movongEntity.getY();
            double nominator = (double) (hops.size()-1) * 2.0;
            for (int idx = 1; idx < hops.size(); idx++) {
                Hex hop = hops.get(idx);
                // get position at end of hop
                int newx = hop.getX(FIELD_DIST) + WIDTH / 2;
                int newy = hop.getY(FIELD_DIST) + HEIGHT / 2;
                // make a step in the middle
                movongEntity.setZIndex(2);
                movongEntity.setX((newx + lastx) / 2);
                movongEntity.setY((newy + lasty) / 2);
                movongEntity.setScale(0.5);
                graphicEntityModule.commitEntityState((idx * 2 - 1) / nominator, movongEntity);
                // make a step at the endof hop
                movongEntity.setX(newx);
                movongEntity.setY(newy);
                movongEntity.setScale(1);
                graphicEntityModule.commitEntityState((idx * 2) / nominator, movongEntity);
                // store current x,y for next move
                lastx = newx;
                lasty = newy;
            }
        }
        // save entity at right pos for next move
        movongEntity.setZIndex(1);
        pieces.put(hops.get(hops.size() - 1), movongEntity);
    }
}
