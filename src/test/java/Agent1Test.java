
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codingame.game.Board;
import com.codingame.game.Hex;
import com.codingame.game.Move;
import com.codingame.game.Piece;

public class Agent1Test {
    Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void testGetPossibleMoves_defaultSetup() {
        board.init(5, 3);
        List<Move> moves = Agent1.getPossibleMoves(board.getAllPieces());
        assertEquals(22, moves.size());
    }

    @Test
    void testGetPossibleMoves_sparseSetup() {
        board.init(5, 3, Arrays.asList(
                new Piece(new Hex(0, 0), 0),
                new Piece(new Hex(0, 1), 1),
                new Piece(new Hex(1, 0), 2),
                new Piece(new Hex(2, 0), 2)));
        List<Move> moves = Agent1.getPossibleMoves(board.getAllPieces());
        assertEquals(5, moves.size());
    }
}
