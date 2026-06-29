package com.grandline.core.network;

import com.grandline.GrandLineMod;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

/**
 * Manages network packet registration and handling for the mod.
 * This class is responsible for:
 * - Registering custom packet types
 * - Setting up packet handlers for client and server
 * - Providing utilities for sending packets
 * 
 * All network communication must go through this manager to ensure
 * proper synchronization and compatibility with dedicated servers.
 */
public class NetworkManager {
    
    private boolean packetsRegistered = false;
    
    /**
     * Registers all custom packet types.
     * This must be called during mod initialization.
     * 
     * @throws IllegalStateException if called multiple times
     */
    public void registerPackets() {
        if (packetsRegistered) {
            throw new IllegalStateException("Packets already registered!");
        }
        
        GrandLineMod.LOGGER.info("Registering network packets...");
        
        try {
            // Phase 0: No packets to register yet
            // Future phases will register packets here using:
            // PayloadTypeRegistry.playC2S().register(...)
            // PayloadTypeRegistry.playS2C().register(...)
            
            packetsRegistered = true;
            GrandLineMod.LOGGER.info("Network packets registered successfully");
            
        } catch (Exception e) {
            GrandLineMod.LOGGER.error("Failed to register network packets", e);
            throw new RuntimeException("Packet registration failed", e);
        }
    }
    
    /**
     * Registers server-side packet handlers.
     * Called during dedicated server initialization.
     */
    public void registerServerPacketHandlers() {
        GrandLineMod.LOGGER.debug("Registering server packet handlers...");
        
        // Future phases will register handlers here using:
        // ServerPlayNetworking.registerGlobalReceiver(...)
    }
    
    /**
     * Registers client-side packet handlers.
     * Called during client initialization.
     */
    public void registerClientPacketHandlers() {
        GrandLineMod.LOGGER.debug("Registering client packet handlers...");
        
        // Future phases will register handlers here using:
        // ClientPlayNetworking.registerGlobalReceiver(...)
    }
}
