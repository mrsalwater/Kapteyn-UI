package com.mrsalwater.kapteyn.ui.util;

import com.mrsalwater.kapteyn.ui.ui.theme.UITheme;
import com.mrsalwater.kapteyn.ui.ui.theme.UIThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;

public final class JHyperLink extends JLabel {

    private static final Color LIGHT_COLOR = new Color(0, 0, 238);
    private static final Color DARK_COLOR = new Color(238, 238, 238);

    public JHyperLink() {
        this("", null, 10);
    }

    public JHyperLink(Icon image) {
        this(null, image, 0);
    }

    public JHyperLink(Icon image, int horizontalAlignment) {
        this(null, image, horizontalAlignment);
    }

    public JHyperLink(String text) {
        this(text, null, 10);
    }

    public JHyperLink(String text, int horizontalAlignment) {
        this(text, null, horizontalAlignment);
    }

    public JHyperLink(String link, Icon icon, int horizontalAlignment) {
        super(null, icon, horizontalAlignment);
        setText("<HTML><FONT color=\"" + getColor() + "\"><U>" + link + "</U></FONT></HTML>");

        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                try {
                    Desktop.getDesktop().browse(URI.create(link));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void mouseEntered(MouseEvent event) {
                setText("<HTML><FONT color=\"" + getColor() + "\"><U>" + link + "</U></FONT></HTML>");
            }

            @Override
            public void mouseExited(MouseEvent event) {
                setText("<HTML><FONT color=\"" + getColor() + "\"><U>" + link + "</U></FONT></HTML>");
            }
        });
    }

    private String getColor() {
        Color color = LIGHT_COLOR;
        if (UIThemeManager.getTheme() == UITheme.DARK) {
            color = DARK_COLOR;
        }

        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

}
