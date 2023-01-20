import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.codingame.game.Board;
import com.codingame.game.Hex;
import com.codingame.game.Move;
import com.codingame.game.Piece;
import com.codingame.game.Referee;

public class Boss {
    private static final Logger logger = LogManager.getLogger(Boss.class);
    static Set<Hex> winPos;

    public static List<Move> getPossibleMoves(List<Piece> pieces) {
        int myPlayerId = 0;
        Board board = new Board();
        board.init(Referee.GROUND_SIZE, 3, pieces);
        final Function<Piece, List<Move>> getDirectMove = p -> p.pos
                .getNeighbours()
                .stream()
                .filter(board::isFree)
                .filter(board::isOnMap)
                .map(n -> new Move(p.pos, n))
                .collect(Collectors.toList());

        final Function<Hex, List<Move>> getJumpMoves = start -> {
            // list with all posible next hexes
            LinkedList<Hex> openList = new LinkedList<>();
            List<Hex> closeList = new ArrayList<>();
            openList.add(start);

            while (!openList.isEmpty()) {
                final Hex current = openList.pop();
                if (!current.equals(start)) {
                    closeList.add(current);
                }
                // add all free jump positions to the open set
                current.getNeighbours()
                        .stream()
                        .filter(x -> !board.isFree(x))
                        .map(n -> n.getOpposite(current))
                        .filter(board::isFree)
                        .filter(board::isOnMap)
                        .filter(o -> !closeList.contains(o))
                        .forEach(j -> openList.add(j));
            }
            return closeList.stream().map(e -> new Move(start, e)).collect(Collectors.toList());
        };

        List<Move> directMoves = pieces.stream()
                .filter(p -> p.playerID == myPlayerId)
                .map(getDirectMove)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        logger.debug(String.format("Got %d direct moves", directMoves.size()));

        List<Move> jumpMoves = pieces.stream()
                .filter(p -> p.playerID == myPlayerId)
                .map(p -> p.pos)
                .map(getJumpMoves)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        logger.debug(String.format("Got %d jump moves", jumpMoves.size()));

        List<Move> allMoves = new ArrayList<>(directMoves);
        allMoves.addAll(jumpMoves);
        logger.debug(String.format("Got %d moves in total", allMoves.size()));
        return allMoves;
    }

    public static void printMove(Move m) {
        System.out.printf("%d %d %d %d %d %d\n",
                m.start.q, m.start.r, m.start.s,
                m.end.q, m.end.r, m.end.s);
    }

    public static int score(Set<Hex> currentPos) {
        // for all winning positions
        // find closes piece
        // add distance to score
        // remove piece
        int score = 0;
        for (Hex pos : winPos) {
            int min = 10000;
            Hex closest = null;
            for (Hex cur : currentPos) {
                int d = pos.distance(cur);
                if (d < min) {
                    min = d;
                    closest = cur;
                }
            }
            score += min;
            currentPos.remove(closest);
        }
        return score;
    }

    public static Move getNextMove(List<Piece> pieces) {
        Random rng = new Random();
        final Set<Hex> currentPos = pieces.stream().filter(p -> p.playerID == 0).map(p -> p.pos)
                .collect(Collectors.toSet());
        List<Move> allMoves = getPossibleMoves(pieces);
        // compute score for each move
        List<Integer> scores = allMoves.stream()
                .map(m -> {
                    Set<Hex> newPos = new HashSet<>(currentPos);
                    newPos.remove(m.start);
                    newPos.add(m.end);
                    return score(newPos);
                })
                .collect(Collectors.toList());
        // only keep best moves (with lowest score)
        int rating = Integer.MAX_VALUE;
        List<Move> bestMoves = new ArrayList<>();
        for (int i = 0; i < scores.size(); i++) {
            int s = scores.get(i);
            if (s < rating) {
                rating = s;
                bestMoves = new ArrayList<>();
                bestMoves.add(allMoves.get(i));
            } else if (rating == s) {
                bestMoves.add(allMoves.get(i));
            }
        }
        logger.debug(String.format("Got %d moves with best rating of %d", bestMoves.size(), rating));
        return bestMoves.get(rng.nextInt(bestMoves.size()));
    }

    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            Board b = new Board();
            b.init(Referee.GROUND_SIZE, 3);
            winPos = b.getStartFields(3);

            while (true) {
                long start = System.currentTimeMillis();
                int n = in.nextInt();
                logger.debug(String.format("Start reading %d pieces", n));
                List<Piece> pieces = new ArrayList<Piece>(n);
                for (int i = 0; i < n; i++) {
                    int id = in.nextInt();
                    int q = in.nextInt();
                    int r = in.nextInt();
                    int s = in.nextInt();
                    pieces.add(new Piece(new Hex(q, r, s), id));
                }
                long end = System.currentTimeMillis();
                logger.debug(String.format("Reading took %d ms", end - start));
                start = end;
                printMove(getNextMove(pieces));
                end = System.currentTimeMillis();
                logger.debug(String.format("Searching took %d ms", end - start));
            }
        }
    }
}
