import java.util.ArrayList;

import static java.lang.Math.abs;

public class Bishop extends Piece {
    public Bishop(int x, int y, boolean black) {
        super(x, y, black, 'b');
    }

    @Override
    protected void move(int x, int y) {
        if (abs(this.x - x) == abs(this.y - y) && this.x != x) {
            this.x = x;
            this.y = y;
        }
    }

    @Override
    boolean movePossible(int x, int y) {
        return abs(this.x - x) == abs(this.y - y) && this.x != x;
    }
    @Override
    protected ArrayList<int[]> allPotentialMoves() {
        move[0] = x;
        move[1] = y;
        move[4] = -1;
        for (int i = 0; i < 8; i++) {
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
