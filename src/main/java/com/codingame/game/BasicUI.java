package com.codingame.game;

import java.util.HashMap;

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

    private HashMap<Piece, Circle> pieces = new HashMap<>();
    private Board board;

    public void initBackground() {
        // add triangles
        // add triangles for background of start position
        final int m = board.getSize() - 1;
        for (int id = 0; id < board.getNumPlayers(); id++) {
            Hex one = new Hex(2 * m, -m).rotate(id);
            Hex two = new Hex(m, -m).rotate(id);
            Hex three = new Hex(m, 0).rotate(id);
            graphicEntityModule.createPolygon()
                    .addPoint(one.getX(FIELD_DIST) + WIDTH / 2, one.getY(FIELD_DIST) + HEIGHT / 2)
                    .addPoint(two.getX(FIELD_DIST) + WIDTH / 2, two.getY(FIELD_DIST) + HEIGHT / 2)
                    .addPoint(three.getX(FIELD_DIST) + WIDTH / 2, three.getY(FIELD_DIST) + HEIGHT / 2)
                    // .setFillColor(player.getColorToken())
                    .setAlpha(0.5);

            // add second darker triangle for end positions
            one = new Hex(-2 * m, m).rotate(id);
            two = new Hex(-m, m).rotate(id);
            three = new Hex(-m, 0).rotate(id);
            graphicEntityModule.createPolygon()
                    .addPoint(one.getX(FIELD_DIST) + WIDTH / 2, one.getY(FIELD_DIST) + HEIGHT / 2)
                    .addPoint(two.getX(FIELD_DIST) + WIDTH / 2, two.getY(FIELD_DIST) + HEIGHT / 2)
                    .addPoint(three.getX(FIELD_DIST) + WIDTH / 2, three.getY(FIELD_DIST) + HEIGHT / 2)
                    // .setFillColor(player.getColorToken())
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
                    // .setFillColor(player.getColorToken())
                    .setX(piece.pos.getX(FIELD_DIST) + WIDTH / 2)
                    .setY(piece.pos.getY(FIELD_DIST) + HEIGHT / 2);
            pieces.put(piece, circle);
        });
    }

    @Override
    public void init(Board board) {
        this.board = board;

        initBackground();
        initPlayers();
    }

    @Override
    public void update(Move m) {
        // TODO Auto-generated method stub

    }
}
