package Controller;

import java.awt.event.KeyEvent;

import Model.*;
import View.*;

/**
 * Controls the game. Acts as a bridge between the model and view.
 */
public class Controller {
    private Model model;
    private View view;
    private boolean canMakeMove;

    public Controller(Model model) {
        this.model = model;
        canMakeMove = false;
    }

    public void setView(View view) {
        this.view = view;
    }

    public Tile[][] getGrid() {
        return model.getGridCopy();
    }

    public int getScore() {
        return model.getScore();
    }

    public void startGame() {
        // Clear the view panel
        view.removeAll();
        view.revalidate();
        view.repaint();

        // Show the main game screen and initialize the grid
        view.buildGrid();
        view.buildTopPanel();
        view.setBestScore(model.getBestScore());
        view.implementMoves(model.initialize());
        view.repaint();
        canMakeMove = true; 
    }

    public void onKeyPressed(KeyEvent e) { 
        Direction dir;

        // Translate arrow key to direction
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
                dir = null;
                break;
        }

        // Makes a move if possible
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

        // Show the move in the view
        view.implementMoves(movePlan);
    }

    public void applyMoveAfterAnimation(MovePlan movePlan) {
        model.applyMove(movePlan);
        view.setScore(model.getScore());
        checkGameOver();
        canMakeMove = true;
    } 

    public void checkGameOver() {
        if (!model.canMove()) {
            canMakeMove = false;

            // Record the user's best score
            model.setBestScore();
            view.setBestScore(model.getBestScore());
            
            // Show the end screen
            view.removeAll();
            view.revalidate();
            view.repaint();
            view.gameOverScreen();
        }
    }

    public void restart() {
        model.setScore(0);
        view.setScore(0);
        startGame();
        canMakeMove = true;
    }
}
