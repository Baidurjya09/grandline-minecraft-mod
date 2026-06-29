package com.grandline.ability.particle;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;

/**
 * Represents a particle effect for abilities.
 * Defines what particles to spawn and how.
 */
public class AbilityParticleEffect {
    
    private final ParticleEffect particleType;
    private final ParticlePattern pattern;
    private final int count;
    private final Vec3d offset;
    private final double speed;
    
    public AbilityParticleEffect(ParticleEffect particleType, ParticlePattern pattern,
                                int count, Vec3d offset, double speed) {
        this.particleType = particleType;
        this.pattern = pattern;
        this.count = count;
        this.offset = offset;
        this.speed = speed;
    }
    
    public ParticleEffect getParticleType() {
        return particleType;
    }
    
    public ParticlePattern getPattern() {
        return pattern;
    }
    
    public int getCount() {
        return count;
    }
    
    public Vec3d getOffset() {
        return offset;
    }
    
    public double getSpeed() {
        return speed;
    }
    
    /**
     * Creates a simple burst particle effect.
     */
    public static AbilityParticleEffect burst() {
        return new AbilityParticleEffect(
            ParticleTypes.FLAME,
            ParticlePattern.BURST,
            20,
            new Vec3d(0.5, 0.5, 0.5),
            0.1
        );
    }
}
