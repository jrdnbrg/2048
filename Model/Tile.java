package Model;

/**
 * Represents a single tile.
 */
public class Tile {
    public int value;
    public String id; 
    public boolean merged;

    public Tile(String id) {
        this.value = 0; 
        this.id = id;
        this.merged = false;
    }

    public int getValue() {
        return value;
    }

    void setValue(int value) {
        this.value = value;
    }

    String getId() {
        return id;
    }

    boolean isMerged() {
        return merged;
    }

    void setMerged(boolean merged){
        this.merged = merged;
    }

    boolean isEmpty() {
        return value == 0;
    }

    void reset() {
        value = 0;
        merged = false;
    }
}