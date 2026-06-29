package com.grandline.ability;

import com.grandline.ability.animation.AbilityAnimation;
import com.grandline.ability.particle.ParticleEffect;
import com.grandline.ability.sound.AbilitySound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for all abilities in the Grand Line mod.
 * 
 * Abilities are special powers that players can use with cooldowns,
 * animations, particles, and sound effects.
 * 
 * Implementation Guidelines:
 * - Override canActivate() for custom validation
 * - Override activate() for ability logic
 * - Call super.activate() to handle effects
 * - Make abilities server-authoritative
 */
public abstract class Ability {
    
    protected final Identifier id;
    protected final String name;
    protected final String description;
    protected final int cooldownTicks;
    protected final int energyCost;
    protected final int requiredLevel;
    
    protected final List<ParticleEffect> particles;
    protected final List<AbilitySound> sounds;
    protected AbilityAnimation animation;
    
    /**
     * Creates a new ability.
     * 
     * @param id The ability identifier
     * @param name The display name
     * @param description The description
     * @param cooldownTicks Cooldown in ticks (20 = 1 second)
     * @param energyCost Energy cost to use
     * @param requiredLevel Minimum level required
     */
    protected Ability(Identifier id, String name, String description,
                     int cooldownTicks, int energyCost, int requiredLevel) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cooldownTicks = cooldownTicks;
        this.energyCost = energyCost;
        this.requiredLevel = requiredLevel;
        this.particles = new ArrayList<>();
        this.sounds = new ArrayList<>();
    }
    
    /**
     * Gets the ability identifier.
     * 
     * @return The identifier
     */
    public Identifier getId() {
        return id;
    }
    
    /**
     * Gets the display name.
     * 
     * @return The name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the description.
     * 
     * @return The description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Gets the cooldown duration in ticks.
     * 
     * @return The cooldown ticks
     */
    public int getCooldownTicks() {
        return cooldownTicks;
    }
    
    /**
     * Gets the energy cost.
     * 
     * @return The energy cost
     */
    public int getEnergyCost() {
        return energyCost;
    }
    
    /**
     * Gets the required level.
     * 
     * @return The required level
     */
    public int getRequiredLevel() {
        return requiredLevel;
    }
    
    /**
     * Gets the particle effects.
     * 
     * @return The particle effects list
     */
    public List<ParticleEffect> getParticles() {
        return particles;
    }
    
    /**
     * Gets the sound effects.
     * 
     * @return The sound effects list
     */
    public List<AbilitySound> getSounds() {
        return sounds;
    }
    
    /**
     * Gets the animation.
     * 
     * @return The animation, or null if none
     */
    public AbilityAnimation getAnimation() {
        return animation;
    }
    
    /**
     * Checks if a player can activate this ability.
     * Override this method for custom validation logic.
     * 
     * @param player The player
     * @return true if can activate, false otherwise
     */
    public boolean canActivate(ServerPlayerEntity player) {
        // Default: check level requirement
        com.grandline.data.PlayerData data = 
            com.grandline.data.PlayerDataManager.getInstance().getPlayerData(player);
        
        return data.getLevel() >= requiredLevel;
    }
    
    /**
     * Activates this ability for a player.
     * Override this method to implement ability logic.
     * 
     * IMPORTANT: Call super.activate() to handle effects.
     * 
     * @param player The player
     */
    public abstract void activate(ServerPlayerEntity player);
    
    /**
     * Called when the cooldown for this ability completes.
     * Override to add custom behavior.
     * 
     * @param player The player
     */
    public void onCooldownComplete(ServerPlayerEntity player) {
        // Default: no action
    }
    
    /**
     * Adds a particle effect to this ability.
     * 
     * @param effect The particle effect
     */
    protected void addParticle(ParticleEffect effect) {
        particles.add(effect);
    }
    
    /**
     * Adds a sound effect to this ability.
     * 
     * @param sound The sound effect
     */
    protected void addSound(AbilitySound sound) {
        sounds.add(sound);
    }
    
    /**
     * Sets the animation for this ability.
     * 
     * @param animation The animation
     */
    protected void setAnimation(AbilityAnimation animation) {
        this.animation = animation;
    }
    
    @Override
    public String toString() {
        return "Ability{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cooldown=" + cooldownTicks +
                ", cost=" + energyCost +
                ", level=" + requiredLevel +
                '}';
    }
}
