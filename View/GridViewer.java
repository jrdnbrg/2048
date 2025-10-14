package View;

import javax.imageio.plugins.jpeg.JPEGImageReadParam;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;


public class GridViewer extends JFrame  {
    TileLabel[][] tiles = new TileLabel[SIZE][SIZE];
    private static final int SIZE = 4;
    private int[][] board = new int[SIZE][SIZE];
    //private JLabel[][] tiles  = new JLabel[SIZE][SIZE]; //creates an array for storing the tiles
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
       
        JPanel gridPanel = new JPanel();
        //allignment of the gridPanel in the centre
        gridPanel.setBackground(new Color(0xf4b6c2));
        gridPanel.setLayout(new GridLayout(SIZE,SIZE,10,10));
        gridPanel.setPreferredSize(new Dimension(350,350));

        //for initializing the gridtiles
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                
                tiles[r][c] = new TileLabel("");
                tiles[r][c].setBackground(new Color(0xf9d8e2));
                tiles[r][c].setForeground(Color.DARK_GRAY);
                tiles[r][c].setOpaque(false);
                tiles[r][c].setFont(tileFont);
                gridPanel.add(tiles[r][c]);
            }
        }

        //To get the grid in the center of the frame
        JPanel setter = new JPanel();
        setter.setLayout(new GridBagLayout());
        setter.setBackground(new Color(0xfaf8ef));
        setter.add(gridPanel, new GridBagConstraints());
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
        topPanel.setPreferredSize(new Dimension(10,50));
        topPanel.add(newGameButton, BorderLayout.WEST); // add button to panel
        topPanel.add(scoreButton , BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        setLocationRelativeTo(null);
        spawnRandomTile();
        spawnRandomTile();
        updateBoard();



        setVisible(true);

        




    }

    /*public String scoreBoard() {
        int score = 0; //initial score is zero
        if()
        //code which convert the score into the string so that it could be updated on the button
        return 
    }*/

    public void spawnRandomTile() {
        int emptyCount = 0;//initial empty count 

        //count all empty cells
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (board[r][c] == 0) emptyCount++;
                    
            }
        }

        if (emptyCount==0) return; //no space left

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
                    tiles[r][c].setText("");}
                else {
                    tiles[r][c].setText(String.valueOf(value));
                }
                tiles[r][c].setBackground(getTileColor(value));
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
        new GridViewer();
    }
        

    


}
