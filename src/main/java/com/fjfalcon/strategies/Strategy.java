package com.fjfalcon.strategies;

import java.util.Map;
import java.util.TreeMap;

public abstract class Strategy<K> {
    final Map<K, Long> map;
    final TreeMap<K, Long> sortedMap;

    Strategy() {
        this.map = new TreeMap<>();
        this.sortedMap = new TreeMap<>(new ValueComparator<>(map));
    }

    public abstract void call(K key);

    public void remove(K key) {
        if(contains(key)) {
            map.remove(key);
        }
    }

    public boolean contains(K key) {
        return map.containsKey(key);
    }

    public void clear() {
        map.clear();
    }

    public K getReplacementKey() {
        sortedMap.putAll(map);
        return sortedMap.firstKey();
    }
}
