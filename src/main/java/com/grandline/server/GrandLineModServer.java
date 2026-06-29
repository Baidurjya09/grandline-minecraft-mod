package com.grandline.server;

import com.grandline.GrandLineMod;
import com.grandline.core.network.NetworkManager;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

/**
 * Dedicated server entry point for the Grand Line mod.
 * This class handles server-specific initialization for dedicated servers.
 * 
 * CRITICAL: This class only runs on dedicated servers, NOT on integrated servers.
 * For logic that runs on both integrated and dedicated servers, use server-side logic in GrandLineMod.
 */
@Environment(EnvType.SERVER)
public class GrandLineModServer implements DedicatedServerModInitializer {
    
    private static boolean initialized = false;
    private static MinecraftServer server;
    
    @Override
    public void onInitializeServer() {
        if (initialized) {
            GrandLineMod.LOGGER.warn("Attempted to initialize dedicated server multiple times!");
            return;
        }
        
        long startTime = System.currentTimeMillis();
        GrandLineMod.LOGGER.info("Initializing dedicated server systems...");
        
        // Register server lifecycle events
        ServerLifecycleEvents.SERVER_STARTING.register(srv -> {
            server = srv;
            GrandLineMod.LOGGER.info("Server starting, initializing player data system...");
        });
        
        ServerLifecycleEvents.SERVER_STARTED.register(srv -> {
            // Initialize player data when server is fully started
            if (server != null) {
                com.grandline.data.PlayerDataManager.initialize(server);
            }
        });
        
        ServerLifecycleEvents.SERVER_STOPPING.register(srv -> {
            GrandLineMod.LOGGER.info("Server stopping, saving all player data...");
            try {
                com.grandline.data.PlayerDataManager.getInstance().saveAll();
            } catch (Exception e) {
                GrandLineMod.LOGGER.error("Error saving player data on shutdown", e);
            }
        });
        
        try {
            // Phase 1: Register server packet handlers
            NetworkManager networkManager = GrandLineMod.getNetworkManager();
            networkManager.registerServerPacketHandlers();
            
            // Phase 2: Server-specific setup
            // - Server commands
            // - Server-only tick handlers
            // - Performance monitoring
            // - Admin utilities
            
            // Phase 3: Fire server init event
            com.grandline.core.event.EventBus.post(
                new com.grandline.core.event.events.ModInitEvent(
                    com.grandline.core.event.events.ModInitEvent.InitPhase.SERVER));
            
            initialized = true;
            com.grandline.core.event.EventBus.post(
                new com.grandline.core.event.events.ModInitEvent(
                    com.grandline.core.event.events.ModInitEvent.InitPhase.SERVER));
            
            initialized = true;
            
            long duration = System.currentTimeMillis() - startTime;
            GrandLineMod.LOGGER.info("Dedicated server initialization completed in {}ms", duration);
            
        } catch (Exception e) {
            GrandLineMod.LOGGER.error("Failed to initialize dedicated server", e);
            throw new RuntimeException("Critical server initialization failure", e);
        }
    }
    
    /**
     * Checks if dedicated server has been fully initialized.
     * 
     * @return true if initialized, false otherwise
     */
    public static boolean isInitialized() {
        return initialized;
    }
}
