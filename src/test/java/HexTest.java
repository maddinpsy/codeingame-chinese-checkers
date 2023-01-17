import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.codingame.game.Hex;

public class HexTest {
    @Test
    void testConstructor_calcS() {
        Hex hex = new Hex(3, 2);
        assertEquals(-5, hex.s);
    }

    @Test
    void testConstructor_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Hex(1, 1, 1));
    }

    @Test
    void testRotateClockwise1() {
        Hex hex = new Hex(1, 0);
        Hex expected = new Hex(0, 1);
        assertEquals(expected, hex.rotate(1));
    }

    @Test
    void testRotateClockwise2() {
        Hex hex = new Hex(-1, -1);
        Hex expected = new Hex(2, -1);
        assertEquals(expected, hex.rotate(2));
    }

    @Test
    void testRotateCounterClockwise1() {
        Hex hex = new Hex(-1, 3);
        Hex expected = new Hex(2, 1);
        assertEquals(expected, hex.rotate(-1));
    }

    @Test
    void testRotateCounterClockwise2() {
        Hex hex = new Hex(-1, 3);
        Hex expected = new Hex(3, -2);
        assertEquals(expected, hex.rotate(-2));
    }

    @Test
    void testNeighbours() {
        Hex hex = new Hex(2, -1);
        Set<Hex> expected = new HashSet<>(Arrays.asList(
                new Hex(2, 0),
                new Hex(2, -2),
                new Hex(1, -1),
                new Hex(3, -1),
                new Hex(3, -2),
                new Hex(1, 0)));
        assertEquals(expected, hex.getNeighbours());
    }

    @Test
    void TestGetOpposite0() {
        Hex center = new Hex(0, 0);
        Hex neigbour = new Hex(-1, 1);
        Hex expected = new Hex(1, -1);
        assertEquals(expected, center.getOpposite(neigbour));
    }

    @Test
    void TestGetOpposite1() {
        Hex center = new Hex(1, -2);
        Hex neigbour = new Hex(1, -1);
        Hex expected = new Hex(1, -3);
        assertEquals(expected, center.getOpposite(neigbour));
    }

    @Test
    void TestGetOpposite2() {
        Hex center = new Hex(-2, 0);
        Hex neigbour = new Hex(-1, 0);
        Hex expected = new Hex(-3, 0);
        assertEquals(expected, center.getOpposite(neigbour));
    }
}
