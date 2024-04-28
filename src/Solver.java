import javax.swing.*;
import java.util.*;


// Algorytm A*
public class Solver {
    private char[][] maze;
    private Node start;
    private Node end;

    public Solver(char[][] maze, Node start, Node end) {
        this.maze = maze;
        this.start = start;
        this.end = end;
    }

    public List<Node> solve() {
        PriorityQueue<Node> openSet = new PriorityQueue<>(); // Nieodwiedzone komórki
        Set<Node> closedSet = new HashSet<>(); // Odwiedzone komórki

        start.setCost(0);
        openSet.add(start);

        // Dopóki wszysktkie komórki nie są odwiedzone:
        // - Pobierz komórkę z kolejki
        // - Jeśli komórka jest końcem, zwróć ścieżkę
        // - Dodaj komórkę do zbioru odwiedzonych
        // - Jeśli sąsiad był odwiedzony, przejdź do następnego sąsiada
        // - Dla każdego sąsiada komórki sprawdź czy można do niego dojść
        // - Jeśli można dojść do sąsiada, oblicz koszt dojścia i dodaj do odwiedzonych
        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.equals(end)) {
                return constructPath(current);
            }

            closedSet.add(current);

            for (Node neighbor : getNeighbors(current)) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                double tentativeCost = current.getCost() + 1; // Równy koszt dla każdej komórki

                if (tentativeCost < neighbor.getCost()) {
                    neighbor.setCameFrom(current);
                    neighbor.setCost(tentativeCost);
                    neighbor.setEstimatedCost(tentativeCost);

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }
        return null; // Brak ścieżki
    }

    // Konstruowanie ścieżki: kropki w wektorze w miejsach komórek ścieżki
    private List<Node> constructPath(Node node) {
        List<Node> path = new ArrayList<>();

        while (node != null) {
            path.add(node);
            if(maze[node.getX()][node.getY()] != 'P' && maze[node.getX()][node.getY()] != 'K'){
                maze[node.getX()][node.getY()] = '.';
            }
            node = node.getCameFrom();
        }
        Collections.reverse(path);

        return path;
    }

    // Pobieranie sąsiadów komórki
    private List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();

        int[][] directions = {
                {-2, 0}, // góra
                {2, 0},  // dół
                {0, -2}, // lewo
                {0, 2}   // prawo
        };
        for (int[] direction : directions) {
            int newX = node.getX() + direction[0];
            int newY = node.getY() + direction[1];

            // Czy istnieje połączenie między komórkami
            if (newX >= 0 && newX < maze.length && newY >= 0 && newY < maze[0].length && maze[newX][newY] != 'X') {
                int betweenX = node.getX() + direction[0] / 2;
                int betweenY = node.getY() + direction[1] / 2;
                if (maze[betweenX][betweenY] == ' ') {
                    neighbors.add(new Node(newX, newY));
                }
            }
        }
        return neighbors;
    }
}