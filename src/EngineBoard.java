public class EngineBoard {
    public static void main(String[] args) {
        Board board = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", Board.ENGINE_DISABLED);
        Engine engine = new Engine("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        //System.out.println("--1");
        while (true) {
            //System.out.println("--2");
            try {
                Thread.sleep(1000);
            } catch (Exception e) {

            }
            if (!board.position.isSideMove()) {
                //int[] move = engine.returnMove2(board.position);
                //board.makeMove(move[0], move[1], move[2], move[3], move[4]);
                engine.recursiveEvaluation(4, -15000, 15000, 4);
                int[] move = engine.returnMove();
                System.out.println(move[0] + " " + move[1] + " " + move[2] + " " + move[3] + " " + move[4]);
                board.makeMove(move[0], move[1], move[2], move[3], move[4]);
                engine.makeMove(move);

            }
            else {
                int[] move = engine.returnMove2(board.position);
                board.makeMove(move[0], move[1], move[2], move[3], move[4]);
                System.out.println(move[0] + " " + move[1] + " " + move[2] + " " + move[3] + " " + move[4]);
                engine.makeMove(move);
            }
            System.out.println(board.position.getFEN());
        }
    }
}