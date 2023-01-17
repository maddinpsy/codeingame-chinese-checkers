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
        // 200 moves, each 150ms => max game length: 30 sec
        gameManager.setTurnMaxTime(150);
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
        // start with player 0, than round robin based on turn
        Player player = gameManager.getActivePlayers().get((turn - 1) % gameManager.getActivePlayers().size());
        // send all pieces of all players to current player
        List<Piece> allPieces = board.getAllPieces();
        player.sendInputLine(String.format("%d", allPieces.size()));
        for (Piece p : allPieces) {
            // correct playerId, so that the AI always has id 0
            int correctedId = (p.playerID - player.getIndex() + gameManager.getPlayerCount())
            % gameManager.getPlayerCount();
            // correct piece position, so that the AI always plays from bottom to top.
            Hex correctedPos = p.pos.rotate(-player.getIndex() * 2);
            player.sendInputLine(
                    String.format("%d %d %d %d", correctedId, correctedPos.q, correctedPos.r, correctedPos.s));
        }
        player.execute();

        try {
            List<String> outputs = player.getOutputs();
            // Check validity of the player output
            Move m = parseMove(outputs.get(0));
            // correct AI move back to global board coordinates 
            Move correctedMove = new Move(m.start.rotate(player.getIndex() * 2), m.end.rotate(player.getIndex() * 2));
            List<Hex> hops = board.makeMove(player.getIndex(), correctedMove);
            // and compute the new game state
            ui.update(hops);

        } catch (TimeoutException e) {
            String msg = String.format("$%d timeout!", player.getIndex());
            logger.info(msg);
            player.deactivate(msg);
        } catch (IllegalArgumentException e) {
            String msg = String.format("$%d invalid move: %s", player.getIndex(), e.getMessage());
            logger.info(msg);
            player.deactivate(msg);
        }

        if (gameManager.getActivePlayers().size() <= 1) {
            gameManager.endGame();
        }
    }
}
