package Model;

import java.awt.*;
import java.util.*;

/**
 * Object that stores the grid of the game.
 */
public class Grid {
    private static final int GRID_SIZE = 4;
    private Random random;
    private Tile[][] tileArray;
    private int score;

    /**
     * Constructor.
     */
    Grid() {
        random = new Random();
        tileArray = new Tile[GRID_SIZE][GRID_SIZE];
        score = 0;
    }
    
    /**
     * Initialize the grid.
     */
    public MovePlan initialize() {
        String id;

        // Add tiles to the grid
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                id = UUID.randomUUID().toString();
                Tile tile = new Tile(UUID.randomUUID().toString());
                tileArray[r][c] = tile;
            }
        }

        // Spawn 2 random tiles
        var movePlan = new MovePlan(null);
        movePlan.addAction(spawnRandomTile());
        movePlan.addAction(spawnRandomTile());

        return movePlan;
    }

    public Tile[][] getTileArray() {
        return tileArray;
    }

    public int getScore() {
        return score;
    }

    /**
     * Get the empty tiles in the grid.
     * @return ArrayList of empty tiles
     */
    public ArrayList<Tile> getEmptyTiles() {
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
     * Spawn a tile with value 2 at a random position in the grid.
     */
    public MoveAction spawnRandomTile() {
        // Spawn a tile
        var emptyTiles = getEmptyTiles();
        Tile randomTile = emptyTiles.get(random.nextInt(emptyTiles.size()));
        randomTile.setValue(2);

        // Make move action for spawning a tile
        Point position = getGridPosition(randomTile);
        int row = (int) position.getX();
        int col = (int) position.getY();
        MoveAction action = new MoveAction(row, col, row, col, 0, randomTile.getValue(), "spawn");

        return action;
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
                movePlan.addScore(value);
                movePlan.setChanged(true);

                // Change the tile array copy
                tileArrayCopy[startRow][startCol].setValue(0);
                tileArrayCopy[newRow][newCol].setValue(value);
            }
        }
        return movePlan;
    }

    /**
     * Apply a move computed in computeMove to the grid and spawn a new tile.
     * @param movePlan containing MoveActions
     * @return MovePlan updated with spawn action
     */
    public MovePlan applyMove(MovePlan movePlan) {
        if (!movePlan.isChanged()) {
            return movePlan;
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

        movePlan.addAction(spawnRandomTile());

        return movePlan;
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
        tileArray[0][0].setValue(2);
        tileArray[0][1].setValue(4);
        tileArray[0][2].setValue(8);
        tileArray[0][3].setValue(16);

        tileArray[1][0].setValue(32);
        tileArray[1][1].setValue(64);
        tileArray[1][2].setValue(128);
        tileArray[1][3].setValue(256);

        tileArray[2][0].setValue(512);
        tileArray[2][1].setValue(1024);
        tileArray[2][2].setValue(2048);
        tileArray[2][3].setValue(4096);

        tileArray[3][0].setValue(8192);
        tileArray[3][1].setValue(16384);
        tileArray[3][2].setValue(32768);
        tileArray[3][3].setValue(65536);
    }

    public static void main(String[] args) {
        var grid = new Grid();
        grid.initialize();
        grid.testGrid();
        System.out.println(grid);
        grid.applyMove(grid.computeMove(Direction.LEFT));
        System.out.println(grid);
    }
}
