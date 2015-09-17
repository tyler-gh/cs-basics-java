package com.github.davityle.csbasics.data.tree;

import org.junit.Test;

import static org.junit.Assert.*;

public class TrieTest {

    @Test
    public void testAdd() throws Exception {
        Trie root = new Trie();

        root.add("yellow");
        root.add("green");
        root.add("purple");
        root.add("puse");

        assertTrue(root.has("yellow"));
        assertTrue(root.has("green"));
        assertTrue(root.has("purple"));
        assertTrue(root.has("puse"));

    }

    @Test
    public void testHas() throws Exception {
        Trie root = new Trie();

        assertFalse(root.has("yellow"));
        assertFalse(root.has("green"));
        assertFalse(root.has("purple"));
        assertFalse(root.has("puse"));

        root.add("green");

        assertTrue(root.has("green"));
        assertFalse(root.has("gree"));
        assertFalse(root.has("greeny"));

        root.add("grape");

        assertFalse(root.has("gr"));
        assertFalse(root.has("grap"));
        assertTrue(root.has("grape"));
    }
}