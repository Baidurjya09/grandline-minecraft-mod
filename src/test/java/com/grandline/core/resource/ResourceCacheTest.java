package com.grandline.core.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ResourceCache.
 */
class ResourceCacheTest {
    
    private ResourceCache cache;
    
    @BeforeEach
    void setUp() {
        cache = new ResourceCache(3);
    }
    
    @Test
    void testPutAndGet() {
        cache.put("key1", "value1");
        
        String value = cache.get("key1", String.class);
        assertEquals("value1", value);
    }
    
    @Test
    void testGetWrongType() {
        cache.put("key1", "value1");
        
        Integer value = cache.get("key1", Integer.class);
        assertNull(value);
    }
    
    @Test
    void testLRUEviction() {
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");
        
        // Access key1 to make it recently used
        cache.get("key1", String.class);
        
        // Add key4, should evict key2 (least recently used)
        cache.put("key4", "value4");
        
        assertNotNull(cache.get("key1", String.class));
        assertNull(cache.get("key2", String.class)); // Evicted
        assertNotNull(cache.get("key3", String.class));
        assertNotNull(cache.get("key4", String.class));
    }
    
    @Test
    void testRemove() {
        cache.put("key1", "value1");
        cache.remove("key1");
        
        assertNull(cache.get("key1", String.class));
    }
    
    @Test
    void testClear() {
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.clear();
        
        assertEquals(0, cache.size());
        assertNull(cache.get("key1", String.class));
        assertNull(cache.get("key2", String.class));
    }
    
    @Test
    void testSize() {
        assertEquals(0, cache.size());
        
        cache.put("key1", "value1");
        assertEquals(1, cache.size());
        
        cache.put("key2", "value2");
        assertEquals(2, cache.size());
    }
    
    @Test
    void testMaxSize() {
        assertEquals(3, cache.getMaxSize());
    }
}
