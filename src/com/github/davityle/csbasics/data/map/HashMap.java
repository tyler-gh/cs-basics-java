package com.github.davityle.csbasics.data.map;

import java.util.Optional;

@SuppressWarnings("unchecked")
public abstract class HashMap<T, R> {

    private static final int MIN_CAPACITY = 2;
    protected Entry<T, R>[] table;
    private int internalSize, size;
    private float fillRatio;

    public HashMap() {
        this(MIN_CAPACITY);
    }

    public HashMap(int initialCapacity) {
        this(initialCapacity, .75f);
    }

    public HashMap(int initialCapacity, float fillRatio) {
        this.table = new Entry[Math.max(MIN_CAPACITY, initialCapacity)];
        this.fillRatio = fillRatio;
    }

    public Optional<R> put(T key, R value) {
        int index = getIndex(key);
        Entry<T, R> current = table[index];
        table[index] = new Entry<>(key, value);

        if (value == null && current != null && current.getValue() != null) {
            size--;
        } else if (value != null && (current == null || current.getValue() == null)) {
            size++;
        }

        if (current == null && ++internalSize >= ((table.length * fillRatio) - 1)) {
            resizeTable();
        }

        if (current != null) {
            return Optional.ofNullable(current.getValue());
        }
        return Optional.empty();
    }

    public Optional<R> get(T key) {
        int index = getIndex(key);
        Entry<T, R> entry = table[index];
        if (entry != null)
            return Optional.ofNullable(entry.getValue());
        return Optional.empty();
    }

    public boolean has(T key) {
        return get(key).isPresent();
    }

    public int getSize() {
        return size;
    }

    protected void resizeTable() {
        internalSize = size = 0;
        Entry<T, R>[] tmp = table;
        table = new Entry[(int) ((table.length / fillRatio) * 2)];
        for (Entry<T, R> entry : tmp) {
            if (entry != null) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    protected int modLength(int hash) {
        return (((hash % table.length) + table.length) % table.length);
    }


    protected abstract int getIndex(T key);

    public static final class Entry<T, R> {
        private final T key;
        private final R value;

        public Entry(T key, R value) {
            this.key = key;
            this.value = value;
        }

        public T getKey() {
            return key;
        }

        public R getValue() {
            return value;
        }
    }

}
