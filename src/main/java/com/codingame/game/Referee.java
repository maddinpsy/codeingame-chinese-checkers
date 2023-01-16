package com.codingame.game;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        String[] parts = in.split(" ");
        // check six parts
        if (parts.length != 6) {
            throw new IllegalArgumentException("Move must be 6 numbers seperated by space");
        }
        // check all intergers
        int sq, sr, ss, eq, er, es;
        try {
            sq = Integer.parseInt(parts[0]);
            sr = Integer.parseInt(parts[1]);
            ss = Integer.parseInt(parts[2]);
            eq = Integer.parseInt(parts[3]);
            er = Integer.parseInt(parts[4]);
            es = Integer.parseInt(parts[5]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e);
        }
        Hex start = new Hex(sq, sr, ss);
        Hex end = new Hex(eq, er, es);
        return new Move(start, end);
    }

    @Override
    public void gameTurn(int turn) {
        // select current player based on turn
        Player player = gameManager.getActivePlayers().get(turn % gameManager.getPlayerCount());
        // send all pieces of all players to current player
        for (Piece p : board.getAllPieces()) {
            player.sendInputLine(String.format("%d %d %d %d", p.playerID, p.pos.q, p.pos.r, p.pos.s));
        }
        player.execute();

        try {
            List<String> outputs = player.getOutputs();
            // Check validity of the player output
            Move m = parseMove(outputs.get(0));
            board.checkMove(player.getIndex(), m);
            // and compute the new game state
            ui.update(m);

        } catch (TimeoutException e) {
            player.deactivate(String.format("$%d timeout!", player.getIndex()));
        } catch (IllegalArgumentException e) {
            player.deactivate(String.format("$%d invalid move!", player.getIndex()));
        }
    }
}
