package com.grandline.core.resource;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRU cache for loaded resources.
 * Automatically evicts least recently used entries when capacity is reached.
 */
public class ResourceCache {
    
    private final int maxSize;
    private final Map<String, Object> cache;
    
    public ResourceCache(int maxSize) {
        this.maxSize = maxSize;
        this.cache = new LinkedHashMap<String, Object>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Object> eldest) {
                return size() > ResourceCache.this.maxSize;
            }
        };
    }
    
    /**
     * Gets a cached value.
     * 
     * @param key The cache key
     * @param type The expected type
     * @param <T> The value type
     * @return The cached value, or null if not found or wrong type
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        Object value = cache.get(key);
        if (value != null && type.isInstance(value)) {
            return (T) value;
        }
        return null;
    }
    
    /**
     * Puts a value into the cache.
     * 
     * @param key The cache key
     * @param value The value to cache
     */
    public void put(String key, Object value) {
        cache.put(key, value);
    }
    
    /**
     * Removes a value from the cache.
     * 
     * @param key The cache key
     */
    public void remove(String key) {
        cache.remove(key);
    }
    
    /**
     * Clears the entire cache.
     */
    public void clear() {
        cache.clear();
    }
    
    /**
     * Gets the current cache size.
     * 
     * @return The number of cached entries
     */
    public int size() {
        return cache.size();
    }
    
    /**
     * Gets the maximum cache size.
     * 
     * @return The max size
     */
    public int getMaxSize() {
        return maxSize;
    }
}
