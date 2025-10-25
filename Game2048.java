import javax.swing.ImageIcon;
import javax.swing.JFrame;

import Controller.Controller;
import Model.Model;
import View.View;

public class Game2048 {
    public static void main(String[] args) {
        Model model = new Model();
        Controller controller = new Controller(model);
        View view = new View(controller);
        controller.setView(view);
        
        JFrame frame = new JFrame("2048"); 
        frame.add(view);
        frame.setSize(600, 600);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        ImageIcon image = new ImageIcon(Game2048.class.getResource("View/images/icon.png"));
        frame.setIconImage(image.getImage());
        frame.setVisible(true);
    } 
}
