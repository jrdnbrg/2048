import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.Border;

public class Board extends JFrame{
    private static final int GRID_SIZE = 4;

    private JFrame frame;
    private JPanel topRow;
    private JPanel gridPanel;
    private JLabel score;

    TileLabel[][] grid;

    Board() {
        setUpBoard();
    }

    public void setUpBoard() {
        
        // Create frame
        frame = new JFrame();
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));

        // Create topRow
        topRow = new JPanel();
        topRow.setMaximumSize(new Dimension(400, 50));
        
        score = new JLabel("0");
        score.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Add topRow to frame
        topRow.add(score);
        frame.add(topRow);

        // Create gridPanel
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(4, 4, 10, 10));
        gridPanel.setMaximumSize(new Dimension(300, 300));
        gridPanel.setBackground(Color.PINK);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create grid array
        grid = new TileLabel[GRID_SIZE][GRID_SIZE];
        
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                // Create tileLabel
                TileLabel tileLabel = new TileLabel();
                tileLabel.setValue(0);

                // Add tileLabel to gridPanel
                gridPanel.add(tileLabel);

                // Initialize arrays
                grid[r][c] = tileLabel;
            } 
        }

        // Add gridPanel to frame
        frame.add(gridPanel);

        // Set the value of two random tiles to 2;
        Random random = new Random();
        grid[random.nextInt(GRID_SIZE)][random.nextInt(GRID_SIZE)].setValue(2);
        grid[random.nextInt(GRID_SIZE)][random.nextInt(GRID_SIZE)].setValue(2);
        // !!!!!!!!!!!!!!!!! MAKE SURE RANDOM VALUES ARE NOT THE SAME !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        updateGrid();

        // Show frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }

    /**
     * Update the grid. Call this method everytime the grid changes.
     */
    public void updateGrid() {
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                grid[r][c].displayTile();
            }
        }

    }
    public static void main(String[] args) {
        new Board();
    }
}

