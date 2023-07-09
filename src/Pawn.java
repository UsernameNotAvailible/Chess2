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
        move[0] = x;
        move[1] = y;
        move[4] = 0;
        if (black) {
            move[2] = x + 1;
            move[3] = y - 1;
            potentialMoves.add(move.clone());
            move[2] = x - 1;
            potentialMoves.add(move.clone());
            move[2] = x;
            potentialMoves.add(move.clone());
            if (y == 1) {
                move[4] = 1;
                potentialMoves.add(move.clone());
                move[2] = x - 1;
                potentialMoves.add(move.clone());
                move[2] = x + 1;
                potentialMoves.add(move.clone());
                move[4] = 2;
                potentialMoves.add(move.clone());
                move[2] = x;
                potentialMoves.add(move.clone());
                move[2] = x - 1;
                potentialMoves.add(move.clone());
                move[4] = 3;
                potentialMoves.add(move.clone());
                move[2] = x + 1;
                potentialMoves.add(move.clone());
                move[2] = x;
                potentialMoves.add(move.clone());
            }
            if (y == 6) {
                move[3] = y - 2;
                potentialMoves.add(move.clone());
            }
        }
        else {
            move[2] = x + 1;
            move[3] = y + 1;
            potentialMoves.add(move.clone());
            move[2] = x - 1;
            potentialMoves.add(move.clone());
            move[2] = x;
            potentialMoves.add(move.clone());
            if (y == 6) {
                move[4] = 1;
                potentialMoves.add(move.clone());
                move[2] = x - 1;
                potentialMoves.add(move.clone());
                move[2] = x + 1;
                potentialMoves.add(move.clone());
                move[4] = 2;
                potentialMoves.add(move.clone());
                move[2] = x;
                potentialMoves.add(move.clone());
                move[2] = x - 1;
                potentialMoves.add(move.clone());
                move[4] = 3;
                potentialMoves.add(move.clone());
                move[2] = x + 1;
                potentialMoves.add(move.clone());
                move[2] = x;
                potentialMoves.add(move.clone());
            }
            if (y == 1) {
                move[3] = y + 2;
                potentialMoves.add(move.clone());
            }
        }
        return potentialMoves;
    }
}
