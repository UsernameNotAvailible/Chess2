import java.util.ArrayList;

import static java.lang.Math.abs;

public class Queen extends Piece {
    public Queen(int x, int y, boolean black) {
        super(x, y, black, 'q');
    }

    @Override
    protected void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    boolean movePossible(int x, int y) {
        if ((this.x == x ^ this.y == y) || (abs(this.x - x) == abs(this.y - y) && this.x != x)) {
            return true;
        }
        return false;
    }
    @Override
    protected ArrayList<int[]> allPotentialMoves() {
        move[0] = x;
        move[1] = y;
        move[4] = -1;
        for (int i = 0; i < 8; i++) {
            move[2] = x;
            move[3] = i;
            if (i != y) {
                potentialMoves.add(move.clone());
            }
            move[2] = i;
            move[3] = y;
            if (i != x) {
                potentialMoves.add(move.clone());
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x + i < 8 && i + y < 8) {
                move[2] = x + i;
                move[3] = y + i;
                potentialMoves.add(move.clone());
            }
            if (x - i > -1 && y - i > -1) {
                move[2] = x - i;
                move[3] = y - i;
                potentialMoves.add(move.clone());
            }
            if (x + i < 8 && y - i > -1) {
                move[2] = x + i;
                move[3] = y - i;
                potentialMoves.add(move.clone());
            }
            if (x - i > -1 && y + i < 8) {
                move[2] = x - i;
                move[3] = y + i;
                potentialMoves.add(move.clone());
            }
        }
        return potentialMoves;
    }
}
