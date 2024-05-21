import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MazeToImage {
    public static void saveMazeToImage(char[][] maze, String filename) {
        int maxImageSize = 2050;
        int cellSize = Math.max(maxImageSize / Math.max(maze.length, maze[0].length), 10);

        int imageWidth = cellSize * maze[0].length;
        int imageHeight = cellSize * maze.length;

        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();



        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                Color color;
                switch (maze[i][j]) {
                    case 'X':
                        color = Color.BLACK; // ściana
                        break;
                    case ' ':
                        color = Color.WHITE; // ścieżka
                        break;
                    case 'P':
                        color = Color.GREEN; // punkt startowy
                        break;
                    case 'K':
                        color = Color.RED; // punkt końcowy
                        break;
                    case '.':
                        color = Color.YELLOW; // ścieżka rozwiązania
                        break;
                    default:
                        color = Color.WHITE;
                }
                int count = 0;
                if(maze[i][j] != 'X') {
                    if (i > 0 && maze[i - 1][j] == '.') {
                        count++;
                    }
                    if (i < maze.length - 1 && maze[i + 1][j] == '.') {
                        count++;
                    }
                    if (j > 0 && maze[i][j - 1] == '.') {
                        count++;
                    }
                    if (j < maze[i].length - 1 && maze[i][j + 1] == '.') {
                        count++;
                    }
                    if (count >= 2) {
                        graphics.setColor(Color.YELLOW);
                        graphics.fillRect(j * cellSize, i*cellSize, cellSize, cellSize);
                        continue;
                    }
                }
                graphics.setColor(color);
                graphics.fillRect(j * cellSize, i*cellSize, cellSize, cellSize);
            }
        }

        graphics.dispose();

        try {
            ImageIO.write(image, "png", new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}