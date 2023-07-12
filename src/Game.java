import java.util.*;

import static java.lang.Math.*;

public class Game {
    //private ArrayList<int[]> legalMoves = new ArrayList<>();
    private ArrayList<int[]> opponentAttackMap = new ArrayList<>();
    //private Stack<Piece> capturedPieces = new Stack<>();
    //private Stack<int[]> moves = new Stack<>();
    private Stack<String> FENStack = new Stack<>();
    private Piece[] pieces = new Piece[32];
    private Piece[] pieces2 = new Piece[32];
    private String FEN;
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
        int x = 0;
        int y = 7;
        int pieceCounter = 0;
        char[] pieces = {'r', 'n', 'b', 'q', 'k', 'p', 'R', 'N', 'B', 'Q', 'K', 'P'};
        int spaceCounter = 0;
        StringBuilder s = new StringBuilder();
        StringBuilder s1 = new StringBuilder();
        castlingAvailability = new boolean[] {false, false, false, false};
        for (char i: FEN.toCharArray()) {
            if (contains(pieces, i) && spaceCounter == 0) {
                create(i, pieceCounter, x, y);
                pieceCounter++;
            }
            else if (i < 57 && i > 47) {
                x += Integer.parseInt(String.valueOf(i)) - 1;
            }
            if (i == '/') {x--; y--;}
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
        halfMoveCounter = Integer.parseInt(s.reverse().toString().trim());
        fullMoveCounter = Integer.parseInt(s1.reverse().toString().trim());
    }
    private boolean contains(char[] array, char a) {
        for (char i: array) {
            if (i == a) return true;
        }
        return false;
    }
    private void create(char piece, int pieceCounter, int x, int y) {
        if (piece == 'r') {pieces[pieceCounter] = new Rook(x, y, true); /*pieces[pieceCounter].move(x, y);*/}
        else if (piece == 'n') {pieces[pieceCounter] = new Knight(x, y, true);/*pieces[pieceCounter].move(x, y);*/}
        else if (piece == 'b') {pieces[pieceCounter] = new Bishop(x, y, true); /*pieces[pieceCounter].move(x, y);*/}
        else if (piece == 'q') {pieces[pieceCounter] = new Queen(x, y, true); /*pieces[pieceCounter].move(x, y);*/}
        else if (piece == 'k') {pieces[pieceCounter] = new King(x, y, true); blackKingPosition[0] = x; blackKingPosition[1] = y; blackKingPosition2[0] = x; blackKingPosition2[1] = y;}
        else if (piece == 'p') {pieces[pieceCounter] = new Pawn(x, y, true); /*pieces[pieceCounter].move(x, y);*/}
        else if (piece == 'R') {pieces[pieceCounter] = new Rook(x, y, false); /*pieces[pieceCounter].move(x, y);*/}
        else if (piece == 'N') {pieces[pieceCounter] = new Knight(x, y, false); /*pieces[pieceCounter].move(x, y);*/}
        else if (piece == 'B') {pieces[pieceCounter] = new Bishop(x, y, false); /*pieces[pieceCounter].move(x, y);*/}
        else if (piece == 'Q') {pieces[pieceCounter] = new Queen(x, y, false); /*pieces[pieceCounter].move(x, y);*/}
        else if (piece == 'K') {pieces[pieceCounter] = new King(x, y, false); whiteKingPosition[0] = x; whiteKingPosition[1] = y; whiteKingPosition2[0] = x; whiteKingPosition2[1] = y;}
        else if (piece == 'P') {pieces[pieceCounter] = new Pawn(x, y, false); /*pieces[pieceCounter].move(x, y);*/}
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
        int slashCounter = 0;
        int nonNullCounter = 0;
        int counter = 0;
        StringBuilder s = new StringBuilder();
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
        makeAnOpponentAttackMap();
        sideMove = !sideMove;
    }
    public void returnThePieces() {
        int counter = 0;
        voidPieces();
        for (Piece piece: pieces2) {
            if (piece != null) {
                if (piece.black) {
                    if (piece.type == 'r') {
                        pieces[counter] = new Rook(piece.x, piece.y, true);
                        //pieces[counter].move(piece.x, piece.y);
                    } else if (piece.type == 'n') {
                        pieces[counter] = new Knight(piece.x, piece.y, true);
                        //pieces[counter].move(piece.x, piece.y);
                    } else if (piece.type == 'b') {
                        pieces[counter] = new Bishop(piece.x, piece.y, true);
                        //pieces[counter].move(piece.x, piece.y);
                    } else if (piece.type == 'q') {
                        pieces[counter] = new Queen(piece.x, piece.y, true);
                    } else if (piece.type == 'k') {
                        pieces[counter] = new King(piece.x, piece.y, true);
                    } else if (piece.type == 'p') {
                        pieces[counter] = new Pawn(piece.x, piece.y, true);
                    }
                } else {
                    if (piece.type == 'r') {
                        pieces[counter] = new Rook(piece.x,piece.y, false);
                    } else if (piece.type == 'n') {
                        pieces[counter] = new Knight(piece.x,piece.y, false);
                    } else if (piece.type == 'b') {
                        pieces[counter] = new Bishop(piece.x,piece.y, false);
                    } else if (piece.type == 'q') {
                        pieces[counter] = new Queen(piece.x,piece.y, false);
                    } else if (piece.type == 'k') {
                        pieces[counter] = new King(piece.x,piece.y, false);
                    } else if (piece.type == 'p') {
                        pieces[counter] = new Pawn(piece.x,piece.y, false);
                    }
                }
                counter++;
            }
        }
    }
    public void copyPieces() {
        int counter = 0;
        voidPieces2();
        for (Piece piece: pieces) {
            if (piece != null) {
                if (piece.black) {
                    if (piece.type == 'r') {
                        pieces2[counter] = new Rook(piece.x, piece.y, true);
                    } else if (piece.type == 'n') {
                        pieces2[counter] = new Knight(piece.x, piece.y, true);
                    } else if (piece.type == 'b') {
                        pieces2[counter] = new Bishop(piece.x, piece.y, true);
                    } else if (piece.type == 'q') {
                        pieces2[counter] = new Queen(piece.x, piece.y, true);
                    } else if (piece.type == 'k') {
                        pieces2[counter] = new King(piece.x, piece.y, true);
                    } else if (piece.type == 'p') {
                        pieces2[counter] = new Pawn(piece.x, piece.y, true);
                    }
                } else {
                    if (piece.type == 'r') {
                        pieces2[counter] = new Rook(piece.x, piece.y, false);
                    } else if (piece.type == 'n') {
                        pieces2[counter] = new Knight(piece.x, piece.y, false);
                    } else if (piece.type == 'b') {
                        pieces2[counter] = new Bishop(piece.x, piece.y, false);
                    } else if (piece.type == 'q') {
                        pieces2[counter] = new Queen(piece.x, piece.y, false);
                    } else if (piece.type == 'k') {
                        pieces2[counter] = new King(piece.x, piece.y, false);
                    } else if (piece.type == 'p') {
                        pieces2[counter] = new Pawn(piece.x, piece.y, false);
                    }
                }
                counter++;
            }
        }
    }
    public void makeMove(int xStart, int yStart, int xEnd, int yEnd, int type) {
        //ruleCheck(xStart, yStart, xEnd, yEnd);
        /*move[0] = xStart;
        move[1] = yStart;
        move[2] = xEnd;
        move[3] = yEnd;
        move[4] = type;
        moves.push(move);
        if (isCapture) {
            capturedPieces.push(pieceAtTheEnd);
        }
        else {
            capturedPieces.push(null);
        }*/
        FENStack.push(FEN);
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
        /*move = moves.pop();
        c = capturedPieces.pop();
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
    ArrayList<String> FENs = new ArrayList<>();
    public int countLegalPositions(int depth) {
        ArrayList<int[]> moves;
        if (depth == 0) {
            FENs.add(FEN);
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
    private void makeAnOpponentAttackMap() {
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
    private boolean isCapture(int x, int y) {
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
        if (move1[4] != -1 && ((move1[1] == 1 && move1[3] == 0) || (move1[1] == 6 && move1[3] == 7))) {
            evaluation1++;
        }
        if (move2[4] != -1 && ((move1[1] == 1 && move1[3] == 0) || (move1[1] == 6 && move1[3] == 7))) {
            evaluation2++;
        }
        return -Integer.compare(evaluation1, evaluation2);
    }

}
