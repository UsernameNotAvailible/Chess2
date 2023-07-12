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
        potentialMoves.clear();
        move[0] = x;
        move[1] = y;
        move[4] = -1;
        if (x + 2 < 8 && y + 1 < 8) {
            move[2] = x + 2;
            move[3] = y + 1;
            potentialMoves.add(move.clone());
        }
        if (x + 2 < 8 && y - 1 > -1) {
            move[2] = x + 2;
            move[3] = y - 1;
            potentialMoves.add(move.clone());
        }
        if (x - 2 > -1 && y + 1 < 8) {
            move[2] = x - 2;
            move[3] = y + 1;
            potentialMoves.add(move.clone());
        }
        if (x - 2 > -1 && y - 1 > -1) {
            move[2] = x - 2;
            move[3] = y - 1;
            potentialMoves.add(move.clone());
        }
        if (x + 1 < 8 && y + 2 < 8) {
            move[2] = x + 1;
            move[3] = y + 2;
            potentialMoves.add(move.clone());
        }
        if (x + 1 < 8 && y - 2 > -1) {
            move[2] = x + 1;
            move[3] = y - 2;
            potentialMoves.add(move.clone());
        }
        if (x - 1 > -1 && y + 2 < 8) {
            move[2] = x - 1;
            move[3] = y + 2;
            potentialMoves.add(move.clone());
        }
        if (x - 1 > -1 && y - 2 > -1) {
            move[2] = x - 1;
            move[3] = y - 2;
            potentialMoves.add(move.clone());
        }
        return potentialMoves;
    }
}
