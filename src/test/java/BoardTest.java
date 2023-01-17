import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codingame.game.Board;
import com.codingame.game.Hex;
import com.codingame.game.Move;
import com.codingame.game.Piece;

public class BoardTest {
    Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
        board.init(5, 3);
    }

    @Test
    void testIsFree_player1() {
        board.init(5, 3);
        assertFalse(board.isFree(new Hex(5, -3)));
    }

    @Test
    void testIsFree_player2() {
        board.init(5, 3);
        assertFalse(board.isFree(new Hex(-4, 8)));
    }

    @Test
    void testIsFree_free() {
        board.init(5, 3);
        assertTrue(board.isFree(new Hex(0, 3)));
    }

    @Test
    void testGetRoute_jumpOverOthers() {
        List<Piece> pieces = Arrays.asList(
                new Piece(new Hex(3, -2), 1),
                new Piece(new Hex(2, -1), 1),
                new Piece(new Hex(0, 1), 1),
                new Piece(new Hex(-1, 1), 1),
                new Piece(new Hex(-2, 1), 2),
                new Piece(new Hex(2, 0), 2),
                new Piece(new Hex(0, -3), 2));
        board.init(5, 3, pieces);
        List<Hex> route = board.getRoute(new Hex(3, -2), new Hex(-3, 2));
        List<Hex> expected = Arrays.asList(
                new Hex(3, -2),
                new Hex(1, 0),
                new Hex(-1, 2),
                new Hex(-1, 0),
                new Hex(-3, 2));
        assertEquals(expected, route);
    }

    @Test
    void testGetRoute_jumpingBackandForth() {
        List<Piece> pieces = Arrays.asList(
                new Piece(new Hex(0, 0), 2),
                new Piece(new Hex(-1, 0), 1),
                new Piece(new Hex(-2, 1), 1),
                new Piece(new Hex(-1, 2), 1),
                new Piece(new Hex(1, 1), 2),
                new Piece(new Hex(2, -1), 2),
                new Piece(new Hex(1, -2), 2));
        board.init(5, 3, pieces);
        List<Hex> route = board.getRoute(new Hex(0, 0), new Hex(0, -2));
        List<Hex> expected = Arrays.asList(
                new Hex(0, 0),
                new Hex(-2, 0),
                new Hex(-2, 2),
                new Hex(0, 2),
                new Hex(2, 0),
                new Hex(2, -2),
                new Hex(0, -2));
        assertEquals(expected, route);
    }

    @Test
    void testGetRoute_noRoute() {
        board.init(5, 3);
        List<Hex> route = board.getRoute(new Hex(0, 0), new Hex(0, -2));
        assertEquals(0, route.size());
    }

    @Test
    void testIsOnMap_faraway() {
        board.init(5, 3);
        assertFalse(board.isOnMap(new Hex(8, 8)));
    }

    @Test
    void testIsOnMap_betweenStart() {
        board.init(5, 3);
        assertFalse(board.isOnMap(new Hex(5, 0)));
    }

    @Test
    void testIsOnMap_betweenStart2() {
        board.init(5, 3);
        assertFalse(board.isOnMap(new Hex(6, -5)));
    }

    @Test
    void testCheckMove_startOutside() {
        board.init(5, 3, Arrays.asList(new Piece(new Hex(5, 0), 0)));
        assertThrows(
                IllegalArgumentException.class,
                () -> board.checkMove(0, new Move(new Hex(5, 0), new Hex(4, 0))));
    }

    @Test
    void testCheckMove_endOutside() {
        board.init(5, 3, Arrays.asList(new Piece(new Hex(4, 0), 0)));
        assertThrows(
                IllegalArgumentException.class,
                () -> board.checkMove(0, new Move(new Hex(4, 0), new Hex(5, 0))));
    }

    @Test
    void testCheckMove_movingOtherPiece() {
        board.init(5, 3, Arrays.asList(new Piece(new Hex(3, 0), 0)));
        assertThrows(
                IllegalArgumentException.class,
                () -> board.checkMove(1, new Move(new Hex(3, 0), new Hex(3, 1))));
    }

    @Test
    void testCheckMove_moveEmptyField() {
        board.init(5, 3, Arrays.asList(new Piece(new Hex(3, 0), 0)));
        assertThrows(
                IllegalArgumentException.class,
                () -> board.checkMove(0, new Move(new Hex(2, 0), new Hex(2, 1))));
    }

    @Test
    void testCheckMove_endOccupied() {
        board.init(5, 3, Arrays.asList(
                new Piece(new Hex(2, 1), 0),
                new Piece(new Hex(2, 0), 0)));
        assertThrows(
                IllegalArgumentException.class,
                () -> board.checkMove(0, new Move(new Hex(2, 0), new Hex(2, 1))));

    }

    @Test
    void testMakeMove_neighbours() {
        board.init(5, 3, Arrays.asList(
                new Piece(new Hex(2, 0), 0)));
        board.makeMove(0, new Move(new Hex(2, 0), new Hex(2, 1)));
        assertEquals(new Piece(new Hex(2, 1), 0), board.getAllPieces().get(0));
        assertTrue(board.isFree(new Hex(2, 0)));
    }

    @Test
    void testMakeMove_Route() {
        board.init(5, 3, Arrays.asList(
                new Piece(new Hex(2, 0), 1),
                new Piece(new Hex(1, 1), 2),
                new Piece(new Hex(-1, 2), 0)));
        board.makeMove(1, new Move(new Hex(2, 0), new Hex(-2, 2)));
        assertTrue(board.getAllPieces().contains(new Piece(new Hex(-2, 2), 1)));
        assertTrue(board.isFree(new Hex(2, 0)));
    }

}
