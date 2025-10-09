package Model;

import java.util.*;

public class Grid {
    private static final int GRID_SIZE = 4;
    private Tile[][] tileArray;
    private int score;

    Grid() {
        tileArray = new Tile[GRID_SIZE][GRID_SIZE];

    }
    
    public void initialize() {
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                Tile tile = new Tile();
                tile.setValue(0);
                tileArray[r][c] = tile;
            }
        }

        var randomHashSet = new HashSet<Integer>();
        var random = new Random();
        while (randomHashSet.size() < 2) {
            randomHashSet.add(random.nextInt(GRID_SIZE));
        }
        

    }
}
