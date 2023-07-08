import java.util.ArrayList;

import static java.lang.Math.sqrt;

public class Knight extends Piece {
    public Knight(int x, int y, boolean black) {
        super(x, y, black, 'n');
    }

    @Override
    protected void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    boolean movePossible(int x, int y) {
        return sqrt((this.x - x) * (this.x - x) + (this.y - y) * (this.y - y)) == sqrt(5);
    }

    @Override
    protected ArrayList<int[]> allPotentialMoves() {
        if (x + 2 < 8 && y + 1 < 8) {
            move[0] = x + 2;
            move[1] = y + 1;
            potentialMoves.add(move);
        }
        if (x + 2 < 8 && y - 1 > -1) {
            move[0] = x + 2;
            move[1] = y - 1;
            potentialMoves.add(move);
        }
        if (x - 2 > -1 && y + 1 < 8) {
            move[0] = x - 2;
            move[1] = y + 1;
            potentialMoves.add(move);
        }
        if (x - 2 > -1 && y - 1 > -1) {
            move[0] = x - 2;
            move[1] = y - 1;
            potentialMoves.add(move);
        }
        if (x + 1 < 8 && y + 2 < 8) {
            move[0] = x + 1;
            move[1] = y + 2;
            potentialMoves.add(move);
        }
        if (x + 1 < 8 && y - 2 > -1) {
            move[0] = x + 1;
            move[1] = y - 2;
            potentialMoves.add(move);
        }
        if (x - 1 > -1 && y + 2 < 8) {
            move[0] = x - 1;
            move[1] = y + 2;
            potentialMoves.add(move);
        }
        if (x - 1 > -1 && y - 2 > -1) {
            move[0] = x - 1;
            move[1] = y - 2;
            potentialMoves.add(move);
        }
        return potentialMoves;
    }
}
