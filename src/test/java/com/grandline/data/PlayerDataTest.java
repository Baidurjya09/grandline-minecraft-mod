package com.grandline.data;

import com.grandline.data.stats.StatType;
import net.minecraft.nbt.NbtCompound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PlayerData.
 */
class PlayerDataTest {
    
    private PlayerData data;
    private UUID playerId;
    
    @BeforeEach
    void setUp() {
        playerId = UUID.randomUUID();
        data = new PlayerData(playerId, "TestPlayer");
    }
    
    @Test
    void testDefaultValues() {
        assertEquals(playerId, data.getPlayerId());
        assertEquals("TestPlayer", data.getPlayerName());
        assertEquals(0, data.getExperience());
        assertEquals(1, data.getLevel());
        assertEquals(0, data.getSkillPoints());
    }
    
    @Test
    void testExperienceManipulation() {
        data.setExperience(100);
        assertEquals(100, data.getExperience());
        
        data.addExperience(50);
        assertEquals(150, data.getExperience());
    }
    
    @Test
    void testLevelManipulation() {
        data.setLevel(10);
        assertEquals(10, data.getLevel());
        
        data.setLevel(-5); // Should clamp to 1
        assertEquals(1, data.getLevel());
    }
    
    @Test
    void testSkillPoints() {
        data.setSkillPoints(5);
        assertEquals(5, data.getSkillPoints());
        
        data.addSkillPoints(3);
        assertEquals(8, data.getSkillPoints());
        
        assertTrue(data.spendSkillPoints(3));
        assertEquals(5, data.getSkillPoints());
        
        assertFalse(data.spendSkillPoints(10)); // Not enough
        assertEquals(5, data.getSkillPoints());
    }
    
    @Test
    void testStats() {
        assertEquals(0, data.getStat(StatType.KILLS));
        
        data.setStat(StatType.KILLS, 10);
        assertEquals(10, data.getStat(StatType.KILLS));
        
        data.addStat(StatType.KILLS, 5);
        assertEquals(15, data.getStat(StatType.KILLS));
    }
    
    @Test
    void testDirtyFlag() {
        PlayerData cleanData = new PlayerData(UUID.randomUUID(), "Test");
        cleanData.markClean();
        assertFalse(cleanData.isDirty());
        
        cleanData.setExperience(100);
        assertTrue(cleanData.isDirty());
    }
    
    @Test
    void testSerialization() {
        data.setExperience(500);
        data.setLevel(5);
        data.setSkillPoints(3);
        data.setStat(StatType.KILLS, 10);
        
        NbtCompound nbt = data.serializeNbt();
        PlayerData restored = PlayerData.deserializeNbt(nbt);
        
        assertEquals(data.getPlayerId(), restored.getPlayerId());
        assertEquals(data.getPlayerName(), restored.getPlayerName());
        assertEquals(data.getExperience(), restored.getExperience());
        assertEquals(data.getLevel(), restored.getLevel());
        assertEquals(data.getSkillPoints(), restored.getSkillPoints());
        assertEquals(data.getStat(StatType.KILLS), restored.getStat(StatType.KILLS));
    }
}
