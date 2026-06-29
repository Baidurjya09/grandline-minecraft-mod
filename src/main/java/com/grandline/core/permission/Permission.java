package com.grandline.core.permission;

/**
 * Defines permission nodes for the Grand Line mod.
 * Permission nodes follow the pattern: grandline.category.action
 */
public class Permission {
    
    // Command permissions
    public static final String COMMAND_HELP = "grandline.command.help";
    public static final String COMMAND_RELOAD = "grandline.command.reload";
    public static final String COMMAND_DEBUG = "grandline.command.debug";
    public static final String COMMAND_VERSION = "grandline.command.version";
    
    // Admin permissions
    public static final String ADMIN = "grandline.admin";
    public static final String ADMIN_ALL = "grandline.admin.*";
    
    // Player data commands (Phase 2)
    public static final String COMMAND_STATS = "grandline.command.stats";
    public static final String COMMAND_XP = "grandline.command.xp";
    public static final String COMMAND_LEVEL = "grandline.command.level";
    public static final String COMMAND_PLAYERDATA = "grandline.command.playerdata";
    
    private Permission() {
        throw new AssertionError("Utility class should not be instantiated");
    }
}
