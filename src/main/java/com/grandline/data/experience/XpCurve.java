package com.grandline.data.experience;

import com.grandline.GrandLineMod;

/**
 * Calculates experience requirements for leveling.
 * Supports multiple curve types for different progression rates.
 */
public class XpCurve {
    
    public enum CurveType {
        LINEAR,
        EXPONENTIAL,
        POLYNOMIAL
    }
    
    private final CurveType curveType;
    private final int baseXp;
    private final double multiplier;
    
    public XpCurve(CurveType curveType, int baseXp, double multiplier) {
        this.curveType = curveType;
        this.baseXp = baseXp;
        this.multiplier = multiplier;
    }
    
    /**
     * Gets XP required for a specific level.
     * 
     * @param level The target level
     * @return The XP required to reach that level
     */
    public int getXpForLevel(int level) {
        if (level <= 1) {
            return 0;
        }
        
        return switch (curveType) {
            case LINEAR -> calculateLinear(level);
            case EXPONENTIAL -> calculateExponential(level);
            case POLYNOMIAL -> calculatePolynomial(level);
        };
    }
    
    /**
     * Gets the level for a given XP amount.
     * 
     * @param xp The experience points
     * @return The level
     */
    public int getLevelForXp(int xp) {
        if (xp <= 0) {
            return 1;
        }
        
        // Binary search for level
        int low = 1;
        int high = 100; // Max level from config
        
        while (low < high) {
            int mid = (low + high + 1) / 2;
            if (getXpForLevel(mid) <= xp) {
                low = mid;
            } else {
                high = mid - 1;
            }
        }
        
        return low;
    }
    
    /**
     * Gets XP needed to reach the next level.
     * 
     * @param currentXp Current experience
     * @param currentLevel Current level
     * @return XP needed for next level
     */
    public int getXpToNextLevel(int currentXp, int currentLevel) {
        int nextLevelXp = getXpForLevel(currentLevel + 1);
        return Math.max(0, nextLevelXp - currentXp);
    }
    
    /**
     * Gets XP progress towards next level as a percentage.
     * 
     * @param currentXp Current experience
     * @param currentLevel Current level
     * @return Progress from 0.0 to 1.0
     */
    public float getLevelProgress(int currentXp, int currentLevel) {
        int currentLevelXp = getXpForLevel(currentLevel);
        int nextLevelXp = getXpForLevel(currentLevel + 1);
        int xpInLevel = currentXp - currentLevelXp;
        int xpNeeded = nextLevelXp - currentLevelXp;
        
        if (xpNeeded <= 0) {
            return 1.0f;
        }
        
        return (float) xpInLevel / xpNeeded;
    }
    
    private int calculateLinear(int level) {
        // Linear: xp = baseXp * (level - 1)
        return (int) (baseXp * (level - 1));
    }
    
    private int calculateExponential(int level) {
        // Exponential: xp = baseXp * (multiplier ^ (level - 1))
        return (int) (baseXp * Math.pow(multiplier, level - 1));
    }
    
    private int calculatePolynomial(int level) {
        // Polynomial: xp = baseXp * ((level - 1) ^ multiplier)
        return (int) (baseXp * Math.pow(level - 1, multiplier));
    }
    
    /**
     * Creates an XP curve from config.
     * 
     * @return The XP curve
     */
    public static XpCurve fromConfig() {
        var config = GrandLineMod.getConfigManager().getConfig();
        
        CurveType type;
        try {
            type = CurveType.valueOf(config.leveling.xpCurveType.toUpperCase());
        } catch (IllegalArgumentException e) {
            GrandLineMod.LOGGER.warn("Invalid XP curve type: {}, using EXPONENTIAL", 
                config.leveling.xpCurveType);
            type = CurveType.EXPONENTIAL;
        }
        
        return new XpCurve(type, config.leveling.baseXp, config.leveling.xpMultiplier);
    }
}
