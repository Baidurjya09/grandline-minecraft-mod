package com.grandline.ability;

import com.grandline.GrandLineMod;
import net.minecraft.util.Identifier;

import java.util.*;

/**
 * Central registry for all abilities.
 * Manages ability registration and lookup.
 */
public class AbilityRegistry {
    
    private static final Map<Identifier, Ability> abilities = new HashMap<>();
    private static boolean initialized = false;
    
    /**
     * Initializes the ability registry.
     */
    public static void initialize() {
        if (initialized) {
            GrandLineMod.LOGGER.warn("AbilityRegistry already initialized!");
            return;
        }
        
        GrandLineMod.LOGGER.info("Initializing ability registry...");
        
        // Phase 3: No abilities registered yet
        // Future phases will register abilities here
        
        initialized = true;
        GrandLineMod.LOGGER.info("Ability registry initialized with {} abilities", abilities.size());
    }
    
    /**
     * Registers an ability.
     * 
     * @param ability The ability to register
     * @throws IllegalArgumentException if ability ID is already registered
     */
    public static void register(Ability ability) {
        if (abilities.containsKey(ability.getId())) {
            throw new IllegalArgumentException("Ability already registered: " + ability.getId());
        }
        
        abilities.put(ability.getId(), ability);
        GrandLineMod.LOGGER.debug("Registered ability: {}", ability.getId());
    }
    
    /**
     * Gets an ability by ID.
     * 
     * @param id The ability ID
     * @return The ability, or null if not found
     */
    public static Ability get(Identifier id) {
        return abilities.get(id);
    }
    
    /**
     * Gets all registered abilities.
     * 
     * @return Unmodifiable collection of abilities
     */
    public static Collection<Ability> getAll() {
        return Collections.unmodifiableCollection(abilities.values());
    }
    
    /**
     * Checks if an ability is registered.
     * 
     * @param id The ability ID
     * @return true if registered, false otherwise
     */
    public static boolean isRegistered(Identifier id) {
        return abilities.containsKey(id);
    }
    
    /**
     * Gets the number of registered abilities.
     * 
     * @return The count
     */
    public static int size() {
        return abilities.size();
    }
    
    /**
     * Clears all registered abilities.
     * Used for testing and cleanup.
     */
    public static void clear() {
        abilities.clear();
        initialized = false;
    }
}
