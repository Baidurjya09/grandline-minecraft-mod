package com.grandline.core.resource;

import com.grandline.GrandLineMod;
import com.grandline.core.GrandLineUtil;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Loads resources from the mod's resource packs.
 * Provides caching and validation for efficient resource access.
 */
public class ResourceLoader {
    
    private final ResourceManager resourceManager;
    private final ResourceCache cache;
    
    public ResourceLoader(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        this.cache = new ResourceCache(
            GrandLineMod.getConfigManager().getConfig().performance.maxCachedEntries);
    }
    
    /**
     * Loads a resource as a string.
     * 
     * @param path The resource path (e.g., "data/example.json")
     * @return The resource content as string
     * @throws ResourceException if resource cannot be loaded
     */
    public String loadString(String path) throws ResourceException {
        // Check cache first
        String cached = cache.get(path, String.class);
        if (cached != null) {
            return cached;
        }
        
        try {
            Identifier id = GrandLineUtil.id(path);
            InputStream stream = resourceManager.getResource(id)
                .orElseThrow(() -> new ResourceException("Resource not found: " + path))
                .getInputStream();
            
            String content = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            cache.put(path, content);
            return content;
            
        } catch (IOException e) {
            throw new ResourceException("Failed to load resource: " + path, e);
        }
    }
    
    /**
     * Loads a resource as a byte array.
     * 
     * @param path The resource path
     * @return The resource content as bytes
     * @throws ResourceException if resource cannot be loaded
     */
    public byte[] loadBytes(String path) throws ResourceException {
        try {
            Identifier id = GrandLineUtil.id(path);
            return resourceManager.getResource(id)
                .orElseThrow(() -> new ResourceException("Resource not found: " + path))
                .getInputStream()
                .readAllBytes();
                
        } catch (IOException e) {
            throw new ResourceException("Failed to load resource: " + path, e);
        }
    }
    
    /**
     * Checks if a resource exists.
     * 
     * @param path The resource path
     * @return true if resource exists, false otherwise
     */
    public boolean exists(String path) {
        Identifier id = GrandLineUtil.id(path);
        return resourceManager.getResource(id).isPresent();
    }
    
    /**
     * Clears the resource cache.
     */
    public void clearCache() {
        cache.clear();
        GrandLineMod.LOGGER.info("Resource cache cleared");
    }
    
    /**
     * Gets the resource cache for inspection.
     * 
     * @return The resource cache
     */
    public ResourceCache getCache() {
        return cache;
    }
}
