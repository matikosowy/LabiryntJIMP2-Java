import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JPanel {
    private char[][] maze;

    public GUI(char[][] maze) {
        this.maze = maze;
        setPreferredSize(new Dimension(200, 200)); // Set the preferred panel size

        // Add mouse listener
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int cellSize = Math.min(getWidth() / maze[0].length, getHeight() / maze.length);
                int row = e.getY() / cellSize;
                int col = e.getX() / cellSize;

                // Check if the clicked cell is on the border and not a corner
                if ((row == 0 || row == maze.length - 1 || col == 0 || col == maze[0].length - 1)
                        && !(row == 0 && col == 0)
                        && !(row == 0 && col == maze[0].length - 1)
                        && !(row == maze.length - 1 && col == 0)
                        && !(row == maze.length - 1 && col == maze[0].length - 1)) {

                    // Before setting a new 'P' or 'K', replace any existing 'P' or 'K' with 'X'
                    if(maze[row][col]!= 'P' && maze[row][col]!= 'K') {
                        for (int i = 0; i < maze.length; i++) {
                            for (int j = 0; j < maze[i].length; j++) {
                                if (e.isShiftDown() && maze[i][j] == 'K') {
                                    maze[i][j] = 'X';
                                } else if (!e.isShiftDown() && maze[i][j] == 'P') {
                                    maze[i][j] = 'X';
                                }
                            }
                        }
                    }

                    // Change the cell to 'P' or 'K' based on the currently selected key
                    // Only if the cell is not already 'P' or 'K'
                    if (maze[row][col] != 'P' && maze[row][col] != 'K') {
                        if (e.isShiftDown()) {
                            maze[row][col] = 'K'; // Set 'K' for Shift+click
                        } else {
                            maze[row][col] = 'P'; // Set 'P' for normal click
                        }
                    }
                    repaint(); // Repaint the panel to reflect the changes
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (maze == null) {
            return;
        }

        int cellSize = Math.min(getWidth() / maze[0].length, getHeight() / maze.length);

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

                g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }
    }
}
