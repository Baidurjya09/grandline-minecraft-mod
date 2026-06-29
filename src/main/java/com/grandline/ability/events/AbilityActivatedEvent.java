package com.grandline.ability.events;

import com.grandline.ability.Ability;
import com.grandline.core.event.Cancellable;
import com.grandline.core.event.Event;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Event fired when a player activates an ability.
 * Can be cancelled to prevent activation.
 */
public class AbilityActivatedEvent extends Event implements Cancellable {
    
    private final ServerPlayerEntity player;
    private final Ability ability;
    
    public AbilityActivatedEvent(ServerPlayerEntity player, Ability ability) {
        this.player = player;
        this.ability = ability;
    }
    
    public ServerPlayerEntity getPlayer() {
        return player;
    }
    
    public Ability getAbility() {
        return ability;
    }
}
