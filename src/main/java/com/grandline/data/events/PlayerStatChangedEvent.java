package com.grandline.data.events;

import com.grandline.core.event.Event;
import com.grandline.data.PlayerData;
import com.grandline.data.stats.StatType;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Event fired when a player statistic changes.
 */
public class PlayerStatChangedEvent extends Event {
    
    private final ServerPlayerEntity player;
    private final PlayerData data;
    private final StatType statType;
    private final long oldValue;
    private final long newValue;
    
    public PlayerStatChangedEvent(ServerPlayerEntity player, PlayerData data,
                                 StatType statType, long oldValue, long newValue) {
        this.player = player;
        this.data = data;
        this.statType = statType;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
    
    public ServerPlayerEntity getPlayer() {
        return player;
    }
    
    public PlayerData getData() {
        return data;
    }
    
    public StatType getStatType() {
        return statType;
    }
    
    public long getOldValue() {
        return oldValue;
    }
    
    public long getNewValue() {
        return newValue;
    }
    
    public long getChange() {
        return newValue - oldValue;
    }
}
