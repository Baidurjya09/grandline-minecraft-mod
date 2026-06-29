package com.grandline.data.stats;

import net.minecraft.nbt.NbtCompound;

import java.util.EnumMap;
import java.util.Map;

/**
 * Container for player statistics.
 * Tracks various gameplay metrics for a player.
 */
public class PlayerStats {
    
    private final Map<StatType, Long> stats;
    
    public PlayerStats() {
        this.stats = new EnumMap<>(StatType.class);
        // Initialize all stats to 0
        for (StatType type : StatType.values()) {
            stats.put(type, 0L);
        }
    }
    
    /**
     * Gets a stat value.
     * 
     * @param type The stat type
     * @return The stat value
     */
    public long getStat(StatType type) {
        return stats.getOrDefault(type, 0L);
    }
    
    /**
     * Sets a stat value.
     * 
     * @param type The stat type
     * @param value The new value
     */
    public void setStat(StatType type, long value) {
        stats.put(type, Math.max(0, value));
    }
    
    /**
     * Adds to a stat value.
     * 
     * @param type The stat type
     * @param amount The amount to add
     */
    public void addStat(StatType type, long amount) {
        long current = getStat(type);
        setStat(type, current + amount);
    }
    
    /**
     * Increments a stat by 1.
     * 
     * @param type The stat type
     */
    public void increment(StatType type) {
        addStat(type, 1);
    }
    
    /**
     * Resets a stat to 0.
     * 
     * @param type The stat type
     */
    public void resetStat(StatType type) {
        stats.put(type, 0L);
    }
    
    /**
     * Resets all stats to 0.
     */
    public void resetAll() {
        for (StatType type : StatType.values()) {
            stats.put(type, 0L);
        }
    }
    
    /**
     * Gets all stats as an unmodifiable map.
     * 
     * @return The stats map
     */
    public Map<StatType, Long> getAllStats() {
        return new EnumMap<>(stats);
    }
    
    /**
     * Serializes stats to NBT.
     * 
     * @return The NBT compound
     */
    public NbtCompound serializeNbt() {
        NbtCompound nbt = new NbtCompound();
        for (Map.Entry<StatType, Long> entry : stats.entrySet()) {
            nbt.putLong(entry.getKey().getId(), entry.getValue());
        }
        return nbt;
    }
    
    /**
     * Deserializes stats from NBT.
     * 
     * @param nbt The NBT compound
     */
    public void deserializeNbt(NbtCompound nbt) {
        for (StatType type : StatType.values()) {
            if (nbt.contains(type.getId())) {
                stats.put(type, nbt.getLong(type.getId()));
            }
        }
    }
    
    @Override
    public String toString() {
        return "PlayerStats{" + stats + '}';
    }
}
