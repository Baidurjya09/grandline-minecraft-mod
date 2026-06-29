package com.grandline.data.events;

import com.grandline.core.event.Cancellable;
import com.grandline.core.event.Event;
import com.grandline.data.PlayerData;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Event fired when a player levels up.
 * Can be cancelled to prevent level up.
 */
public class PlayerLevelUpEvent extends Event implements Cancellable {
    
    private final ServerPlayerEntity player;
    private final PlayerData data;
    private final int oldLevel;
    private final int newLevel;
    
    public PlayerLevelUpEvent(ServerPlayerEntity player, PlayerData data, 
                             int oldLevel, int newLevel) {
        this.player = player;
        this.data = data;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }
    
    public ServerPlayerEntity getPlayer() {
        return player;
    }
    
    public PlayerData getData() {
        return data;
    }
    
    public int getOldLevel() {
        return oldLevel;
    }
    
    public int getNewLevel() {
        return newLevel;
    }
    
    public int getLevelsGained() {
        return newLevel - oldLevel;
    }
}
