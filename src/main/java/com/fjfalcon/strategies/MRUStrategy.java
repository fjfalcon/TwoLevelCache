package com.fjfalcon.strategies;

/**
 * MRU Strategy - Most Recently Used
 */

public class MRUStrategy<K> extends Strategy<K> {
    @Override
    public void call(K key) {
        map.put(key, System.nanoTime());
    }

    @Override
    public K getReplacementKey() {
        sortedMap.putAll(map);
        return sortedMap.lastKey();
    }
}