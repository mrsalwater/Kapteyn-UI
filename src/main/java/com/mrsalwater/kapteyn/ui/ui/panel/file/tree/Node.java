package com.mrsalwater.kapteyn.ui.ui.panel.file.tree;

import java.util.ArrayList;
import java.util.List;

public final class Node {

    private final List<Node> children = new ArrayList<>();

    private String name;
    private boolean file;

    public Node(String name) {
        this(name, false);
    }

    private Node(String name, boolean file) {
        this.name = name;
        this.file = file;
    }

    public void merge(Node node) {
        children.remove(node);
        children.addAll(node.getChildren());
        name = name.concat("/").concat(node.name);
    }


    public Node addChild(String value, boolean file) {
        Node node = new Node(value, file);
        children.add(node);
        return node;
    }

    public Node getChild(String value) {
        for (Node child : children) {
            if (child.name.equals(value)) {
                return child;
            }
        }

        return null;
    }

    public boolean hasChild(String value) {
        for (Node child : children) {
            if (child.name.equals(value)) {
                return true;
            }
        }

        return false;
    }

    public List<Node> getChildren() {
        return children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFile() {
        return file;
    }

}
