package com.grandline.core.command;

import net.minecraft.server.command.ServerCommandSource;

/**
 * Interface for all Grand Line commands.
 * Commands provide a way to interact with the mod through chat.
 * 
 * Implementation Guidelines:
 * - Keep execute() methods fast and non-blocking
 * - Validate all inputs
 * - Check permissions before execution
 * - Provide helpful error messages
 * - Support tab completion when possible
 */
public interface Command {
    
    /**
     * Gets the name of this command.
     * This is the primary way players invoke the command.
     * 
     * @return The command name (lowercase, no spaces)
     */
    String getName();
    
    /**
     * Gets the permission node required to use this command.
     * 
     * @return The permission string, or null if no permission required
     */
    String getPermission();
    
    /**
     * Gets the usage string for this command.
     * Should show the syntax with required and optional parameters.
     * 
     * @return The usage string
     */
    String getUsage();
    
    /**
     * Gets a brief description of what this command does.
     * 
     * @return The command description
     */
    String getDescription();
    
    /**
     * Gets aliases for this command (alternate names).
     * 
     * @return Array of aliases, or empty array if none
     */
    default String[] getAliases() {
        return new String[0];
    }
    
    /**
     * Executes this command.
     * 
     * @param source The command source (player, console, etc.)
     * @param context The command execution context
     * @return The execution result
     */
    CommandResult execute(ServerCommandSource source, CommandContext context);
}
