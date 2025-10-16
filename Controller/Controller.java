package Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Model.*;
import View.*;

/**
 * Controls the game. Acts as a bridge between the model and view.
 */
public class Controller {
    private Model model;
    private GridViewer view;
    private boolean canMakeMove;

    public Controller(Model model) {
        this.model = model;
        canMakeMove = true;
    }

    public void setView(GridViewer view) {
        this.view = view;
    }

    public Tile[][] getGrid() {
        return model.getGridCopy();
    }

    public void startGame() {
        view.implementMoves(model.initialize());
    }

    public void onKeyPressed(KeyEvent e) {
        
        Direction dir = null;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                dir = Direction.LEFT;
                break;
            case KeyEvent.VK_RIGHT:
                dir = Direction.RIGHT;
                break;
            case KeyEvent.VK_UP:
                dir = Direction.UP;
                break;
            case KeyEvent.VK_DOWN:
                dir = Direction.DOWN;
                break;
            default:
                break;
        }

        if (dir != null && canMakeMove) {
            handleMove(dir);
        }
    }

    public void handleMove(Direction dir) {
        canMakeMove = false;
        MovePlan movePlan = model.computeMove(dir);
        
        if (!movePlan.isChanged()) {
            canMakeMove = true;
            return;
        }

        view.implementMoves(movePlan);
    }

    public void applyMoveAfterAnimation(MovePlan movePlan) {
        model.applyMove(movePlan);
        view.repaint();
        view.setScore(model.getScore());
        checkGameOver();
        canMakeMove = true;
    } 

    public void checkGameOver() {
        if (!model.canMove()) {
            canMakeMove = false;
            //view.gameOverScreen();
            restart();
        }
    }

    public void restart() {
        view.implementMoves(model.initialize());
        model.setScore(0);
        view.setScore(0);
        canMakeMove = true;
    }
}
