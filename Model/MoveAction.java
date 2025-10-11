package Model;


/**
 * Represents a single movement.
 */
public class MoveAction {
    private int startRow;
    private int startCol;
    private int endRow;
    private int endCol;
    private int oldValue;
    private int newValue;
    private String type; // "slide" or "merge" or "spawn"
    // private int tileId

    /**
     * Constructor.
     * @param startRow of tile
     * @param startCol of tile
     * @param endRow of tile
     * @param endCol of tile
     * @param oldValue of tile
     * @param newValue of tile
     * @param type of move
     */
    MoveAction(int startRow, int startCol, int endRow, int endCol, int oldValue, int newValue, String type) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.type = type;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getStartCol() {
        return startCol;
    }

    public int getEndRow() {
        return endRow;
    }

    public int getEndCol() {
        return endCol;
    }

    public int getOldValue() {
        return oldValue;
    }

    public int getNewValue() {
        return newValue;
    }

    public boolean isSlide() {
        return type.equals("slide");
    }

    public boolean isMerge() {
        return type.equals("merge");
    }

    public boolean isSpawn() {
        return type.equals("spawn");
    }

    public boolean hasPositionChange() {
        if (startRow == endRow && startCol == endCol) {
            return false;
        }
        return true;
    }

    // For testing purposes
    @Override
    public String toString() {
        String str = "";
        str += "startRow: " + startRow + "\n";
        str += "startCol: " + startCol + "\n";
        str += "endRow: " + endRow + "\n";
        str += "endCol: " + endCol + "\n";
        str += "oldValue: " + oldValue + "\n";
        str += "newValue: " + newValue + "\n";
        str += "type: " + type + "\n\n";
        return str;
    }

}
