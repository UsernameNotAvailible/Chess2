import java.util.*;

import static java.lang.Math.*;

public class Game {
    //private ArrayList<int[]> legalMoves = new ArrayList<>();
    //private ArrayList<int[]> opponentAttackMap = new ArrayList<>();
    //private Stack<Piece> capturedPieces = new Stack<>();
    //private Stack<int[]> moves = new Stack<>();
    private Stack<String> FENStack = new Stack<>();
    HashMap<String, Integer> FENsHashMap = new HashMap<String, Integer>();
    private Piece[] pieces = new Piece[32];
    private Piece[] pieces2 = new Piece[32];
    private String FEN;
    private String FENtrimmed;
    private int[] enpassantSquare = new int[2];
    private boolean sideMove;
    private int halfMoveCounter;
    private int fullMoveCounter;
    private boolean[] castlingAvailability = new boolean[] {false, false, false, false};
    private boolean isCapture = false;
    private boolean[] ILLEGAL_MOVE = new boolean[] {false, false};
    private boolean[] LEGAL_MOVE_NO_PROMOTION = new boolean[] {true, false};
    private boolean[] LEGAL_MOVE_PROMOTION = new boolean[] {true, true};
    private boolean[] moveLegality = new boolean[] {false, false};;
    private boolean pieceExists = false;
    private boolean pieceExistsAtTheEnd = false;
    //private boolean bishopCheck = false;
    //private boolean rookCheck = false;
    private boolean isCastling = false;
    private int evaluation1 = 0;
    private int evaluation2 = 0;
    private boolean[] NO_CASTLING = new boolean[] {false, false, false, false};
    private int[] whiteKingPosition = new int[] {-1, -1};
    private int[] blackKingPosition = new int[] {-1, -1};
    private int[] whiteKingPosition2 = whiteKingPosition;
    private int[] blackKingPosition2 = blackKingPosition;
    private int[] move = new int[] {-1, -1, -1, -1, -1};
    private int[] square = new int[] {-1, -1};
    private Piece movingPiece = null;
    private Piece pieceAtTheEnd = null;
    private Piece c;
    private int whitePawnCounter = 0;
    private int blackPawnCounter = 0;
    private int whiteRookCounter = 0;
    private int blackRookCounter = 0;
    private int whiteKnightCounter = 0;
    private int blackKnightCounter = 0;
    private int whiteBishopCounter = 0;
    private int blackBishopCounter = 0;
    private int whiteQueenCounter = 0;
    private int blackQueenCounter = 0;
    private int pieceCounter = 0;
    private int positionCounter = 1;

    StringBuilder s = new StringBuilder();
    StringBuilder s1 = new StringBuilder();
    StringBuilder s2 = new StringBuilder();

    public int getPieceCounter() {
        return pieceCounter;
    }
    public int getHalfMoveCounter() {
        return halfMoveCounter;
    }

    public int getPositionCounter() {
        return positionCounter;
    }

    public Piece[] getPieces() {
        return pieces;
    }
    public Piece[] getPieces2() {
        return pieces2;
    }

    public boolean isSideMove() {
        return sideMove;
    }

    public int[] getBlackKingPosition() {
        return blackKingPosition;
    }
    public int[] getWhiteKingPosition() {
        return whiteKingPosition;
    }
    public String getFEN() {
        return FEN;
    }

    private void voidPieces() {
        pieces = new Piece[32];
    }
    private void voidPieces2() {
        pieces2 = new Piece[32];
    }

