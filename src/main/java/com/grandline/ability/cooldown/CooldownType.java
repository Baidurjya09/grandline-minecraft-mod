package com.grandline.ability.cooldown;

/**
 * Types of cooldowns for abilities.
 */
public enum CooldownType {
    
    /**
     * Specific ability cooldown.
     * Only affects the specific ability that was used.
     */
    ABILITY,
    
    /**
     * Global cooldown.
     * Affects all abilities briefly after any ability is used.
     */
    GLOBAL,
    
    /**
     * Category cooldown.
     * Affects all abilities in a category (e.g., all fire abilities).
     */
    CATEGORY
}
