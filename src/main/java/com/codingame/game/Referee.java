package com.codingame.game;

import java.util.HashMap;
import java.util.List;

import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.Circle;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.google.inject.Inject;

public class Referee extends AbstractReferee {
    // Uncomment the line below and comment the line under it to create a Solo Game
    // @Inject private SoloGameManager<Player> gameManager;
    @Inject
    private MultiplayerGameManager<Player> gameManager;
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
    /** Distance of two fileds */
    private static int FIELD_DIST = 40;
    /** Side lenght of the starting triangles and the central hexagon */
    private static int GROUND_SIZE = 5;
    private static int NUM_PICES = GROUND_SIZE * (GROUND_SIZE - 1) / 2;

    private HashMap<Hex, Circle> circles = new HashMap<>();

    private int changeColor(int c, double amout) {
        int r = c & 0xFF0000;
        int g = c & 0x00FF00;
        int b = c & 0x0000FF;

        return (int) Math.round(r * amout + g * amout + b * amout);
    }

    @Override
    public void init() {
        for (Player player : gameManager.getActivePlayers()) {
            // add triangles for background
            Hex one = new Hex(GROUND_SIZE - 1, GROUND_SIZE - 1);
            Hex two = new Hex(0, GROUND_SIZE - 1);
            Hex three = new Hex(GROUND_SIZE - 1, 0);
            graphicEntityModule.createPolygon()
                    .addPoint(one.getX(FIELD_DIST) + WIDTH / 2, one.getY(FIELD_DIST) + HEIGHT / 2)
                    .addPoint(two.getX(FIELD_DIST) + WIDTH / 2, two.getY(FIELD_DIST) + HEIGHT / 2)
                    .addPoint(three.getX(FIELD_DIST) + WIDTH / 2, three.getY(FIELD_DIST) + HEIGHT / 2)
                    .setFillColor(changeColor(player.getColorToken(), 0.5))
                    .setAlpha(0.5);

            break;
        }
        int max_coord = GROUND_SIZE * 2;
        for (int q = -max_coord; q <= max_coord; q++) {
            for (int r = -max_coord; r <= max_coord; r++) {
                Hex hex = new Hex(q, r);
                boolean qo = Math.abs(hex.q) >= GROUND_SIZE;
                boolean ro = Math.abs(hex.r) >= GROUND_SIZE;
                boolean so = Math.abs(hex.s) >= GROUND_SIZE;
                if ((qo && ro) || (qo && so) || (ro && so)) {
                    // if at least two areover the limit contine
                    continue;
                }
                Circle circle = graphicEntityModule.createCircle()
                        .setRadius(FIELD_RADIUS)
                        .setFillColor(FIELD_COLOR)
                        .setX(hex.getX(FIELD_DIST) + WIDTH / 2)
                        .setY(hex.getY(FIELD_DIST) + HEIGHT / 2);
                tooltips.setTooltipText(circle, String.format("%d %d %d", hex.q, hex.r, hex.s));

                circles.put(hex, circle);
            }
        }

        for (Player player : gameManager.getActivePlayers()) {
            // add pieses
        }

        gameManager.endGame();
    }

    @Override
    public void gameTurn(int turn) {
        for (Player player : gameManager.getActivePlayers()) {
            player.sendInputLine("input");
            player.execute();
        }

        for (Player player : gameManager.getActivePlayers()) {
            try {
                List<String> outputs = player.getOutputs();
                // Check validity of the player output and compute the new game state
            } catch (TimeoutException e) {
                player.deactivate(String.format("$%d timeout!", player.getIndex()));
            }
        }
    }
}
