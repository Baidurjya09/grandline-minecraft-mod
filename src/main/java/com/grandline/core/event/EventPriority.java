package com.grandline.core.event;

/**
 * Defines the execution priority for event handlers.
 * Handlers are executed in order from lowest to highest priority.
 * 
 * Priority Guidelines:
 * - LOWEST: For monitoring, should not modify event state
 * - LOW: For most regular listeners
 * - NORMAL: Default priority, general gameplay logic
 * - HIGH: For important gameplay systems
 * - HIGHEST: For critical systems like anti-cheat
 * - MONITOR: For logging/monitoring only, should never modify state or cancel
 */
public enum EventPriority {
    
    /**
     * Lowest priority, executed first.
     * Use for early modifications that other systems may override.
     */
    LOWEST(0),
    
    /**
     * Low priority, executed after LOWEST.
     * Use for non-critical listeners.
     */
    LOW(1),
    
    /**
     * Normal priority, the default.
     * Use for most standard event handlers.
     */
    NORMAL(2),
    
    /**
     * High priority, executed after NORMAL.
     * Use for important gameplay systems.
     */
    HIGH(3),
    
    /**
     * Highest priority, executed near the end.
     * Use for critical systems that need final say.
     */
    HIGHEST(4),
    
    /**
     * Monitor priority, executed last.
     * Should ONLY be used for monitoring/logging.
     * Must NOT modify event state or cancel events.
     */
    MONITOR(5);
    
    private final int slot;
    
    EventPriority(int slot) {
        this.slot = slot;
    }
    
    /**
     * Gets the numerical slot for this priority.
     * Lower numbers execute first.
     * 
     * @return The priority slot
     */
    public int getSlot() {
        return slot;
    }
}
