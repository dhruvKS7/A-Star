package astarsearch;
public class Node {
    private int[][] puzzle;
    private int g;
    private int h;
    private String direction;
    private Node parent;
    public Node(int[][] puzzle, int g, int h, String direction, Node parent) {
        this.puzzle = puzzle;
        this.g = g;
        this.h = h;
        this.direction = direction;
        this.parent = parent;
    }
    public int[][] getPuzzle() {
        return puzzle;
    }
    public int getG() {
        return g;
    }
    public int getH() {
        return h;
    }
    public String getDirection() {
        return direction;
    }
    public Node getParent() {
        return parent;
    }
    public int getF() {
        return g + h;
    }
}
