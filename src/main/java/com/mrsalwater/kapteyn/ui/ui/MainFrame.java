package com.mrsalwater.kapteyn.ui.ui;

import com.mrsalwater.kapteyn.ui.Main;
import com.mrsalwater.kapteyn.ui.decompiler.Decompiler;
import com.mrsalwater.kapteyn.ui.decompiler.DynamicDecompiler;
import com.mrsalwater.kapteyn.ui.ui.handler.DragAndDropHandler;
import com.mrsalwater.kapteyn.ui.ui.panel.code.CodePanel;
import com.mrsalwater.kapteyn.ui.ui.panel.file.FilePanel;
import com.mrsalwater.kapteyn.ui.ui.panel.find.FindPanel;

import javax.swing.*;

public final class MainFrame extends JFrame {

    private final FindPanel findPanel;
    private final JSplitPane subSplitPane;

    public MainFrame() {
        super(Main.NAME);

        MainMenuBar menuBar = new MainMenuBar();
        FilePanel filePanel = new FilePanel();
        CodePanel codePanel = new CodePanel();
        findPanel = new FindPanel();
        findPanel.setVisible(false);

        Decompiler decompiler = new DynamicDecompiler(codePanel, filePanel);
        menuBar.initialize(decompiler, this);
        filePanel.initialize(decompiler, this);
        findPanel.initialize(decompiler, this);

        subSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, filePanel, findPanel);
        subSplitPane.setResizeWeight(0.5);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, subSplitPane, codePanel);
        splitPane.setTransferHandler(new DragAndDropHandler(decompiler, this));

        setJMenuBar(menuBar);
        add(splitPane);
        pack();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1080, 720);
        setLocationByPlatform(true);
        setVisible(true);
    }

    public void toggleFindMenu() {
        if (findPanel.isVisible()) {
            findPanel.setVisible(false);
        } else {
            findPanel.setVisible(true);
            subSplitPane.setDividerLocation(0.5);
            subSplitPane.updateUI();
        }
    }

}
