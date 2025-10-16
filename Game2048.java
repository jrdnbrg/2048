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
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        view.requestFocusInWindow();

        controller.startGame();
    } 
}
