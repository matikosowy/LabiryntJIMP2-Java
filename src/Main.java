import java.io.IOException;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        String filePath = "src/maze.txt";
        int rows = 0;
        int columns = 0;
        try {
            columns = Input.liczKolumny(filePath);
            System.out.println("Liczba kolumn: " + columns);
        } catch (Exception e) {
            System.out.println("Nie udało się odczytać pliku");
        }

        try {
            rows = Input.liczWiersze(filePath);
            System.out.println("Liczba wierszy: " + rows);
        } catch (Exception e) {
            System.out.println("Nie udało się odczytać pliku");
        }

        String binaryFilePath = "src/maze.bin";

        try {
            Input.binaryToText(binaryFilePath);
        } catch (IOException e) {
            System.out.println("Failed to convert binary file to text");
        }

        char[][] maze;

        try {
            maze = Input.readMaze(filePath, rows, columns);
        } catch (Exception e) {
            System.out.println("Nie udało się odczytać pliku");
            maze = new char[rows][columns]; // Assign a default value in case of an exception
        }

        final char[][] finalMaze = maze;

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Maze Display");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new GUI(finalMaze)); // Use the effectively final variable here
            frame.pack();
            frame.setLocationRelativeTo(null); // Wyśrodkowanie okna
            frame.setVisible(true);
        });
    }
}