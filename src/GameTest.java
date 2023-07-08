import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @org.junit.jupiter.api.Test
    void ruleCheck() {
    }

    @org.junit.jupiter.api.Test
    void makeMove() {
//        assert (1, 1)
    }

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void getPieces() {
    }

    @org.junit.jupiter.api.Test
    void getPieces2() {
    }

    @org.junit.jupiter.api.Test
    void isSideMove() {
    }

    @org.junit.jupiter.api.Test
    void makeFEN() {
    }

    @org.junit.jupiter.api.Test
    void testRuleCheck() {
    }

    @org.junit.jupiter.api.Test
    void promotePawn() {
    }

    @org.junit.jupiter.api.Test
    void returnThePieces() {
    }

    @org.junit.jupiter.api.Test
    void copyPieces() {
    }

    @org.junit.jupiter.api.Test
    void testMakeMove() {
    }

    @org.junit.jupiter.api.Test
    void debugPrint() {
    }

    @org.junit.jupiter.api.Test
    void unmakeMove() {
    }

    @org.junit.jupiter.api.Test
    void legalMoves() {
        var game = new Game("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        ArrayList<int[]> moves = game.legalMoves();
    }

    @org.junit.jupiter.api.Test
    void countLegalPositions() {
        var game = new Game("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        int number = game.countLegalPositions(4);
        HashMap<String, Integer> repetitions = new HashMap<String, Integer>();

        for (String fen: game.FENs) {
            if (!repetitions.containsKey(fen)) {
                repetitions.put(fen, 1);
            }
        }
        System.out.println(repetitions.size());
        assertEquals(197281, number);
    }
}