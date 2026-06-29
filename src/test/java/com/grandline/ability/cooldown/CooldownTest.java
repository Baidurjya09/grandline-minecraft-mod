package com.grandline.ability.cooldown;

import com.grandline.core.GrandLineUtil;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Cooldown.
 */
class CooldownTest {
    
    @Test
    void testCooldownCreation() {
        UUID playerId = UUID.randomUUID();
        Cooldown cooldown = new Cooldown(
            playerId,
            GrandLineUtil.id("test"),
            100,
            CooldownType.ABILITY
        );
        
        assertEquals(playerId, cooldown.getPlayerId());
        assertEquals(GrandLineUtil.id("test"), cooldown.getAbilityId());
        assertEquals(CooldownType.ABILITY, cooldown.getType());
    }
    
    @Test
    void testCooldownActive() throws InterruptedException {
        Cooldown cooldown = new Cooldown(
            UUID.randomUUID(),
            GrandLineUtil.id("test"),
            2, // 100ms (2 ticks at 50ms each)
            CooldownType.ABILITY
        );
        
        assertTrue(cooldown.isOnCooldown());
        assertTrue(cooldown.getRemaining() > 0);
        
        Thread.sleep(150); // Wait for cooldown to expire
        
        assertFalse(cooldown.isOnCooldown());
        assertEquals(0, cooldown.getRemaining());
    }
    
    @Test
    void testCooldownProgress() {
        Cooldown cooldown = new Cooldown(
            UUID.randomUUID(),
            GrandLineUtil.id("test"),
            100,
            CooldownType.ABILITY
        );
        
        float progress = cooldown.getProgress();
        assertTrue(progress >= 0.0f && progress <= 1.0f);
    }
    
    @Test
    void testRemainingTicks() {
        Cooldown cooldown = new Cooldown(
            UUID.randomUUID(),
            GrandLineUtil.id("test"),
            100,
            CooldownType.ABILITY
        );
        
        int ticks = cooldown.getRemainingTicks();
        assertTrue(ticks >= 0 && ticks <= 100);
    }
}
