package View;

import javax.imageio.plugins.jpeg.JPEGImageReadParam;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import Model.*; //to import the model class

public class GridViewer extends JFrame  {
    TileLabel[][] tiles = new TileLabel[SIZE][SIZE];
    private JButton scoreButton;
    
    private static final int MARGIN = 10;
    private static final int TILE_SIZE = 100;
    private static final int GAP = 10;
    private static final int SIZE = 4;
   
    private Model model;
    private Random random = new Random();
    

    //constructor
    public GridViewer(Model model) {
        this.model = model;

        //setup frame
        setTitle("2048 Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(0xfaf8ef));
        setResizable(false);

        buildGrid();
        buildTopPanel();

        setLocationRelativeTo(null);
        updateBoard(model.getTileArray());

        setVisible(true);
    }

    public void buildGrid() {

        Font tileFont = new Font("Arial", Font.BOLD, 32);

        /*LayeredPanes for creating layers 
        *firt for the fixed grid and second for the tiles
        */
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(450,450));
        layeredPane.setBackground(new Color(0xbbada0));
        layeredPane.setOpaque(true);

        //Background grid(fixed tiles)
        JPanel backgroundGrid = new JPanel(new GridLayout(SIZE,SIZE,GAP,GAP));
        backgroundGrid.setBackground(new Color(0xbbada0));
        backgroundGrid.setBounds(10, 10, (TILE_SIZE + GAP) * SIZE - GAP, 
            (TILE_SIZE + GAP) * SIZE - GAP);
        
        for (int i = 0; i < SIZE * SIZE; i++) {
            JPanel cell = new JPanel();
            cell.setBackground(new Color(0xf9d8e2));
            backgroundGrid.add(cell);
        }

        //Adding the fixed grid add back layer 
        layeredPane.add(backgroundGrid, JLayeredPane.DEFAULT_LAYER);

        //for initializing the gridtiles
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                
                TileLabel tile = new TileLabel("");
                tile.setOpaque(true);
                tile.setBackground(new Color(0xf9d8e2));
                tile.setForeground(Color.DARK_GRAY);
                tile.setFont(tileFont);
                tile.setBounds(getTileX(c), getTileY(r), TILE_SIZE, TILE_SIZE);
                //tile.setVisible(false);
                tiles[r][c] = tile;
                layeredPane.add(tile, JLayeredPane.PALETTE_LAYER); //top layer
            }
        }


        //To get the grid in the center of the frame
        JPanel setter = new JPanel();
        setter.setLayout(new GridBagLayout());
        setter.setBackground(new Color(0xfaf8ef));
        setter.add(layeredPane, new GridBagConstraints()); 

        add(setter, BorderLayout.CENTER);
    }

    /*
     * 
     */
    public void buildTopPanel() {
        scoreButton = new JButton();
        scoreButton.setText("score : " + model.getScore());
        scoreButton.setBounds(20,20,100,50);
        scoreButton.setFocusable(false);

        JButton newGameButton = new JButton("New Game");
        newGameButton.setBounds(20,20,100,50);
        newGameButton.setFocusable(false);
        newGameButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == newGameButton) {
                    dispose();
                    Model newModel = new Model();
                    MovePlan spawnPlan = newModel.initialize();
                    GridViewer newViewer = new GridViewer(newModel);
                    newViewer.implementMoves(spawnPlan);
                    
                }
            }
            
        });

        //panel to store button
        JPanel topPanel = new JPanel(new BorderLayout() );
        topPanel.setAlignmentY(10); 
        topPanel.setBackground(new Color(0xfaf8ef)); // match window bg
        topPanel.setPreferredSize(new Dimension(10, 50));
        topPanel.add(newGameButton, BorderLayout.WEST); // add button to panel
        topPanel.add(scoreButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

    }

    public void updateScore() {
    scoreButton.setText("score : " + model.getScore());
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
                    tiles[r][c].setText("");
                    tiles[r][c].setBackground(new Color(0xf9d8e2));
                    //tiles[r][c].setVisible(false);
                }
                else {
                    //tiles[r][c].setVisible(true);
                    tiles[r][c].setText(String.valueOf(value));
                    tiles[r][c].setBackground(getTileColor(value));
                }
                tile.setBounds(getTileX(c), getTileY(r), TILE_SIZE, TILE_SIZE);
            }
        }
    }

    

    public void implementMoves(MovePlan movePlan) {
        java.util.List<MoveAction> actions = movePlan.getActions();
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
                    model.applyMove(movePlan);
                    updateBoard(model.getTileArray());
                    updateScore();
                }
            }
        });
        timer.start();
    }

    //setting the color of tile based on the value
    
    private Color getTileColor(int value) {
        switch (value) {
            case 2: return new Color(0xffe4ec);
            case 4: return new Color(0xf9c6d2);
            case 8: return new Color(0xf7a6ba);
            case 16: return new Color(0xf284ac);
            case 32: return new Color(0xef639b);
            case 64: return new Color(0xeb4b92);
            case 128: return new Color(0xe33680);
            case 256: return new Color(0xcc2b70);
            case 512: return new Color(0xb02064);
            case 1024: return new Color(0x8d1755);
            case 2048: return new Color(0x6b0f47);
            default: return new Color(0xf9d8e2);
        }
    }

    //for testing 
    public static void main(String[] args) {
        Model model = new Model();
        MovePlan spawnPlan = model.initialize(); // spawn initial tiles
        GridViewer gridViewer = new GridViewer(model);

        // Animate the initial tiles
        gridViewer.implementMoves(spawnPlan);

        // Optional: simulate a move for testing
        new Timer(1000, e -> {
            //MovePlan movePlan = model.computeMove(Direction.RIGHT);
            MovePlan movePlan = model.computeMove(Direction.LEFT);
            model.applyMove(movePlan);
            gridViewer.implementMoves(movePlan);
        }).start();

        

        
    }



}