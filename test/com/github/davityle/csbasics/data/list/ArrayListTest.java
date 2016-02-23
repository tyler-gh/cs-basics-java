package com.github.davityle.csbasics.data.list;

import org.junit.Test;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.junit.Assert.*;

public class ArrayListTest {

    @Test
    public void testAdd() throws Exception {
        ArrayList<String> list = new ArrayList<>();

        list.add("ABCD");
        list.add("CDEF");

        assertTrue(list.has("ABCD"));
        assertTrue(list.has("CDEF"));

        list.add("EFGH");
        list.add("GHIJ");

        assertTrue(list.has("EFGH"));
        assertTrue(list.has("GHIJ"));
    }

    @Test
    public void testGet() throws Exception {
        ArrayList<String> queenSongs = new ArrayList<>();

        BiConsumer<String, Integer> assertGet = (song, index) -> {
            assertEquals(song, queenSongs.get(index));
        };

        queenSongs.add("We Will Rock You");
        queenSongs.add("Another One Bites The Dust");
        queenSongs.add("We Are The Champions");
        queenSongs.add("Don't Stop Me Now");
        queenSongs.add("Love of My Life");

        assertGet.accept("We Will Rock You", 0);
        assertGet.accept("Love of My Life", 4);

        queenSongs.remove(0);

        assertGet.accept("Love of My Life", 3);

        queenSongs.add("Under Pressure");

        assertGet.accept("Under Pressure", 4);

    }

    @Test
    public void testIndexOf() throws Exception {
        ArrayList<String> beatlesSongs = new ArrayList<>();

        BiConsumer<String, Integer> assertIndex = (song, index) -> {
            assertNotEquals(Optional.empty(), beatlesSongs.indexOf(song));
            assertEquals((int)index, beatlesSongs.indexOf(song).getAsInt());
        };

        beatlesSongs.add("Hey Jude");
        beatlesSongs.add("Here Comes the Sun");
        beatlesSongs.add(null);
        beatlesSongs.add("Come Together");
        beatlesSongs.add("Eleanor Rigby");
        beatlesSongs.add(null);
        beatlesSongs.add("Yellow Submarine");
        beatlesSongs.add("All You Need Is Love");

        assertIndex.accept("Hey Jude", 0);
        assertIndex.accept("Come Together", 3);
        assertIndex.accept("Yellow Submarine", 6);
        assertIndex.accept("Eleanor Rigby", 4);

        assertEquals(OptionalInt.empty(), beatlesSongs.indexOf("We Will Rock You"));
    }

    @Test
    public void testHas() throws Exception {
        ArrayList<String> list = new ArrayList<>();

        assertFalse(list.has("ABCD"));
        assertFalse(list.has("CDEF"));
        assertFalse(list.has("EFGH"));
        assertFalse(list.has("GHIJ"));

        list.add("ABCD");
        list.add("CDEF");

        assertFalse(list.has("EFGH"));
        assertFalse(list.has("GHIJ"));

        list.add("EFGH");
        list.add("GHIJ");

        assertTrue(list.has("ABCD"));
        assertTrue(list.has("CDEF"));
        assertTrue(list.has("EFGH"));
        assertTrue(list.has("GHIJ"));

        assertFalse(list.has("IJKL"));
        assertFalse(list.has("KLMN"));
    }

    @Test
    public void testGetSize() throws Exception {
        ArrayList<String> list = new ArrayList<>();

        assertEquals(0, list.getSize());

        list.add("ABCD");
        list.add("CDEF");

        assertEquals(2, list.getSize());

        list.remove(1);

        assertEquals(1, list.getSize());
    }

    @Test
    public void testRemove() throws Exception {
        ArrayList<String> list = new ArrayList<>();

        list.add("ABCD");
        list.add("CDEF");
        list.add("EFGH");
        list.add("GHIJ");

        assertEquals("GHIJ", list.remove(list.getSize() - 1));
        assertEquals(3, list.getSize());

        assertEquals("CDEF", list.remove(1));
        assertEquals(2, list.getSize());

        assertEquals("ABCD", list.remove(0));
        assertEquals(1, list.getSize());
    }
}