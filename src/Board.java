import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.util.Scanner;

public class Board extends JFrame{
    Game position;
    private int clickCounter = 0;
    private int xStart = 0;
    private int yStart = 0;
    private int xEnd = 0;
    private int yEnd = 0;
    private int pawn = -1;
    private static Container chessBoard;
    //private static Container promotionDialogue = new Container();
    private JButton[][] squares = new JButton[8][8];
    private JButton[] promotionChoices = new JButton[4];
    private JButton[] promotionChoicesBlack = new JButton[4];
    private JDialog promotionDialogue = new JDialog();
    private JDialog promotionDialogueBlack = new JDialog();
    private JPanel buttonPanel = new JPanel();
    private JButton[] bottomButtons = new JButton[4];
    private Engine engine;
    //private Engine engine = new Engine();
    private Color colour = Color.DARK_GRAY;
    public static boolean ENGINE_ENABLED = true;
    public static boolean ENGINE_DISABLED = false;
    private boolean engineEnabled;
    private int AUTOQUEEN = 0;
    public int[] getMove() {
        return new int[] {xStart, yStart, xEnd, yEnd};
    }

    //Scanner scanner = new Scanner();
    public Board(String FEN, boolean engineEnabled) {
        super("chess board");
        this.engineEnabled = engineEnabled;
        position = new Game(FEN);
        setLayout(new BorderLayout());
        chessBoard = new JPanel();
        chessBoard.setLayout(new GridLayout(8, 8));
        if (engineEnabled) {
            engine = new Engine(FEN);
        }
        //promotionDialogue.setLayout(new GridLayout(4,1));
        //setLayout(new GridLayout(8, 8));
        ActionListener1 buttonHandler = new ActionListener1();
        for (int i = 7; i > -1; i--) {
            for (int j = 0; j < 8; j++) {
                squares[i][j] = new JButton();
                if ( (i + j) % 2 == 0 ) {
                    squares[i][j].setBackground(colour);
                }
                else {
                    squares[i][j].setBackground(Color.WHITE);
                }
                squares[i][j].setOpaque(true);
                squares[i][j].setBorderPainted(false);
                chessBoard.add(squares[i][j]);
                //add(squares[i][j]);
                squares[i][j].addActionListener(buttonHandler);

            }
        }
        for (int i = 0; i < 4; i++) {
            bottomButtons[i] = new JButton();
            bottomButtons[i].addActionListener(new bottomButtonsHandler());
            buttonPanel.add(bottomButtons[i]);
        }
        for (int i = 0; i < 4; i++) {
            promotionChoices[i] = new JButton();
            promotionChoices[i].addActionListener(new PromotionButtonHandler());
            promotionDialogue.add(promotionChoices[i]);
        }
        for (int i = 0; i < 4; i++) {
            promotionChoicesBlack[i] = new JButton();
            promotionChoicesBlack[i].addActionListener(new PromotionButtonHandler());
            promotionDialogueBlack.add(promotionChoicesBlack[i]);
        }

        promotionChoices[0].setIcon(Icons.whiteQueen);
        promotionChoices[1].setIcon(Icons.whiteRook);
        promotionChoices[2].setIcon(Icons.whiteBishop);
        promotionChoices[3].setIcon(Icons.whiteKnight);
        promotionChoicesBlack[0].setIcon(Icons.blackQueen);
        promotionChoicesBlack[1].setIcon(Icons.blackRook);
        promotionChoicesBlack[2].setIcon(Icons.blackBishop);
        promotionChoicesBlack[3].setIcon(Icons.blackKnight);

        //promotionDialogue.setSize(100, 400);
        //promotionDialogue.setName("Choose piece");

        add(chessBoard, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        chessBoard.setSize(800, 800);
        setSize(800, 820);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPieces(position.getPieces());
        setVisible(true);
        while (this.engineEnabled) { // position.legalMoves().size() != 0
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
            System.out.println(this.engineEnabled);
            if (!position.isSideMove()) {
                engine.recursiveEvaluation(4, -15000, 15000, 4);
                int[] move = engine.returnMove();
                System.out.println(move[0] + " " + move[1] + " " + move[2] + " " + move[3] + " " + move[4]);
                makeMove(move[0], move[1], move[2], move[3], move[4]);
                engine.makeMove(move);
            }
        }
    }
    private void resetPieces() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squares[i][j].setIcon(Icons.emptySquare);
            }
        }
        setPieces(position.getPieces());
    }
    public void makeMove (int xStart, int yStart, int xEnd, int yEnd, int pawn) {
        boolean[] moveLegality = position.ruleCheck(xStart, yStart, xEnd, yEnd);

        if (moveLegality[0]) {

            if (moveLegality[1] && pawn == -1) {
                if (position.isSideMove()) {
                    JOptionPane.showOptionDialog(chessBoard, "", "Chose pieces", JOptionPane.DEFAULT_OPTION, -1, Icons.whiteKing, promotionChoices, null);
                }
                else {
                    JOptionPane.showOptionDialog(chessBoard, "", "Chose pieces", JOptionPane.DEFAULT_OPTION, -1, Icons.blackKing, promotionChoicesBlack, null);
                }

            }
            else {
                this.pawn = pawn;
            }
            if (engineEnabled) {
                if (position.isSideMove()) {
                    engine.makeMove(xStart, yStart, xEnd, yEnd, pawn);
                }
            }
            position.makeMove(xStart, yStart, xEnd, yEnd, this.pawn);
            //position.updatePieceList();
            resetPieces();
        }
        else {
            System.out.println("Illegal move");
            //position.voidNewList();
        }
    }
    private class ActionListener1 implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e == null) {
                return;
            }
            Object source = e.getSource();
            for (int i = 7; i > -1; i--) {
                for (int j = 0; j < 8; j++) {
                    if (source == squares[i][j]) {
                        System.out.print("x = " + j + " y = " + i + "\n");
                        clickCounter = (clickCounter + 1) % 2;
                        if (clickCounter == 1) {
                            xStart = j;
                            yStart = i;
                        } else {
                            xEnd = j;
                            yEnd = i;
                            makeMove(xStart, yStart, xEnd, yEnd, -1);
                            position.debugPrint();

                            return;
                        }
                    }
                }
            }
        }
    }
    private class PromotionButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e == null) {
                return;
            }
            Object source = e.getSource();
            for (int i = 0; i < 4; i++) {
                if (source == promotionChoices[i]) {
                    pawn = i;
                    //promotionDialogue.
                }
                if (source == promotionChoicesBlack[i]) {
                    pawn = i;
                }
            }

        }
    }
    private class bottomButtonsHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e == null) {
                return;
            }
            Object source = e.getSource();
            if (source == bottomButtons[0]) {
                position.unmakeMove();
                resetPieces();
            }
            if (source == bottomButtons[3]) {
                engineEnabled = false;
            }
        }
    }
    private void setPieces(Piece[] pieces) {
        for (var i = 0; i < 8; i++) {
            for (var j = 0; j < 8; j++) {
                squares[i][j].setIcon(Icons.emptySquare);
            }
        }

        for (Piece piece: pieces) {
            if (piece != null && piece.x != -1) {
                if (piece.black) {
                    if (piece.type == 'r') {
                        squares[piece.y][piece.x].setIcon(Icons.blackRook);
                    } else if (piece.type == 'n') {
                        squares[piece.y][piece.x].setIcon(Icons.blackKnight);
                    } else if (piece.type == 'b') {
                        squares[piece.y][piece.x].setIcon(Icons.blackBishop);
                    } else if (piece.type == 'q') {
                        squares[piece.y][piece.x].setIcon(Icons.blackQueen);
                    } else if (piece.type == 'k') {
                        squares[piece.y][piece.x].setIcon(Icons.blackKing);
                    } else if (piece.type == 'p') {
                        squares[piece.y][piece.x].setIcon(Icons.blackPawn);
                    }
                }
                else {
                    if (piece.type == 'r') {
                        squares[piece.y][piece.x].setIcon(Icons.whiteRook);
                    } else if (piece.type == 'n') {
                        squares[piece.y][piece.x].setIcon(Icons.whiteKnight);
                    } else if (piece.type == 'b') {
                        squares[piece.y][piece.x].setIcon(Icons.whiteBishop);
                    } else if (piece.type == 'q') {
                        squares[piece.y][piece.x].setIcon(Icons.whiteQueen);
                    } else if (piece.type == 'k') {
                        squares[piece.y][piece.x].setIcon(Icons.whiteKing);
                    } else if (piece.type == 'p') {
                        squares[piece.y][piece.x].setIcon(Icons.whitePawn);
                    }
                }
            }
        }
    }
    public static void main(String[] args) {
        Board chessBoard = new Board("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8", ENGINE_DISABLED);


    }
}
