package View;

import java.awt.*;
import javax.swing.*;

public class TileLabel extends JLabel {
    private int arc = 25;
    

    public TileLabel(String text) {
        super(text, SwingConstants.CENTER);
        setOpaque(false);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
    }

    
    /*
     * For creating rounded tilelabels 
     */

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // rounded background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
        g2.dispose();

        // draw text
        super.paintComponent(g);
    }

}
