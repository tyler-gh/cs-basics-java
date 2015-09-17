package com.github.davityle.csbasics.data.list;

import org.junit.Test;

import java.util.Iterator;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.BiConsumer;

import static org.junit.Assert.*;

/**
 *
 */
public class LinkedListTest {

    @Test
    public void testAdd() throws Exception {
        LinkedList<UUID> linkedList = new LinkedList<>();
        ArrayList<UUID> arrayList = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            UUID uuid = UUID.randomUUID();
            arrayList.add(uuid);
            linkedList.add(uuid);
        }
        // O(n^2)
        for (int i = 0; i < 10000; i++) {
            assertTrue(linkedList.has(arrayList.get(i)));
        }
    }

    @Test
    public void testGet() throws Exception {
        LinkedList<UUID> linkedList = new LinkedList<>();
        ArrayList<UUID> arrayList = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            UUID uuid = UUID.randomUUID();
            arrayList.add(uuid);
            linkedList.add(uuid);
        }
        // O(n^2)
        for (int i = 0; i < 1000; i++) {
            assertEquals(linkedList.get(i), arrayList.get(i));
        }
    }

    @Test
    public void testRemove() throws Exception {
        LinkedList<UUID> linkedList = new LinkedList<>();
        ArrayList<UUID> arrayList = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            UUID uuid = UUID.randomUUID();
            arrayList.add(uuid);
            linkedList.add(uuid);
        }

        Random random = new Random();
        // O(n^2)
        for(int i = 999; i >= 0; i--) {
            int ran = random.nextInt(i + 1);
            UUID x = arrayList.remove(ran), y = linkedList.remove(ran);
            assertEquals(x, y);
            assertEquals(i, linkedList.getSize());
        }

        assertEquals(0, linkedList.getSize());

        LinkedList<String> stringList = new LinkedList<>();

        stringList.add("a");
        assertEquals("a", stringList.remove(0));
        stringList.add("a");
        stringList.add("b");
        assertEquals("a", stringList.remove(0));
        assertEquals("b", stringList.remove(0));
    }

    @Test(expected = NullPointerException.class)
    public void invalidRemove() {
        LinkedList<String> list = new LinkedList<>();

        list.add("a");
        assertEquals("a", list.remove(0));
        list.remove(0);
    }

    @Test
    public void testIndexOf() throws Exception {
        LinkedList<String> linkedList = new LinkedList<>();

        BiConsumer<String, Integer> testIndexOf = (s, index) -> {
            linkedList.add(s);
            assertEquals(index, linkedList.indexOf(s).get());
        };

        for(int i = 0; i < 26; i++) {
            testIndexOf.accept(Character.toString((char) (i + 'a')), i);
        }

        assertEquals(Integer.valueOf(1), linkedList.indexOf("b").get());
        assertEquals(Integer.valueOf(25), linkedList.indexOf("z").get());
        assertEquals(Integer.valueOf(23), linkedList.indexOf("x").get());
        assertEquals(Integer.valueOf(3), linkedList.indexOf("d").get());

        assertEquals(Optional.empty(), linkedList.indexOf("color"));
    }

    @Test
    public void testHas() throws Exception {
        LinkedList<UUID> linkedList = new LinkedList<>();
        ArrayList<UUID> arrayList = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            UUID uuid = UUID.randomUUID();
            arrayList.add(uuid);
            linkedList.add(uuid);
        }
        // O(n^2)
        for (int i = 0; i < 10000; i++) {
            assertTrue(linkedList.has(arrayList.get(i)));
        }
    }

    @Test
    public void testGetSize() throws Exception {
        LinkedList<UUID> linkedList = new LinkedList<>();

        for (int i = 0; i < 10000; i++) {
            linkedList.add(UUID.randomUUID());
            assertEquals(i + 1, linkedList.getSize());
        }
        linkedList.pop();
        assertEquals(9999, linkedList.getSize());

        linkedList.remove(20);
        assertEquals(9998, linkedList.getSize());

        linkedList.poll();
        assertEquals(9997, linkedList.getSize());
    }
}