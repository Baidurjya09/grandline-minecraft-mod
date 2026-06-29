package com.grandline.core.command.commands;

import com.grandline.core.command.Command;
import com.grandline.core.command.CommandContext;
import com.grandline.core.command.CommandResult;
import com.grandline.core.command.GrandLineCommandManager;
import com.grandline.core.permission.Permission;
import com.grandline.core.permission.PermissionManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Shows help information about available commands.
 */
public class HelpCommand implements Command {
    
    @Override
    public String getName() {
        return "help";
    }
    
    @Override
    public String getPermission() {
        return Permission.COMMAND_HELP;
    }
    
    @Override
    public String getUsage() {
        return "/grandline help [command]";
    }
    
    @Override
    public String getDescription() {
        return "Shows help information about commands";
    }
    
    @Override
    public String[] getAliases() {
        return new String[]{"?", "commands"};
    }
    
    @Override
    public CommandResult execute(ServerCommandSource source, CommandContext context) {
        if (context.getArgCount() > 0) {
            // Show help for specific command
            String cmdName = context.getArg(0);
            Command cmd = GrandLineCommandManager.getCommand(cmdName);
            
            if (cmd == null) {
                return CommandResult.failure("Unknown command: " + cmdName);
            }
            
            Text message = Text.literal("Command: ").formatted(Formatting.GOLD)
                .append(Text.literal(cmd.getName()).formatted(Formatting.YELLOW))
                .append(Text.literal("\nUsage: ").formatted(Formatting.GOLD))
                .append(Text.literal(cmd.getUsage()).formatted(Formatting.WHITE))
                .append(Text.literal("\nDescription: ").formatted(Formatting.GOLD))
                .append(Text.literal(cmd.getDescription()).formatted(Formatting.WHITE));
            
            if (cmd.getAliases().length > 0) {
                message = message.append(Text.literal("\nAliases: ").formatted(Formatting.GOLD))
                    .append(Text.literal(String.join(", ", cmd.getAliases())).formatted(Formatting.WHITE));
            }
            
            return CommandResult.success(message);
        }
        
        // Show list of all commands
        Text message = Text.literal("=== Grand Line Commands ===").formatted(Formatting.GOLD, Formatting.BOLD)
            .append(Text.literal("\n"));
        
        for (Command cmd : GrandLineCommandManager.getAllCommands()) {
            // Skip commands user doesn't have permission for
            if (cmd.getPermission() != null && 
                !PermissionManager.hasPermission(source, cmd.getPermission())) {
                continue;
            }
            
            message = message.append(Text.literal("\n/" + cmd.getName()).formatted(Formatting.YELLOW))
                .append(Text.literal(" - ").formatted(Formatting.GRAY))
                .append(Text.literal(cmd.getDescription()).formatted(Formatting.WHITE));
        }
        
        message = message.append(Text.literal("\n\nUse /grandline help <command> for more info")
            .formatted(Formatting.GRAY, Formatting.ITALIC));
        
        return CommandResult.success(message);
    }
}
