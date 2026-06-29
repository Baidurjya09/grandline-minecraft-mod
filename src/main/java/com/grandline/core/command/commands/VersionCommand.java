package com.grandline.core.command.commands;

import com.grandline.GrandLineMod;
import com.grandline.core.command.Command;
import com.grandline.core.command.CommandContext;
import com.grandline.core.command.CommandResult;
import com.grandline.core.permission.Permission;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Shows mod version information.
 */
public class VersionCommand implements Command {
    
    @Override
    public String getName() {
        return "version";
    }
    
    @Override
    public String getPermission() {
        return Permission.COMMAND_VERSION;
    }
    
    @Override
    public String getUsage() {
        return "/grandline version";
    }
    
    @Override
    public String getDescription() {
        return "Shows mod version information";
    }
    
    @Override
    public String[] getAliases() {
        return new String[]{"ver", "about"};
    }
    
    @Override
    public CommandResult execute(ServerCommandSource source, CommandContext context) {
        ModContainer modContainer = FabricLoader.getInstance()
            .getModContainer(GrandLineMod.MOD_ID)
            .orElse(null);
        
        if (modContainer == null) {
            return CommandResult.failure("Could not retrieve mod information");
        }
        
        String version = modContainer.getMetadata().getVersion().getFriendlyString();
        String mcVersion = FabricLoader.getInstance()
            .getModContainer("minecraft")
            .map(mc -> mc.getMetadata().getVersion().getFriendlyString())
            .orElse("Unknown");
        
        Text message = Text.literal("=== Project Grand Line ===").formatted(Formatting.GOLD, Formatting.BOLD)
            .append(Text.literal("\nVersion: ").formatted(Formatting.GRAY))
            .append(Text.literal(version).formatted(Formatting.YELLOW))
            .append(Text.literal("\nMinecraft: ").formatted(Formatting.GRAY))
            .append(Text.literal(mcVersion).formatted(Formatting.YELLOW))
            .append(Text.literal("\nPhase: ").formatted(Formatting.GRAY))
            .append(Text.literal("1 - Core Framework").formatted(Formatting.AQUA));
        
        return CommandResult.success(message);
    }
}
