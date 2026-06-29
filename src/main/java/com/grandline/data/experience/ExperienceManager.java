package com.grandline.data.experience;

import com.grandline.GrandLineMod;
import com.grandline.core.event.EventBus;
import com.grandline.data.PlayerData;
import com.grandline.data.events.PlayerLevelUpEvent;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Manages player experience and leveling.
 */
public class ExperienceManager {
    
    private final XpCurve xpCurve;
    
    public ExperienceManager() {
        this.xpCurve = XpCurve.fromConfig();
    }
    
    /**
     * Awards experience to a player.
     * Handles level ups automatically.
     * 
     * @param player The player
     * @param playerData The player's data
     * @param amount The XP amount
     * @return The number of levels gained
     */
    public int awardExperience(ServerPlayerEntity player, PlayerData playerData, int amount) {
        int oldXp = playerData.getExperience();
        int oldLevel = playerData.getLevel();
        
        playerData.addExperience(amount);
        int newLevel = xpCurve.getLevelForXp(playerData.getExperience());
        
        if (newLevel > oldLevel) {
            playerData.setLevel(newLevel);
            handleLevelUp(player, playerData, oldLevel, newLevel);
            return newLevel - oldLevel;
        }
        
        return 0;
    }
    
    /**
     * Sets a player's level, adjusting XP accordingly.
     * 
     * @param player The player
     * @param playerData The player's data
     * @param level The target level
     */
    public void setLevel(ServerPlayerEntity player, PlayerData playerData, int level) {
        int oldLevel = playerData.getLevel();
        int maxLevel = GrandLineMod.getConfigManager().getConfig().leveling.maxLevel;
        level = Math.max(1, Math.min(level, maxLevel));
        
        playerData.setLevel(level);
        playerData.setExperience(xpCurve.getXpForLevel(level));
        
        if (level > oldLevel) {
            handleLevelUp(player, playerData, oldLevel, level);
        }
    }
    
    /**
     * Handles level up rewards and events.
     */
    private void handleLevelUp(ServerPlayerEntity player, PlayerData playerData, 
                               int oldLevel, int newLevel) {
        // Award skill points
        int pointsPerLevel = GrandLineMod.getConfigManager()
            .getConfig().leveling.skillPointsPerLevel;
        int levelsGained = newLevel - oldLevel;
        playerData.addSkillPoints(pointsPerLevel * levelsGained);
        
        // Fire event
        PlayerLevelUpEvent event = new PlayerLevelUpEvent(
            player, playerData, oldLevel, newLevel);
        EventBus.post(event);
        
        GrandLineMod.LOGGER.info("{} leveled up from {} to {}", 
            player.getName().getString(), oldLevel, newLevel);
    }
    
    /**
     * Gets the XP curve.
     * 
     * @return The XP curve
     */
    public XpCurve getXpCurve() {
        return xpCurve;
    }
}
