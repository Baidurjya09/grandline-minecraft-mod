package com.grandline.ability.animation;

/**
 * Represents an animation for an ability.
 * Phase 3: Basic structure, full implementation in future phases.
 */
public class AbilityAnimation {
    
    private final String animationId;
    private final int durationTicks;
    
    public AbilityAnimation(String animationId, int durationTicks) {
        this.animationId = animationId;
        this.durationTicks = durationTicks;
    }
    
    public String getAnimationId() {
        return animationId;
    }
    
    public int getDurationTicks() {
        return durationTicks;
    }
}
