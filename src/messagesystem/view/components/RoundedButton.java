package messagesystem.view.components;

import messagesystem.view.ColorScheme;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RoundedButton extends JButton {
    private static final int ARC_WIDTH = 15;
    private static final int ARC_HEIGHT = 15;
    private Color hoverBackground;
    private boolean isHovered = false;

    public RoundedButton(String text) {
        super(text);
        setupButton();
    }

    private void setupButton() {
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setBackground(ColorScheme.BUTTON_BACKGROUND);
        setForeground(ColorScheme.BUTTON_TEXT);
        setFont(new Font("Dialog", Font.BOLD, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Calculate hover color (slightly darker)
        hoverBackground = new Color(
            Math.max((int)(getBackground().getRed() * 0.9), 0),
            Math.max((int)(getBackground().getGreen() * 0.9), 0),
            Math.max((int)(getBackground().getBlue() * 0.9), 0)
        );

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Paint background
        g2.setColor(isHovered ? hoverBackground : getBackground());
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), ARC_WIDTH, ARC_HEIGHT));

        // Add subtle gradient
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(255, 255, 255, 30),
            0, getHeight(), new Color(0, 0, 0, 30)
        );
        g2.setPaint(gradient);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), ARC_WIDTH, ARC_HEIGHT));

        // Paint text
        FontMetrics metrics = g2.getFontMetrics(getFont());
        Rectangle stringBounds = metrics.getStringBounds(getText(), g2).getBounds();
        
        int x = (getWidth() - stringBounds.width) / 2;
        int y = (getHeight() - stringBounds.height) / 2 + metrics.getAscent();
        
        g2.setColor(getForeground());
        g2.setFont(getFont());
        g2.drawString(getText(), x, y);
        
        g2.dispose();
    }
} 