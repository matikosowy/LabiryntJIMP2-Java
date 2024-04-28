import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


public class Main extends JFrame{
    private JButton selectFileButton;
    private JButton generateMazeButton;
    private char[][] maze;
    private JPanel panel;
    private JLabel dimensions;
    private JButton solveButton;
    private JLabel fileName;
    private JPanel panelMaze;
    private JButton helpButton;
    private JButton resetButton;
    private JButton fitButton;
    private JButton errorButton;

    private void showErrorAndResetPanel(String errorMessage) {
        JOptionPane.showMessageDialog(Main.this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
        GUI gui = new GUI(null);
        panelMaze.removeAll();
        panelMaze.revalidate();
        panelMaze.repaint();
    }
    public void updateMaze(char[][] maze) {
        this.maze = maze;

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


    public Main() {
        setTitle("Maze Solver");
        setIconImage(new ImageIcon("src/icon.png").getImage());
        Taskbar.getTaskbar().setIconImage(new ImageIcon("src/icon.png").getImage());
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        selectFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Wybór pliku z labiryntem
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();

                    int rows = 0;
                    int columns = 0;

                    // Dla pliku binarnego
                    if(filePath.endsWith(".bin")) {
                        try {
                            // Interpretacja tekstowa labiryntu z pliku binarnego
                            Input.binaryToText(filePath);
                        } catch (Exception ex) {
                            showErrorAndResetPanel("Nie udało się odczytać pliku binarnego!");
                        }
                        try {
                            columns = Input.countColumns("maze_decoded.txt");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(Main.this, "Nie udało się policzyc kolumn!", "Error", JOptionPane.ERROR_MESSAGE);
                            showErrorAndResetPanel("Nie udało się policzyc kolumn!");
                        }
                        try {
                            rows = Input.countRows("maze_decoded.txt");
                        } catch (Exception ex) {
                            showErrorAndResetPanel("Nie udało się policzyc wierszy!");
                        }
                        try {
                            // Zapis labiryntu do wektora 2d
                            maze = Input.readMaze("maze_decoded.txt", rows, columns);
                        } catch (Exception ex) {
                            maze = new char[rows][columns];
                            showErrorAndResetPanel("Nie udało się odczytać pliku z labiryntem po konwersji z binarnego!");
                        }
                    }else if(filePath.endsWith(".txt")) {
                        try {
                            columns = Input.countColumns(filePath);
                        } catch (Exception ex) {
                            showErrorAndResetPanel("Nie udało się policzyc kolumn!");
                        }
                        try {
                            rows = Input.countRows(filePath);
                        } catch (Exception ex) {
                            showErrorAndResetPanel("Nie udało się policzyc wierszy!");
                        }
                        try {
                            maze = Input.readMaze(filePath, rows, columns);
                        } catch (Exception ex) {
                            showErrorAndResetPanel("Nie udało się odczytać pliku z labiryntem!");
                            maze = new char[rows][columns];
                        }

                    }else{
                        JOptionPane.showMessageDialog(Main.this, "Nieprawidłowy format pliku! Tylko .txt lub .bin", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    dimensions.setText("WYMIARY: " + rows/2 + " x " + columns/2);
                    fileName.setText("PLIK: " + selectedFile.getAbsolutePath());

                    if(columns == 0 || rows == 0) {
                        showErrorAndResetPanel("Nie udało się odczytać pliku z labiryntem!");
                        maze = null;
                    }
                }
            }
        });
        generateMazeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (maze != null) {
                    if (Errors.invalidMaze(maze, maze.length, maze[0].length) != 0) {
                        showErrorAndResetPanel("Plik uszkodzony! Jedna z linii ma inny wymiar!");
                    } else if (Errors.entryexitError(maze, maze.length, maze[0].length) != 0) {
                        showErrorAndResetPanel("Za dużo punktów startowych lub końcowych!");
                    } else if (Errors.unrecognisedCharacter(maze, maze.length, maze[0].length) != ' ') {
                        char znak = Errors.unrecognisedCharacter(maze, maze.length, maze[0].length);
                        showErrorAndResetPanel("Plik uszkodzony! Znaleziono nieznany znak: " + znak);
                    } else {
                        updateMaze(maze);
                    }
                }else{
                    JOptionPane.showMessageDialog(Main.this, "Nie wybrano pliku!", "Error", JOptionPane.ERROR_MESSAGE);

                }
            }
        });
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Szukanie początku i końca labiryntu
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
                    // Rozwiązywanie labiryntu
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
                    String filePath = fileName.getText().substring(6);
                    if(filePath.endsWith(".bin")) {
                        try {
                            // Zapis labiryntu z pliku binarnego
                            Output.outputFromBinary(filePath, maze);
                        } catch (Exception ex) {
                            showErrorAndResetPanel("Nie udało się zapisać labiryntu do pliku!");
                        }
                    }else if(filePath.endsWith(".txt")) {
                        try {
                            // Zapis labiryntu z pliku tekstowego
                            Output.outputFromText(filePath, maze);
                        } catch (Exception ex) {
                            showErrorAndResetPanel("Nie udało się zapisać labiryntu do pliku!");
                        }
                    }
                }
            }
        });
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(Main.this, "1. Wybierz plik z labiryntem przy użyciu przycisku Plik.\n" +
                        "2. Naciśnij przycisk Generuj w celu wygenerowania labiryntu na ekranie.\n" +
                        "3. Naciśnij przycisk Rozwiąż w celu znalezienia ścieżki.\n" +
                        "4. Naciskaj LPM lub Shift+LPM na krawędź labiryntu, aby wybrać nowe wejście/wyjście.\n" +
                        "5. Przybliżaj scrollem i przesuwaj przeciągając myszką.", "Pomoc", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filePath = fileName.getText().substring(6);

                if(filePath.endsWith(".bin")) {
                    try {
                        maze = Input.readMaze("maze_decoded.txt", maze.length, maze[0].length);
                    } catch (Exception ex) {
                        showErrorAndResetPanel("Nie udało się odczytać pliku z labiryntem!");
                    }
                } else if(filePath.endsWith(".txt")){
                    try {
                        maze = Input.readMaze(filePath, maze.length, maze[0].length);
                    } catch (Exception ex) {
                        showErrorAndResetPanel("Nie udało się odczytać pliku z labiryntem!");
                    }
                }
                GUI gui = new GUI(maze);
                panelMaze.removeAll();
                panelMaze.setLayout(new BorderLayout());
                panelMaze.add(gui, BorderLayout.CENTER);
                panelMaze.revalidate();
                panelMaze.repaint();
            }
        });

        errorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
                setUndecorated(true);
                setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        Main MainWindow = new Main();
    }
}