package Controller;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Model.*;
import View.*;


public class Controller {
    private Grid model;
    private GridViewer view;
    private boolean canMakeMove;

    public Controller(Grid model) {
        this.model = model;
        canMakeMove = true;
    }

    public void setView(GridViewer view) {
        this.view = view;
    }

    public void onKeyPressed(KeyEvent e) {

        Direction dir = switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> Direction.LEFT;
            case KeyEvent.VK_RIGHT -> Direction.RIGHT;
            case KeyEvent.VK_UP -> Direction.UP;
            case KeyEvent.VK_DOWN -> Direction.DOWN;
            default -> null;
        };

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

        view.implementMove(movePlan);
    }

    public void applyMoveAfterAnimation(MovePlan movePlan) {
        // in view, once animation is complete call controller.applyMoveAfteranimation(movePlan)

        model.applyMove(movePlan);
        view.updateBoard();
        view.setScore(model.getScore());
        checkGameOver();
        canMakeMove = true;
    } 

    public void checkGameOver() {
        if (!model.canMove()) {
            canMakeMove = false;
            view.gameOverScreen();
        }
    }
}
