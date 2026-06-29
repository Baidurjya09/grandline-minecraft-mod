package com.grandline.core.config;

/**
 * Main configuration class for the Grand Line mod.
 * This class contains all configurable options for the mod.
 * 
 * All fields should have sensible defaults and be validated on load.
 */
public class GrandLineConfig {
    
    // General Settings
    public GeneralSettings general = new GeneralSettings();
    
    // Network Settings
    public NetworkSettings network = new NetworkSettings();
    
    // Performance Settings
    public PerformanceSettings performance = new PerformanceSettings();
    
    // Debug Settings
    public DebugSettings debug = new DebugSettings();
    
    // Player Data Settings (Phase 2)
    public PlayerDataSettings playerData = new PlayerDataSettings();
    
    // Leveling Settings (Phase 2)
    public LevelingSettings leveling = new LevelingSettings();
    
    // Statistics Settings (Phase 2)
    public StatisticsSettings statistics = new StatisticsSettings();
    
    // Ability Settings (Phase 3)
    public AbilitySettings abilities = new AbilitySettings();
    
    /**
     * Validates the configuration values.
     * 
     * @return true if all values are valid, false otherwise
     */
    public boolean validate() {
        return general.validate() 
            && network.validate() 
            && performance.validate() 
            && debug.validate()
            && playerData.validate()
            && leveling.validate()
            && statistics.validate()
            && abilities.validate();
    }
    
    /**
     * Applies default values to any invalid configuration options.
     */
    public void applyDefaults() {
        if (!general.validate()) {
            general = new GeneralSettings();
        }
        if (!network.validate()) {
            network = new NetworkSettings();
        }
        if (!performance.validate()) {
            performance = new PerformanceSettings();
        }
        if (!debug.validate()) {
            debug = new DebugSettings();
        }
        if (!playerData.validate()) {
            playerData = new PlayerDataSettings();
        }
        if (!leveling.validate()) {
            leveling = new LevelingSettings();
        }
        if (!statistics.validate()) {
            statistics = new StatisticsSettings();
        }
        if (!abilities.validate()) {
            abilities = new AbilitySettings();
        }
    }
    
    public static class GeneralSettings {
        public boolean enableMod = true;
        public String locale = "en_us";
        
        public boolean validate() {
            return locale != null && !locale.isEmpty();
        }
    }
    
    public static class NetworkSettings {
        public int packetCompressionThreshold = 256;
        public int maxPacketSize = 32767;
        public boolean enablePacketLogging = false;
        
        public boolean validate() {
            return packetCompressionThreshold >= 0 
                && maxPacketSize > 0 
                && maxPacketSize <= 32767;
        }
    }
    
    public static class PerformanceSettings {
        public int tickRateHz = 20;
        public boolean enableAsyncOperations = true;
        public int maxCachedEntries = 1000;
        
        public boolean validate() {
            return tickRateHz > 0 
                && tickRateHz <= 100 
                && maxCachedEntries >= 0;
        }
    }
    
    public static class DebugSettings {
        public boolean enableDebugLogging = false;
        public boolean enablePerformanceMetrics = false;
        public boolean enableStackTraces = true;
        
        public boolean validate() {
            return true;
        }
    }
}

    
    public static class PlayerDataSettings {
        public int autoSaveIntervalSeconds = 300;
        public boolean backupOnSave = true;
        public int maxBackups = 3;
        public boolean syncOnStatChange = true;
        
        public boolean validate() {
            return autoSaveIntervalSeconds > 0 
                && maxBackups >= 0;
        }
    }
    
    public static class LevelingSettings {
        public String xpCurveType = "exponential";
        public int baseXp = 100;
        public double xpMultiplier = 1.15;
        public int skillPointsPerLevel = 1;
        public int maxLevel = 100;
        
        public boolean validate() {
            return baseXp > 0 
                && xpMultiplier > 0 
                && skillPointsPerLevel >= 0
                && maxLevel > 0 
                && maxLevel <= 1000;
        }
    }
    
    public static class StatisticsSettings {
        public boolean trackPlaytime = true;
        public boolean trackMovement = true;
        public boolean trackCombat = true;
        
        public boolean validate() {
            return true;
        }
    }

    
    public static class AbilitySettings {
        public boolean enableAbilities = true;
        public int globalCooldownTicks = 20;
        public boolean enableClientPrediction = true;
        public int maxActiveAbilities = 10;
        
        public boolean validate() {
            return globalCooldownTicks >= 0 
                && maxActiveAbilities > 0;
        }
    }
