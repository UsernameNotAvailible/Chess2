import java.util.ArrayList;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public abstract class Piece {
    protected int x;
    protected int y;
    boolean black;
    char type;
    protected ArrayList<int[]> potentialMoves = new ArrayList<>();
    protected int[] move = new int[5];

    protected Piece (int x, int y, boolean black, char type) {
        this.y = y;
        this.x = x;
        this.black = black;
        this.type = type;
    }
    protected abstract void move(int x, int y);
    abstract boolean movePossible(int x, int Y);
    protected abstract ArrayList<int[]> allPotentialMoves();
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(type);
        s.append("\t");
        return s.toString();
    }
}

