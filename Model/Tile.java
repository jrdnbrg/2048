package Model;
import java.util.UUID;

public class Tile {
    private int value;
    private String id;
    private boolean merged;

    //constructor
    public Tile(int value) {
        this.value = 0;
        //this.id = UUID.randomUUID().toString(); //to create unique id and and to convrt it into
        // string 
        this.merged = false;
    }

    boolean isEmpty(){
        return value == 0;
    }

    void reset() {
        value = 0;
        merged = false;
    }

    void setValue(int newValue) {

    }


}


    

