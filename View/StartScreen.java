package View;

import java.awt.*;
import javax.swing.*;

import Controller.Controller;

public class StartScreen extends JPanel {
    public StartScreen(Controller controller) {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(600, 600));

        JLabel background = new JLabel(new ImageIcon(StartScreen.class.getResource("2048.png")));
        background.setBounds(0, 0, 600, 600);
        layeredPane.add(background, Integer.valueOf(0));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBounds(0, 390, 600, 210);
        buttonPanel.setOpaque(false);

        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        startButton.setPreferredSize(new Dimension(259, 61));
        startButton.setBackground(new Color(153, 221, 255));
        startButton.setBorder(BorderFactory.createLineBorder(new Color(119, 170, 221), 4));
        startButton.addActionListener(e -> controller.startGame());

        buttonPanel.add(startButton, BorderLayout.NORTH);
        layeredPane.add(buttonPanel, Integer.valueOf(1));
        add(layeredPane);
    }
}