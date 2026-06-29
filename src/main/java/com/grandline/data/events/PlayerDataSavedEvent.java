package com.grandline.data.events;

import com.grandline.core.event.Event;
import com.grandline.data.PlayerData;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Event fired when player data is saved.
 */
public class PlayerDataSavedEvent extends Event {
    
    private final ServerPlayerEntity player;
    private final PlayerData data;
    
    public PlayerDataSavedEvent(ServerPlayerEntity player, PlayerData data) {
        this.player = player;
        this.data = data;
    }
    
    public ServerPlayerEntity getPlayer() {
        return player;
    }
    
    public PlayerData getData() {
        return data;
    }
}
