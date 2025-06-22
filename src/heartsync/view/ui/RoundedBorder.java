package heartsync.view.ui;

import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RoundedBorder extends EmptyBorder {
    private final Color color;

    public RoundedBorder(Color color) {
        super(6, 16, 6, 16);
        this.color = color;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(x, y, width - 1, height - 1, 14, 14);
    }
} 