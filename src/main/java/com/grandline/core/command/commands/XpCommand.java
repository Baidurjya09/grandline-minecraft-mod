package com.grandline.core.command.commands;

import com.grandline.core.command.Command;
import com.grandline.core.command.CommandContext;
import com.grandline.core.command.CommandResult;
import com.grandline.core.permission.Permission;
import com.grandline.data.PlayerData;
import com.grandline.data.PlayerDataManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;

/**
 * Manages player experience.
 */
public class XpCommand implements Command {
    
    @Override
    public String getName() {
        return "xp";
    }
    
    @Override
    public String getPermission() {
        return Permission.COMMAND_XP;
    }
    
    @Override
    public String getUsage() {
        return "/grandline xp <add|set|get> <amount> [player]";
    }
    
    @Override
    public String getDescription() {
        return "Manages player experience";
    }
    
    @Override
    public CommandResult execute(ServerCommandSource source, CommandContext context) {
        if (context.getArgCount() < 1) {
            return CommandResult.failure("Usage: " + getUsage());
        }
        
        String action = context.getArg(0).toLowerCase();
        ServerPlayerEntity player = source.getPlayer();
        
        if (player == null) {
            return CommandResult.failure("This command must be run by a player");
        }
        
        PlayerData data = PlayerDataManager.getInstance().getPlayerData(player);
        
        return switch (action) {
            case "get" -> {
                yield CommandResult.success(
                    net.minecraft.text.Text.literal("Current XP: " + data.getExperience())
                        .formatted(Formatting.YELLOW));
            }
            case "add" -> {
                if (context.getArgCount() < 2) {
                    yield CommandResult.failure("Usage: /grandline xp add <amount>");
                }
                int amount = context.getArgAsInt(1, 0);
                int levelsGained = PlayerDataManager.getInstance()
                    .getExperienceManager()
                    .awardExperience(player, data, amount);
                
                if (levelsGained > 0) {
                    yield CommandResult.success(
                        net.minecraft.text.Text.literal("Added " + amount + " XP and gained " + 
                            levelsGained + " level(s)!")
                            .formatted(Formatting.GREEN));
                } else {
                    yield CommandResult.success(
                        net.minecraft.text.Text.literal("Added " + amount + " XP")
                            .formatted(Formatting.YELLOW));
                }
            }
            case "set" -> {
                if (context.getArgCount() < 2) {
                    yield CommandResult.failure("Usage: /grandline xp set <amount>");
                }
                int amount = context.getArgAsInt(1, 0);
                data.setExperience(amount);
                yield CommandResult.success(
                    net.minecraft.text.Text.literal("Set XP to " + amount)
                        .formatted(Formatting.GREEN));
            }
            default -> CommandResult.failure("Unknown action: " + action + 
                ". Use: get, add, or set");
        };
    }
}
