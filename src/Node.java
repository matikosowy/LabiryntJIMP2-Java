import java.util.Objects;

public class Node implements Comparable<Node> {
    private int x;
    private int y;
    private Node cameFrom;
    private double cost;
    private double estimatedCost;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.cost = Double.POSITIVE_INFINITY;
        this.estimatedCost = Double.POSITIVE_INFINITY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Node getCameFrom() {
        return cameFrom;
    }

    public void setCameFrom(Node cameFrom) {
        this.cameFrom = cameFrom;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    @Override
    public int compareTo(Node other) {
        return Double.compare(this.estimatedCost, other.estimatedCost);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Node node = (Node) obj;
        return x == node.x && y == node.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}