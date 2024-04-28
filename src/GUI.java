import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JPanel {
    private char[][] maze;
    private double zoom = 1.0;
    private Point offset = new Point();
    private Point lastPoint;

    public GUI(char[][] maze) {
        this.maze = maze;

        // Zoomowanie labiryntu
        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double oldZoom = zoom;
                if (e.getWheelRotation() < 0) {
                    zoom *= 1.1;
                } else if (e.getWheelRotation() > 0) {
                    zoom /= 1.1;
                }

                offset.x += e.getX() * (1/oldZoom - 1/zoom);
                offset.y += e.getY() * (1/oldZoom - 1/zoom);
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastPoint = e.getPoint();
            }

            @Override
            // Ustawianie punktów startowego i końcowego
            public void mouseClicked(MouseEvent e) {
                int cellSize = (int) (Math.min(getWidth() / maze[0].length, getHeight() / maze.length) * zoom);
                int row = (int) ((e.getY() - offset.y) / cellSize);
                int col = (int) ((e.getX() - offset.x) / cellSize);

                // Musi być na krawędzi labiryntu, nie może być w rogu, musi być w koordynatach nieparzystych
                if ((row == 0 || row == maze.length - 1 || col == 0 || col == maze[0].length - 1)
                        && !(row == 0 && col == 0)
                        && !(row == 0 && col == maze[0].length - 1)
                        && !(row == maze.length - 1 && col == 0)
                        && !(row == maze.length - 1 && col == maze[0].length - 1)
                        && (row%2 != 0 || col%2 != 0)) {

                    // Usuń scieżkę przy zmianie P i K
                    for (int i = 0; i < maze.length; i++) {
                        for (int j = 0; j < maze[i].length; j++) {
                            if (maze[i][j] == '.') {
                                maze[i][j] = ' ';
                            }
                        }
                    }

                    // Usuń stare P lub K
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

                    // Ustawianie nowego P lub K (z shiftem)
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

        // Przesuwanie labiryntu
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point point = e.getPoint();
                offset.x += point.x - lastPoint.x;
                offset.y += point.y - lastPoint.y;
                lastPoint = point;
                repaint();
            }
        });
    }

    @Override
    // Rysowanie labiryntu
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (maze == null) {
            return;
        }
        int cellSize = (int) (Math.min(getWidth() / maze[0].length, getHeight() / maze.length)*zoom) ;

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
                g.fillRect((int)(j * cellSize + offset.x), (int)(i * cellSize + offset.y), cellSize, cellSize);
            }
        }
    }
}