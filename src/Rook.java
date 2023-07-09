import java.util.ArrayList;

public class Rook extends Piece {
    public Rook(int x, int y, boolean black) {
        super(x, y, black, 'r');
    }

    @Override
    protected void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    boolean movePossible(int x, int y) {
        return this.x == x ^ this.y == y;
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
        return potentialMoves;
    }
}
