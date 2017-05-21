package com.fjfalcon.cache;

/**
 *
 * Interface for cache
 */
public interface Cache<K,V> {
    /**
     * Put value to cache
     * @param key
     * @param value
     */
    void put(K key, V value);

    /**
     * @param key
     * @return true if cache contains key
     */
    boolean contains(K key);

    /**
     * Get value from cache
     * @param key
     * @return
     */
    V get(K key);

    /**
     * Remove value from cache
     * @param key
     */
    void remove(K key);

    /**
     * Delete all elems from cache
     */
    void clear();

    /**
     * @return amount of cached elems
     */
    int size();

    /**
     *
     * @return true if cache already limits it's maxsize
     */
    boolean isFull();
}
