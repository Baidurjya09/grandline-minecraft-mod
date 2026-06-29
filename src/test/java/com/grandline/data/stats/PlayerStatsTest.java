package com.grandline.data.stats;

import net.minecraft.nbt.NbtCompound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PlayerStats.
 */
class PlayerStatsTest {
    
    private PlayerStats stats;
    
    @BeforeEach
    void setUp() {
        stats = new PlayerStats();
    }
    
    @Test
    void testDefaultValues() {
        for (StatType type : StatType.values()) {
            assertEquals(0, stats.getStat(type));
        }
    }
    
    @Test
    void testSetStat() {
        stats.setStat(StatType.KILLS, 10);
        assertEquals(10, stats.getStat(StatType.KILLS));
        
        stats.setStat(StatType.KILLS, -5); // Should clamp to 0
        assertEquals(0, stats.getStat(StatType.KILLS));
    }
    
    @Test
    void testAddStat() {
        stats.setStat(StatType.DAMAGE_DEALT, 100);
        stats.addStat(StatType.DAMAGE_DEALT, 50);
        assertEquals(150, stats.getStat(StatType.DAMAGE_DEALT));
    }
    
    @Test
    void testIncrement() {
        stats.increment(StatType.JUMPS);
        assertEquals(1, stats.getStat(StatType.JUMPS));
        
        stats.increment(StatType.JUMPS);
        assertEquals(2, stats.getStat(StatType.JUMPS));
    }
    
    @Test
    void testResetStat() {
        stats.setStat(StatType.DEATHS, 5);
        stats.resetStat(StatType.DEATHS);
        assertEquals(0, stats.getStat(StatType.DEATHS));
    }
    
    @Test
    void testResetAll() {
        stats.setStat(StatType.KILLS, 10);
        stats.setStat(StatType.DEATHS, 5);
        
        stats.resetAll();
        
        assertEquals(0, stats.getStat(StatType.KILLS));
        assertEquals(0, stats.getStat(StatType.DEATHS));
    }
    
    @Test
    void testSerialization() {
        stats.setStat(StatType.KILLS, 10);
        stats.setStat(StatType.DAMAGE_DEALT, 500);
        
        NbtCompound nbt = stats.serializeNbt();
        
        PlayerStats restored = new PlayerStats();
        restored.deserializeNbt(nbt);
        
        assertEquals(10, restored.getStat(StatType.KILLS));
        assertEquals(500, restored.getStat(StatType.DAMAGE_DEALT));
    }
}
