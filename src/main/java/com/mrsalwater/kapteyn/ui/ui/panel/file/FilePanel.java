package com.mrsalwater.kapteyn.ui.ui.panel.file;

import com.mrsalwater.kapteyn.decompiler.exception.ClassFileException;
import com.mrsalwater.kapteyn.ui.data.AppData;
import com.mrsalwater.kapteyn.ui.decompiler.Decompiler;
import com.mrsalwater.kapteyn.ui.ui.MainFrame;
import com.mrsalwater.kapteyn.ui.ui.panel.file.handler.ClassFileHandler;
import com.mrsalwater.kapteyn.ui.ui.panel.file.handler.FileHandler;
import com.mrsalwater.kapteyn.ui.ui.panel.file.handler.JarFileHandler;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public final class FilePanel extends JPanel {

    private final JTree tree;
    private final DefaultTreeModel model;
    private final DefaultMutableTreeNode node;

    private FileHandler fileHandler;

    public FilePanel() {
        super(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));

        node = new DefaultMutableTreeNode();

        tree = new JTree(node);
        tree.setRootVisible(false);
        tree.setEditable(false);

        model = (DefaultTreeModel) tree.getModel();

        JScrollPane scrollPane = new JScrollPane(tree);
        add(scrollPane);
    }

    public void initialize(Decompiler decompiler, MainFrame frame) {
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    TreePath treePath = tree.getPathForLocation(event.getX(), event.getY());

                    if (treePath != null && fileHandler.isValidPath(treePath)) {
                        try {
                            decompiler.selectFile(fileHandler.getName(treePath));
                        } catch (IOException | ClassFileException e) {
                            AppData.getLog().error(e.getMessage());
                            JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public void setFile(File file) throws IOException {
        node.removeAllChildren();

        String fileName = file.getName();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (fileExtension.equals("class")) {
            fileHandler = new ClassFileHandler();
        } else {
            fileHandler = new JarFileHandler();
        }

        DefaultMutableTreeNode root = fileHandler.createTreeRoot(file);
        node.add(root);

        model.reload();
        tree.expandPath(new TreePath(root.getPath()));
    }

    public void clearFile() {
        node.removeAllChildren();
        model.reload();
    }

}
