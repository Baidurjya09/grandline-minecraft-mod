package com.grandline.ability.events;

import com.grandline.ability.Ability;
import com.grandline.ability.cooldown.Cooldown;
import com.grandline.core.event.Event;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Event fired when a cooldown completes.
 */
public class CooldownCompletedEvent extends Event {
    
    private final ServerPlayerEntity player;
    private final Ability ability;
    private final Cooldown cooldown;
    
    public CooldownCompletedEvent(ServerPlayerEntity player, Ability ability,
                                 Cooldown cooldown) {
        this.player = player;
        this.ability = ability;
        this.cooldown = cooldown;
    }
    
    public ServerPlayerEntity getPlayer() {
        return player;
    }
    
    public Ability getAbility() {
        return ability;
    }
    
    public Cooldown getCooldown() {
        return cooldown;
    }
}
