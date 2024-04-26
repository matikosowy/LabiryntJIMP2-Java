import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Main extends JFrame{
    private JButton selectFileButton;
    private JButton generateMazeButton;
    private JPanel panelMaze;
    private char[][] maze;
    private JPanel panel;
    private JLabel dimensions;
    private JButton solveButton;
    private JLabel fileName;

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
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();

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
                    fileName.setText("PLIK: " + selectedFile.getAbsolutePath());
                }
            }
        });
        generateMazeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (maze != null){
                    if(Errors.invalidMaze(maze, maze.length, maze[0].length) != 0) {
                        GUI gui = new GUI(null);
                        panelMaze.removeAll();
                        panelMaze.revalidate();
                        panelMaze.repaint();
                        JOptionPane.showMessageDialog(Main.this, "Plik uszkodzony! Jedna z linii ma inny wymiar!", "Error", JOptionPane.ERROR_MESSAGE);
                    }else if(Errors.entryexitError(maze, maze.length, maze[0].length) != 0) {
                        GUI gui = new GUI(null);
                        panelMaze.removeAll();
                        panelMaze.revalidate();
                        panelMaze.repaint();
                        JOptionPane.showMessageDialog(Main.this, "Za dużo punktów startowych lub końcowych!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    }else if(Errors.unrecognisedCharacter(maze, maze.length, maze[0].length) != ' '){
                        GUI gui = new GUI(null);
                        panelMaze.removeAll();
                        panelMaze.revalidate();
                        panelMaze.repaint();
                        char znak = Errors.unrecognisedCharacter(maze, maze.length, maze[0].length);
                        JOptionPane.showMessageDialog(Main.this, "Plik uszkodzony! Znaleziono nieznany znak: " + znak, "Error", JOptionPane.ERROR_MESSAGE);
                    }else{
                        GUI gui = new GUI(maze);
                        panelMaze.removeAll();
                        panelMaze.setLayout(new BorderLayout());
                        panelMaze.add(gui, BorderLayout.CENTER);
                        panelMaze.revalidate();
                        panelMaze.repaint();

                        int cellSize = Math.min(getWidth() / maze[0].length, getHeight() / maze.length);
                        int preferredWidth = cellSize * maze[0].length;
                        int preferredHeight = cellSize * maze.length;

                        gui.setPreferredSize(new Dimension(preferredWidth, preferredHeight));
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
                    if (start != null && end != null && Errors.entryexitError(maze, maze.length, maze[0].length) == 0) {
                        Solver solver = new Solver(maze, start, end);
                        java.util.List<Node> path = solver.solve();
                        if (path != null) {
                            for (Node node : path) {
                                maze[node.getX()][node.getY()] = '.';
                            }
                            GUI gui = new GUI(maze);
                            panelMaze.removeAll();
                            panelMaze.setLayout(new BorderLayout());
                            panelMaze.add(gui, BorderLayout.CENTER);
                            panelMaze.revalidate();
                            panelMaze.repaint();
                        } else {
                            JOptionPane.showMessageDialog(Main.this, "Nie znaleziono ścieżki!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(Main.this, "Nieprawidlowa liczba początków lub końców!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        Main MainWindow = new Main();
    }
}