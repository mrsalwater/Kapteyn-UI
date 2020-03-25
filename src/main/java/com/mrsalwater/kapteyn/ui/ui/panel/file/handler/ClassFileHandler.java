package com.mrsalwater.kapteyn.ui.ui.panel.file.handler;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.io.File;

public final class ClassFileHandler implements FileHandler {

    private String name;

    @Override
    public DefaultMutableTreeNode createTreeRoot(File file) {
        name = file.getName();
        return new DefaultMutableTreeNode(file.getName());
    }

    @Override
    public boolean isValidPath(TreePath treePath) {
        return true;
    }

    @Override
    public String getName(TreePath treePath) {
        return name;
    }

}
