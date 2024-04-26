import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JPanel {
    private char[][] maze;

    public GUI(char[][] maze) {
        this.maze = maze;
        setPreferredSize(new Dimension(200, 200));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int cellSize = Math.min(getWidth() / maze[0].length, getHeight() / maze.length);
                int row = e.getY() / cellSize;
                int col = e.getX() / cellSize;

                if ((row == 0 || row == maze.length - 1 || col == 0 || col == maze[0].length - 1)
                        && !(row == 0 && col == 0)
                        && !(row == 0 && col == maze[0].length - 1)
                        && !(row == maze.length - 1 && col == 0)
                        && !(row == maze.length - 1 && col == maze[0].length - 1)
                        && (row%2 != 0 || col%2 != 0)) {

                    for (int i = 0; i < maze.length; i++) {
                        for (int j = 0; j < maze[i].length; j++) {
                            if (maze[i][j] == '.') {
                                maze[i][j] = ' ';
                            }
                        }
                    }

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

                    if (maze[row][col] != 'P' && maze[row][col] != 'K') {
                        if (e.isShiftDown()) {
                            maze[row][col] = 'K';
                        } else {
                            maze[row][col] = 'P';
                        }
                    }
                    repaint();
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
                } else if (maze[i][j] == '.' && !(maze[i][j] == 'P' || maze[i][j] == 'K')) {
                    g.setColor(Color.YELLOW);
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
                        g.setColor(Color.YELLOW);
                    }
                }
                g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }
    }
}
