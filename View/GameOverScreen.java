package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Controller.Controller;

/**
 * JPanel that shows the game over screen.
 */
public class GameOverScreen extends JPanel {
    public GameOverScreen(Controller controller) {
        // Make a layered pane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(600,600));

        // Make a background image
        ImageIcon originalIcon = new ImageIcon(GameOverScreen.class.getResource("images/Gameover.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(600, 600, Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(scaledImage));
        background.setBounds(0, 0, 600, 600);

        // Make a score panel
        JPanel scorePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        scorePanel.setBounds(0,300,600,90);
        scorePanel.setOpaque(false);

        // Make a score label
        JLabel scoreLabel = new JLabel();
        scoreLabel.setText("Score : " + controller.getScore());
        scoreLabel.setBounds(250,250,50,80);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Make a button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBounds(0, 390, 600, 210);
        buttonPanel.setOpaque(false);

        // Make a play again button
        JButton playAgain = new JButton("Play Again");
        playAgain.setFont(new Font("Arial", Font.BOLD, 24));
        playAgain.setBounds(0,300,50,10);
        playAgain.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(119, 170, 221), 4),
            BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        playAgain.setBackground(new Color(153, 221, 255));
        playAgain.setOpaque(true);
        playAgain.addActionListener(e -> controller.restart());

        // Add all elements to the layered pane
        scorePanel.add(scoreLabel, BorderLayout.NORTH);
        buttonPanel.add(playAgain, BorderLayout.CENTER);
        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(scorePanel, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(buttonPanel, JLayeredPane.PALETTE_LAYER);

        // Add the layered pane to the view
        add(layeredPane);
    }    
}

