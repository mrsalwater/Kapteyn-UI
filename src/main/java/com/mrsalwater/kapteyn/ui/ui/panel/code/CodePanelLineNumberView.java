package com.mrsalwater.kapteyn.ui.ui.panel.code;

import com.mrsalwater.kapteyn.ui.ui.theme.UITheme;
import com.mrsalwater.kapteyn.ui.ui.theme.UIThemeChangeListener;
import com.mrsalwater.kapteyn.ui.ui.theme.UIThemeManager;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

// source: http://rememberjava.com/ui/2017/02/19/line_numbers.html
public final class CodePanelLineNumberView extends JComponent implements CaretListener, ComponentListener, DocumentListener {

    private static final int MARGIN = 5;

    private static final Color COLOR_LIGHT = new Color(153, 153, 153);
    private static final Color COLOR_DARK = new Color(96, 99, 102);

    private final JTextComponent component;
    private final FontMetrics fontMetrics;
    private final Font font;

    private Color color;

    public CodePanelLineNumberView(JTextComponent component) {
        this.component = component;
        fontMetrics = component.getFontMetrics(component.getFont());
        font = component.getFont();

        component.addCaretListener(this);
        component.addComponentListener(this);
        component.getDocument().addDocumentListener(this);

        UIThemeChangeListener listener = theme -> {
            if (theme == UITheme.DARK) {
                color = COLOR_DARK;
            }

            color = COLOR_LIGHT;
        };

        listener.onChange(UIThemeManager.getTheme());
        UIThemeManager.addListener(listener);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Rectangle clip = g.getClipBounds();
        int startOffset = component.viewToModel(new Point(0, clip.y));
        int endOffset = component.viewToModel(new Point(0, clip.y + clip.height));

        while (startOffset <= endOffset) {
            try {
                String lineNumber = getLineNumber(startOffset);

                if (lineNumber != null) {
                    int x = getInsets().left + 2;
                    int y = getOffsetY(startOffset);

                    g.setFont(font);
                    g.setColor(color);
                    g.drawString(lineNumber, x, y);
                }

                startOffset = Utilities.getRowEnd(component, startOffset) + 1;
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        documentChanged();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        updateSize();
        documentChanged();
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {
        updateSize();
        documentChanged();
    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        documentChanged();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        documentChanged();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        documentChanged();
    }

    public void updateSize() {
        Element root = component.getDocument().getDefaultRootElement();
        String lines = String.valueOf(root.getElementCount());

        Dimension dimension = new Dimension(fontMetrics.stringWidth(lines) + MARGIN, component.getHeight());
        setPreferredSize(dimension);
        setSize(dimension);
    }

    private void documentChanged() {
        SwingUtilities.invokeLater(this::repaint);
    }

    private String getLineNumber(int offset) {
        Element root = component.getDocument().getDefaultRootElement();
        int index = root.getElementIndex(offset);

        Element line = root.getElement(index);
        return line.getStartOffset() == offset ? String.valueOf(index + 1) : null;
    }

    private int getOffsetY(int offset) throws BadLocationException {
        int descent = fontMetrics.getDescent();
        Rectangle rectangle = component.modelToView(offset);
        return rectangle.y + rectangle.height - descent;
    }

}
