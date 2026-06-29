package com.grandline.ability.cooldown;

import com.grandline.GrandLineMod;
import com.grandline.ability.Ability;
import com.grandline.core.event.EventBus;
import com.grandline.ability.events.CooldownStartedEvent;
import com.grandline.ability.events.CooldownCompletedEvent;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages cooldowns for all players and abilities.
 * Tracks active cooldowns and provides cooldown checking.
 */
public class CooldownManager {
    
    private static CooldownManager instance;
    
    // Map: Player UUID -> Map of Ability ID -> Cooldown
    private final Map<UUID, Map<Identifier, Cooldown>> cooldowns;
    
    private CooldownManager() {
        this.cooldowns = new ConcurrentHashMap<>();
    }
    
    /**
     * Gets the singleton instance.
     * 
     * @return The cooldown manager
     */
    public static CooldownManager getInstance() {
        if (instance == null) {
            instance = new CooldownManager();
            GrandLineMod.LOGGER.info("CooldownManager initialized");
        }
        return instance;
    }
    
    /**
     * Starts a cooldown for a player's ability.
     * 
     * @param player The player
     * @param ability The ability
     */
    public void startCooldown(ServerPlayerEntity player, Ability ability) {
        UUID playerId = player.getUuid();
        Identifier abilityId = ability.getId();
        
        Cooldown cooldown = new Cooldown(
            playerId, 
            abilityId, 
            ability.getCooldownTicks(), 
            CooldownType.ABILITY
        );
        
        cooldowns.computeIfAbsent(playerId, k -> new ConcurrentHashMap<>())
                 .put(abilityId, cooldown);
        
        // Fire event
        EventBus.post(new CooldownStartedEvent(player, ability, cooldown));
        
        GrandLineMod.LOGGER.debug("Started cooldown for {} - {}", 
            player.getName().getString(), abilityId);
        
        // Schedule cooldown completion check
        scheduleCooldownCheck(player, ability, cooldown);
    }
    
    /**
     * Checks if a player has an ability on cooldown.
     * 
     * @param player The player
     * @param abilityId The ability ID
     * @return true if on cooldown, false otherwise
     */
    public boolean isOnCooldown(ServerPlayerEntity player, Identifier abilityId) {
        Map<Identifier, Cooldown> playerCooldowns = cooldowns.get(player.getUuid());
        if (playerCooldowns == null) {
            return false;
        }
        
        Cooldown cooldown = playerCooldowns.get(abilityId);
        if (cooldown == null) {
            return false;
        }
        
        if (cooldown.isOnCooldown()) {
            return true;
        }
        
        // Cooldown expired, remove it
        playerCooldowns.remove(abilityId);
        return false;
    }
    
    /**
     * Gets the remaining cooldown time in ticks.
     * 
     * @param player The player
     * @param abilityId The ability ID
     * @return The remaining ticks, or 0 if not on cooldown
     */
    public int getRemainingTicks(ServerPlayerEntity player, Identifier abilityId) {
        Map<Identifier, Cooldown> playerCooldowns = cooldowns.get(player.getUuid());
        if (playerCooldowns == null) {
            return 0;
        }
        
        Cooldown cooldown = playerCooldowns.get(abilityId);
        return cooldown != null ? cooldown.getRemainingTicks() : 0;
    }
    
    /**
     * Gets a cooldown for a player's ability.
     * 
     * @param player The player
     * @param abilityId The ability ID
     * @return The cooldown, or null if none
     */
    public Cooldown getCooldown(ServerPlayerEntity player, Identifier abilityId) {
        Map<Identifier, Cooldown> playerCooldowns = cooldowns.get(player.getUuid());
        return playerCooldowns != null ? playerCooldowns.get(abilityId) : null;
    }
    
    /**
     * Resets a specific cooldown for a player.
     * 
     * @param player The player
     * @param abilityId The ability ID
     */
    public void resetCooldown(ServerPlayerEntity player, Identifier abilityId) {
        Map<Identifier, Cooldown> playerCooldowns = cooldowns.get(player.getUuid());
        if (playerCooldowns != null) {
            playerCooldowns.remove(abilityId);
            GrandLineMod.LOGGER.debug("Reset cooldown for {} - {}", 
                player.getName().getString(), abilityId);
        }
    }
    
    /**
     * Resets all cooldowns for a player.
     * 
     * @param player The player
     */
    public void resetAllCooldowns(ServerPlayerEntity player) {
        cooldowns.remove(player.getUuid());
        GrandLineMod.LOGGER.debug("Reset all cooldowns for {}", 
            player.getName().getString());
    }
    
    /**
     * Gets all active cooldowns for a player.
     * 
     * @param player The player
     * @return Collection of cooldowns
     */
    public Collection<Cooldown> getPlayerCooldowns(ServerPlayerEntity player) {
        Map<Identifier, Cooldown> playerCooldowns = cooldowns.get(player.getUuid());
        return playerCooldowns != null ? 
            playerCooldowns.values() : Collections.emptyList();
    }
    
    /**
     * Schedules a check for cooldown completion.
     */
    private void scheduleCooldownCheck(ServerPlayerEntity player, Ability ability, 
                                      Cooldown cooldown) {
        // This would use a scheduled task in a full implementation
        // For now, cooldowns are checked on-demand
    }
    
    /**
     * Called when a cooldown completes.
     */
    private void onCooldownComplete(ServerPlayerEntity player, Ability ability, 
                                   Cooldown cooldown) {
        EventBus.post(new CooldownCompletedEvent(player, ability, cooldown));
        ability.onCooldownComplete(player);
    }
    
    /**
     * Cleans up expired cooldowns for all players.
     * Should be called periodically.
     */
    public void cleanupExpiredCooldowns() {
        int removed = 0;
        for (Map<Identifier, Cooldown> playerCooldowns : cooldowns.values()) {
            playerCooldowns.entrySet().removeIf(entry -> {
                if (!entry.getValue().isOnCooldown()) {
                    removed++;
                    return true;
                }
                return false;
            });
        }
        
        if (removed > 0) {
            GrandLineMod.LOGGER.debug("Cleaned up {} expired cooldowns", removed);
        }
    }
}
