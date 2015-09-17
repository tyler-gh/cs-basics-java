package com.github.davityle.csbasics.data.tree;

/**
 * lowercase only, array backed, trie
 */
public class Trie {

    private static final int ALPHABET_SIZE = 26;
    private final Trie[] children = new Trie[ALPHABET_SIZE];
    private boolean isWordEnd;

    public void add(String s) {
        if(s.length() > 0) {
            int index = getIndex(s.charAt(0));
            if(children[index] == null) {
                children[index] = new Trie();
            }
            Trie child = children[index];
            child.add(s.substring(1));
        } else {
            isWordEnd = true;
        }
    }

    public boolean has(String s) {
        if(s.length() == 0)
            return isWordEnd;
        int index = getIndex(s.charAt(0));
        if(children[index] == null)
            return false;
        return children[index].has(s.substring(1));
    }

    private int getIndex(char c) {
        return c - 'a';
    }
}
