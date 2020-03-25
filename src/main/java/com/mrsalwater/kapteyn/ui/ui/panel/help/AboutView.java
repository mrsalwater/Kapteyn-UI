package com.mrsalwater.kapteyn.ui.ui.panel.help;

import com.mrsalwater.kapteyn.ui.Main;
import com.mrsalwater.kapteyn.ui.ui.theme.UITheme;
import com.mrsalwater.kapteyn.ui.ui.theme.UIThemeManager;
import com.mrsalwater.kapteyn.ui.util.JHyperLink;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class AboutView extends JDialog {

    private static final Color LIGHT_COLOR = new Color(255, 255, 255);
    private static final Color DARK_COLOR = new Color(43, 43, 43);

    private static final String LICENSE_LOCATION = "LICENSE";

    public static void display(Frame owner) {
        SwingUtilities.invokeLater(() -> new AboutView(owner));
    }

    private AboutView(Frame owner) {
        super(owner, "Application Information", ModalityType.TOOLKIT_MODAL);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(getColor());

        JLabel labelHeader = new JLabel(Main.NAME + " " + Main.VERSION);
        labelHeader.setFont(new Font(labelHeader.getFont().getName(), Font.BOLD, 16));
        panel.add(labelHeader);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JHyperLink hyperLinkGithub = new JHyperLink(Main.WEBSITE);
        panel.add(hyperLinkGithub);

        JLabel labelRights = new JLabel("Copyright 2020 " + Main.AUTHOR + ". All rights reserved.");
        panel.add(labelRights);

        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        JTextArea textAreaLicense = new JTextArea(getLicense());
        textAreaLicense.setEditable(false);

        JScrollPane scrollPaneTextArea = new JScrollPane(textAreaLicense);
        panel.add(scrollPaneTextArea);

        add(panel);

        setLocationRelativeTo(owner);
        setSize(600, 500);
        setResizable(false);
        setVisible(true);
    }

    private String getLicense() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(LICENSE_LOCATION);
            ByteArrayOutputStream result = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }

            return result.toString(StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Color getColor() {
        if (UIThemeManager.getTheme() == UITheme.DARK) {
            return DARK_COLOR;
        }

        return LIGHT_COLOR;
    }

}
