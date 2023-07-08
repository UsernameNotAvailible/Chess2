import java.util.ArrayList;

import static java.lang.Math.abs;

public class Pawn extends Piece {
    public Pawn(int x, int y, boolean black) {
        super(x, y, black, 'p');
    }

    @Override
    protected void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    boolean movePossible(int x, int y) {
        return ((((this.black && y - this.y == -1) || (!this.black && y - this.y == 1)) || (((this.black && this.y == 6) || (!this.black && this.y == 1)) && abs(y - this.y) == 2) && this.x == x) && abs(this.x - x) <= 1);
    }

    @Override
    protected ArrayList<int[]> allPotentialMoves() {
        if (black) {
            move[0] = x + 1;
            move[1] = y - 1;
            potentialMoves.add(move);
            move[0] = x - 1;
            potentialMoves.add(move);
            move[0] = x;
            move[1] = y - 1;
            potentialMoves.add(move);
            if (y == 6) {
                move[1] = y - 2;
                potentialMoves.add(move);
            }
        }
        else {
            move[0] = x + 1;
            move[1] = y + 1;
            potentialMoves.add(move);
            move[0] = x - 1;
            potentialMoves.add(move);
            move[0] = x;
            move[1] = y - 1;
            potentialMoves.add(move);
            if (y == 6) {
                move[1] = y + 2;
                potentialMoves.add(move);
            }
        }
        return potentialMoves;
    }
}
