package astarsearch;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.PriorityQueue;
import java.util.Scanner;
public class AStarSearch {
    public static void main(String[] args) {
        AStarSearch run = new AStarSearch();
    }
    PriorityQueue<Node> leaves = new PriorityQueue<>(Comparator.comparing(Node::getF));
    ArrayList<Node> visitedNodes = new ArrayList<>();
    ArrayList<Node> repeated = new ArrayList<>();
    int movesMade = 0;
    int puzzleSize = 0;
    AStarSearch() {
        Scanner reader = new Scanner(System.in);
        try {
            System.out.print("Enter Puzzle Size: ");
            puzzleSize = reader.nextInt();
            int[][] initialState = new int[puzzleSize][puzzleSize];
            int[][] goalState = new int[puzzleSize][puzzleSize];
            int counter = 1;
            for (int i = 0; i < puzzleSize; i++) {
                for (int j = 0; j < puzzleSize; j++) {
                    if (i == puzzleSize - 1 && j == puzzleSize - 1) {
                        goalState[i][j] = 0;
                    } else {
                        goalState[i][j] = counter;
                        counter++;
                    }
                }
            }
            ArrayList<Integer> inputTester = new ArrayList<>();
            for (int i = 0; i < puzzleSize; i++) {
                for (int j = 0; j < puzzleSize; j++) {
                    System.out.print("Enter Tile [" + i + "][" + j + "]: ");
                    int tester = reader.nextInt();
                    if (tester < 0 || tester > (puzzleSize * puzzleSize) - 1) {
                        System.out.println("Inputted number is not in the acceptable range. Please retry.");
                        System.exit(0);
                    }
                    for (int a = 0; a < inputTester.size(); a++) {
                        if (tester == inputTester.get(a)) {
                            System.out.println("Inputted number has already been previously inputted. Please retry.");
                            System.exit(0);
                        }
                    }
                    inputTester.add(tester);
                    initialState[i][j] = tester;
                }
            }
            System.out.println("-----------------------------------");
            System.out.println("INITIAL STATE:");
            for (int i = 0; i < initialState.length; i++) {
                for (int j = 0; j < initialState[0].length; j++) {
                    System.out.print(initialState[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println("-----------------------------------");
            System.out.println("GOAL STATE:");
            for (int i = 0; i < goalState.length; i++) {
                for (int j = 0; j < goalState[0].length; j++) {
                    System.out.print(goalState[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println("-----------------------------------");
            boolean solvable = puzzleSolvability(initialState);
            if (solvable == false) {
                System.out.println("Error - goal state cannot be reached!");
            } else {
                findSolution(initialState, goalState);
            }
        } catch (InputMismatchException e) {
            System.out.println("Input was not recognized as an integer. Please retry.");
            System.exit(0);
        }
    }
    void findSolution(int[][] puzzle, int[][] goalState) {
        int h = findManhattan(puzzle, goalState);
        leaves.add(new Node(puzzle, 0, h, "None", null));
        while (true) {
            Node currentState = leaves.peek();
            leaves.remove();
            int[][] currentPuzzle = currentState.getPuzzle();
            movesMade = currentState.getG();
            repeated.add(currentState);
            boolean done = samePuzzle(currentPuzzle, goalState);
            if (done == true) {
                System.out.println("Reached the goal state!");
                System.out.println("Number of moves: " + movesMade);
                findMoves(currentState);
                System.out.print("Moves: ");
                for (int i = visitedNodes.size() - 3; i > 0; i--) {
                    System.out.print(visitedNodes.get(i).getDirection() + ", ");
                }
                System.out.print(visitedNodes.get(0).getDirection());
                System.out.println("");
                System.exit(0);
            } else {
                expandChildren(currentState, goalState);
            }
        }
    }
    boolean samePuzzle(int[][] puzzle1, int[][] puzzle2) {
        for (int i = 0; i < puzzleSize; i++) {
            for (int j = 0; j < puzzleSize; j++) {
                if (puzzle1[i][j] != puzzle2[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    void expandChildren(Node currentState, int[][] goalState) {
        int[][] currentPuzzle = currentState.getPuzzle();
        int g = currentState.getG();
        int rowCoordinate = 0;
        int columnCoordinate = 0;
        for (int i = 0; i < puzzleSize; i++) {
            for (int j = 0; j < puzzleSize; j++) {
                if (currentPuzzle[i][j] == 0) {
                    rowCoordinate = i;
                    columnCoordinate = j;
                }
            }
        }
        if (rowCoordinate - 1 >= 0) {
            int[][] newPuzzle = new int[puzzleSize][puzzleSize];
            for (int i = 0; i < puzzleSize; i++) {
                for (int j = 0; j < puzzleSize; j++) {
                    newPuzzle[i][j] = currentPuzzle[i][j];
                }
            }
            int temp = newPuzzle[rowCoordinate][columnCoordinate];
            newPuzzle[rowCoordinate][columnCoordinate] = newPuzzle[rowCoordinate - 1][columnCoordinate];
            newPuzzle[rowCoordinate - 1][columnCoordinate] = temp;
            int h = findManhattan(newPuzzle, goalState);
            boolean tester = false;
            for (int i = 0; i < repeated.size(); i++) {
                int[][] test = repeated.get(i).getPuzzle();
                if (samePuzzle(test, newPuzzle)) {
                    tester = true;
                    break;
                }
            }
            if (tester == false) {
                leaves.add(new Node(newPuzzle, g + 1, h, "Up", currentState));
            }
        }
        if (rowCoordinate + 1 < puzzleSize) {
            int[][] newPuzzle = new int[puzzleSize][puzzleSize];
            for (int i = 0; i < puzzleSize; i++) {
                for (int j = 0; j < puzzleSize; j++) {
                    newPuzzle[i][j] = currentPuzzle[i][j];
                }
            }
            int temp = newPuzzle[rowCoordinate][columnCoordinate];
            newPuzzle[rowCoordinate][columnCoordinate] = newPuzzle[rowCoordinate + 1][columnCoordinate];
            newPuzzle[rowCoordinate + 1][columnCoordinate] = temp;
            int h = findManhattan(newPuzzle, goalState);
            boolean tester = false;
            for (int i = 0; i < repeated.size(); i++) {
                int[][] test = repeated.get(i).getPuzzle();
                if (samePuzzle(test, newPuzzle)) {
                    tester = true;
                    break;
                }
            }
            if (tester == false) {
                leaves.add(new Node(newPuzzle, g + 1, h, "Down", currentState));
            }
        }
        if (columnCoordinate - 1 >= 0) {
            int[][] newPuzzle = new int[puzzleSize][puzzleSize];
            for (int i = 0; i < puzzleSize; i++) {
                for (int j = 0; j < puzzleSize; j++) {
                    newPuzzle[i][j] = currentPuzzle[i][j];
                }
            }
            int temp = newPuzzle[rowCoordinate][columnCoordinate];
            newPuzzle[rowCoordinate][columnCoordinate] = newPuzzle[rowCoordinate][columnCoordinate - 1];
            newPuzzle[rowCoordinate][columnCoordinate - 1] = temp;
            int h = findManhattan(newPuzzle, goalState);
            boolean tester = false;
            for (int i = 0; i < repeated.size(); i++) {
                int[][] test = repeated.get(i).getPuzzle();
                if (samePuzzle(test, newPuzzle)) {
                    tester = true;
                    break;
                }
            }
            if (tester == false) {
                leaves.add(new Node(newPuzzle, g + 1, h, "Left", currentState));
            }
        }
        if (columnCoordinate + 1 < puzzleSize) {
            int[][] newPuzzle = new int[puzzleSize][puzzleSize];
            for (int i = 0; i < puzzleSize; i++) {
                for (int j = 0; j < puzzleSize; j++) {
                    newPuzzle[i][j] = currentPuzzle[i][j];
                }
            }
            int temp = newPuzzle[rowCoordinate][columnCoordinate];
            newPuzzle[rowCoordinate][columnCoordinate] = newPuzzle[rowCoordinate][columnCoordinate + 1];
            newPuzzle[rowCoordinate][columnCoordinate + 1] = temp;
            int h = findManhattan(newPuzzle, goalState);
            boolean tester = false;
            for (int i = 0; i < repeated.size(); i++) {
                int[][] test = repeated.get(i).getPuzzle();
                if (samePuzzle(test, newPuzzle)) {
                    tester = true;
                    break;
                }
            }
            if (tester == false) {
                leaves.add(new Node(newPuzzle, g + 1, h, "Right", currentState));
            }
        }
    }
    int findManhattan(int[][] currentPuzzle, int[][] goalState) {
        int h = 0;
        for (int i = 0; i < puzzleSize; i++) {
            for (int j = 0; j < puzzleSize; j++) {
                int numberTest = currentPuzzle[i][j];
                for (int a = 0; a < puzzleSize; a++) {
                    for (int b = 0; b < puzzleSize; b++) {
                        if (goalState[a][b] == numberTest) {
                            h = h + Math.abs(a - i) + Math.abs(b - j);
                        }
                    }
                }
            }
        }
        return h;
    }
    void findMoves(Node finalState) {
        try {
            visitedNodes.add(finalState);
            findMoves(finalState.getParent());
        } catch (NullPointerException e) {
        }
    }
    boolean puzzleSolvability(int[][] initialState) {
        int numberOfInversions = findInversions(initialState);
        if (puzzleSize % 2 == 0) {
            int rowCoordinate = 0;
            for (int i = 0; i < puzzleSize; i++) {
                for (int j = 0; j < puzzleSize; j++) {
                    if (initialState[i][j] == 0) {
                        rowCoordinate = i;
                    }
                }
            }
            if ((numberOfInversions+rowCoordinate) % 2 == 0) {
                return false;
            } else {
                return true;
            }
        } else {
            if (numberOfInversions % 2 == 0) {
                return true;
            } else {
                return false;
            }
        }
    }
    public int findInversions(int[][] initialState) {
        int counter = 0;
        int[] initialState1D = new int[puzzleSize * puzzleSize];
        int index = 0;
        for (int i = 0; i < puzzleSize; i++) {
            for (int j = 0; j < puzzleSize; j++) {
                initialState1D[index] = initialState[i][j];
                index++;
            }
        }
        for (int i = 0; i < initialState1D.length - 1; i++) {
            for (int j = i + 1; j < initialState1D.length; j++) {
                if (initialState1D[i] != 0 && initialState1D[j] != 0) {
                    if (initialState1D[i] > initialState1D[j]) {
                        counter++;
                    }
                }
            }
        }
        return counter;
    }
}
