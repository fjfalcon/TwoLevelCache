package com.fjfalcon.cache.memory;

import com.fjfalcon.cache.Cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryCache<K, V> implements Cache<K, V> {
    private final Map<K, V> map;
    private final int maxSize;

    public MemoryCache(int maxSize) {
        this.map = new ConcurrentHashMap<>();
        this.maxSize = maxSize;
    }

    @Override
    public void put(K key, V value) {
        map.put(key, value);
    }

    @Override
    public boolean contains(K key) {
        return map.containsKey(key);
    }

    @Override
    public V get(K key) {
        return map.get(key);
    }

    @Override
    public void remove(K key) {
        map.remove(key);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isFull() {
        return size() == maxSize;
    }
}
