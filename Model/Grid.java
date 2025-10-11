package Model;

import java.util.*;

/**
 * Object that stores the grid of the game.
 */
public class Grid {
    private static final int GRID_SIZE = 4;
    private Tile[][] tileArray;
    private int score;

    /**
     * Constructor.
     */
    Grid() {
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
        var random = new Random(); 
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
     * See what would happen by doing a certain move.
     * @param direction of move
     * @return MovePlan containing MoveActions
     */
    public MovePlan computeMove(Direction dir) {
        MovePlan movePlan = switch (dir) {
            case LEFT -> computeLeftMove();
            case RIGHT -> computeRightMove();
            case UP -> computeUpMove();
            case DOWN -> computeDownMove();
            default -> computeDownMove();
        };
        return movePlan;
    }

    /**
     * See what would happen by doing a move to the left.
     * @return MovePlan containing MoveActions.
     */
    public MovePlan computeLeftMove() {
        boolean merged = false;
        int endCol;
        int newValue;
        int i;
        String type;
        var movePlan = new MovePlan(Direction.LEFT);

        for (int startRow = 0; startRow < GRID_SIZE; startRow++) {
            for (int startCol = 1; startCol < GRID_SIZE; startCol++) {
                if (tileArray[startRow][startCol].isEmpty()) {
                    continue;
                }
                
                endCol = startCol;
                newValue = tileArray[startRow][startCol].getValue();
                i = startCol - 1;
                while (i >= 0) {
                    if (tileArray[startRow][i].isEmpty()) {
                        endCol = i;
                    } else if (tileArray[startRow][i].getValue() == tileArray[startRow][startCol].getValue() && !merged) {
                        merged = true;
                        newValue = tileArray[startRow][startCol].getValue() * 2;
                        endCol = i;
                    }
                    i--;
                }
                type = merged ? "merge" : "slide";

                var moveAction = new MoveAction(startRow, startCol, startRow, endCol, tileArray[startRow][startCol].getValue(), newValue, type);
                movePlan.addAction(moveAction);
                movePlan.setChanged(movePlan.isChanged() ? true : moveAction.hasPositionChange());
                movePlan.addScore(moveAction.getNewValue());
            }
        }
        return movePlan;
    }

    /**
     * See what would happen by doing a move to the right.
     * @return MovePlan containing MoveActions.
     */
    public MovePlan computeRightMove() {
        boolean merged = false;
        int endCol;
        int newValue;
        int i;
        String type;
        var movePlan = new MovePlan(Direction.RIGHT); 

        for (int startRow = 0; startRow < GRID_SIZE; startRow++) {
            for (int startCol = GRID_SIZE - 2; startCol >= 0; startCol--) { 
                if (tileArray[startRow][startCol].isEmpty()) {
                    continue;
                }
                
                endCol = startCol;
                newValue = tileArray[startRow][startCol].getValue();
                i = startCol + 1; 
                while (i <= GRID_SIZE - 1) { 
                    if (tileArray[startRow][i].isEmpty()) {
                        endCol = i;
                    } else if (tileArray[startRow][i].getValue() == tileArray[startRow][startCol].getValue() && !merged) {
                        merged = true;
                        newValue = tileArray[startRow][startCol].getValue() * 2;
                        endCol = i;
                    }
                    i++; 
                }
                type = merged ? "merge" : "slide";

                var moveAction = new MoveAction(startRow, startCol, startRow, endCol, tileArray[startRow][startCol].getValue(), newValue, type);
                movePlan.addAction(moveAction);
                movePlan.setChanged(movePlan.isChanged() ? true : moveAction.hasPositionChange());
                movePlan.addScore(moveAction.getNewValue());
            }
        }
        return movePlan;
    }

    /**
     * See what would happen by doing an upward move.
     * @return MovePlan containing MoveActions.
     */
    public MovePlan computeUpMove() {
        boolean merged = false;
        int endRow;
        int newValue;
        int i;
        String type;
        var movePlan = new MovePlan(Direction.UP); 

        for (int startRow = 1; startRow < GRID_SIZE; startRow++) {
            for (int startCol = 0; startCol < GRID_SIZE; startCol++) { 
                if (tileArray[startRow][startCol].isEmpty()) {
                    continue;
                }
                
                endRow = startRow;
                newValue = tileArray[startRow][startCol].getValue();
                i = startRow - 1;
                while (i >= 0) {
                    if (tileArray[i][startCol].isEmpty()) {
                        endRow = i;
                    } else if (tileArray[i][startCol].getValue() == tileArray[startRow][startCol].getValue() && !merged) {
                        merged = true;
                        newValue = tileArray[startRow][startCol].getValue() * 2;
                        endRow = i;
                    }
                    i--;
                }
                type = merged ? "merge" : "slide";

                var moveAction = new MoveAction(startRow, startCol, endRow, startCol, tileArray[startRow][startCol].getValue(), newValue, type);
                movePlan.addAction(moveAction);
                movePlan.setChanged(movePlan.isChanged() ? true : moveAction.hasPositionChange());
                movePlan.addScore(moveAction.getNewValue());
            }
        }
        return movePlan;
    }

    /**
     * See what would happen by doing a downward move.
     * @return MovePlan containing MoveActions.
     */
    public MovePlan computeDownMove() {
        boolean merged = false;
        int endRow;
        int newValue;
        int i;
        String type;
        var movePlan = new MovePlan(Direction.DOWN); 

        for (int startRow = GRID_SIZE - 2; startRow >= 0; startRow--) {
            for (int startCol = 0; startCol < GRID_SIZE; startCol++) { 
                if (tileArray[startRow][startCol].isEmpty()) {
                    continue;
                }
                
                endRow = startRow;
                newValue = tileArray[startRow][startCol].getValue();
                i = startRow + 1;
                while (i < GRID_SIZE) {
                    if (tileArray[i][startCol].isEmpty()) {
                        endRow = i;
                    } else if (tileArray[i][startCol].getValue() == tileArray[startRow][startCol].getValue() && !merged) {
                        merged = true;
                        newValue = tileArray[startRow][startCol].getValue() * 2;
                        endRow = i;
                    }
                    i++;
                }
                type = merged ? "merge" : "slide";

                var moveAction = new MoveAction(startRow, startCol, endRow, startCol, tileArray[startRow][startCol].getValue(), newValue, type);
                movePlan.addAction(moveAction);
                movePlan.setChanged(movePlan.isChanged() ? true : moveAction.hasPositionChange());
                movePlan.addScore(moveAction.getNewValue());
            }
        }
        return movePlan;
    }
    

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
