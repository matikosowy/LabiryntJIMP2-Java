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
                            System.out.println("Nie udało się odczytać pliku binarnego");
                        }
                        try {
                            columns = Input.liczKolumny("maze_translated.txt");
                        } catch (Exception ex) {
                            System.out.println("Nie udało się odczytać pliku");
                        }

                        try {
                            rows = Input.liczWiersze("maze_translated.txt");
                        } catch (Exception ex) {
                            System.out.println("Nie udało się odczytać pliku");
                        }
                        try {
                            maze = Input.readMaze("maze_translated.txt", rows, columns);
                        } catch (Exception ex) {
                            System.out.println("Nie udało się odczytać pliku");
                            maze = new char[rows][columns];
                        }



                    }else {
                        try {
                            columns = Input.liczKolumny(filePath);
                        } catch (Exception ex) {
                            System.out.println("Nie udało się odczytać pliku");
                        }

                        try {
                            rows = Input.liczWiersze(filePath);
                        } catch (Exception ex) {
                            System.out.println("Nie udało się odczytać pliku");
                        }
                        try {
                            maze = Input.readMaze(filePath, rows, columns);
                        } catch (Exception ex) {
                            System.out.println("Nie udało się odczytać pliku");
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
                }
            }
        });
    }

    public static void main(String[] args) {
        Main MainWindow = new Main();
    }
}