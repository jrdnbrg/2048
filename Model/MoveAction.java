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

    public MoveAction(int startRow, int startCol, int endRow, int endCol, int oldValue, int newValue, String type) {
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
}