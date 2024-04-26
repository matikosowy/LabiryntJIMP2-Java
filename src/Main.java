import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Main extends JFrame{
    private JButton selectFileButton;
    private JButton generateMazeButton;
    private JPanel panelMain;
    private char[][] maze;
    private JPanel panel;
    private JLabel dimensions;
    private JButton solveButton;

    public Main() {
        setTitle("Maze Solver");
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        selectFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());

                    int rows = 0;
                    int columns = 0;


                    if(filePath.endsWith(".bin")) {
                        try {
                            Input.binaryToText(filePath);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(Main.this, "Nie udało się odczytać pliku binarnego!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        try {
                            columns = Input.liczKolumny("maze_translated.txt");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(Main.this, "Nie udało się policzyc kolumn!", "Error", JOptionPane.ERROR_MESSAGE);
                        }

                        try {
                            rows = Input.liczWiersze("maze_translated.txt");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(Main.this, "Nie udało się policzyc wierszy!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        try {
                            maze = Input.readMaze("maze_translated.txt", rows, columns);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(Main.this, "Nie udało się odczytać pliku z labiryntem po konwersji z binarnego!", "Error", JOptionPane.ERROR_MESSAGE);
                            maze = new char[rows][columns];
                        }



                    }else {
                        try {
                            columns = Input.liczKolumny(filePath);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(Main.this, "Nie udało się policzyc kolumn!", "Error", JOptionPane.ERROR_MESSAGE);
                        }

                        try {
                            rows = Input.liczWiersze(filePath);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(Main.this, "Nie udało się policzyc wierszy!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        try {
                            maze = Input.readMaze(filePath, rows, columns);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(Main.this, "Nie udało się odczytać pliku z labiryntem!", "Error", JOptionPane.ERROR_MESSAGE);
                            maze = new char[rows][columns];
                        }

                    }
                    dimensions.setText("WYMIARY: W - " + rows + ", K - " + columns);
                }
            }
        });
        generateMazeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (maze != null) {
                    GUI gui = new GUI(maze);
                    panelMain.removeAll();
                    panelMain.setLayout(new BorderLayout());
                    panelMain.add(gui, BorderLayout.CENTER);
                    panelMain.revalidate();
                    panelMain.repaint();

                    // Calculate the preferred size based on the maze size
                    int cellSize = Math.min(getWidth() / maze[0].length, getHeight() / maze.length);
                    int preferredWidth = cellSize * maze[0].length;
                    int preferredHeight = cellSize * maze.length;

                    // Set the preferred size of the GUI panel
                    gui.setPreferredSize(new Dimension(preferredWidth, preferredHeight));

                    // Pack the frame to fit the preferred size of its contents
                    pack();
                }
            }
        });
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (maze != null) {
                    Node start = null;
                    Node end = null;
                    for (int i = 1; i < maze.length; i+=2) {
                        for (int j = 1; j < maze[i].length; j+=2) {
                            if (maze[i-1][j] == 'P' || maze[i+1][j] == 'P' || maze[i][j-1] == 'P' || maze[i][j+1] == 'P') {
                                start = new Node(i, j);
                            } else if (maze[i-1][j] == 'K' || maze[i+1][j] == 'K' || maze[i][j-1] == 'K' || maze[i][j+1] == 'K') {
                                end = new Node(i, j);
                            }
                        }
                    }
                    if (start != null && end != null) {
                        Solver solver = new Solver(maze, start, end);
                        java.util.List<Node> path = solver.solve();
                        if (path != null) {
                            for (Node node : path) {
                                maze[node.getX()][node.getY()] = '.';
                            }
                            GUI gui = new GUI(maze);
                            panelMain.removeAll();
                            panelMain.setLayout(new BorderLayout());
                            panelMain.add(gui, BorderLayout.CENTER);
                            panelMain.revalidate();
                            panelMain.repaint();
                        } else {
                            JOptionPane.showMessageDialog(Main.this, "No path found!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(Main.this, "Start or end point not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        Main MainWindow = new Main();
    }
}