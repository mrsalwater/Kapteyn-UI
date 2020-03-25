package com.mrsalwater.kapteyn.ui.ui.panel.file.tree;

import java.util.List;

public final class Tree {

    public static Tree createTree(String rootValue, List<String> paths) {
        Node root = new Node(rootValue);

        for (String path : paths) {
            if (path.contains("/")) {
                String[] values = path.split("/");

                Node currentNode = root;
                for (int i = 0, length = values.length; i < length; i++) {
                    String value = values[i];

                    if (currentNode.hasChild(value)) {
                        currentNode = currentNode.getChild(value);
                    } else {
                        currentNode = currentNode.addChild(value, (i + 1 == length));
                    }
                }
            } else {
                root.addChild(path, true);
            }
        }

        for (Node child : root.getChildren()) {
            merge(child);
        }

        return new Tree(root);
    }

    private static void merge(Node node) {
        while (node.getChildren().size() == 1 && !node.getChildren().get(0).isFile()) {
            node.merge(node.getChildren().get(0));
        }

        for (Node child : node.getChildren()) {
            merge(child);
        }
    }

    private final Node root;

    public Tree(Node root) {
        this.root = root;
    }

    public Node getRoot() {
        return root;
    }

}
