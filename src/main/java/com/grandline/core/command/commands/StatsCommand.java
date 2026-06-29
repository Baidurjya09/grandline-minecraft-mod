package com.grandline.core.command.commands;

import com.grandline.core.command.Command;
import com.grandline.core.command.CommandContext;
import com.grandline.core.command.CommandResult;
import com.grandline.core.permission.Permission;
import com.grandline.data.PlayerData;
import com.grandline.data.PlayerDataManager;
import com.grandline.data.stats.StatType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Shows player statistics.
 */
public class StatsCommand implements Command {
    
    @Override
    public String getName() {
        return "stats";
    }
    
    @Override
    public String getPermission() {
        return Permission.COMMAND_STATS;
    }
    
    @Override
    public String getUsage() {
        return "/grandline stats [player]";
    }
    
    @Override
    public String getDescription() {
        return "Shows player statistics";
    }
    
    @Override
    public CommandResult execute(ServerCommandSource source, CommandContext context) {
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) {
            return CommandResult.failure("This command must be run by a player");
        }
        
        PlayerData data = PlayerDataManager.getInstance().getPlayerData(player);
        
        Text message = Text.literal("=== " + player.getName().getString() + "'s Stats ===")
            .formatted(Formatting.GOLD, Formatting.BOLD)
            .append(Text.literal("\n\nLevel: ").formatted(Formatting.GRAY))
            .append(Text.literal(String.valueOf(data.getLevel())).formatted(Formatting.YELLOW))
            .append(Text.literal(" | XP: ").formatted(Formatting.GRAY))
            .append(Text.literal(String.valueOf(data.getExperience())).formatted(Formatting.YELLOW))
            .append(Text.literal(" | Skill Points: ").formatted(Formatting.GRAY))
            .append(Text.literal(String.valueOf(data.getSkillPoints())).formatted(Formatting.AQUA))
            .append(Text.literal("\n\n=== Statistics ===").formatted(Formatting.GOLD))
            .append(Text.literal("\nDamage Dealt: ").formatted(Formatting.GRAY))
            .append(Text.literal(String.valueOf(data.getStat(StatType.DAMAGE_DEALT))).formatted(Formatting.WHITE))
            .append(Text.literal("\nDamage Taken: ").formatted(Formatting.GRAY))
            .append(Text.literal(String.valueOf(data.getStat(StatType.DAMAGE_TAKEN))).formatted(Formatting.WHITE))
            .append(Text.literal("\nKills: ").formatted(Formatting.GRAY))
            .append(Text.literal(String.valueOf(data.getStat(StatType.KILLS))).formatted(Formatting.WHITE))
            .append(Text.literal("\nDeaths: ").formatted(Formatting.GRAY))
            .append(Text.literal(String.valueOf(data.getStat(StatType.DEATHS))).formatted(Formatting.WHITE))
            .append(Text.literal("\nDistance: ").formatted(Formatting.GRAY))
            .append(Text.literal(String.valueOf(data.getStat(StatType.DISTANCE_TRAVELED) / 100) + "m").formatted(Formatting.WHITE));
        
        return CommandResult.success(message);
    }
}
