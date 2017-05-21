package com.fjfalcon.cache.memory;

import com.fjfalcon.cache.Cache;
import com.fjfalcon.cache.filesystem.FileSystemCache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MemoryCacheTest {
    private Cache<Integer, String> cache;

    @Before
    public void setUp() throws Exception {
        cache = new MemoryCache<>(3);
    }

    @After
    public void tearDown() throws Exception {
        cache.clear();
    }


    @Test
    public void put() throws Exception {
        cache.put(0, "value");
        assertEquals("value", cache.get(0));
    }

    @Test
    public void contains() throws Exception {
        cache.put(0, "value");
        assertTrue(cache.contains(0));
    }

    @Test
    public void get() throws Exception {
        cache.put(0, "value");
        assertEquals("value", cache.get(0));
        assertNull(cache.get(123));
    }

    @Test
    public void remove() throws Exception {
        cache.put(0, "value");
        cache.put(1, "value");
        cache.remove(0);
        assertTrue(cache.contains(1));
        assertFalse(cache.contains(0));
    }

    @Test
    public void clear() throws Exception {
        cache.put(0, "value");
        cache.put(1, "value");
        assertEquals(cache.size(), 2);
        cache.clear();
        assertEquals(cache.size(), 0);
    }

    @Test
    public void size() throws Exception {
        assertEquals(0, cache.size());
        cache.put(1, "value");
        assertEquals(cache.size(), 1);
        cache.put(2, "value");
        assertEquals(cache.size(), 2);
        cache.remove(2);
        assertEquals(cache.size(), 1);
    }

    @Test
    public void isFull() throws Exception {
        cache.put(0, "value");
        cache.put(1, "value");
        assertFalse(cache.isFull());
        cache.put(2, "value");
        assertTrue(cache.isFull());
    }}