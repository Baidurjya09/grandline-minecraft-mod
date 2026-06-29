package com.grandline.ability;

import com.grandline.GrandLineMod;
import com.grandline.ability.cooldown.CooldownManager;
import com.grandline.ability.events.AbilityActivatedEvent;
import com.grandline.core.event.EventBus;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.*;

/**
 * Manages ability usage and player ability assignments.
 */
public class AbilityManager {
    
    private static AbilityManager instance;
    
    // Map: Player UUID -> Set of Ability IDs
    private final Map<UUID, Set<Identifier>> playerAbilities;
    
    private AbilityManager() {
        this.playerAbilities = new HashMap<>();
    }
    
    /**
     * Gets the singleton instance.
     * 
     * @return The ability manager
     */
    public static AbilityManager getInstance() {
        if (instance == null) {
            instance = new AbilityManager();
            GrandLineMod.LOGGER.info("AbilityManager initialized");
        }
        return instance;
    }
    
    /**
     * Attempts to activate an ability for a player.
     * 
     * @param player The player
     * @param abilityId The ability ID
     * @return true if activated, false if failed
     */
    public boolean activateAbility(ServerPlayerEntity player, Identifier abilityId) {
        // Get ability
        Ability ability = AbilityRegistry.get(abilityId);
        if (ability == null) {
            player.sendMessage(Text.literal("Unknown ability!").formatted(Formatting.RED));
            return false;
        }
        
        // Check if player has the ability
        if (!hasAbility(player, abilityId)) {
            player.sendMessage(Text.literal("You don't have this ability!")
                .formatted(Formatting.RED));
            return false;
        }
        
        // Check cooldown
        CooldownManager cooldownMgr = CooldownManager.getInstance();
        if (cooldownMgr.isOnCooldown(player, abilityId)) {
            int remaining = cooldownMgr.getRemainingTicks(player, abilityId);
            player.sendMessage(Text.literal("Ability on cooldown: " + 
                (remaining / 20) + "s").formatted(Formatting.YELLOW));
            return false;
        }
        
        // Check if can activate
        if (!ability.canActivate(player)) {
            player.sendMessage(Text.literal("Cannot use this ability right now!")
                .formatted(Formatting.RED));
            return false;
        }
        
        // Fire event
        AbilityActivatedEvent event = new AbilityActivatedEvent(player, ability);
        EventBus.post(event);
        
        if (event.isCancelled()) {
            return false;
        }
        
        // Activate ability
        try {
            ability.activate(player);
            cooldownMgr.startCooldown(player, ability);
            
            player.sendMessage(Text.literal("Activated: " + ability.getName())
                .formatted(Formatting.GREEN));
            
            GrandLineMod.LOGGER.info("{} activated ability: {}", 
                player.getName().getString(), abilityId);
            
            return true;
            
        } catch (Exception e) {
            GrandLineMod.LOGGER.error("Error activating ability", e);
            player.sendMessage(Text.literal("Error activating ability!")
                .formatted(Formatting.RED));
            return false;
        }
    }
    
    /**
     * Gives an ability to a player.
     * 
     * @param player The player
     * @param abilityId The ability ID
     */
    public void giveAbility(ServerPlayerEntity player, Identifier abilityId) {
        playerAbilities.computeIfAbsent(player.getUuid(), k -> new HashSet<>())
                      .add(abilityId);
        
        GrandLineMod.LOGGER.info("Gave ability {} to {}", 
            abilityId, player.getName().getString());
    }
    
    /**
     * Removes an ability from a player.
     * 
     * @param player The player
     * @param abilityId The ability ID
     */
    public void removeAbility(ServerPlayerEntity player, Identifier abilityId) {
        Set<Identifier> abilities = playerAbilities.get(player.getUuid());
        if (abilities != null) {
            abilities.remove(abilityId);
            GrandLineMod.LOGGER.info("Removed ability {} from {}", 
                abilityId, player.getName().getString());
        }
    }
    
    /**
     * Checks if a player has an ability.
     * 
     * @param player The player
     * @param abilityId The ability ID
     * @return true if player has the ability
     */
    public boolean hasAbility(ServerPlayerEntity player, Identifier abilityId) {
        Set<Identifier> abilities = playerAbilities.get(player.getUuid());
        return abilities != null && abilities.contains(abilityId);
    }
    
    /**
     * Gets all abilities a player has.
     * 
     * @param player The player
     * @return Set of ability IDs
     */
    public Set<Identifier> getPlayerAbilities(ServerPlayerEntity player) {
        return playerAbilities.getOrDefault(player.getUuid(), Collections.emptySet());
    }
}
