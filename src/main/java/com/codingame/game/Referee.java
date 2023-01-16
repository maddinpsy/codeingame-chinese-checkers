package com.codingame.game;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.google.inject.Inject;

public class Referee extends AbstractReferee {
    // Uncomment the line below and comment the line under it to create a Solo Game
    // @Inject private SoloGameManager<Player> gameManager;
    @Inject
    private MultiplayerGameManager<Player> gameManager;
    private static final Logger logger = LogManager.getLogger(Referee.class);

    /** Side lenght of the starting triangles and the central hexagon */
    private static int GROUND_SIZE = 5;

    @Inject
    private Board board;
    @Inject
    private BasicUI ui;

    @Override
    public void init() {
        board.init(GROUND_SIZE, gameManager.getPlayerCount());
        ui.init(board);
        gameManager.endGame();
    }

    public Move parseMove(String in) {
        return new Move(new Hex(0, 0), new Hex(0, 0));
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
                Move m = parseMove(outputs.get(0));
                ui.update(m);
                // Check validity of the player output and compute the new game state
            } catch (TimeoutException e) {
                player.deactivate(String.format("$%d timeout!", player.getIndex()));
            }
        }
    }
}
