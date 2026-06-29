package com.grandline.core.permission;

import com.grandline.GrandLineMod;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.command.ServerCommandSource;

/**
 * Manages permission checking for commands and features.
 * Integrates with Fabric Permissions API when available.
 */
public class PermissionManager {
    
    private static final int DEFAULT_PERMISSION_LEVEL = 2; // OP level 2 for admin commands
    
    /**
     * Checks if a command source has the specified permission.
     * 
     * @param source The command source
     * @param permission The permission node
     * @return true if permission is granted, false otherwise
     */
    public static boolean hasPermission(ServerCommandSource source, String permission) {
        if (permission == null || permission.isEmpty()) {
            return true; // No permission required
        }
        
        try {
            // Try using Fabric Permissions API
            return Permissions.check(source, permission, getDefaultLevel(permission));
        } catch (NoClassDefFoundError e) {
            // Fabric Permissions API not available, fall back to OP level
            GrandLineMod.LOGGER.debug("Fabric Permissions API not available, using OP level");
            return source.hasPermissionLevel(getDefaultLevel(permission));
        }
    }
    
    /**
     * Gets the default permission level for a permission node.
     * 
     * @param permission The permission node
     * @return The default OP level required
     */
    private static int getDefaultLevel(String permission) {
        // Command permissions
        if (permission.equals(Permission.COMMAND_HELP) || 
            permission.equals(Permission.COMMAND_VERSION)) {
            return 0; // Everyone
        }
        
        if (permission.equals(Permission.COMMAND_RELOAD) || 
            permission.equals(Permission.COMMAND_DEBUG) ||
            permission.startsWith("grandline.admin")) {
            return DEFAULT_PERMISSION_LEVEL; // Ops only
        }
        
        // Default to level 0 (everyone) for unknown permissions
        return 0;
    }
    
    /**
     * Checks if a source has admin permission.
     * 
     * @param source The command source
     * @return true if admin, false otherwise
     */
    public static boolean isAdmin(ServerCommandSource source) {
        return hasPermission(source, Permission.ADMIN);
    }
}