    private void FENReader(String FEN) {
        blackPawnCounter = 0;
        whitePawnCounter = 0;
        blackRookCounter = 0;
        whiteRookCounter = 0;
        blackKnightCounter = 0;
        whiteKnightCounter = 0;
        blackBishopCounter = 0;
        whiteBishopCounter = 0;
        blackQueenCounter = 0;
        whiteQueenCounter = 0;
        int x = 0;
        int y = 7;
        pieceCounter = 0;
        char[] pieces = {'r', 'n', 'b', 'q', 'k', 'p', 'R', 'N', 'B', 'Q', 'K', 'P'};
        int spaceCounter = 0;
        s.delete(0, s.length());
        s1.delete(0, s1.length());
        s2.delete(0, s2.length());
        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();
        castlingAvailability = NO_CASTLING.clone();
        for (char i: FEN.toCharArray()) {
            if (contains(pieces, i) && spaceCounter == 0) {
                create(i, pieceCounter, x, y);
                pieceCounter++;
                s2.append(i);
            }
            else if (i < 57 && i > 47) {
                x += Integer.parseInt(String.valueOf(i)) - 1;
                if (spaceCounter == 0) {
                    s2.append(i);
                }
            }
            if (i == '/') {x--; y--; s2.append(i);}
            x = (x + 1)%8;
            if (i == ' ') {spaceCounter++;}
            if (spaceCounter == 1) {determineMove(i);}
            else if (spaceCounter == 2) {determineCastlingAvailability(i, x - 2);}
            else if (spaceCounter == 3) {
                if (i > 96 && i < 105) {enpassantSquare[0] = i - 97;}
                else if (i > 47 && i < 57) {enpassantSquare[1] = i - 49;}
                if (i == '-') {
                    enpassantSquare[0] = -1;
                    enpassantSquare[1] = -1;
                }
            }
            else if (spaceCounter == 4) {
                s.append(i);
            }
            else if (spaceCounter == 5) {
                s1.append(i);
            }
        }
        halfMoveCounter = Integer.parseInt(s.toString().trim());
        fullMoveCounter = Integer.parseInt(s1.toString().trim());
        FENtrimmed = s2.toString();
    }
    private boolean contains(char[] array, char a) {
        for (char i: array) {
            if (i == a) return true;
        }
        return false;
    }
    private void create(char piece, int pieceCounter, int x, int y) {
        if (piece == 'r') {
            if (blackRookCounter == 0) {
                pieces[pieceCounter] = BLACK_ROOK; pieces[pieceCounter].move(x, y);
            }
            else if (blackRookCounter == 1) {
                pieces[pieceCounter] = BLACK_ROOK1; pieces[pieceCounter].move(x, y);
            }
            else {
                pieces[pieceCounter] = new Rook(x, y, true);
            }
            blackRookCounter++;
        }
        else if (piece == 'n') {
            if (blackKnightCounter == 0) {
                pieces[pieceCounter] = BLACK_KNIGHT;
                pieces[pieceCounter].move(x, y);
            } else if (blackKnightCounter == 1) {
                pieces[pieceCounter] = BLACK_KNIGHT1;
                pieces[pieceCounter].move(x, y);
            } else {
                pieces[pieceCounter] = new Knight(x, y, true);
            }
            blackKnightCounter++;
        }
        else if (piece == 'b') {
            if (blackBishopCounter == 0) {
                pieces[pieceCounter] = BLACK_BISHOP;
                pieces[pieceCounter].move(x, y);
            } else if (blackBishopCounter == 1) {
                pieces[pieceCounter] = BLACK_BISHOP1;
                pieces[pieceCounter].move(x, y);
            } else {
                pieces[pieceCounter] = new Bishop(x, y, true);
            }
            blackBishopCounter++;
        }
        else if (piece == 'q') {
            if (blackQueenCounter == 0) {
                pieces[pieceCounter] = BLACK_QUEEN;
                pieces[pieceCounter].move(x, y);
            } else if (blackQueenCounter == 1) {
                pieces[pieceCounter] = BLACK_QUEEN1;
                pieces[pieceCounter].move(x, y);
            }
            else {
                pieces[pieceCounter] = new Queen(x, y, true);
            }
            blackQueenCounter++;
        }
        else if (piece == 'k') {
            pieces[pieceCounter] = BLACK_KING;
            pieces[pieceCounter].move(x, y);
            blackKingPosition[0] = x; blackKingPosition[1] = y;
            blackKingPosition2[0] = x; blackKingPosition2[1] = y;
        }
        else if (piece == 'p') {
            if (blackPawnCounter == 0) {
                pieces[pieceCounter] = BLACK_PAWN;
                pieces[pieceCounter].move(x, y);
            } else if (blackPawnCounter == 1) {
                pieces[pieceCounter] = BLACK_PAWN1;
                pieces[pieceCounter].move(x, y);
            } else if (blackPawnCounter == 2) {
                pieces[pieceCounter] = BLACK_PAWN2;
                pieces[pieceCounter].move(x, y);
            } else if (blackPawnCounter == 3) {
                pieces[pieceCounter] = BLACK_PAWN3;
                pieces[pieceCounter].move(x, y);
            } else if (blackPawnCounter == 4) {
                pieces[pieceCounter] = BLACK_PAWN4;
                pieces[pieceCounter].move(x, y);
            } else if (blackPawnCounter == 5) {
                pieces[pieceCounter] = BLACK_PAWN5;
                pieces[pieceCounter].move(x, y);
            } else if (blackPawnCounter == 6) {
                pieces[pieceCounter] = BLACK_PAWN6;
                pieces[pieceCounter].move(x, y);
            } else if (blackPawnCounter == 7) {
                pieces[pieceCounter] = BLACK_PAWN7;
                pieces[pieceCounter].move(x, y);
            }
            else {
                pieces[pieceCounter] = new Pawn(x, y, true);
            }
            blackPawnCounter++;
        }
        else if (piece == 'R') {
            if (whiteRookCounter == 0) {
                pieces[pieceCounter] = WHITE_ROOK; pieces[pieceCounter].move(x, y);
            }
            else if (whiteRookCounter == 1) {
                pieces[pieceCounter] = WHITE_ROOK1; pieces[pieceCounter].move(x, y);
            }
            else {
                pieces[pieceCounter] = new Rook(x, y, false);
            }
            whiteRookCounter++;
        }
        else if (piece == 'N') {
            if (whiteKnightCounter == 0) {
                pieces[pieceCounter] = WHITE_KNIGHT;
                pieces[pieceCounter].move(x, y);
            } else if (whiteKnightCounter == 1) {
                pieces[pieceCounter] = WHITE_KNIGHT1;
                pieces[pieceCounter].move(x, y);
            } else {
                pieces[pieceCounter] = new Knight(x, y, false);
            }
            whiteKnightCounter++;
        }
        else if (piece == 'B') {
            if (whiteBishopCounter == 0) {
                pieces[pieceCounter] = WHITE_BISHOP;
                pieces[pieceCounter].move(x, y);
            } else if (whiteBishopCounter == 1) {
                pieces[pieceCounter] = WHITE_BISHOP1;
                pieces[pieceCounter].move(x, y);
            } else {
                pieces[pieceCounter] = new Bishop(x, y, false);
            }
            whiteBishopCounter++;
        }
        else if (piece == 'Q') {
            if (whiteQueenCounter == 0) {
                pieces[pieceCounter] = WHITE_QUEEN;
                pieces[pieceCounter].move(x, y);
            } else if (whiteQueenCounter == 1) {
            pieces[pieceCounter] = WHITE_QUEEN1;
                pieces[pieceCounter].move(x, y);
            }else {
                pieces[pieceCounter] = new Queen(x, y, false);
            }
            whiteQueenCounter++;
        }
        else if (piece == 'K') {
            pieces[pieceCounter] = WHITE_KING;
            pieces[pieceCounter].move(x, y);
            whiteKingPosition[0] = x; whiteKingPosition[1] = y;
            whiteKingPosition2[0] = x; whiteKingPosition2[1] = y;
        }
        else if (piece == 'P') {
            if (whitePawnCounter == 0) {
                pieces[pieceCounter] = WHITE_PAWN;
                pieces[pieceCounter].move(x, y);
            } else if (whitePawnCounter == 1) {
                pieces[pieceCounter] = WHITE_PAWN1;
                pieces[pieceCounter].move(x, y);
            } else if (whitePawnCounter == 2) {
                pieces[pieceCounter] = WHITE_PAWN2;
                pieces[pieceCounter].move(x, y);
            } else if (whitePawnCounter == 3) {
                pieces[pieceCounter] = WHITE_PAWN3;
                pieces[pieceCounter].move(x, y);
            } else if (whitePawnCounter == 4) {
                pieces[pieceCounter] = WHITE_PAWN4;
                pieces[pieceCounter].move(x, y);
            } else if (whitePawnCounter == 5) {
                pieces[pieceCounter] = WHITE_PAWN5;
                pieces[pieceCounter].move(x, y);
            } else if (whitePawnCounter == 6) {
                pieces[pieceCounter] = WHITE_PAWN6;
                pieces[pieceCounter].move(x, y);
            } else if (whitePawnCounter == 7) {
                pieces[pieceCounter] = WHITE_PAWN7;
                pieces[pieceCounter].move(x, y);
            }
            else {
                pieces[pieceCounter] = new Pawn(x, y, true);
            }
            whitePawnCounter++;
        }
    }
    private void determineMove (char i) {
        if (i == 'w') {sideMove = true;}
        else if (i == 'b') {sideMove = false;}
    }
    private void determineCastlingAvailability (char i, int x) {
        if (i == 'K') {castlingAvailability[0] = true;}
        else if (i == 'Q') {castlingAvailability[1] = true;}
        else if (i == 'k') {castlingAvailability[2] = true;}
        else if (i == 'q') {castlingAvailability[3] = true;}
        else if (i == '-') {
            castlingAvailability = new boolean[]{false, false, false, false};
        }
    }
    public String makeFEN() {
        char[][] piecesArray = new char[8][8];
        int[] gaps = new int[40];
        int nonNullCounter = 0;
        int counter = 0;
        s.delete(0, s.length());
        s1.delete(0, s1.length());
        for (Piece c: pieces) {
            if (c != null && c.x != -1) {
                if (c.black) {
                    piecesArray[c.x][c.y] = c.type;
                } else {
                    piecesArray[c.x][c.y] = (char) (c.type - 32);
                }
            }
        }
        for (int i = 7; i > -1; i--) {
            for (int j = 0; j < 8; j++)
            {
                s.append(piecesArray[j][i]);
            }
            //System.out.println(i);
            s.append("/");
        }
        s.delete(s.lastIndexOf("/"), s.lastIndexOf(""));
        //s.reverse();

        for (char c: s.toString().toCharArray()) {
            if (c == '\0') {
                gaps[nonNullCounter]++;
                s.replace(counter, counter + 1, String.valueOf(gaps[nonNullCounter]));
            }
            else  {
                nonNullCounter++;
            }
            counter++;
        }
        counter = 0;
        nonNullCounter = 0;
        for (char c: s.toString().toCharArray()) {
            if (c < 57 && c > 47 && (c - 48) < gaps[nonNullCounter]) {
                s.deleteCharAt(counter);
                counter--;
            }

            else if (c - 48 != gaps[nonNullCounter]) {
                nonNullCounter++;
            }
            counter++;
        }
        s1.append(s);
        s.append(' ');
        if (sideMove) {
            s.append('w');
        }
        else {
            s.append('b');
        }
        s.append(' ');
        if (Arrays.equals(castlingAvailability, NO_CASTLING)) {
            s.append('-');
        }
        else {
            if (castlingAvailability[0]) {
                s.append('K');
            }
            if (castlingAvailability[1]) {
                s.append('Q');
            }
            if (castlingAvailability[2]) {
                s.append('k');
            }
            if (castlingAvailability[3]) {
                s.append('q');
            }
        }
        s.append(' ');
        if (enpassantSquare != null && enpassantSquare[0] != -1) {
            s.append((char) (enpassantSquare[0] + 97));
            s.append((char) (enpassantSquare[1] + 49));
        }
        else {
            s.append('-');
        }
        s.append(' ');
        s.append(halfMoveCounter);
        s.append(' ');
        s.append(fullMoveCounter);
        FENtrimmed = s1.toString();
        return s.toString();
    }
    // Methods to check rules
    private Piece findPiece(int x, int y) {
        for (Piece c: pieces) {
            if (c != null && c.x == x && c.y == y) {
                return c;
            }
        }
        return null;
    }
    private Piece findPiece2(int x, int y) {
        for (Piece c: pieces2) {
            if (c != null && c.x == x && c.y == y) {
                return c;
            }
        }
        return null;
    }
    private int[] findPiece(char type, boolean black) {
        for (Piece c: pieces) {
            if (c != null && (c.type == type) && (c.black == black) && (c.x != -1)) return new int[]{c.x, c.y};
        }
        return new int[]{-1, -1};
    
    }
    private boolean checkPiece(int x, int y) {
        for (Piece c: pieces) {
            if (c != null && c.x == x && c.y == y) {
                return true;
            }
        }
        return false;
    }
    private boolean checkPiece2(int x, int y) {
        for (Piece c: pieces2) {
            if (c != null && c.x == x && c.y == y) {
                return true;
            }
        }
        return false;
    }
    private boolean rookCheck(int xStart, int yStart, int xEnd, int yEnd) {
        if (yEnd == yStart && xEnd > xStart) {
            for (int i = xStart + 1; i < xEnd; i++) {
                if (checkPiece(i, yStart)) {
                    return true;
                }
            }
        }
        if (yEnd == yStart && xEnd < xStart) {
            for (int i = xStart - 1; i > xEnd; i--) {
                if (checkPiece(i, yStart)) {
                    return true;
                }
            }
        }
        if (yEnd > yStart && xEnd == xStart) {
            for (int i = yStart + 1; i < yEnd; i++) {
                if (checkPiece(xStart, i)) {
                    return true;
                }
            }
        }
        if (yEnd < yStart && xEnd == xStart) {
            for (int i = yStart - 1; i > yEnd; i--) {
                if (checkPiece(xStart, i)) {
                    return true;
                }
            }
        }
        return false;
    }
    private boolean bishopChep(int xStart, int yStart, int xEnd, int yEnd) {
        if (xEnd > xStart && yEnd > yStart) {
            for (int i = 1; i < (xEnd - xStart); i++) {
                if (checkPiece(xStart + i, yStart + i)) {
                    return true;
                }
            }
        }
        if (xEnd > xStart && yEnd < yStart) {
            for (int i = 1; i < xEnd - xStart; i++) {
                if (checkPiece(xStart + i, yStart - i)) {
                    return true;
                }
            }
        }
        if (xEnd < xStart && yEnd > yStart) {
            for (int i = 1; i < yEnd - yStart; i++) {
                if (checkPiece(xStart - i, yStart + i)) {
                    return true;
                }
            }
        }
        if (xEnd < xStart && yEnd < yStart) {
            for (int i = 1; i < yStart - yEnd; i++) {
                if (checkPiece(xStart - i, yStart - i)) {
                    return true;
                }
            }
        }
        return false;
    }
    private void deletePiece(int x, int y, boolean black) {
        for (int i = 0; i < 32; i++) {
            if (pieces[i] != null && pieces[i].x == x && pieces[i].y == y && (pieces[i].black == black)) {
                pieces[i] = null;
                pieceCounter--;
                return;
            }
        }
    }
    private boolean kingInCheck (int x, int y, boolean black) {
        for (int i = x + 1; i < 8; i++) {
            c = findPiece(i, y);
            if (c != null && (c.type == 'r' || c.type == 'q') && (c.black != black)) {
                return true;
            }
            else if (c != null) {
                break;
            }
            
        }
        for (int i = x - 1; i > -1; i--) {
            c = findPiece(i, y);
            if (c != null && (c.type == 'r' || c.type == 'q') && (c.black != black)) {
                return true;
            }
            else if (c != null) {
                break;
            }
        }
        for (int i = y + 1; i < 8; i++) {
            c = findPiece(x, i);
            if (c != null && (c.type == 'r' || c.type == 'q') && (c.black != black)) {
                return true;
            }
            else if (c != null) {
                break;
            }
        }
        for (int i = y - 1; i > -1; i--) {
            c = findPiece(x, i);
            if (c != null && (c.type == 'r' || c.type == 'q') && (c.black != black)) {
                return true;
            }
            else if (c != null) {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            c = findPiece(x + i, y + i);
            if (c != null && (c.type == 'b' || c.type == 'q') && (c.black != black)) {
                return true; 
            }
            else if (c != null) {
                break;
            }
            
        }
        for (int i = 1; i < 8; i++) {
            c = findPiece(x + i, y - i);
            if (c != null && (c.type == 'b' || c.type == 'q') && (c.black != black)) {
                return true;
            }
            else if (c != null) {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            c = findPiece(x - i, y + i);
            //System.out.print(c + " " + c.x + " " + c.y + " " + c.black);
            if (c != null && (c.type == 'b' || c.type == 'q') && (c.black != black)) {
                return true;
            }
            else if (c != null) {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            c = findPiece(x - i, y - i);
            if (c != null && (c.type == 'b' || c.type == 'q') && (c.black != black)) {
                return true;
            }
            else if (c != null) {
                break;
            }
        }
        
        c = findPiece(x + 1, y + 2);
        if (c != null && (c.black != black) && (c.type == 'n')) return true;
        c = findPiece(x + 1, y - 2);
        if (c != null && (c.black != black) && (c.type == 'n')) return true;
        c = findPiece(x - 1, y + 2);
        if (c != null && (c.black != black) && (c.type == 'n')) return true;
        c = findPiece(x - 1, y - 2);
        if (c != null && (c.black != black) && (c.type == 'n')) return true;
        c = findPiece(x + 2, y + 1);
        if (c != null && (c.black != black) && (c.type == 'n')) return true;
        c = findPiece(x + 2, y - 1);
        if (c != null && (c.black != black) && (c.type == 'n')) return true;
        c = findPiece(x - 2, y + 1);
        if (c != null && (c.black != black) && (c.type == 'n')) return true;
        c = findPiece(x - 2, y - 1);
        if (c != null && (c.black != black) && (c.type == 'n')) return true;

        if (black) {
            c = findPiece(x - 1, y - 1);
            if (c != null && !c.black && c.type == 'p') {
                return true;
            }
            c = findPiece(x + 1, y - 1);
            if (c != null && !c.black && c.type == 'p') {
                return true;
            }
        }
        else {
            c = findPiece(x - 1, y + 1);
            if (c != null && c.black && c.type == 'p') {
                return true;
            }
            c = findPiece(x + 1, y + 1);
            if (c != null && c.black && c.type == 'p') {
                return true;
            }

        }
        return false;
    }
    private void moveTheRook(int xStart, int yStart, int xEnd, int yEnd) {
        if (xEnd - xStart == 2) {
            c = findPiece(7, yEnd);
        }
        else if (xEnd - xStart == -2) {
            c = findPiece(0, yEnd);
        }
        if (c != null) {
            c.move((xStart + xEnd)/2, yEnd);
        }
    }
    public boolean[] ruleCheck(int xStart, int yStart, int xEnd, int yEnd) {
        if (xEnd < 0 || xEnd > 7 || yEnd < 0 || yEnd > 7 ||
                xStart < 0 || xStart > 7 || yStart < 0 || yStart > 7 || (xEnd == xStart && yStart == yEnd)) {
            return ILLEGAL_MOVE;
        }
        pieceExists = checkPiece(xStart, yStart);
        if (!pieceExists) {
            return ILLEGAL_MOVE;
        }
        movingPiece = findPiece(xStart, yStart);
        if (movingPiece.black == sideMove) {
            return ILLEGAL_MOVE;
        }
        pieceExistsAtTheEnd = checkPiece(xEnd, yEnd);
        if (pieceExistsAtTheEnd) {
            pieceAtTheEnd = findPiece(xEnd, yEnd);
        }
        if (pieceExistsAtTheEnd && (pieceAtTheEnd.black == movingPiece.black)) {
            return ILLEGAL_MOVE;
        }
        if (!movingPiece.movePossible(xEnd, yEnd)) {
            return ILLEGAL_MOVE;
        }
        isCapture = pieceExistsAtTheEnd && (pieceAtTheEnd.black != movingPiece.black);
        if (movingPiece.type == 'r') {
            if (rookCheck(xStart, yStart, xEnd, yEnd)) {
                return ILLEGAL_MOVE;
            }
        }
        if (movingPiece.type == 'b') {
            if (bishopChep(xStart, yStart, xEnd, yEnd)) {
                return ILLEGAL_MOVE;
            }
        }
        if (movingPiece.type == 'q') {
            if (rookCheck(xStart, yStart, xEnd, yEnd)) {
                return ILLEGAL_MOVE;
            }
            if (bishopChep(xStart, yStart, xEnd, yEnd)) {
                return ILLEGAL_MOVE;
            }
        }
        blackKingPosition2 = blackKingPosition.clone();
        whiteKingPosition2 = whiteKingPosition.clone();
        isCastling = false;
        if (movingPiece.type == 'k') {

            if (abs(xEnd - xStart) == 2) {
                if (xEnd == 6 && yEnd == 0 && !castlingAvailability[0]) {
                    return ILLEGAL_MOVE;
                }
                else if (xEnd == 2 && yEnd == 0 && !castlingAvailability[1]) {
                    return ILLEGAL_MOVE;
                }
                else if (xEnd == 6 && yEnd == 7 && !castlingAvailability[2]) {
                    return ILLEGAL_MOVE;
                }
                else if (xEnd == 2 && yEnd == 7 && !castlingAvailability[3]) {
                    return ILLEGAL_MOVE;
                }
                else if (checkPiece((xStart + xEnd)/2, yEnd)) {
                    return ILLEGAL_MOVE;
                }
                isCastling = true;
            }
            if (movingPiece.black) {
                blackKingPosition2[0] = xEnd;
                blackKingPosition2[1] = yEnd;
            }
            else {
                whiteKingPosition2[0] = xEnd;
                whiteKingPosition2[1] = yEnd;
            }
            if (isCastling && kingInCheck(xStart, yStart, movingPiece.black)) {
                return ILLEGAL_MOVE;
            }
        }

        copyPieces();
        
        if (movingPiece.type == 'p') {
            if (abs(yEnd - yStart) == 2 && checkPiece(xEnd, (yEnd + yStart)/2)) {
                returnThePieces();
                return ILLEGAL_MOVE;
            }
            if (isCapture && xStart == xEnd) {
                returnThePieces();
                return ILLEGAL_MOVE;
            }
            if (!isCapture && xStart != xEnd && (enpassantSquare[0] != xEnd || enpassantSquare[1] != yEnd)) {
                returnThePieces();
                return ILLEGAL_MOVE;
            }
            if (xEnd == enpassantSquare[0] && yEnd == enpassantSquare[1]) {
                deletePiece(xEnd, (yEnd + yStart)/2, !movingPiece.black);
            }
        }
        
        if (isCapture) {
            deletePiece(xEnd, yEnd, !movingPiece.black);
        }

        movingPiece.move(xEnd, yEnd);

        if (isCastling) {
            moveTheRook(xStart, yStart, xEnd, yEnd);
        }
        if (sideMove) {
            if (kingInCheck(whiteKingPosition2[0], whiteKingPosition2[1], false)) {
                returnThePieces();
                return ILLEGAL_MOVE;
            }
        }
        else {
            if (kingInCheck(blackKingPosition2[0], blackKingPosition2[1], true)) {
                returnThePieces();
                return ILLEGAL_MOVE;
            }
        }
        if (sqrt(pow(whiteKingPosition2[0] - blackKingPosition2[0], 2) + pow(whiteKingPosition2[1] - blackKingPosition2[1], 2)) <= sqrt(2)) {
            returnThePieces();
            return ILLEGAL_MOVE;
        }
        if (movingPiece.type == 'p') {
            if (yEnd == 0 || yEnd == 7) {
                returnThePieces();
                return LEGAL_MOVE_PROMOTION;
            }
        }
        returnThePieces();
        return LEGAL_MOVE_NO_PROMOTION;
    }
    public void promotePawn(int j) {
        char type = ' ';
        if (j == 0) {
            type = 'q';
        }
        else if (j == 1) {
            type = 'r';
        }
        else if (j == 2) {
            type = 'b';
        }
        else if (j == 3) {
            type = 'n';
        }
        int x = -2;
        int y = 0;
        for (Piece c: pieces) {
            if (c != null && c.type == 'p' && (c.y == 7 || c.y == 0)) {
                x = c.x;
                y = c.y;
                break;
            }
        }
        for (int i = 0; i < 32; i++) {
            if (pieces[i] != null && pieces[i].x == x && pieces[i].y == y) {
                if (type == 'q') {
                    pieces[i] = new Queen(x, y, pieces[i].black);
                    return;
                } else if (type == 'r') {
                    pieces[i] = new Rook(x, y, pieces[i].black);
                    return;
                } else if (type == 'b') {
                    pieces[i] = new Bishop(x, y, pieces[i].black);
                    return;
                } else if (type == 'n') {
                    pieces[i] = new Knight(x, y, pieces[i].black);
                    return;
                }
            }
        }
    }
    private void updateProperties(int xStart, int yStart, int xEnd, int yEnd, int type) {
        movingPiece = findPiece(xStart, yStart);
        pieceExistsAtTheEnd = checkPiece(xEnd, yEnd);
        if (pieceExistsAtTheEnd) {
            pieceAtTheEnd = findPiece(xEnd, yEnd);
        }

        isCapture = pieceExistsAtTheEnd && (pieceAtTheEnd.black != movingPiece.black);
        isCastling = abs(xEnd - xStart) == 2 && movingPiece.type == 'k';

        if (isCastling) {
            moveTheRook(xStart, yStart, xEnd, yEnd);
        }
        if (isCapture) {
            deletePiece(xEnd, yEnd, !movingPiece.black);
            halfMoveCounter = 0;
        }

        movingPiece.move(xEnd, yEnd);
        if (movingPiece.type != 'p' || (yEnd != enpassantSquare[1] || xEnd != enpassantSquare[0]) ) {
            enpassantSquare[0] = -1;
            enpassantSquare[1] = -1;
        }

        if (movingPiece.type == 'p') {
            halfMoveCounter = 0;
            if (abs(yEnd - yStart) == 2) {
                enpassantSquare[0] = xStart;
                enpassantSquare[1] = (yEnd + yStart)/2;
            }
            if (enpassantSquare[0] == xEnd && enpassantSquare[1] == yEnd) {
                if (movingPiece.black) {
                    deletePiece(xEnd, yEnd + 1, false);
                }
                else {
                    deletePiece(xEnd, yEnd - 1, true);
                }
            }
            if (yEnd == 7 || yEnd == 0) {
                promotePawn(type);
            }
        }

        else if (movingPiece.type == 'r') {
            if (xStart == 7 && yStart == 0) {
                castlingAvailability[0] = false;
            }
            else if (xStart == 0 && yStart == 0) {
                castlingAvailability[1] = false;
            }
            else if (xStart == 7 && yStart == 7) {
                castlingAvailability[2] = false;
            }
            else if (xStart == 0 && yStart == 7) {
                castlingAvailability[3] = false;
            }
        }
        else if (movingPiece.type == 'k') {
            if (movingPiece.black) {
                castlingAvailability[2] = false;
                castlingAvailability[3] = false;
            }
            else {
                castlingAvailability[0] = false;
                castlingAvailability[1] = false;
            }
            if (movingPiece.black) {
                blackKingPosition[0] = xEnd;
                blackKingPosition[1] = yEnd;
            }
            else {
                whiteKingPosition[0] = xEnd;
                whiteKingPosition[1] = yEnd;
            }
        }

        else if (movingPiece.type == 'n') {

        }
        else if (movingPiece.type == 'b') {

        }
        else if (movingPiece.type == 'q') {

        }
        if (movingPiece.type != 'p' && !isCapture) {
            halfMoveCounter++;
        }
        if (isCapture && pieceAtTheEnd.type == 'r') {
            if (xEnd == 7 && yEnd == 0) {
                castlingAvailability[0] = false;
            }
            else if (xEnd == 0 && yEnd == 0) {
                castlingAvailability[1] = false;
            }
            else if (xEnd == 7 && yEnd == 7) {
                castlingAvailability[2] = false;
            }
            else if (xEnd == 0 && yEnd == 7) {
                castlingAvailability[3] = false;
            }
        }
        if (!sideMove) {
            fullMoveCounter++;
        }

        blackKingPosition2[0] = blackKingPosition[0];
        blackKingPosition2[1] = blackKingPosition[1];
        whiteKingPosition2[0] = whiteKingPosition[0];
        whiteKingPosition2[1] = whiteKingPosition[1];
        //makeAnOpponentAttackMap();
        sideMove = !sideMove;
    }
    public void returnThePieces() {
        blackKnightCounter = 0;
        blackQueenCounter = 0;
        blackPawnCounter = 0;
        blackBishopCounter = 0;
        blackRookCounter = 0;
        whiteBishopCounter = 0;
        whiteKnightCounter = 0;
        whiteRookCounter = 0;
        whitePawnCounter = 0;
        whiteQueenCounter = 0;
        int counter = 0;
        voidPieces();
        for (Piece piece: pieces2) {
            if (piece != null) {
                if (piece.black) {
                    if (piece.type == 'r') {
                        if (blackRookCounter == 0) {
                            pieces[counter] = BLACK_ROOK; pieces[counter].move(piece.x, piece.y);
                        }
                        else if (blackRookCounter == 1) {
                            pieces[counter] = BLACK_ROOK1; pieces[counter].move(piece.x, piece.y);
                        }
                        else {
                            pieces[counter] = new Rook(piece.x, piece.y, true);
                        }
                        blackRookCounter++;
                    } else if (piece.type == 'n') {
                        if (blackKnightCounter == 0) {
                            pieces[counter] = BLACK_KNIGHT; pieces[counter].move(piece.x, piece.y);
                        }
                        else if (blackKnightCounter == 1) {
                            pieces[counter] = BLACK_KNIGHT1; pieces[counter].move(piece.x, piece.y);
                        }
                        else {
                            pieces[counter] = new Knight(piece.x, piece.y, true);
                        }
                        blackKnightCounter++;
                    } else if (piece.type == 'b') {
                        if (blackBishopCounter == 0) {
                            pieces[counter] = BLACK_BISHOP; pieces[counter].move(piece.x, piece.y);
                        }
                        else if (blackBishopCounter == 1) {
                            pieces[counter] = BLACK_BISHOP1; pieces[counter].move(piece.x, piece.y);
                        }
                        else {
                            pieces[counter] = new Bishop(piece.x, piece.y, true);
                        }
                        blackBishopCounter++;
                    } else if (piece.type == 'q') {
                        if (blackQueenCounter == 0) {
                            pieces[counter] = BLACK_QUEEN; pieces[counter].move(piece.x, piece.y);
                        }
                        else if (blackQueenCounter == 1) {
                            pieces[counter] = BLACK_QUEEN1; pieces[counter].move(piece.x, piece.y);
                        }
                        else {
                            pieces[counter] = new Queen(piece.x, piece.y, true);
                        }
                        blackQueenCounter++;
                    } else if (piece.type == 'k') {
                        pieces[counter] = BLACK_KING;
                        pieces[counter].move(piece.x, piece.y);

                    } else if (piece.type == 'p') {
                        if (blackPawnCounter == 0) {
                            pieces[counter] = BLACK_PAWN; pieces[counter].move(piece.x, piece.y);
                        }
                        else if (blackPawnCounter == 1) {
                            pieces[counter] = BLACK_PAWN1; pieces[counter].move(piece.x, piece.y);
                        }
                        else if (blackPawnCounter == 2) {
                            pieces[counter] = BLACK_PAWN2; pieces[counter].move(piece.x, piece.y);
                        }
                        else if (blackPawnCounter == 3) {
                            pieces[counter] = BLACK_PAWN3; pieces[counter].move(piece.x, piece.y);
                        }
                        else if (blackPawnCounter == 4) {
                            pieces[counter] = BLACK_PAWN4; pieces[counter].move(piece.x, piece.y);
                        }
                        else if (blackPawnCounter == 5) {
                            pieces[counter] = BLACK_PAWN5; pieces[counter].move(piece.x, piece.y);
                        }
                        else if (blackPawnCounter == 6) {
                            pieces[counter] = BLACK_PAWN6; pieces[counter].move(piece.x, piece.y);
                        }
                        else if (blackPawnCounter == 7) {
                            pieces[counter] = BLACK_PAWN7; pieces[counter].move(piece.x, piece.y);
                        }
                        else {
                            pieces[counter] = new Pawn(piece.x, piece.y, true);
                        }
                        blackPawnCounter++;
                    }
                } else {
                    if (piece.type == 'r') {
                        if (whiteRookCounter == 0) {
                            pieces[counter] = WHITE_ROOK; pieces[counter].move(piece.x, piece.y);
                        }
                        else if (whiteRookCounter == 1) {
                            pieces[counter] = WHITE_ROOK1; pieces[counter].move(piece.x, piece.y);
                        }
                        else {
                            pieces[counter] = new Rook(piece.x, piece.y, false);
                        }
                        whiteRookCounter++;
                    } else if (piece.type == 'n') {
                        if (whiteKnightCounter == 0) {
                            pieces[counter] = WHITE_KNIGHT; pieces[counter].move(piece.x, piece.y);
                        }
                        else if (whiteKnightCounter == 1) {
                            pieces[counter] = WHITE_KNIGHT1; pieces[counter].move(piece.x, piece.y);
                        }
                        else {
                            pieces[counter] = new Knight(piece.x, piece.y, false);
                        }
                        whiteKnightCounter++;
                    } else if (piece.type == 'b') {
                        if (whiteBishopCounter == 0) {
                            pieces[counter] = WHITE_BISHOP; pieces[counter].move(piece.x, piece.y);
                        }
                        else if (whiteBishopCounter == 1) {
                            pieces[counter] = WHITE_BISHOP1; pieces[counter].move(piece.x, piece.y);
                        }
                        else {
                            pieces[counter] = new Bishop(piece.x, piece.y, false);
                        }
                        whiteBishopCounter++;
                    } else if (piece.type == 'q') {
                        if (whiteQueenCounter == 0) {
                            pieces[counter] = WHITE_QUEEN; pieces[counter].move(piece.x, piece.y);
                        }
                        else if (whiteQueenCounter == 1) {
                            pieces[counter] = WHITE_QUEEN1; pieces[counter].move(piece.x, piece.y);
                        }
                        else {
                            pieces[counter] = new Queen(piece.x, piece.y, false);
                        }
                        whiteQueenCounter++;
                    } else if (piece.type == 'k') {
                        pieces[counter] = WHITE_KING;
                        pieces[counter].move(piece.x, piece.y);

                    } else if (piece.type == 'p') {
                        if (whitePawnCounter == 0) {
                            pieces[counter] = WHITE_PAWN; pieces[counter].move(piece.x, piece.y);
                        }
                        else if (whitePawnCounter == 1) {
                            pieces[counter] = WHITE_PAWN1; pieces[counter].move(piece.x, piece.y);
                        }
                        else if (whitePawnCounter == 2) {
                            pieces[counter] = WHITE_PAWN2; pieces[counter].move(piece.x, piece.y);
                        }
                        else if (whitePawnCounter == 3) {
                            pieces[counter] = WHITE_PAWN3; pieces[counter].move(piece.x, piece.y);
                        }
                        else if (whitePawnCounter == 4) {
                            pieces[counter] = WHITE_PAWN4; pieces[counter].move(piece.x, piece.y);
                        }
                        else if (whitePawnCounter == 5) {
                            pieces[counter] = WHITE_PAWN5; pieces[counter].move(piece.x, piece.y);
                        }
                        else if (whitePawnCounter == 6) {
                            pieces[counter] = WHITE_PAWN6; pieces[counter].move(piece.x, piece.y);
                        }
                        else if (whitePawnCounter == 7) {
                            pieces[counter] = WHITE_PAWN7; pieces[counter].move(piece.x, piece.y);
                        }
                        else {
                            pieces[counter] = new Pawn(piece.x, piece.y, false);
                        }
                        whitePawnCounter++;
                    }
                }
                counter++;
            }
        }
    }
    public void copyPieces() {
        blackKnightCounter = 0;
        blackQueenCounter = 0;
        blackPawnCounter = 0;
        blackBishopCounter = 0;
        blackRookCounter = 0;
        whiteBishopCounter = 0;
        whiteKnightCounter = 0;
        whiteRookCounter = 0;
        whitePawnCounter = 0;
        whiteQueenCounter = 0;
        int counter = 0;
        voidPieces2();
        for (Piece piece: pieces) {
            if (piece != null) {
                if (piece.black) {
                    if (piece.type == 'r') {
                        if (blackRookCounter == 0) {
                            pieces2[counter] = BLACK_ROOK_SPARE; pieces2[counter].move(piece.x, piece.y);
                        }
                        else if (blackRookCounter == 1) {
                            pieces2[counter] = BLACK_ROOK_SPARE1; pieces2[counter].move(piece.x, piece.y);
                        }
                        else {
                            pieces2[counter] = new Rook(piece.x, piece.y, true);
                        }
                        blackRookCounter++;
                    } else if (piece.type == 'n') {
                        if (blackKnightCounter == 0) {
                            pieces2[counter] = BLACK_KNIGHT_SPARE; pieces2[counter].move(piece.x, piece.y);
                        }
                        else if (blackKnightCounter == 1) {
                            pieces2[counter] = BLACK_KNIGHT_SPARE1; pieces2[counter].move(piece.x, piece.y);
                        }
                        else {
                            pieces2[counter] = new Knight(piece.x, piece.y, true);
                        }
                        blackKnightCounter++;
                    } else if (piece.type == 'b') {
                        if (blackBishopCounter == 0) {
                            pieces2[counter] = BLACK_BISHOP_SPARE; pieces2[counter].move(piece.x, piece.y);
                        }
                        else if (blackBishopCounter == 1) {
                            pieces2[counter] = BLACK_BISHOP_SPARE1; pieces2[counter].move(piece.x, piece.y);
                        }
                        else {
                            pieces2[counter] = new Bishop(piece.x, piece.y, true);
                        }
                        blackBishopCounter++;
                    } else if (piece.type == 'q') {
                        if (blackQueenCounter == 0) {
                            pieces2[counter] = BLACK_QUEEN_SPARE; pieces2[counter].move(piece.x, piece.y);
                        }
                        else if (blackQueenCounter == 1) {
                            pieces2[counter] = BLACK_QUEEN_SPARE1; pieces2[counter].move(piece.x, piece.y);
                        }
                        else {
                            pieces2[counter] = new Queen(piece.x, piece.y, true);
                        }
                        blackQueenCounter++;
                    } else if (piece.type == 'k') {
                        pieces2[counter] = BLACK_KING_SPARE;
                        pieces2[counter].move(piece.x, piece.y);

                    } else if (piece.type == 'p') {
                        if (blackPawnCounter == 0) {
                            pieces2[counter] = BLACK_PAWN_SPARE; pieces2[counter].move(piece.x, piece.y);
                        }
                        else if (blackPawnCounter == 1) {
                            pieces2[counter] = BLACK_PAWN_SPARE1; pieces2[counter].move(piece.x, piece.y);
                        }
                        else if (blackPawnCounter == 2) {
                            pieces2[counter] = BLACK_PAWN_SPARE2; pieces2[counter].move(piece.x, piece.y);
                        }
                        else if (blackPawnCounter == 3) {
                            pieces2[counter] = BLACK_PAWN_SPARE3; pieces2[counter].move(piece.x, piece.y);
                        }
                        else if (blackPawnCounter == 4) {
                            pieces2[counter] = BLACK_PAWN_SPARE4; pieces2[counter].move(piece.x, piece.y);
                        }
                        else if (blackPawnCounter == 5) {
                            pieces2[counter] = BLACK_PAWN_SPARE5; pieces2[counter].move(piece.x, piece.y);
                        }
                        else if (blackPawnCounter == 6) {
                            pieces2[counter] = BLACK_PAWN_SPARE6; pieces2[counter].move(piece.x, piece.y);
                        }
                        else if (blackPawnCounter == 7) {
                            pieces2[counter] = BLACK_PAWN_SPARE7; pieces2[counter].move(piece.x, piece.y);
                        }
                        else {
                            pieces2[counter] = new Pawn(piece.x, piece.y, true);
                        }
                        blackPawnCounter++;
                    }
                } else {
                    if (piece.type == 'r') {
                        if (whiteRookCounter == 0) {
                            pieces2[counter] = WHITE_ROOK_SPARE; pieces2[counter].move(piece.x, piece.y);
                        }
                        else if (whiteRookCounter == 1) {
                            pieces2[counter] = WHITE_ROOK_SPARE1; pieces2[counter].move(piece.x, piece.y);
                        }
                        else {
                            pieces2[counter] = new Rook(piece.x, piece.y, false);
                        }
                        whiteRookCounter++;
                    } else if (piece.type == 'n') {
                        if (whiteKnightCounter == 0) {
                            pieces2[counter] = WHITE_KNIGHT_SPARE; pieces2[counter].move(piece.x, piece.y);
                        }
                        else if (whiteKnightCounter == 1) {
                            pieces2[counter] = WHITE_KNIGHT_SPARE1; pieces2[counter].move(piece.x, piece.y);
                        }
                        else {
                            pieces2[counter] = new Knight(piece.x, piece.y, false);
                        }
                        whiteKnightCounter++;
                    } else if (piece.type == 'b') {
                        if (whiteBishopCounter == 0) {
                            pieces2[counter] = WHITE_BISHOP_SPARE; pieces2[counter].move(piece.x, piece.y);
                        }
                        else if (whiteBishopCounter == 1) {
                            pieces2[counter] = WHITE_BISHOP_SPARE1; pieces2[counter].move(piece.x, piece.y);
                        }
                        else {
                            pieces2[counter] = new Bishop(piece.x, piece.y, false);
                        }
                        whiteBishopCounter++;
                    } else if (piece.type == 'q') {
                        if (whiteQueenCounter == 0) {
                            pieces2[counter] = WHITE_QUEEN_SPARE; pieces2[counter].move(piece.x, piece.y);
                        }
                        else if (whiteQueenCounter == 1) {
                            pieces2[counter] = WHITE_QUEEN_SPARE1; pieces2[counter].move(piece.x, piece.y);
                        }
                        else {
                            pieces2[counter] = new Queen(piece.x, piece.y, false);
                        }
                        whiteQueenCounter++;
                    } else if (piece.type == 'k') {
                        pieces2[counter] = WHITE_KING_SPARE;
                        pieces2[counter].move(piece.x, piece.y);

                    } else if (piece.type == 'p') {
                        if (whitePawnCounter == 0) {
                            pieces2[counter] = WHITE_PAWN_SPARE; pieces2[counter].move(piece.x, piece.y);
                        }
                        else if (whitePawnCounter == 1) {
                            pieces2[counter] = WHITE_PAWN_SPARE1; pieces2[counter].move(piece.x, piece.y);
                        }
                        else if (whitePawnCounter == 2) {
                            pieces2[counter] = WHITE_PAWN_SPARE2; pieces2[counter].move(piece.x, piece.y);
                        }
                        else if (whitePawnCounter == 3) {
                            pieces2[counter] = WHITE_PAWN_SPARE3; pieces2[counter].move(piece.x, piece.y);
                        }
                        else if (whitePawnCounter == 4) {
                            pieces2[counter] = WHITE_PAWN_SPARE4; pieces2[counter].move(piece.x, piece.y);
                        }
                        else if (whitePawnCounter == 5) {
                            pieces2[counter] = WHITE_PAWN_SPARE5; pieces2[counter].move(piece.x, piece.y);
                        }
                        else if (whitePawnCounter == 6) {
                            pieces2[counter] = WHITE_PAWN_SPARE6; pieces2[counter].move(piece.x, piece.y);
                        }
                        else if (whitePawnCounter == 7) {
                            pieces2[counter] = WHITE_PAWN_SPARE7; pieces2[counter].move(piece.x, piece.y);
                        }
                        else {
                            pieces2[counter] = new Pawn(piece.x, piece.y, false);
                        }
                        whitePawnCounter++;
                    }
                }
                counter++;
            }
        }
    }
    public void makeMove(int xStart, int yStart, int xEnd, int yEnd, int type) {
        /*move[0] = xStart;
        move[1] = yStart;
        move[2] = xEnd;
        move[3] = yEnd;
        move[4] = type;
        moves.push(move);
        if (isCapture) {
            capturedpush(pieceAtTheEnd);
        }
        else {
            capturedpush(null);
        }*/
        //boolean[] check = ruleCheck(xStart, yStart, xEnd, yEnd);

        FENStack.push(FEN);
        //positionCounter = 0;
        if (!FENsHashMap.containsKey(FENtrimmed)) {
            FENsHashMap.put(FENtrimmed, 1);
            positionCounter = 1;
        }
        else {
            FENsHashMap.put(FENtrimmed, FENsHashMap.get(FENtrimmed) + 1);
            positionCounter = FENsHashMap.get(FENtrimmed);
        }
        updateProperties(xStart, yStart, xEnd, yEnd, type);
        FEN = makeFEN();

    }

    public Game(String FEN) {
        FENReader(FEN);
        this.FEN = FEN;
    }
    public void debugPrint() {
        System.out.println(makeFEN());
    }
    public void unmakeMove() {
        FEN = FENStack.pop();
        FENReader(FEN);
        if (FENsHashMap.get(FENtrimmed) != null) {
            FENsHashMap.put(FENtrimmed, FENsHashMap.get(FENtrimmed) - 1);
        }
        /*move = moves.pop();
        c = capturedpop();
        movingPiece = findPiece(move[2], move[3]);
        movingPiece.move(move[0], move[1]);
        for (int i = 0; i < 32; i++) {
            if (pieces[i] == null) {
                pieces[i] = c;
            }
        }
        if (move[4] != -1) {
            for (int i = 0; i < 32; i++) {
                if (pieces[i].x == move[3] && pieces[i].y == move[4]) {
                    pieces[i] = new Pawn(move[3], move[4], pieces[i].black);
                    break;
                }
            }
        }
         */
    }

    public ArrayList<int[]> legalMoves() {
        ArrayList<int[]> legalMoves = new ArrayList<>();

        for (Piece c: pieces) {
            if (c != null) {
                if (c.black != sideMove) {
                    legalMoves.addAll(c.allPotentialMoves());
                }
            }
        }

        for (int i = 0; i < legalMoves.size(); i++) {
            move = legalMoves.get(i);
            if (!ruleCheck(move[0], move[1], move[2], move[3])[0]) {
                legalMoves.remove(i);
                i--;
            }
        }

        legalMoves.sort(new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return myComparator(o1, o2);
            }
        });
        return legalMoves;
    }
    public ArrayList<int[]> captureMoves() {
        ArrayList<int[]> captureMoves = new ArrayList<>();

        for (Piece c: pieces) {
            if (c != null) {
                if (c.black != sideMove) {
                    captureMoves.addAll(c.allPotentialMoves());
                }
            }
        }

        for (int i = 0; i < captureMoves.size(); i++) {
            move = captureMoves.get(i);
            if (!ruleCheck(move[0], move[1], move[2], move[3])[0] || !isCapture) {
                captureMoves.remove(i);
                i--;
            }
        }
        return captureMoves;
    }
    //ArrayList<String> FENs = new ArrayList<>();
    public int countLegalPositions(int depth) {
        ArrayList<int[]> moves;
        if (depth == 0) {
            //FENs.add(FEN);
            return 1;
        }
        int number1 = 0;
        moves = legalMoves();
        for (int[] move: moves) {
            makeMove(move[0], move[1], move[2], move[3], move[4]);
            number1 += countLegalPositions(depth - 1);
            unmakeMove();
        }
        return number1;
    }
    public boolean kingInCheck(boolean black) {
        if (black) {
            return kingInCheck(blackKingPosition[0], blackKingPosition[1], true);
        }
        else {
            return kingInCheck(whiteKingPosition[0], whiteKingPosition[1], false);
        }
    }


    /*private void makeAnOpponentAttackMap() {
        for (Piece c: pieces) {
            if (c != null) {
                if (c.black != sideMove) {
                    opponentAttackMap.addAll(c.allPotentialMoves());
                }
            }
        }

        for (int i = 0; i < opponentAttackMap.size(); i++) {
            move = opponentAttackMap.get(i);
            if (move.length == 5) {
                if (ruleCheck(move[0], move[1], move[2], move[3])[0]) {
                    square[0] = move[2];
                    square[1] = move[3];
                    opponentAttackMap.add(square.clone());
                }
                opponentAttackMap.remove(i);
                i--;
            }
        }

    }

     */
    public boolean isCapture(int x, int y) {
        return checkPiece(x, y);
    }
    private int myComparator (int[] move1, int[] move2) {
        int evaluation1 = 0;
        int evaluation2 = 0;
        if (isCapture(move1[2], move1[3])) {
            evaluation1 += 2;
        }
        if (isCapture(move2[2], move2[3])) {
            evaluation2 += 2;
        }
        /*
        square[0] = move1[2];
        square[1] = move1[3];
        if (opponentAttackMap.contains(square)) {
            evaluation1--;
        }
        square[0] = move2[2];
        square[1] = move2[3];
        if (opponentAttackMap.contains(square)) {
            evaluation2--;
        }

         */
        if (move1[4] != -1 && ((move1[1] == 1 && move1[3] == 0) || (move1[1] == 6 && move1[3] == 7) || (move1[1] == 3 || move1[1] == 4))) {
            evaluation1++;
        }
        if (move2[4] != -1 && ((move2[1] == 1 && move2[3] == 0) || (move2[1] == 6 && move2[3] == 7) || (move2[1] == 3 || move2[1] == 4))) {
            evaluation2++;
        }
        return -Integer.compare(evaluation1, evaluation2);
    }
    public  Piece BLACK_PAWN = new Pawn(-1, -1, true);
    public  Piece BLACK_PAWN1 = new Pawn(-1, -1, true);
    public  Piece BLACK_PAWN2 = new Pawn(-1, -1, true);
    public  Piece BLACK_PAWN3 = new Pawn(-1, -1, true);
    public  Piece BLACK_PAWN4 = new Pawn(-1, -1, true);
    public  Piece BLACK_PAWN5 = new Pawn(-1, -1, true);
    public  Piece BLACK_PAWN6 = new Pawn(-1, -1, true);
    public  Piece BLACK_PAWN7 = new Pawn(-1, -1, true);
    public  Piece BLACK_ROOK = new Rook(-1, -1, true);
    public  Piece BLACK_ROOK1 = new Rook(-1, -1, true);
    public  Piece BLACK_KNIGHT = new Knight(-1, -1, true);
    public  Piece BLACK_KNIGHT1 = new Knight(-1, -1, true);
    public  Piece BLACK_BISHOP = new Bishop(-1, -1, true);
    public  Piece BLACK_BISHOP1 = new Bishop(-1, -1, true);
    public  Piece BLACK_QUEEN = new Queen(-1, -1, true);
    public  Piece BLACK_QUEEN1 = new Queen(-1, -1, true);
    public  Piece BLACK_KING = new King(-1, -1, true);
    // __________________________________________________________
    public  Piece WHITE_PAWN = new Pawn(-1, -1, false);
    public  Piece WHITE_PAWN1 = new Pawn(-1, -1, false);
    public  Piece WHITE_PAWN2 = new Pawn(-1, -1, false);
    public  Piece WHITE_PAWN3 = new Pawn(-1, -1, false);
    public  Piece WHITE_PAWN4 = new Pawn(-1, -1, false);
    public  Piece WHITE_PAWN5 = new Pawn(-1, -1, false);
    public  Piece WHITE_PAWN6 = new Pawn(-1, -1, false);
    public  Piece WHITE_PAWN7 = new Pawn(-1, -1, false);

    public  Piece WHITE_ROOK = new Rook(-1, -1, false);
    public  Piece WHITE_ROOK1 = new Rook(-1, -1, false);
    public  Piece WHITE_KNIGHT = new Knight(-1, -1, false);
    public  Piece WHITE_KNIGHT1 = new Knight(-1, -1, false);
    public  Piece WHITE_BISHOP = new Bishop(-1, -1, false);
    public  Piece WHITE_BISHOP1 = new Bishop(-1, -1, false);
    public  Piece WHITE_QUEEN = new Queen(-1, -1, false);
    public  Piece WHITE_QUEEN1 = new Queen(-1, -1, false);
    public  Piece WHITE_KING = new King(-1, -1, false);
    // __________________________________________________________
    public  Piece BLACK_PAWN_SPARE = new Pawn(-1, -1, true);
    public  Piece BLACK_PAWN_SPARE1 = new Pawn(-1, -1, true);
    public  Piece BLACK_PAWN_SPARE2 = new Pawn(-1, -1, true);
    public  Piece BLACK_PAWN_SPARE3 = new Pawn(-1, -1, true);
    public  Piece BLACK_PAWN_SPARE4 = new Pawn(-1, -1, true);
    public  Piece BLACK_PAWN_SPARE5 = new Pawn(-1, -1, true);
    public  Piece BLACK_PAWN_SPARE6 = new Pawn(-1, -1, true);
    public  Piece BLACK_PAWN_SPARE7 = new Pawn(-1, -1, true);


    public  Piece BLACK_ROOK_SPARE = new Rook(-1, -1, true);
    public  Piece BLACK_ROOK_SPARE1 = new Rook(-1, -1, true);
    public  Piece BLACK_KNIGHT_SPARE = new Knight(-1, -1, true);
    public  Piece BLACK_KNIGHT_SPARE1 = new Knight(-1, -1, true);
    public  Piece BLACK_BISHOP_SPARE = new Bishop(-1, -1, true);
    public  Piece BLACK_BISHOP_SPARE1 = new Bishop(-1, -1, true);
    public  Piece BLACK_QUEEN_SPARE = new Queen(-1, -1, true);
    public  Piece BLACK_QUEEN_SPARE1 = new Queen(-1, -1, true);
    public  Piece BLACK_KING_SPARE = new King(-1, -1, true);
    // __________________________________________________________
    public  Piece WHITE_PAWN_SPARE = new Pawn(-1, -1, false);
    public  Piece WHITE_PAWN_SPARE1 = new Pawn(-1, -1, false);
    public  Piece WHITE_PAWN_SPARE2 = new Pawn(-1, -1, false);
    public  Piece WHITE_PAWN_SPARE3 = new Pawn(-1, -1, false);
    public  Piece WHITE_PAWN_SPARE4 = new Pawn(-1, -1, false);
    public  Piece WHITE_PAWN_SPARE5 = new Pawn(-1, -1, false);
    public  Piece WHITE_PAWN_SPARE6 = new Pawn(-1, -1, false);
    public  Piece WHITE_PAWN_SPARE7 = new Pawn(-1, -1, false);

    public  Piece WHITE_ROOK_SPARE = new Rook(-1, -1, false);
    public  Piece WHITE_ROOK_SPARE1 = new Rook(-1, -1, false);
    public  Piece WHITE_KNIGHT_SPARE = new Knight(-1, -1, false);
    public  Piece WHITE_KNIGHT_SPARE1 = new Knight(-1, -1, false);
    public  Piece WHITE_BISHOP_SPARE = new Bishop(-1, -1, false);
    public  Piece WHITE_BISHOP_SPARE1 = new Bishop(-1, -1, false);
    public  Piece WHITE_QUEEN_SPARE = new Queen(-1, -1, false);
    public  Piece WHITE_QUEEN_SPARE1 = new Queen(-1, -1, false);
    public  Piece WHITE_KING_SPARE = new King(-1, -1, false);

}
