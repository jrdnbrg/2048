package View;
import Model.*;
import Controller.Controller;
import java.awt.*;
import javax.swing.*;

public class GameOverScreen extends JPanel {
    JLabel scoreViewer;
    public GameOverScreen(Controller controller) {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(600,600));

        ImageIcon originalIcon = new ImageIcon(GameOverScreen.class.getResource("Gameover.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(600, 600, Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(scaledImage));
        background.setBounds(0, 0, 600, 600);

        background.setBounds(0,0,600,600);
        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);

        JPanel scorePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        scorePanel.setBounds(0,300,600,90);
        scorePanel.setOpaque(false);

        scoreViewer = new JLabel();
        scoreViewer.setText("Score : " + 0 );
        scoreViewer.setBounds(250,250,50,80);
        scoreViewer.setFont(new Font("Arial", Font.BOLD, 24));


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBounds(0, 390, 600, 210);
        buttonPanel.setOpaque(false);

        JButton startAgain = new JButton("Play Again");
        startAgain.setFont(new Font("Arial", Font.BOLD, 24));
        startAgain.setBounds(0,300,50,10);
        startAgain.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0X1b4b9d), 4),
            BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        startAgain.setBackground(new Color(119, 170, 221));
        startAgain.setOpaque(true);
        startAgain.setFocusable(false);
        startAgain.addActionListener(e -> controller.startGame());

        scorePanel.add(scoreViewer, BorderLayout.NORTH);
        buttonPanel.add(startAgain, BorderLayout.CENTER);
        layeredPane.add(scorePanel, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(buttonPanel, JLayeredPane.PALETTE_LAYER);
        add(layeredPane);


    }

    public void setScore(int score) {
        scoreViewer.setText("score : " + score);
    }

    
}

