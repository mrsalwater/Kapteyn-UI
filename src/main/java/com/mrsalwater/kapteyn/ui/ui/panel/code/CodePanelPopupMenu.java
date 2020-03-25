package com.mrsalwater.kapteyn.ui.ui.panel.code;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

public final class CodePanelPopupMenu extends JPopupMenu {

    public CodePanelPopupMenu(JTextComponent textComponent) {
        super("Edit");

        /* Menu Item Copy */

        JMenuItem menuItemCopy = new JMenuItem("Copy");
        menuItemCopy.addActionListener(event -> {
            String selectedText = textComponent.getSelectedText();

            if (selectedText != null) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(selectedText), null);
            }
        });
        add(menuItemCopy);

        /* Menu Item Select All*/

        JMenuItem menuItemSelectAll = new JMenuItem("Select all");
        menuItemSelectAll.addActionListener(event -> textComponent.selectAll());
        add(menuItemSelectAll);
    }

}
