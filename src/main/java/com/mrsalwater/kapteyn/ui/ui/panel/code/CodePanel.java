package com.mrsalwater.kapteyn.ui.ui.panel.code;

import com.mrsalwater.kapteyn.ui.ui.theme.UIThemeChangeListener;
import com.mrsalwater.kapteyn.ui.ui.theme.UIThemeManager;

import javax.swing.*;
import java.awt.*;

public final class CodePanel extends JPanel {

    private static final Color LIGHT_COLOR = new Color(255, 255, 255);
    private static final Color DARK_COLOR = new Color(43, 43, 43);

    private final JTextPane textPane;

    private final CodePanelLineNumberView lineNumberView;
    private final CodeStyledDocument codeStyledDocument;

    public CodePanel() {
        super(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        textPane = new JTextPane() {
            @Override
            public boolean getScrollableTracksViewportWidth() {
                return getUI().getPreferredSize(this).width <= getParent().getSize().width;
            }

            @Override
            public Dimension getPreferredSize() {
                return getUI().getPreferredSize(this);
            }
        };
        textPane.setBackground(Color.WHITE);
        textPane.setFont(new Font("Consolas", Font.PLAIN, 16));
        textPane.setEditable(false);
        textPane.setComponentPopupMenu(new CodePanelPopupMenu(textPane));

        codeStyledDocument = new CodeStyledDocument(textPane);

        lineNumberView = new CodePanelLineNumberView(textPane);
        lineNumberView.setVisible(false);

        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
        scrollPane.setRowHeaderView(lineNumberView);
        add(scrollPane);

        UIThemeChangeListener listener = theme -> {
            switch (UIThemeManager.getTheme()) {
                case LIGHT:
                    textPane.setBackground(LIGHT_COLOR);
                    break;
                case DARK:
                    textPane.setBackground(DARK_COLOR);
                    break;
            }

            codeStyledDocument.updateTextStyles();
        };

        listener.onChange(UIThemeManager.getTheme());
        UIThemeManager.addListener(listener);
    }

    public void setText(String text) {
        textPane.setText(text);
        textPane.setCaretPosition(0);
        codeStyledDocument.updateTextStyles();
        lineNumberView.setVisible(!text.isEmpty());
        lineNumberView.updateSize();
    }

}
