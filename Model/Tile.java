package Model;

import java.util.UUID;

public class Tile {
    private int value; //0 means empty
    private String id; 
    private boolean merged; //to check if the tile is merged or no

    //constructor
    public Tile(int value) {
        this.value = 0; 
        //this.id = UUID.randomUUID().toString(); //to create unique id and and to convrt it into
        // string 
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
    int getValue() {
        return value;
    }

    boolean isMerged() {
        return merged;
    }

    void setMerged(boolean merged){
        this.merged = merged;
    }


}


    

