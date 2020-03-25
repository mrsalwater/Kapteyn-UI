package com.mrsalwater.kapteyn.ui.ui.panel.file.handler;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.io.File;
import java.io.IOException;

public interface FileHandler {

    DefaultMutableTreeNode createTreeRoot(File file) throws IOException;

    boolean isValidPath(TreePath treePath);

    String getName(TreePath treePath);

}
