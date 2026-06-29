package com.grandline.core.command.commands;

import com.grandline.GrandLineMod;
import com.grandline.core.command.Command;
import com.grandline.core.command.CommandContext;
import com.grandline.core.command.CommandResult;
import com.grandline.core.permission.Permission;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

/**
 * Reloads the mod configuration.
 */
public class ReloadCommand implements Command {
    
    @Override
    public String getName() {
        return "reload";
    }
    
    @Override
    public String getPermission() {
        return Permission.COMMAND_RELOAD;
    }
    
    @Override
    public String getUsage() {
        return "/grandline reload";
    }
    
    @Override
    public String getDescription() {
        return "Reloads the mod configuration";
    }
    
    @Override
    public CommandResult execute(ServerCommandSource source, CommandContext context) {
        try {
            long startTime = System.currentTimeMillis();
            
            // Reload configuration
            GrandLineMod.getConfigManager().reload();
            
            long duration = System.currentTimeMillis() - startTime;
            
            return CommandResult.success(
                net.minecraft.text.Text.literal("Configuration reloaded in " + duration + "ms")
                    .formatted(Formatting.GREEN));
                    
        } catch (Exception e) {
            GrandLineMod.LOGGER.error("Failed to reload configuration", e);
            return CommandResult.failure("Failed to reload configuration: " + e.getMessage());
        }
    }
}
