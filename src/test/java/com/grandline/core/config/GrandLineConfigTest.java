package com.grandline.core.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GrandLineConfig.
 */
class GrandLineConfigTest {
    
    @Test
    void testDefaultConfigIsValid() {
        GrandLineConfig config = new GrandLineConfig();
        assertTrue(config.validate());
    }
    
    @Test
    void testGeneralSettings() {
        GrandLineConfig config = new GrandLineConfig();
        assertTrue(config.general.enableMod);
        assertEquals("en_us", config.general.locale);
        assertTrue(config.general.validate());
    }
    
    @Test
    void testInvalidLocale() {
        GrandLineConfig.GeneralSettings general = new GrandLineConfig.GeneralSettings();
        general.locale = null;
        assertFalse(general.validate());
        
        general.locale = "";
        assertFalse(general.validate());
    }
    
    @Test
    void testNetworkSettings() {
        GrandLineConfig config = new GrandLineConfig();
        assertEquals(256, config.network.packetCompressionThreshold);
        assertEquals(32767, config.network.maxPacketSize);
        assertFalse(config.network.enablePacketLogging);
        assertTrue(config.network.validate());
    }
    
    @Test
    void testInvalidNetworkSettings() {
        GrandLineConfig.NetworkSettings network = new GrandLineConfig.NetworkSettings();
        
        network.packetCompressionThreshold = -1;
        assertFalse(network.validate());
        
        network.packetCompressionThreshold = 256;
        network.maxPacketSize = 0;
        assertFalse(network.validate());
        
        network.maxPacketSize = 40000;
        assertFalse(network.validate());
    }
    
    @Test
    void testPerformanceSettings() {
        GrandLineConfig config = new GrandLineConfig();
        assertEquals(20, config.performance.tickRateHz);
        assertTrue(config.performance.enableAsyncOperations);
        assertEquals(1000, config.performance.maxCachedEntries);
        assertTrue(config.performance.validate());
    }
    
    @Test
    void testInvalidPerformanceSettings() {
        GrandLineConfig.PerformanceSettings perf = new GrandLineConfig.PerformanceSettings();
        
        perf.tickRateHz = 0;
        assertFalse(perf.validate());
        
        perf.tickRateHz = 200;
        assertFalse(perf.validate());
        
        perf.tickRateHz = 20;
        perf.maxCachedEntries = -1;
        assertFalse(perf.validate());
    }
    
    @Test
    void testDebugSettings() {
        GrandLineConfig config = new GrandLineConfig();
        assertFalse(config.debug.enableDebugLogging);
        assertFalse(config.debug.enablePerformanceMetrics);
        assertTrue(config.debug.enableStackTraces);
        assertTrue(config.debug.validate());
    }
    
    @Test
    void testApplyDefaults() {
        GrandLineConfig config = new GrandLineConfig();
        config.general.locale = null;
        
        assertFalse(config.validate());
        config.applyDefaults();
        assertTrue(config.validate());
    }
}
