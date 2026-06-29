package com.grandline.ability.cooldown;

import net.minecraft.util.Identifier;

import java.util.UUID;

/**
 * Represents a cooldown timer for an ability.
 * Tracks when an ability was used and when it can be used again.
 */
public class Cooldown {
    
    private final UUID playerId;
    private final Identifier abilityId;
    private final long startTime;
    private final long durationMillis;
    private final CooldownType type;
    
    /**
     * Creates a new cooldown.
     * 
     * @param playerId The player's UUID
     * @param abilityId The ability ID
     * @param durationTicks The duration in ticks
     * @param type The cooldown type
     */
    public Cooldown(UUID playerId, Identifier abilityId, int durationTicks, CooldownType type) {
        this.playerId = playerId;
        this.abilityId = abilityId;
        this.startTime = System.currentTimeMillis();
        this.durationMillis = durationTicks * 50L; // 20 ticks per second
        this.type = type;
    }
    
    /**
     * Gets the player UUID.
     * 
     * @return The UUID
     */
    public UUID getPlayerId() {
        return playerId;
    }
    
    /**
     * Gets the ability ID.
     * 
     * @return The ability ID
     */
    public Identifier getAbilityId() {
        return abilityId;
    }
    
    /**
     * Gets the cooldown type.
     * 
     * @return The type
     */
    public CooldownType getType() {
        return type;
    }
    
    /**
     * Checks if this cooldown is still active.
     * 
     * @return true if on cooldown, false if expired
     */
    public boolean isOnCooldown() {
        return getRemaining() > 0;
    }
    
    /**
     * Gets the remaining time in milliseconds.
     * 
     * @return The remaining time, or 0 if expired
     */
    public long getRemaining() {
        long elapsed = System.currentTimeMillis() - startTime;
        long remaining = durationMillis - elapsed;
        return Math.max(0, remaining);
    }
    
    /**
     * Gets the remaining time in ticks.
     * 
     * @return The remaining ticks
     */
    public int getRemainingTicks() {
        return (int) (getRemaining() / 50L);
    }
    
    /**
     * Gets the cooldown progress from 0.0 to 1.0.
     * 0.0 = just started, 1.0 = completed
     * 
     * @return The progress
     */
    public float getProgress() {
        if (durationMillis == 0) {
            return 1.0f;
        }
        
        long elapsed = System.currentTimeMillis() - startTime;
        float progress = (float) elapsed / durationMillis;
        return Math.min(1.0f, Math.max(0.0f, progress));
    }
    
    /**
     * Gets the total duration in milliseconds.
     * 
     * @return The duration
     */
    public long getDuration() {
        return durationMillis;
    }
    
    @Override
    public String toString() {
        return "Cooldown{" +
                "ability=" + abilityId +
                ", remaining=" + getRemaining() + "ms" +
                ", progress=" + String.format("%.1f%%", getProgress() * 100) +
                '}';
    }
}
