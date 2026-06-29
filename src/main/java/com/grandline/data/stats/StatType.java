package com.grandline.data.stats;

/**
 * Enumeration of all trackable player statistics.
 */
public enum StatType {
    
    /**
     * Total damage dealt to entities.
     */
    DAMAGE_DEALT("damage_dealt", "Damage Dealt"),
    
    /**
     * Total damage taken from any source.
     */
    DAMAGE_TAKEN("damage_taken", "Damage Taken"),
    
    /**
     * Total distance traveled in centimeters.
     */
    DISTANCE_TRAVELED("distance_traveled", "Distance Traveled"),
    
    /**
     * Total playtime in milliseconds.
     */
    PLAYTIME("playtime", "Playtime"),
    
    /**
     * Number of deaths.
     */
    DEATHS("deaths", "Deaths"),
    
    /**
     * Number of kills.
     */
    KILLS("kills", "Kills"),
    
    /**
     * Number of jumps.
     */
    JUMPS("jumps", "Jumps"),
    
    /**
     * Blocks broken.
     */
    BLOCKS_BROKEN("blocks_broken", "Blocks Broken"),
    
    /**
     * Blocks placed.
     */
    BLOCKS_PLACED("blocks_placed", "Blocks Placed"),
    
    /**
     * Items picked up.
     */
    ITEMS_PICKED_UP("items_picked_up", "Items Picked Up");
    
    private final String id;
    private final String displayName;
    
    StatType(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }
    
    /**
     * Gets the stat ID (for serialization).
     * 
     * @return The stat ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Gets the display name (for UI).
     * 
     * @return The display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Gets a stat type by ID.
     * 
     * @param id The stat ID
     * @return The stat type, or null if not found
     */
    public static StatType fromId(String id) {
        for (StatType type : values()) {
            if (type.id.equals(id)) {
                return type;
            }
        }
        return null;
    }
}
