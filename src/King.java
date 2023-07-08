import java.util.ArrayList;

import static java.lang.Math.abs;

public class King extends Piece {
    public King(int x, int y, boolean black) {
        super(x, y, black, 'k');
    }

    @Override
    protected void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    boolean movePossible(int x, int y) {
        return abs(this.x - x) <= 1 && abs(this.y - y) <= 1 ||
                (this.x == 4 && ((this.black && this.y == 7 && y == 7) ||
                        (!this.black && this.y == 0 && y ==0)) && abs(this.x - x) == 2);
    }

    @Override
    protected ArrayList<int[]> allPotentialMoves() {
        if (x + 1 < 8) {
            move[0] = x + 1;
            move[1] = y;
            potentialMoves.add(move);
            if (y + 1 < 8) {
                move[1] = y + 1;
                potentialMoves.add(move);
                move[0] = x;
                potentialMoves.add(move);

            }
            if (y - 1 > -1) {
                move[0] = x + 1;
                move[1] = y - 1;
                potentialMoves.add(move);
                move[0] = x;
                potentialMoves.add(move);
            }
        }
        if (x - 1 > -1) {
            move[0] = x - 1;
            move[1] = y;
            potentialMoves.add(move);
            if (y + 1 < 8) {
                move[1] = y + 1;
                potentialMoves.add(move);
            }
            if (y - 1 > -1) {
                move[1] = y - 1;
                potentialMoves.add(move);
            }
        }
        return potentialMoves;
    }

}
