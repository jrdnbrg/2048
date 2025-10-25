package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import Controller.Controller;

public class StartScreen extends JPanel {
    public StartScreen(Controller controller) {
        // Make a layered pane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(600, 600));

        // Make the background image
        JLabel background = new JLabel(new ImageIcon(StartScreen.class.getResource("images/2048.png")));
        background.setBounds(0, 0, 600, 600);
        layeredPane.add(background, Integer.valueOf(0));

        // Make a button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBounds(0, 390, 600, 210);
        buttonPanel.setOpaque(false);

        // Make a start button
        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        startButton.setPreferredSize(new Dimension(259, 61));
        startButton.setBackground(new Color(153, 221, 255));
        startButton.setBorder(BorderFactory.createLineBorder(new Color(119, 170, 221), 4));
        startButton.addActionListener(e -> controller.startGame());

        // Add all elements to the screen
        buttonPanel.add(startButton, BorderLayout.NORTH);
        layeredPane.add(buttonPanel, Integer.valueOf(1));
        add(layeredPane);
    }
}