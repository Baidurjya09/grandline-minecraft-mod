package com.grandline.ability.sound;

import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

/**
 * Represents a sound effect for abilities.
 */
public class AbilitySound {
    
    private final SoundEvent soundEvent;
    private final SoundType type;
    private final float volume;
    private final float pitch;
    
    public AbilitySound(SoundEvent soundEvent, SoundType type, float volume, float pitch) {
        this.soundEvent = soundEvent;
        this.type = type;
        this.volume = volume;
        this.pitch = pitch;
    }
    
    public SoundEvent getSoundEvent() {
        return soundEvent;
    }
    
    public SoundType getType() {
        return type;
    }
    
    public float getVolume() {
        return volume;
    }
    
    public float getPitch() {
        return pitch;
    }
    
    /**
     * Creates a simple activation sound.
     */
    public static AbilitySound activation() {
        return new AbilitySound(
            SoundEvents.ENTITY_ENDER_DRAGON_FLAP,
            SoundType.ACTIVATION,
            1.0f,
            1.0f
        );
    }
}
