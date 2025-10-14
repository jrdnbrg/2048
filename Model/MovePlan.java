package Model;

import java.util.*;

/**
 * Represents the plan for one move.
 */
public class MovePlan {
    private ArrayList<MoveAction> actions;
    private int scoreGained;
    private boolean changed;
    private Direction dir;

    /**
     * Constructor.
     * @param dir of pressed arrow key
     */
    MovePlan(Direction dir) {
        actions = new ArrayList<MoveAction>();
        scoreGained = 0;
        changed = false;
        this.dir = dir;
    }

    public ArrayList<MoveAction> getActions() {
        return actions;
    }

    public int getScoreGained() {
        return scoreGained;
    }

    public boolean isChanged() {
        return changed;
    }

    public Direction getDirection() {
        return dir;
    }

    public void setChanged(boolean value) {
        changed = value;
    }

    public void addAction(MoveAction action) {
        actions.add(action);
    }

    public void addScore(int value) {
        scoreGained += value;
    }

    // For testing purposes
    @Override
    public String toString() {
        String str = "";
        for (MoveAction action : actions) {
            str += "Moveaction: \n\n";
            str += action.toString();
        }
        return str;
    }
}
