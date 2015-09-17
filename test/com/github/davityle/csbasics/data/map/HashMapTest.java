package com.github.davityle.csbasics.data.map;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class HashMapTest {

    public enum MapType {
        LINEAR {
            @Override
            public <T, R> HashMap<T, R> instantiate() {
                return new HashMapLinearProbing<>();
            }
        },

        QUADRATIC {
            @Override
            public <T, R> HashMap<T, R> instantiate() {
                return new HashMapQuadraticProbing<>();
            }
        };

        public abstract  <T, R> HashMap<T, R> instantiate();
    }


    @Test
    public void testPut() throws Exception {

        for (MapType mapType : MapType.values()) {
            HashMap<String, String> map = mapType.instantiate();
            Consumer<String> assertPutNull = key -> {
                map.put(key, null);
                assertFalse(map.has(key));
                assertEquals(Optional.empty(), map.get(key));
            };

            BiConsumer<String, String> assertPut = (key, val) -> {
                map.put(key, val);
                assertTrue(map.has(key));
                assertNotEquals(Optional.empty(), map.get(key));
                assertEquals(val, map.get(key).get());
            };

            assertPut.accept("a", "z");
            assertPutNull.accept("a");
            assertPut.accept("b", "y");
            assertPut.accept("c", "x");
            assertPut.accept("d", "w");
            assertPut.accept("e", "v");
            assertPutNull.accept("e");
            assertPut.accept("f", "u");
            assertPutNull.accept("f");
            assertPut.accept("g", "t");
            assertPut.accept("h", "s");
            assertPut.accept("i", "r");
            assertPut.accept("j", "q");
            assertEquals(7, map.getSize());
        }
    }

    @Test
    public void testGet() throws Exception {
        for (MapType mapType : MapType.values()) {
            java.util.HashMap<UUID, UUID> javaMap = new java.util.HashMap<>();
            HashMap<UUID, UUID> map = mapType.instantiate();

            for (int i = 0; i < 200000; i++) {
                UUID key = UUID.randomUUID(), value = UUID.randomUUID();
                javaMap.put(key, value);
                map.put(key, value);
            }

            for (java.util.HashMap.Entry<UUID, UUID> entry : javaMap.entrySet()) {
                Optional<UUID> value = map.get(entry.getKey());
                assertTrue(value.isPresent());
                assertEquals(value.get(), entry.getValue());
            }
        }
    }

    @Test
    public void testHas() throws Exception {
        for (MapType mapType : MapType.values()) {
            HashMap<String, String> map = mapType.instantiate();
            Consumer<String> assertNotHas = key -> {
                assertTrue(!map.has(key));
            };

            BiConsumer<String, String> assertHas = (key, val) -> {
                map.put(key, val);
                assertTrue(map.has(key));
            };

            assertNotHas.accept("abcd");
            assertHas.accept("abcd", "efgh");
            map.put("abcd", null);
            assertNotHas.accept("abcd");

            assertHas.accept("1234", "54nj");
            assertHas.accept("12345", "54nj");
            assertHas.accept("12346", "54nj");
            assertHas.accept("12347", "54nj");
            assertHas.accept("12348", "54nj");
            map.put("12348", null);
            assertNotHas.accept("abcd");
        }
    }

    @Test
    public void testGetSize() throws Exception {
        for (MapType mapType : MapType.values()) {
            HashMap<String, String> map = mapType.instantiate();
            assertEquals(0, map.getSize());
            map.put("a", "b");
            assertEquals(1, map.getSize());
            map.put("a", null);
            assertEquals(0, map.getSize());
            List<UUID> uuids = new ArrayList<>();
            for(int i = 1; i < 200; i++) {
                UUID uuid = UUID.randomUUID();
                uuids.add(uuid);
                map.put(uuid.toString(), "");
                assertEquals(i, map.getSize());
            }
            for(int i = 199; i > 0; i--) {
                UUID uuid = uuids.remove(i - 1);
                map.put(uuid.toString(), null);
                assertEquals(i - 1, map.getSize());
            }
        }
    }

    @Test
    public void testResizeTable() throws Exception {
        for (MapType mapType : MapType.values()) {
            java.util.HashMap<UUID, UUID> javaMap = new java.util.HashMap<>();
            HashMap<UUID, UUID> map = mapType.instantiate();

            for (int i = 0; i < 200; i++) {
                UUID key = UUID.randomUUID(), value = UUID.randomUUID();
                javaMap.put(key, value);
                map.put(key, value);
            }

            map.resizeTable();
            map.resizeTable();

            for (java.util.HashMap.Entry<UUID, UUID> entry : javaMap.entrySet()) {
                Optional<UUID> value = map.get(entry.getKey());
                assertTrue(value.isPresent());
                assertEquals(value.get(), entry.getValue());
            }
        }
    }
}