import javax.swing.*;
import java.awt.*;

public class GUI extends JPanel {
    private char[][] maze;

    public GUI(char[][] maze) {
        this.maze = maze;
        setPreferredSize(new Dimension(200, 200)); // Ustawienie preferowanej wielkości panelu
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (maze == null) {
            return;
        }

        int cellSize = Math.min(getWidth() / maze[0].length, getHeight() / maze.length); // Rozmiar komórki labiryntu

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == 'X') {
                    g.setColor(Color.BLACK);
                } else if (maze[i][j] == ' ') {
                    g.setColor(Color.WHITE);
                } else if (maze[i][j] == 'P') {
                    g.setColor(Color.GREEN);
                } else if (maze[i][j] == 'K') {
                    g.setColor(Color.RED);
                }

                g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize); // Rysowanie komórki
            }
        }
    }
}