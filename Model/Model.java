package Model;

import java.awt.*;
import java.util.*;

/**
 * Object that stores the grid of the game.
 */
public class Model {
    private static final int GRID_SIZE = 4;
    private Random random;
    private Tile[][] tileArray;
    private int score;
    private int bestScore;

    /**
     * Constructor.
     */
    public Model() {
        random = new Random();
        tileArray = new Tile[GRID_SIZE][GRID_SIZE];
        score = 0;
        bestScore = 0;
    }

    public Tile[][] getTileArray() {
        return tileArray;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore() {
        bestScore = score > bestScore ? score : bestScore;
    }
    
    /**
     * Initialize the grid.
     */
    public MovePlan initialize() {

        // Add tiles to the grid
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                Tile tile = new Tile(UUID.randomUUID().toString());
                tileArray[r][c] = tile;
            }
        }

        // Make move plan for spawning two random tiles
        var tileArrayCopy = getGridCopy();
        var movePlan = new MovePlan(null);
        movePlan.addAction(spawnRandomTile(tileArrayCopy));
        movePlan.addAction(spawnRandomTile(tileArrayCopy));
        movePlan.setChanged(true);

        return movePlan;
    }

    /**
     * Spawn a tile with value 2 or 4 at a random position in the grid.
     */
    public MoveAction spawnRandomTile(Tile[][] tileArray) {
        // Spawn a tile
        var emptyTiles = getEmptyTiles(tileArray);
        Tile randomTile = emptyTiles.get(random.nextInt(emptyTiles.size()));
        randomTile.setValue(emptyTiles.size() > 14 ? 2 : random.nextFloat() < 0.9 ? 2 : 4);

        // Make move action for spawning a tile
        Point position = getGridPosition(randomTile);
        int row = (int) position.getX();
        int col = (int) position.getY();
        MoveAction action = new MoveAction(row, col, row, col, 0, randomTile.getValue(), "spawn");

        return action;
    }

    /**
     * Get the empty tiles in the grid.
     * @return ArrayList of empty tiles
     */
    public ArrayList<Tile> getEmptyTiles(Tile[][] tileArray) {
        var emptyTiles = new ArrayList<Tile>();

        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                if (tileArray[r][c].isEmpty()) {
                    emptyTiles.add(tileArray[r][c]);
                }
            }
        }

        return emptyTiles;
    }

    /**
     * See what would happen by doing a certain move without changing the grid.
     * @param direction of move
     * @return MovePlan containing MoveActions
     */
    public MovePlan computeMove(Direction dir) {
        MovePlan movePlan = new MovePlan(dir);
        var tileArrayCopy = getGridCopy();

        // Make list for row and column loop order
        int[] rowOrder = new int[GRID_SIZE];
        int[] colOrder = new int[GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            rowOrder[i] = i;
            colOrder[i] = i;
        }

        // Reverse order (or not) based on direction
        if (dir == Direction.DOWN) {
            reverseArray(rowOrder);
        } else if (dir == Direction.RIGHT) {
            reverseArray(colOrder);
        }

        // Store how row and col should change while looping to find new position of tile
        int dRow = dir == Direction.UP ? -1 : dir == Direction.DOWN ? 1 : 0;
        int dCol = dir == Direction.LEFT ? -1 : dir == Direction.RIGHT ? 1 : 0;

        // Loop through all tiles in grid
        for (int rIndex = 0; rIndex < GRID_SIZE; rIndex++) {
            for (int cIndex = 0; cIndex < GRID_SIZE; cIndex++) {
                int r = rowOrder[rIndex];
                int c = colOrder[cIndex];

                Tile current = tileArrayCopy[r][c];
                if (current.isEmpty()) {
                    continue;
                }

                int startRow = r;
                int startCol = c;
                int value = current.getValue();
                int newRow = r;
                int newCol = c;

                int nextRow = r + dRow;
                int nextCol = c + dCol;

                // Loop through all tiles in correct direction to check for slides and merges
                while (nextRow >= 0 && nextRow < GRID_SIZE && nextCol >= 0 && nextCol < GRID_SIZE) {
                    Tile next = tileArrayCopy[nextRow][nextCol];

                    if (next.isEmpty()) {
                        // Move current tile to next tile
                        newRow = nextRow;
                        newCol = nextCol;
                        nextRow += dRow;
                        nextCol += dCol;
                    } else if (next.getValue() == value && !next.isMerged()) {
                        // Merge current tile with next tile
                        newRow = nextRow;
                        newCol = nextCol;
                        value *= 2;
                        next.setMerged(true);
                        break;
                    } else {
                        break;
                    }
                }

                if (newRow == startRow && newCol == startCol) {
                    continue;
                }

                // Add move to move plan
                String type = (value > current.getValue()) ? "merge" : "slide";
                MoveAction action = new MoveAction(startRow, startCol, newRow, newCol, current.getValue(), value, type);
                movePlan.addAction(action);
                movePlan.addScore(current.getValue() != value ? value : 0);
                movePlan.setChanged(true);

                // Change the tile array copy
                tileArrayCopy[startRow][startCol].setValue(0);
                tileArrayCopy[newRow][newCol].setValue(value);
            }
        }

        if (movePlan.isChanged()) {
            movePlan.addAction(spawnRandomTile(tileArrayCopy));
        }
        return movePlan;
    }

    /**
     * Get the position of a tile in the grid.
     * @param tile to check the position of
     * @return point with the row and column index of the tile
     */
    public Point getGridPosition(Tile tile) {
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                if (tile.getId().equals(tileArray[r][c].getId())) {
                    return new Point(r, c);
                }
            }
        }
        return null;
    }

    /**
     * Make a copy of the grid array.
     * @return
     */
    public Tile[][] getGridCopy() {
        var tileArrayCopy = new Tile[GRID_SIZE][GRID_SIZE];

        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                Tile orginal = tileArray[r][c];
                Tile copy = new Tile(orginal.getId());
                copy.setValue(orginal.getValue());
                copy.setMerged(orginal.isMerged());
                tileArrayCopy[r][c] = copy;
            }
        }
        return tileArrayCopy;
    }

    /**
     * Reverse the order of an array. Helper method for computeMove.
     * @param array of integers
     */
    public void reverseArray(int[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            int temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
    }

    /**
     * Apply a move computed in computeMove to the grid and spawn a new tile.
     * @param movePlan containing MoveActions
     * @return MovePlan updated with spawn action
     */
    public void applyMove(MovePlan movePlan) {
        if (!movePlan.isChanged()) {
            return;
        }

        score += movePlan.getScoreGained();

        ArrayList<MoveAction> actions = movePlan.getActions();
        for (MoveAction action : actions) {
            tileArray[action.getStartRow()][action.getStartCol()].setValue(0); 
            tileArray[action.getEndRow()][action.getEndCol()].setValue(action.getNewValue());
        }

        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                tileArray[r][c].setMerged(false);
            }
        }
    }

    /**
     * Check if the user can make another move.
     * @return whether the user can make another move
     */
    public boolean canMove() {
        for (Direction dir : Direction.values()) {
            if (computeMove(dir).isChanged()) {
                return true;
            }
        }
        return false;
    }
    
    // For testing purposes
    @Override
    public String toString() {
        String str = "";
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                str += tileArray[r][c].getValue() + " ";
            }
            str += "\n";
        }
        return str;
    }

    public void testGrid() {
        tileArray[0][0].setValue(4);
        tileArray[0][1].setValue(8);
        tileArray[0][2].setValue(4);
        tileArray[0][3].setValue(2);

        tileArray[1][0].setValue(8);
        tileArray[1][1].setValue(16);
        tileArray[1][2].setValue(8);
        tileArray[1][3].setValue(16);

        tileArray[2][0].setValue(4);
        tileArray[2][1].setValue(2);
        tileArray[2][2].setValue(64);
        tileArray[2][3].setValue(4);

        tileArray[3][0].setValue(2);
        tileArray[3][1].setValue(16);
        tileArray[3][2].setValue(4);
        tileArray[3][3].setValue(2);
    }

    public static void main(String[] args) {
        var grid = new Model();
        grid.applyMove(grid.initialize());
        System.out.println(grid);

        while (grid.canMove()) {
            for (Direction dir : Direction.values()) {
                grid.applyMove(grid.computeMove(dir));
                System.out.println(grid);
            }
        }
    }
}