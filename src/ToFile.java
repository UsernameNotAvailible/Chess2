import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class ToFile {
}

class CreateFile {
    public static void main(String[] args) {
        try {
            File myObj = new File("FENStrings.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
class WriteToFile {
    public static void main(String[] args) {
        var pos = new Game("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        int number = pos.countLegalPositions(4);
        try {
            FileWriter myWriter = new FileWriter("FENStrings.txt");
            HashMap<String, Integer> repetitions = new HashMap<String, Integer>();

            for (String fen: pos.FENs) {
                if (!repetitions.containsKey(fen)) {
                    repetitions.put(fen, 1);
                }
            }
            for (String FEN : pos.FENs) {
                myWriter.write(FEN + "\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
