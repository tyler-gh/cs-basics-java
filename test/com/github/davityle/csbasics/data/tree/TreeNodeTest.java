package com.github.davityle.csbasics.data.tree;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 *
 */
public class TreeNodeTest {
    private TreeNode<Integer> treeNode = new TreeNode<>();

    @Before
    public void setup() {
        treeNode = new TreeNode<>();

        TreeNode<Integer> child1 = treeNode.add(0);
        TreeNode<Integer> child2 = treeNode.add(1);
        TreeNode<Integer> child3 = treeNode.add(2);
        TreeNode<Integer> child4 = treeNode.add(3);

        for(int i = 0; i < 10; i++) {
            child1 = child1.add(10 + i);
            child2 = child2.add(20 + i);
            child3 = child3.add(30 + i);
            child4 = child4.add(40 + i);
        }
    }


    @Test
    public void testAdd() throws Exception {
        assertTrue(treeNode.has(0));
        assertTrue(treeNode.has(1));
        assertTrue(treeNode.has(2));
        assertTrue(treeNode.has(3));
        assertTrue(treeNode.getNodeOrThrow(0).has(10));
        assertTrue(treeNode.getNodeOrThrow(0).getNodeOrThrow(10).has(11));
    }

    @Test
    public void testAddList() throws Exception {
        treeNode.add(Arrays.asList(4, 5, 6, 7, 8, 9, 10));
        for(int i = 4; i <= 10; i++) {
            assertTrue(treeNode.has(i));
        }
    }

    @Test
    public void testGetParent() throws Exception {
        assertEquals(treeNode, treeNode.getNodeOrThrow(0).getParent());
        TreeNode<Integer> node = treeNode.getNodeOrThrow(0);
        assertEquals(node, node.getNodeOrThrow(10).getParent());
    }

    @Test
    public void testGetValue() throws Exception {
        HashMap<Integer, TreeNode<Integer>> children = treeNode.getChildren();
        for(Integer i = 0; i < children.size(); i++) {
            assertEquals(i, children.get(i).getValue());
        }
    }

    @Test
    public void testHashEquals() {
        HashMap<Integer, TreeNode<Integer>> children = treeNode.getChildren();
        TreeNode<Integer> node = children.get(0);
        children.put(node.hashCode(), new TreeNode<>(node.hashCode()));

        assertFalse(node.equals(null));
        assertFalse(node.equals(0));
        assertFalse(node.equals(new TreeNode<Integer>(null)));
        assertFalse(node.equals(new TreeNode<>(-1)));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNotFound() {
        assertFalse(treeNode.getNode(Integer.MAX_VALUE).isPresent());
        treeNode.getNodeOrThrow(Integer.MAX_VALUE);
    }

    @Test
    public void testBreadthIterator() throws Exception {
        ArrayList<Integer> breadthFirst = new ArrayList<>();
        breadthFirst.add(0);
        breadthFirst.add(1);
        breadthFirst.add(2);
        breadthFirst.add(3);
        for(int i = 0; i < 10; i++) {
            breadthFirst.add(10 + i);
            breadthFirst.add(20 + i);
            breadthFirst.add(30 + i);
            breadthFirst.add(40 + i);
        }

        int index = 0;
        Iterator<Integer> bit = treeNode.breadthIterator();
        while(bit.hasNext()) {
            assertEquals(bit.next(), breadthFirst.get(index++));
        }
    }

    @Test
    public void testDepthIterator() throws Exception {
        ArrayList<Integer> depthFirst = new ArrayList<>();

        for(int i = 0; i < 4; i++) {
            depthFirst.add(i);
            for(int j = 0; j < 10; j++) {
                depthFirst.add(10 * (i + 1) + j);
            }
        }

        int index = 0;
        Iterator<Integer> dit = treeNode.depthIterator();
        while(dit.hasNext()) {
            assertEquals(dit.next(), depthFirst.get(index++));
        }
    }
}