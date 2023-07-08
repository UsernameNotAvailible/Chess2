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
        for (int i = 0; i < 8; i++) {
            if (i != x) {
                potentialMoves.add(new int[]{x, i});
            }
            if (i != y) {
                potentialMoves.add(new int[]{i, y});
            }
        }
        return potentialMoves;
    }
}
