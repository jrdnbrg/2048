package Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Grid extends JFrame implements KeyListener {
    private static final int SIZE = 4;
    private static final int TILE_SIZE = 100;
    private static final int GAP = 15;
    private static final int GRID_PADDING = 20;

    private final TileLabel[][] tiles = new TileLabel[SIZE][SIZE];
    private final int[][] board = new int[SIZE][SIZE];
    private final Random random = new Random();
    private JPanel gridPanel;

    public Grid() {
        setTitle("2048 Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 550);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(0xfaf8ef));
        setResizable(false);

        // Background grid (for visual 4x4 look)
        JPanel backgroundGrid = new JPanel(new GridLayout(SIZE, SIZE, GAP, GAP));
        backgroundGrid.setBackground(new Color(0xf4b6c2));
        for (int i = 0; i < SIZE * SIZE; i++) {
            JPanel cell = new JPanel();
            cell.setBackground(new Color(0xf9d8e2));
            backgroundGrid.add(cell);
        }

        // Foreground grid for movable tiles (absolute positioning)
        gridPanel = new JPanel(null);
        gridPanel.setPreferredSize(new Dimension(450, 450));
        gridPanel.setBackground(new Color(0xf4b6c2));
        gridPanel.add(backgroundGrid);
        backgroundGrid.setBounds(GRID_PADDING, GRID_PADDING, 450 - 2 * GRID_PADDING, 450 - 2 * GRID_PADDING);

        // Create tile labels (movable ones)
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                TileLabel tile = new TileLabel("");
                tile.setFont(new Font("Arial", Font.BOLD, 32));
                tile.setBackground(new Color(0xf9d8e2));
                tile.setForeground(Color.DARK_GRAY);
                tile.setBounds(getTileX(c), getTileY(r), TILE_SIZE, TILE_SIZE);
                tile.setVisible(false); // hidden until spawned
                tiles[r][c] = tile;
                gridPanel.add(tile);
            }
        }

        // Top bar
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> {
            dispose();
            new Grid();
        });
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(0xfaf8ef));
        topPanel.setPreferredSize(new Dimension(10, 50));
        topPanel.add(newGameButton, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        add(gridPanel, BorderLayout.CENTER);
        addKeyListener(this);
        setFocusable(true);
        setLocationRelativeTo(null);

        spawnRandomTile();
        spawnRandomTile();
        updateBoard();

        setVisible(true);
    }

    private int getTileX(int col) {
        return GRID_PADDING + col * (TILE_SIZE + GAP);
    }

    private int getTileY(int row) {
        return GRID_PADDING + row * (TILE_SIZE + GAP);
    }

    private void spawnRandomTile() {
        int emptyCount = 0;
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++)
                if (board[r][c] == 0) emptyCount++;

        if (emptyCount == 0) return;

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

    private void updateBoard() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                int value = board[r][c];
                TileLabel t = tiles[r][c];
                if (value == 0) {
                    t.setVisible(false);
                } else {
                    t.setVisible(true);
                    t.setText(String.valueOf(value));
                    t.setBackground(getTileColor(value));
                }
            }
        }
    }

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

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                TileLabel t = tiles[r][c];
                if (t.isVisible()) {
                    if (key == KeyEvent.VK_LEFT) animateMove(t, -1, 0);
                    if (key == KeyEvent.VK_RIGHT) animateMove(t, 1, 0);
                    if (key == KeyEvent.VK_UP) animateMove(t, 0, -1);
                    if (key == KeyEvent.VK_DOWN) animateMove(t, 0, 1);
                }
            }
        }
    }

    private void animateMove(TileLabel tile, int dx, int dy) {
        Timer timer = new Timer(5, null);
        timer.addActionListener(e -> {
            tile.setLocation(tile.getX() + dx, tile.getY() + dy);
            // stop after 50px movement for demo
            if (Math.abs(tile.getX() - getTileX(0)) > 80 || Math.abs(tile.getY() - getTileY(0)) > 80) {
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        new Grid();
    }
}
