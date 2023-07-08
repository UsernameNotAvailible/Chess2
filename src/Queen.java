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
        for (int i = 0; i < 8; i++) {
            if (i != x) {
                potentialMoves.add(new int[]{x, i});
            }
            if (i != y) {
                potentialMoves.add(new int[]{i, y});
            }
        }
        for (int i = 0; i < 8; i++) {
            if (x + i < 8 && i + y < 8) {
                potentialMoves.add(new int[]{x + i, y + i});
            }
            if (x - i > -1 && y - i > -1) {
                potentialMoves.add(new int[]{x - i, y - i});
            }
            if (x + i < 8 && y - i > -1) {
                potentialMoves.add(new int[]{x + i, y - i});
            }
            if (x - i > -1 && y + i < 8) {
                potentialMoves.add(new int[]{x - i, y + i});
            }
        }
        return potentialMoves;
    }
}
