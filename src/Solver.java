import java.util.*;



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
        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Set<Node> closedSet = new HashSet<>();

        start.setCost(0);
        openSet.add(start);

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

                double tentativeCost = current.getCost() + 1; // assuming all moves cost 1

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

        return null; // no path found
    }

    private List<Node> constructPath(Node node) {
        List<Node> path = new ArrayList<>();

        while (node != null) {
            path.add(node);
            if(maze[node.getX()][node.getY()] != 'P' && maze[node.getX()][node.getY()] != 'K'){
                maze[node.getX()][node.getY()] = '.'; // mark the path in the maze
            }

            node = node.getCameFrom();
        }

        Collections.reverse(path);
        return path;
    }

    private List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();

        int[][] directions = {
                {-2, 0}, // up
                {2, 0},  // down
                {0, -2}, // left
                {0, 2}   // right
        };
        for (int[] direction : directions) {
            int newX = node.getX() + direction[0];
            int newY = node.getY() + direction[1];

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