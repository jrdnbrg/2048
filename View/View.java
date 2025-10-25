package View;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Controller.Controller;
import Model.*;

/**
 * Displays everything on the screen.
 */
public class View extends JPanel {
    TileLabel[][] tiles = new TileLabel[SIZE][SIZE];
    private JLabel scoreLabel;
    private JLabel bestScoreLabel;
    
    private static final int MARGIN = 10;
    private static final int TILE_SIZE = 100;
    private static final int GAP = 10;
    private static final int SIZE = 4;
   
    private Controller controller;
    
    public View(Controller controller) {
        this.controller = controller;
        
        // Display the start screen
        setLayout(new BorderLayout());
        add(new StartScreen(controller), BorderLayout.CENTER);

        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                controller.onKeyPressed(e);
            }
        });
    }

    /**
     * Builds the grid in the center of the screen.
     */
    public void buildGrid() {
        setBackground(new Color(255, 255, 255));
        setLayout(new BorderLayout());

        Font tileFont = new Font("Arial", Font.BOLD, 32);

        // Make a layered pane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(450, 450));
        layeredPane.setBackground(new Color(221, 221, 221));
        layeredPane.setOpaque(true);

        // Make the grid
        JPanel backgroundGrid = new JPanel(new GridLayout(SIZE, SIZE, GAP, GAP));
        backgroundGrid.setBackground(new Color(221, 221, 221));
        backgroundGrid.setBounds(10, 10, (TILE_SIZE + GAP) * SIZE - GAP, (TILE_SIZE + GAP) * SIZE - GAP);
        for (int i = 0; i < SIZE * SIZE; i++) {
            JPanel cell = new JPanel();
            cell.setBackground(new Color(255, 255, 255));
            backgroundGrid.add(cell);
        }

        // Add the grid to the layeredPane 
        layeredPane.add(backgroundGrid, JLayeredPane.DEFAULT_LAYER);

        // Initialize the grid tiles
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                TileLabel tile = new TileLabel("");
                tile.setOpaque(true);
                tile.setBackground(new Color(255, 255, 255));
                tile.setForeground(Color.DARK_GRAY);
                tile.setFont(tileFont);
                tile.setBounds(getTileX(c), getTileY(r), TILE_SIZE, TILE_SIZE);
                tiles[r][c] = tile;
                layeredPane.add(tile, JLayeredPane.PALETTE_LAYER);
            }
        }

        // Set the grid in the center of the frame
        JPanel setter = new JPanel();
        setter.setLayout(new GridBagLayout());
        setter.setBackground(new Color(250, 248, 239));
        setter.add(layeredPane, new GridBagConstraints()); 

        add(setter, BorderLayout.CENTER);
    }

    /**
     * Builds a panel containing the "new game" button, the score label and the best score label.
     */
    public void buildTopPanel() {
        Font scoreFont = new Font("Arial", Font.BOLD, 17);

        // Make a score panel
        JPanel scorePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10,5));
        scorePanel.setOpaque(false);

        // Make a score label
        scoreLabel = new JLabel();
        scoreLabel.setText("Score : 0");
        scoreLabel.setFont(scoreFont);
        scoreLabel.setFocusable(false);

        // Make a best score label
        bestScoreLabel = new JLabel();
        bestScoreLabel.setFont(scoreFont);
        bestScoreLabel.setText("Best score : 0");

        scorePanel.add(bestScoreLabel);
        scorePanel.add(scoreLabel);

        // Make a new game button
        JButton newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Arial", Font.BOLD, 17));
        newGameButton.setFocusable(false);
        newGameButton.setBackground(new Color(119, 170, 221));
        newGameButton.setForeground(Color.WHITE);
        newGameButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        newGameButton.addActionListener(e -> controller.restart());

        // Make a top panel and add it to the screen above the grid
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(250, 248, 239));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20)); 
        topPanel.add(newGameButton, BorderLayout.WEST);
        topPanel.add(scorePanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
    }

    public void setScore(int score) {
        scoreLabel.setText("Score : " + score);
    }

    public void setBestScore(int bestScore) {
        bestScoreLabel.setText("best score : " + bestScore);
    }

    public int getTileX(int col) {
        return MARGIN + col * (TILE_SIZE + GAP);
    }

    public int getTileY(int row) {
        return MARGIN + row * (TILE_SIZE + GAP);
    }

    public void updateBoard(Tile[][] grid) {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                int value = grid[r][c].getValue();
                TileLabel tile = tiles[r][c];
                if (value == 0) {
                    tile.setText("");
                    tile.setBackground(new Color(255, 255, 255));
                } else {
                    tile.setText(String.valueOf(value));
                    tile.setBackground(getTileColor(value));
                }
                tile.setBounds(getTileX(c), getTileY(r), TILE_SIZE, TILE_SIZE);
            }
        }
    }

    public void implementMoves(MovePlan movePlan) {
        ArrayList<MoveAction> actions = movePlan.getActions();
        if (actions.isEmpty()) return;

        Timer timer = new Timer(15, null);
        timer.addActionListener(new ActionListener() {
            int step = 0;
            int steps = 10;

            @Override
            public void actionPerformed(ActionEvent e) {
                step++;

                for (MoveAction action : actions) {
                    TileLabel tile = tiles[action.getStartRow()][action.getStartCol()];

                    if (action.isSlide() || action.isMerge()) {
                        int startX = getTileX(action.getStartCol());
                        int startY = getTileY(action.getStartRow());
                        int endX = getTileX(action.getEndCol());
                        int endY = getTileY(action.getEndRow());
                        int newX = startX + (endX - startX) * step / steps;
                        int newY = startY + (endY - startY) * step / steps;
                        tile.setBounds(newX, newY, TILE_SIZE, TILE_SIZE);
                    } else if (action.isSpawn()) {
                        TileLabel spawnTile = tiles[action.getEndRow()][action.getEndCol()];
                        spawnTile.setText(String.valueOf(action.getNewValue()));
                        spawnTile.setBackground(getTileColor(action.getNewValue()));
                    }
                }

                if (step >= steps) {
                    ((Timer)e.getSource()).stop();
                    // Commit final positions
                    controller.applyMoveAfterAnimation(movePlan);
                    updateBoard(controller.getGrid());
                }
            }
        });

        timer.start();
    }

    /**
     * Show the game over screen.
     */
    public void gameOverScreen() {
        GameOverScreen gameOverScreen = new GameOverScreen(controller);
        add(gameOverScreen);
    }

    /**
     * Get the color of a tile based on the value.
     * 
     * The colors are from the light qualitative colour scheme made by Paul Tol to be color-blind friendly.
     * Tol, P. (2021). Colour schemes (SRON/EPS/TN/09-002, Issue 3.2). SRON Netherlands Institute for Space Research.
     * https://sronpersonalpages.nl/~pault/data/colourschemes.pdf
     * 
     * @param value of a tile
     * @return color of tile
     */
    private Color getTileColor(int value) {
        int v = value;
        while (v > 256) {
            v /= 256;
        }

        switch (v) {
            case 2: return new Color(255, 170, 187);
            case 4: return new Color(238,136,102);
            case 8: return new Color(238,221,136);
            case 16: return new Color(170,170,0);
            case 32: return new Color(187,204,51);
            case 64: return new Color(68,187,153);
            case 128: return new Color(153,221,255);
            case 256: return new Color(119,170,221);
            default: return new Color(221,221,221);
        }
    }
}