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
    
    /**
     * Validates the configuration values.
     * 
     * @return true if all values are valid, false otherwise
     */
    public boolean validate() {
        return general.validate() 
            && network.validate() 
            && performance.validate() 
            && debug.validate();
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
