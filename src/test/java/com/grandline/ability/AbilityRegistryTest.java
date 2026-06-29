package com.grandline.ability;

import com.grandline.core.GrandLineUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AbilityRegistry.
 */
class AbilityRegistryTest {
    
    private static class TestAbility extends Ability {
        public TestAbility() {
            super(
                GrandLineUtil.id("test"),
                "Test",
                "Test ability",
                100,
                10,
                1
            );
        }
        
        @Override
        public void activate(ServerPlayerEntity player) {
            // Test implementation
        }
    }
    
    @BeforeEach
    void setUp() {
        AbilityRegistry.clear();
        AbilityRegistry.initialize();
    }
    
    @AfterEach
    void tearDown() {
        AbilityRegistry.clear();
    }
    
    @Test
    void testRegistration() {
        TestAbility ability = new TestAbility();
        AbilityRegistry.register(ability);
        
        assertTrue(AbilityRegistry.isRegistered(ability.getId()));
        assertEquals(1, AbilityRegistry.size());
    }
    
    @Test
    void testGet() {
        TestAbility ability = new TestAbility();
        AbilityRegistry.register(ability);
        
        Ability retrieved = AbilityRegistry.get(ability.getId());
        assertNotNull(retrieved);
        assertEquals(ability.getId(), retrieved.getId());
    }
    
    @Test
    void testDuplicateRegistration() {
        TestAbility ability1 = new TestAbility();
        AbilityRegistry.register(ability1);
        
        TestAbility ability2 = new TestAbility();
        assertThrows(IllegalArgumentException.class, () -> {
            AbilityRegistry.register(ability2);
        });
    }
    
    @Test
    void testGetAll() {
        TestAbility ability = new TestAbility();
        AbilityRegistry.register(ability);
        
        assertEquals(1, AbilityRegistry.getAll().size());
        assertTrue(AbilityRegistry.getAll().contains(ability));
    }
}
