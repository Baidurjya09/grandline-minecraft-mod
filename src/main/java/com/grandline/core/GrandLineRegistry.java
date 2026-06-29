package com.grandline.core;

import com.grandline.GrandLineMod;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

/**
 * Central registry system for all Grand Line mod content.
 * This class manages the registration of all custom content including:
 * - Items
 * - Blocks
 * - Entities
 * - Status Effects
 * - Sounds
 * - Particles
 * - Custom registries (fruits, abilities, etc.)
 * 
 * All registrations must go through this class to ensure proper ordering and tracking.
 */
public class GrandLineRegistry {
    
    private static boolean initialized = false;
    
    /**
     * Initializes all registry systems.
     * This method must be called during mod initialization.
     * 
     * @throws IllegalStateException if called multiple times
     */
    public static void initialize() {
        if (initialized) {
            throw new IllegalStateException("Registry already initialized!");
        }
        
        GrandLineMod.LOGGER.info("Initializing registry system...");
        
        try {
            // Phase 0: No content to register yet
            // Future phases will add:
            // - registerItems()
            // - registerBlocks()
            // - registerEntities()
            // - registerStatusEffects()
            // - registerSounds()
            // - registerParticles()
            // - registerCustomRegistries()
            
            initialized = true;
            GrandLineMod.LOGGER.info("Registry system initialized successfully");
            
        } catch (Exception e) {
            GrandLineMod.LOGGER.error("Failed to initialize registry system", e);
            throw new RuntimeException("Registry initialization failed", e);
        }
    }
    
    /**
     * Checks if registry has been initialized.
     * 
     * @return true if initialized, false otherwise
     */
    public static boolean isInitialized() {
        return initialized;
    }
    
    /**
     * Creates a properly namespaced registry key.
     * 
     * @param registry The registry to create a key for
     * @param path The path/id for the registered object
     * @param <T> The type of the registered object
     * @return A registry key with proper namespacing
     */
    public static <T> RegistryKey<T> createKey(RegistryKey<? extends Registry<T>> registry, String path) {
        return RegistryKey.of(registry, GrandLineUtil.id(path));
    }
}
