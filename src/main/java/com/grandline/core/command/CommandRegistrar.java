package com.grandline.core.command;

import com.grandline.GrandLineMod;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

/**
 * Handles registration of commands with Fabric's command system.
 */
public class CommandRegistrar {
    
    /**
     * Registers all commands with the Fabric command system.
     * Should be called during mod initialization.
     */
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            GrandLineMod.LOGGER.debug("Registering commands with Fabric...");
            GrandLineCommandManager.initialize(dispatcher);
        });
    }
}
