package com.grandline.core.command;

import com.grandline.GrandLineMod;
import com.grandline.core.command.commands.*;
import com.grandline.core.permission.PermissionManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.*;

/**
 * Manages registration and execution of Grand Line commands.
 * Integrates with Minecraft's Brigadier command system.
 */
public class GrandLineCommandManager {
    
    private static final Map<String, Command> commands = new HashMap<>();
    private static final Map<String, Command> aliases = new HashMap<>();
    private static boolean initialized = false;
    
    /**
     * Initializes the command system and registers all commands.
     * 
     * @param dispatcher The command dispatcher
     */
    public static void initialize(CommandDispatcher<ServerCommandSource> dispatcher) {
        if (initialized) {
            GrandLineMod.LOGGER.warn("Command manager already initialized!");
            return;
        }
        
        GrandLineMod.LOGGER.info("Initializing command system...");
        
        // Register all built-in commands
        registerCommand(new HelpCommand());
        registerCommand(new ReloadCommand());
        registerCommand(new DebugCommand());
        registerCommand(new VersionCommand());
        
        // Phase 2: Player data commands
        registerCommand(new StatsCommand());
        registerCommand(new XpCommand());
        
        // Register with Brigadier
        registerBrigadierCommands(dispatcher);
        
        initialized = true;
        GrandLineMod.LOGGER.info("Registered {} commands", commands.size());
    }
    
    /**
     * Registers a command.
     * 
     * @param command The command to register
     * @throws IllegalArgumentException if command name is already registered
     */
    public static void registerCommand(Command command) {
        String name = command.getName().toLowerCase();
        
        if (commands.containsKey(name)) {
            throw new IllegalArgumentException("Command already registered: " + name);
        }
        
        commands.put(name, command);
        
        // Register aliases
        for (String alias : command.getAliases()) {
            String aliasLower = alias.toLowerCase();
            if (aliases.containsKey(aliasLower)) {
                GrandLineMod.LOGGER.warn("Alias {} already registered, skipping", aliasLower);
                continue;
            }
            aliases.put(aliasLower, command);
        }
        
        GrandLineMod.LOGGER.debug("Registered command: {}", name);
    }
    
    /**
     * Gets a command by name or alias.
     * 
     * @param name The command name or alias
     * @return The command, or null if not found
     */
    public static Command getCommand(String name) {
        String nameLower = name.toLowerCase();
        Command cmd = commands.get(nameLower);
        return cmd != null ? cmd : aliases.get(nameLower);
    }
    
    /**
     * Gets all registered commands.
     * 
     * @return Unmodifiable collection of commands
     */
    public static Collection<Command> getAllCommands() {
        return Collections.unmodifiableCollection(commands.values());
    }
    
    /**
     * Registers all commands with Minecraft's Brigadier system.
     */
    private static void registerBrigadierCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        // Main command: /grandline <subcommand> [args...]
        dispatcher.register(
            CommandManager.literal("grandline")
                .then(CommandManager.argument("subcommand", StringArgumentType.greedyString())
                    .executes(GrandLineCommandManager::executeBrigadierCommand))
                .executes(ctx -> {
                    // No subcommand provided, show help
                    Command helpCmd = getCommand("help");
                    if (helpCmd != null) {
                        com.grandline.core.command.CommandContext cmdCtx = 
                            new com.grandline.core.command.CommandContext("help", 
                                Collections.emptyList(), "");
                        CommandResult result = helpCmd.execute(ctx.getSource(), cmdCtx);
                        if (result.hasMessage()) {
                            ctx.getSource().sendFeedback(() -> result.getMessage(), false);
                        }
                        return result.isSuccess() ? 1 : 0;
                    }
                    return 1;
                })
        );
        
        // Alias: /gl <subcommand> [args...]
        dispatcher.register(
            CommandManager.literal("gl")
                .redirect(dispatcher.register(CommandManager.literal("grandline")))
        );
    }
    
    /**
     * Executes a command through Brigadier.
     */
    private static int executeBrigadierCommand(CommandContext<ServerCommandSource> ctx) {
        String input = StringArgumentType.getString(ctx, "subcommand");
        String[] parts = input.split("\\s+", 2);
        String commandName = parts[0];
        String argsString = parts.length > 1 ? parts[1] : "";
        
        Command command = getCommand(commandName);
        if (command == null) {
            ctx.getSource().sendError(net.minecraft.text.Text.literal(
                "Unknown command: " + commandName + ". Use /grandline help for a list of commands."));
            return 0;
        }
        
        // Check permission
        if (command.getPermission() != null && 
            !PermissionManager.hasPermission(ctx.getSource(), command.getPermission())) {
            ctx.getSource().sendError(net.minecraft.text.Text.literal(
                "You don't have permission to use this command."));
            return 0;
        }
        
        // Parse arguments
        List<String> args = argsString.isEmpty() ? 
            Collections.emptyList() : 
            Arrays.asList(argsString.split("\\s+"));
        
        com.grandline.core.command.CommandContext cmdCtx = 
            new com.grandline.core.command.CommandContext(commandName, args, input);
        
        // Execute command
        try {
            CommandResult result = command.execute(ctx.getSource(), cmdCtx);
            
            if (result.hasMessage()) {
                if (result.isSuccess()) {
                    ctx.getSource().sendFeedback(() -> result.getMessage(), false);
                } else {
                    ctx.getSource().sendError(result.getMessage());
                }
            }
            
            return result.isSuccess() ? 1 : 0;
            
        } catch (Exception e) {
            GrandLineMod.LOGGER.error("Error executing command: {}", commandName, e);
            ctx.getSource().sendError(net.minecraft.text.Text.literal(
                "An error occurred while executing the command."));
            return 0;
        }
    }
}
