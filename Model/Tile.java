package Model;

public class Tile {
    public int value; //0 means empty
    public String id; 
    public boolean merged; //to check if the tile is merged or no

    //constructor
    public Tile(String id) {
        this.value = 0; 
        this.id = id;
        this.merged = false; //initially tiles are not merged
    }

    //to check if the tile is empty or not
    boolean isEmpty() {
        return value == 0;
    }

    //to reset the tile to an empty state
    void reset() {
        value = 0;
        merged = false;
    }

    //to set a new value
    void setValue(int newValue) {
        this.value = newValue;

    }

    //to return the current value
    public int getValue() {
        return value;
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


}


    

