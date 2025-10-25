import javax.swing.*;
import Controller.*;
import Model.*;
import View.*;

public class Game2048 {
    public static void main(String[] args) {
        Model model = new Model();
        Controller controller = new Controller(model);
        GridViewer view = new GridViewer(controller);
        controller.setView(view);

        
        JFrame frame = new JFrame("2048"); 
        frame.add(view);
        frame.setSize(600, 600);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        ImageIcon image = new ImageIcon("View/icon.png"); //create an image icon 
        frame.setIconImage(image.getImage());
        frame.setVisible(true);
    } 
}
