package com.grandline.client;

import com.grandline.GrandLineMod;
import com.grandline.core.network.NetworkManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * Client-side entry point for the Grand Line mod.
 * This class handles client-specific initialization such as rendering, keybinds, and client network handlers.
 * 
 * CRITICAL: This class only runs on the physical client.
 * Do NOT put shared logic here - use GrandLineMod for shared initialization.
 */
@Environment(EnvType.CLIENT)
public class GrandLineModClient implements ClientModInitializer {
    
    private static boolean initialized = false;
    
    @Override
    public void onInitializeClient() {
        if (initialized) {
            GrandLineMod.LOGGER.warn("Attempted to initialize client multiple times!");
            return;
        }
        
        long startTime = System.currentTimeMillis();
        GrandLineMod.LOGGER.info("Initializing client-side systems...");
        
        try {
            // Phase 1: Register client packet handlers
            NetworkManager networkManager = GrandLineMod.getNetworkManager();
            networkManager.registerClientPacketHandlers();
            
            // Phase 2: Client-specific registrations will go here in future phases
            // - Keybindings
            // - Rendering handlers
            // - HUD overlays
            // - Particle renderers
            // - Model providers
            
            // Phase 3: Fire client init event
            com.grandline.core.event.EventBus.post(
                new com.grandline.core.event.events.ModInitEvent(
                    com.grandline.core.event.events.ModInitEvent.InitPhase.CLIENT));
            
            initialized = true;
            
            long duration = System.currentTimeMillis() - startTime;
            GrandLineMod.LOGGER.info("Client initialization completed in {}ms", duration);
            
        } catch (Exception e) {
            GrandLineMod.LOGGER.error("Failed to initialize client", e);
            throw new RuntimeException("Critical client initialization failure", e);
        }
    }
    
    /**
     * Checks if client has been fully initialized.
     * 
     * @return true if initialized, false otherwise
     */
    public static boolean isInitialized() {
        return initialized;
    }
}
