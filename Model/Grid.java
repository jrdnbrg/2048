package Model;

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
        initialize();
    }
    
    /**
     * Initialize the grid.
     */
    public void initialize() {
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                Tile tile = new Tile(0);
                tileArray[r][c] = tile;
            }
        }

        spawnRandomTile();
        spawnRandomTile();
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
    public void spawnRandomTile() {
        var emptyTiles = getEmptyTiles();
        emptyTiles.get(random.nextInt(emptyTiles.size())).setValue(2);
    }

    /**
     * Check if the user can make another move.
     * @return whether the user can make another move
     */
    public boolean canMove() {
        // Implement !!!!!!!!!!!!!!!!!!!!!!!!
        return true;
    }

    /**
     * See what would happen by doing a certain move without changing the grid.
     * @param direction of move
     * @return MovePlan containing MoveActions
     */
    public MovePlan computeMove(Direction dir) {
        int nextRow;
        int nextCol;
        int newRow;
        int newCol;
        int oldValue;
        int newValue;
        String type;

        // Make copy of current grid
        var gridCopy = this;
        Tile[][] tileArray = gridCopy.getTileArray();
        MovePlan movePlan = new MovePlan(dir);

        // Compute row and column values used in the upcoming loop depending on direction
        int rowChange = (dir == Direction.UP) ? -1 : (dir == Direction.DOWN) ? 1 : 0;
        int colChange = (dir == Direction.LEFT) ? -1 : (dir == Direction.RIGHT) ? 1 : 0;
        int rowStep = (dir == Direction.DOWN) ? -1 : 1;
        int colStep = (dir == Direction.RIGHT) ? -1 : 1;
        int startRow = (dir == Direction.UP) ? 1 : (dir == Direction.DOWN) ? GRID_SIZE - 2 : 0;
        int startCol = (dir == Direction.LEFT) ? 1 : (dir == Direction.RIGHT) ? GRID_SIZE - 2 : 0;

        // Loop over all tiles in the grid
        for (int r = startRow; r >= 0 && r < GRID_SIZE; r += rowStep) {
            for (int c = startCol; c >= 0 && c < GRID_SIZE; c += colStep) {

                // If tile is empty, continue to the next tile
                if (tileArray[r][c].isEmpty()) {
                    continue;
                }

                oldValue = tileArray[r][c].getValue();
                newValue = oldValue;
                nextRow = r + rowChange;
                nextCol = c + colChange; 
                newRow = nextRow;
                newCol = nextCol;

                // Loop over all values in the row/column before the current value
                while (nextRow >= 0 && nextRow < GRID_SIZE && nextCol >= 0 && nextCol < GRID_SIZE) {

                    // Check if next tile is empty or has the same value as the current tile
                    if (tileArray[nextRow][nextCol].isEmpty()) {
                        // Move the current tile to the position of this next tile
                        newRow = nextRow;
                        newCol = nextCol;
                    } else if (tileArray[nextRow][nextCol].getValue() == tileArray[r][c].getValue() && !tileArray[nextRow][nextCol].isMerged()) {
                        // Move the current tile to the position of this next tile
                        newRow = nextRow;
                        newCol = nextCol;

                        // Merge the tiles
                        newValue = oldValue * 2;
                        tileArray[newRow][newCol].setMerged(true);
                    }

                    nextRow += rowChange;
                    nextCol += colChange;
                }

                // Update the grid
                tileArray[newRow][newCol].setValue(newValue);
                tileArray[r][c].reset();
                
                // Decide the type of the move
                type = tileArray[newRow][newCol].isMerged() ? "merge" : "slide";
                
                // Add moveAction of this move to the MovePlan
                var moveAction = new MoveAction(r, c, newRow, newCol, oldValue, newValue, type);
                movePlan.addAction(moveAction);
                movePlan.setChanged(movePlan.isChanged() ? true : moveAction.hasPositionChange());
                movePlan.addScore(newValue);
            }
        }
        return movePlan;
    }

    /**
     * Apply a move computed in computeMove to the grid.
     * @param movePlan containing MoveActions
     */
    public void applyMove(MovePlan movePlan) {
        if (!movePlan.isChanged()) {
            return;
        }

        score += movePlan.getScoreGained();

        List<MoveAction> actions = movePlan.getActions();
        for (MoveAction action : actions) {
            tileArray[action.getStartRow()][action.getStartCol()].setValue(0); 
            tileArray[action.getEndRow()][action.getEndCol()].setValue(action.getNewValue());
        }
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

    public static void main(String[] args) {
        var grid = new Grid();
        System.out.println(grid);
        grid.applyMove(grid.computeMove(Direction.RIGHT));
        System.out.println(grid);
        grid.applyMove(grid.computeMove(Direction.DOWN));
        System.out.println(grid);
        grid.applyMove(grid.computeMove(Direction.LEFT));
        System.out.println(grid);
        grid.applyMove(grid.computeMove(Direction.UP));  
    }
}
