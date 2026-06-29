package com.grandline;

import com.grandline.core.GrandLineRegistry;
import com.grandline.core.config.ConfigManager;
import com.grandline.core.network.NetworkManager;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entry point for the Grand Line mod.
 * This class is responsible for initializing all core systems in the correct order.
 * 
 * Initialization order:
 * 1. Logger setup
 * 2. Configuration loading
 * 3. Registry initialization
 * 4. Network setup
 * 5. Feature registration
 */
public class GrandLineMod implements ModInitializer {
    
    public static final String MOD_ID = "grandline";
    public static final String MOD_NAME = "Project Grand Line";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
    
    private static GrandLineMod instance;
    private static ConfigManager configManager;
    private static NetworkManager networkManager;
    
    private boolean initialized = false;
    
    @Override
    public void onInitialize() {
        if (initialized) {
            LOGGER.warn("Attempted to initialize mod multiple times!");
            return;
        }
        
        long startTime = System.currentTimeMillis();
        LOGGER.info("Initializing {} ...", MOD_NAME);
        
        try {
            instance = this;
            
            // Phase 1: Configuration
            LOGGER.info("Loading configuration...");
            configManager = new ConfigManager();
            configManager.load();
            
            // Phase 2: Registry
            LOGGER.info("Initializing registry system...");
            GrandLineRegistry.initialize();
            
            // Phase 3: Networking
            LOGGER.info("Setting up network handlers...");
            networkManager = new NetworkManager();
            networkManager.registerPackets();
            
            // Phase 4: Event System (Phase 1)
            LOGGER.info("Initializing event system...");
            com.grandline.core.event.EventBus.register(this);
            
            // Phase 5: Command System (Phase 1)
            LOGGER.info("Registering command system...");
            com.grandline.core.command.CommandRegistrar.register();
            
            // Phase 6: Fire initialization event
            com.grandline.core.event.EventBus.post(
                new com.grandline.core.event.events.ModInitEvent(
                    com.grandline.core.event.events.ModInitEvent.InitPhase.COMMON));
            
            // Phase 7: Mark as initialized
            initialized = true;
            
            long duration = System.currentTimeMillis() - startTime;
            LOGGER.info("{} initialized successfully in {}ms", MOD_NAME, duration);
            LOGGER.info("✓ Phase 0: Foundation Complete");
            LOGGER.info("✓ Phase 1: Core Framework Active");
            
        } catch (Exception e) {
            LOGGER.error("Failed to initialize {}", MOD_NAME, e);
            throw new RuntimeException("Critical initialization failure", e);
        }
    }
    
    /**
     * Gets the singleton instance of the mod.
     * 
     * @return The mod instance
     * @throws IllegalStateException if called before initialization
     */
    public static GrandLineMod getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Mod not yet initialized!");
        }
        return instance;
    }
    
    /**
     * Gets the configuration manager.
     * 
     * @return The configuration manager
     * @throws IllegalStateException if called before initialization
     */
    public static ConfigManager getConfigManager() {
        if (configManager == null) {
            throw new IllegalStateException("Config manager not yet initialized!");
        }
        return configManager;
    }
    
    /**
     * Gets the network manager.
     * 
     * @return The network manager
     * @throws IllegalStateException if called before initialization
     */
    public static NetworkManager getNetworkManager() {
        if (networkManager == null) {
            throw new IllegalStateException("Network manager not yet initialized!");
        }
        return networkManager;
    }
    
    /**
     * Checks if the mod has been fully initialized.
     * 
     * @return true if initialized, false otherwise
     */
    public boolean isInitialized() {
        return initialized;
    }
}
