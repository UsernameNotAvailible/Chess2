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
        var game = new Game("rnb2k1r/pp1Pbppp/2p5/q7/2B5/7P/PPP1NnP1/RNBQK2R w KQ - 1 9");
        ArrayList<int[]> moves = game.legalMoves();
    }

    @org.junit.jupiter.api.Test
    void countLegalPositions() {
        var game = new Game("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");
        int number = game.countLegalPositions(4);
        /*HashMap<String, Integer> repetitions = new HashMap<String, Integer>();

        for (String fen: game.FENs) {
            if (!repetitions.containsKey(fen)) {
                repetitions.put(fen, 1);
            }
        }

         */
        //System.out.println(repetitions.size());
        assertEquals(2103487, number);
    }
}