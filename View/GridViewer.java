package View;

import javax.imageio.plugins.jpeg.JPEGImageReadParam;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import Model.*; //to import the model class
import Model.Grid.*;

public class GridViewer extends JFrame  {
    TileLabel[][] tiles = new TileLabel[SIZE][SIZE];
    
    private static final int MARGIN = 10;
    private static final int TILE_SIZE = 100;
    private static final int GAP = 10;
    private static final int SIZE = 4;
    private int[][] board = new int[SIZE][SIZE];
   // private JLabel[][] tile  = new JLabel[SIZE][SIZE]; //creates an array for storing the tiles
    private Random random = new Random();

    //constructor
    public GridViewer() {

        //setup frmawe
        setTitle("2048 Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(0xfaf8ef));
        setResizable(false);

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

       
       /*  JPanel gridPanel = new JPanel();
        //allignment of the gridPanel in the centre
        gridPanel.setBackground(new Color(0xf4b6c2));
        gridPanel.setLayout(new GridLayout(SIZE,SIZE,10,10));
        gridPanel.setPreferredSize(new Dimension(350,350)); */

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

        //backGrid.add(gridPanel, JLayeredPane.PALETTE_LAYER);

        //To get the grid in the center of the frame
        JPanel setter = new JPanel();
        setter.setLayout(new GridBagLayout());
        setter.setBackground(new Color(0xfaf8ef));
        setter.add(layeredPane, new GridBagConstraints()); 

        add(setter, BorderLayout.CENTER);


        
        JButton scoreButton = new JButton();
        scoreButton.setText("score");
        scoreButton.setBounds(20,20,100,50);
        scoreButton.setFocusable(false);


        /*ImageIcon newGameLogo = new ImageIcon("imageb.jpg");
        Image img = newGameLogo.getImage();
        Image newimg = img.getScaledInstance(115, 35, java.awt.Image.SCALE_SMOOTH);
        newGameLogo = new ImageIcon(newimg); */

        JButton newGameButton = new JButton("New Game");
        newGameButton.setBounds(20,20,100,50);
        newGameButton.setFocusable(false);
        newGameButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == newGameButton) {
                    dispose();
                    GridViewer gridViewer = new GridViewer();
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
        setLocationRelativeTo(null);
        spawnRandomTile();
        spawnRandomTile();
        updateBoard();



        setVisible(true);


    }

    public int getTileY(int row) {
        return MARGIN + row * (TILE_SIZE + GAP);
    }

    public int getTileX(int col) {
        return MARGIN + col * (TILE_SIZE + GAP);
    }


    /*public String scoreBoard() {
        int score = 0; //initial score is zero
        if()
        //code which convert the score into the string so that it could be updated on the button
        return 
    }*/



    public void implementMoves(MovePlan movePlan) {
        int animationDuration = 100; 
        int frameDelay = 10;
        int steps = animationDuration / frameDelay;

        for (MoveAction action : movePlan.getActions()) {
            int sr = action.getStartRow();
            int sc = action.getStartCol();
            int er = action.getEndRow();
            int ec = action.getEndCol();
            TileLabel tile = tiles[sr][sc] ;

            if(action.isSpawn()) {
                //Show new tile 
                board[er][ec] = action.getNewValue();
                tiles[er][ec].setText(String.valueOf(action.getNewValue()));
                tiles[er][ec].setBackground(getTileColor(action.getNewValue()));
                continue;
            }

            if(!action.hasPositionChange()) {
                continue;
            }

            int startX = getTileX(sc);
            int startY = getTileY(sr);
            int endX = getTileX(ec);
            int endY = getTileX(er);
            JLayeredPane layeredPane = new JLayeredPane();

            Timer timer = new Timer(frameDelay, null);
            final int[] currentStep = {0};

            timer.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    double progress = (double) currentStep[0] / steps; //to keep track of the animation
                    int newX = (int) (startX + progress * (endX - startX));
                    int newY = (int) (startY + progress * (endY - startY));
                    tile.setLocation(newX, newY);
                    layeredPane.repaint();
                    currentStep[0]++;

                    if(currentStep[0] >= steps) {
                        timer.stop();
                        tile.setLocation(endX, endY);

                        if (action.isMerge()) {
                            //update the merged value 
                            board[er][ec] = action.getNewValue();
                            tile.setText(String.valueOf(action.getNewValue()));
                        } else {
                            //just move the tile
                            board[er][ec] = board[sr][sc];

                        }

                        board[sr][sc] = 0;

                        updateBoard();
                    }


                }
                
            });

            timer.start();

            
        }
        //JButton scoreButton = new JButton();
        //scoreButton.setText("Score: " + movePlan.getScoreGained());


    }


    public void spawnRandomTile() {
        int emptyCount = 0;//initial empty count 

        //count all empty cells
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (board[r][c] == 0) {
                    emptyCount++;
                }

            }
        }

        if (emptyCount == 0) {
            return; // no space left
        }

        //for a random position of the tile 
        int target = random.nextInt(emptyCount);
        int count = 0;

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (board[r][c] == 0) {
                    if (count == target) {
                        board[r][c] = 2;
                        return;
                    }
                    count++;
                }
            }
        }
                
    }
    
    
    
    public void updateBoard() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                int value = board[r][c];
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
                
            }
        }

    
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

    public static void main(String[] args) {
        GridViewer gridViewer = new GridViewer();
        MoveAction action = new MoveAction(0, 0, 0, 0, 0, 2, "spawn");
        MovePlan movePlan = new MovePlan(null);
        movePlan.addAction(action);
        gridViewer.implementMoves(movePlan);
    
    }

       
        

    


}
