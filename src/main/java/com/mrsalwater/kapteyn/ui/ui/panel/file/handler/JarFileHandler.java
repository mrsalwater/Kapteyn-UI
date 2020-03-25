package com.mrsalwater.kapteyn.ui.ui.panel.file.handler;

import com.mrsalwater.kapteyn.ui.ui.panel.file.tree.Node;
import com.mrsalwater.kapteyn.ui.ui.panel.file.tree.Tree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class JarFileHandler implements FileHandler {

    private final Map<String, byte[]> classFiles = new HashMap<>();

    @Override
    public DefaultMutableTreeNode createTreeRoot(File file) throws IOException {
        Tree tree = createTree(file);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(tree.getRoot().getName());

        appendTree(root, tree.getRoot());
        return root;
    }

    @Override
    public boolean isValidPath(TreePath treePath) {
        String name = getName(treePath);
        return classFiles.containsKey(name) && name.endsWith(".class");
    }

    @Deprecated
    public String getName(TreePath treePath) {
        Object[] path = treePath.getPath();
        StringBuilder builder = new StringBuilder();

        for (int i = 2, length = path.length; i < length; i++) {
            builder.append(path[i]);
            if (i + 1 != length) builder.append(".");
        }

        return builder.toString();
    }

    private Tree createTree(File file) throws IOException {
        List<String> paths = new ArrayList<>();

        JarFile jarFile = new JarFile(file);
        Enumeration<JarEntry> entries = jarFile.entries();

        classFiles.clear();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();

            if (!jarEntry.isDirectory()) {
                paths.add(jarEntry.getName());

                classFiles.put(jarEntry.getName().replace("/", "."), toByteArray(jarFile.getInputStream(jarEntry), (int) jarEntry.getCompressedSize()));
            }
        }

        jarFile.close();
        return Tree.createTree(jarFile.getName().substring(jarFile.getName().lastIndexOf("\\") + 1), paths);
    }

    private void appendTree(DefaultMutableTreeNode tree, Node node) {
        sortList(node.getChildren());

        for (Node child : node.getChildren()) {
            child.setName(child.getName().replace("/", "."));

            DefaultMutableTreeNode treeChild = new DefaultMutableTreeNode(child.getName());
            tree.add(treeChild);

            if (!child.isFile()) {
                appendTree(treeChild, child);
            }
        }
    }

    private void sortList(List<Node> list) {
        list.sort((o1, o2) -> {
            String firstName = o1.getName();
            String secondName = o2.getName();

            if (firstName.equals("META-INF")) {
                return -1;
            } else if (secondName.equals("META-INF")) {
                return 1;
            }

            boolean firstFile = firstName.lastIndexOf(".") > firstName.lastIndexOf("/");
            boolean secondFile = secondName.lastIndexOf(".") > secondName.lastIndexOf("/");

            if (firstFile && !secondFile) {
                return 1;
            } else if (!firstFile && secondFile) {
                return -1;
            }

            return firstName.compareTo(secondName);
        });
    }

    private byte[] toByteArray(InputStream inputStream, int size) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[size];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }

        return byteArrayOutputStream.toByteArray();
    }

}
