package com.github.davityle.csbasics.data.tree;


public class TreeTrie {

    final TreeNode<Character> tree = new TreeNode<>();

    public void add(String s) {
        add(s, tree);
    }

    private void add(String s, TreeNode<Character> node) {
        if (s.length() > 0) {
            TreeNode<Character> child = node.getNode(s.charAt(0)).orElseGet(() -> {
                TreeNode<Character> newNode = new TreeNode<>(s.charAt(0));
                node.add(newNode);
                return newNode;
            });
            add(s.substring(1), child);
        } else {
            node.add('\0');
        }
    }

    public boolean has(String s) {
        return has(s, tree);
    }

    private boolean has(String s, TreeNode<Character> node) {
        if (s.length() == 0)
            return node.has('\0');
        return node.getNode(s.charAt(0)).map(child -> has(s.substring(1), child)).orElseGet(() -> false);
    }

}
