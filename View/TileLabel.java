package View;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class TileLabel extends JLabel {
    public TileLabel(String text) {
        super(text, SwingConstants.CENTER);
        setOpaque(false);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
    }
}
