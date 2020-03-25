package com.mrsalwater.kapteyn.ui.ui.panel.help;

import com.mrsalwater.kapteyn.ui.ui.theme.UITheme;
import com.mrsalwater.kapteyn.ui.ui.theme.UIThemeManager;
import com.mrsalwater.kapteyn.ui.util.JHyperLink;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class LegalView extends JDialog {

    private static final Color LIGHT_COLOR = new Color(255, 255, 255);
    private static final Color DARK_COLOR = new Color(43, 43, 43);

    public static void display(Frame owner) {
        SwingUtilities.invokeLater(() -> new LegalView(owner));
    }

    private LegalView(Frame owner) {
        super(owner, "Legal Information", ModalityType.TOOLKIT_MODAL);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(getColor());

        panel.add(createLibraryPanel("Flatlaf", "https://github.com/JFormDesigner/FlatLaf", "LICENSE-flatlaf"));
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(createLibraryPanel("JSON", "https://github.com/stleary/JSON-java", "LICENSE-json"));
        add(new JScrollPane(panel));

        setLocationRelativeTo(owner);
        setSize(600, 500);
        setResizable(false);
        setVisible(true);
    }

    private JPanel createLibraryPanel(String name, String source, String licenseLocation) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(getColor());

        JLabel labelName = new JLabel(name);
        panel.add(labelName);

        JHyperLink hyperLinkSource = new JHyperLink(source);
        panel.add(hyperLinkSource);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JTextArea textAreaLicense = new JTextArea(getLicense(licenseLocation));
        textAreaLicense.setEditable(false);

        JScrollPane scrollPaneTextArea = new JScrollPane(textAreaLicense);
        scrollPaneTextArea.setPreferredSize(new Dimension(0, 500));
        panel.add(scrollPaneTextArea);

        return panel;
    }

    private String getLicense(String location) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(location);
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
