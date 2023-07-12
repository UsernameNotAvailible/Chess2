import java.util.ArrayList;
import java.util.Random;

public class Engine {
    private int pawnWorth = 100;
    private int rookWorth = 500;
    private int bishopWorth = 315;
    private int knightWorth = 300;
    private int queenWorth = 900;
    private int evaluationBlack = 0;
    private int evaluationWhite = 0;
    private int evaluation;
    private Random rand = new Random();
    int[] move = new int[5];
    private Game game = new Game("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    public void makeMove (int[] move) {
        game.makeMove(move[0], move[1], move[2], move[3], move[4]);
    }

    private int evaluate(Game position, boolean side) {
        int evaluation = 0;
        for (Piece c: position.getPieces()) {
            if (c != null) {
                if (c.black == side) {
                    if (c.type == 'p') {
                        evaluation += pawnWorth;
                    }
                    else if (c.type == 'r') {
                        evaluation += rookWorth;
                    }
                    else if (c.type == 'n') {
                        evaluation += knightWorth;
                    }
                    else if (c.type == 'b') {
                        evaluation += bishopWorth;
                    }
                    else if (c.type == 'q') {
                        evaluation += queenWorth;
                    }
                }
            }
        }
        return evaluation;
    }
    private int evaluate2(Game position) {
        int whiteEval = evaluate(position, false);
        int blackEval = evaluate(position, true);
        int eval = whiteEval - blackEval;
        int perspective = -1;
        if (position.isSideMove()) {
            perspective = 1;
        }
        return eval * perspective;
    }

    public int recursiveEvaluation(int depth, int alpha, int beta, int start) {
        if (depth == 0) { return evaluate2(game); }

        ArrayList<int[]> legalMoves = game.legalMoves();
        if (legalMoves.isEmpty()) {
            if (game.kingInCheck(!game.isSideMove())) {
                return -10000;
            }
            else return 0;
        }
        for (int[] move : legalMoves) {
            game.makeMove(move[0], move[1], move[2], move[3], move[4]);
            int evaluation = -recursiveEvaluation(depth - 1, -beta, -alpha, start);
            game.unmakeMove();
            if (start == depth && alpha < evaluation) {
                this.move = move;
            }
            if (evaluation >= beta) {
                return beta;
            }

            alpha = max(alpha, evaluation);
        }
        return alpha;
    }

    private int max(int a, int b) {
        if (a > b) {
            return a;
        }
        else {
            return b;
        }
    }
    private int min(int a, int b) {
        if (a < b) {
            return a;
        }
        else {
            return b;
        }
    }
    public int[] returnMove() {
        return move;
    }
    public int[] returnMove2(Game position) {
        int[] move = new int[5];
        while (!position.ruleCheck(move[0], move[1], move[2], move[3])[0]) {
            move[0] = rand.nextInt(8);
            move[1] = rand.nextInt(8);
            move[2] = rand.nextInt(8);
            move[3] = rand.nextInt(8);
            move[4] = rand.nextInt(4);
        }
        return move;
    }
    public int[] giveMove(int depth) {
        int evaluation;
        int bestEvaluation = -10000;
        ArrayList<int[]> legalMoves = game.legalMoves();
        for (int[] move: legalMoves) {
            game.makeMove(move[0], move[1], move[2], move[3], move[4]);
            evaluation = recursiveEvaluation(depth, -10000, 10000, depth);
            bestEvaluation = max(bestEvaluation, evaluation);
            if (bestEvaluation == evaluation) {
                this.move = move.clone();
            }
            game.unmakeMove();

        }
        return this.move;
    }



}