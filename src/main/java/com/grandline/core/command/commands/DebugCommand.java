package com.grandline.core.command.commands;

import com.grandline.GrandLineMod;
import com.grandline.core.command.Command;
import com.grandline.core.command.CommandContext;
import com.grandline.core.command.CommandResult;
import com.grandline.core.permission.Permission;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

/**
 * Toggles debug mode.
 */
public class DebugCommand implements Command {
    
    @Override
    public String getName() {
        return "debug";
    }
    
    @Override
    public String getPermission() {
        return Permission.COMMAND_DEBUG;
    }
    
    @Override
    public String getUsage() {
        return "/grandline debug [on|off]";
    }
    
    @Override
    public String getDescription() {
        return "Toggles debug mode";
    }
    
    @Override
    public CommandResult execute(ServerCommandSource source, CommandContext context) {
        boolean enable;
        
        if (context.getArgCount() == 0) {
            // Toggle current state
            enable = !GrandLineMod.getConfigManager().getConfig().debug.enableDebugLogging;
        } else {
            // Set specific state
            String arg = context.getArg(0).toLowerCase();
            enable = arg.equals("on") || arg.equals("true") || arg.equals("1");
        }
        
        GrandLineMod.getConfigManager().getConfig().debug.enableDebugLogging = enable;
        GrandLineMod.getConfigManager().save();
        
        return CommandResult.success(
            net.minecraft.text.Text.literal("Debug mode " + (enable ? "enabled" : "disabled"))
                .formatted(enable ? Formatting.GREEN : Formatting.RED));
    }
}
