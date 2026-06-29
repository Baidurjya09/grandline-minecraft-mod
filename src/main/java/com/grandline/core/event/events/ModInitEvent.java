package com.grandline.core.event.events;

import com.grandline.core.event.Event;

/**
 * Event fired when the mod completes initialization.
 * This event is fired after all core systems are initialized
 * but before the game starts.
 * 
 * Use this event to initialize features that depend on core systems.
 */
public class ModInitEvent extends Event {
    
    private final InitPhase phase;
    
    public ModInitEvent(InitPhase phase) {
        this.phase = phase;
    }
    
    /**
     * Gets the initialization phase.
     * 
     * @return The init phase
     */
    public InitPhase getPhase() {
        return phase;
    }
    
    /**
     * Represents different phases of mod initialization.
     */
    public enum InitPhase {
        /**
         * Common initialization (runs on both client and server).
         */
        COMMON,
        
        /**
         * Client-only initialization.
         */
        CLIENT,
        
        /**
         * Dedicated server initialization.
         */
        SERVER
    }
    
    @Override
    public String toString() {
        return "ModInitEvent{phase=" + phase + "}";
    }
}
