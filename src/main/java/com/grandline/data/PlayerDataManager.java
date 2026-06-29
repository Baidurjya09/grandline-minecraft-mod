package com.grandline.data;

import com.grandline.GrandLineMod;
import com.grandline.core.event.EventBus;
import com.grandline.data.events.PlayerDataLoadedEvent;
import com.grandline.data.events.PlayerDataSavedEvent;
import com.grandline.data.experience.ExperienceManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Central manager for all player data.
 * Handles loading, saving, and caching of player data.
 */
public class PlayerDataManager {
    
    private static PlayerDataManager instance;
    
    private final Map<UUID, PlayerData> cachedData;
    private final ExperienceManager experienceManager;
    private final MinecraftServer server;
    private final Path dataDirectory;
    
    private PlayerDataManager(MinecraftServer server) {
        this.server = server;
        this.cachedData = new ConcurrentHashMap<>();
        this.experienceManager = new ExperienceManager();
        this.dataDirectory = server.getSavePath(null).resolve("grandline/playerdata");
        
        try {
            Files.createDirectories(dataDirectory);
        } catch (IOException e) {
            GrandLineMod.LOGGER.error("Failed to create player data directory", e);
        }
    }
    
    /**
     * Initializes the player data manager.
     * 
     * @param server The server instance
     */
    public static void initialize(MinecraftServer server) {
        if (instance != null) {
            GrandLineMod.LOGGER.warn("PlayerDataManager already initialized!");
            return;
        }
        
        instance = new PlayerDataManager(server);
        instance.registerEvents();
        
        GrandLineMod.LOGGER.info("PlayerDataManager initialized");
    }
    
    /**
     * Gets the singleton instance.
     * 
     * @return The instance
     */
    public static PlayerDataManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("PlayerDataManager not initialized!");
        }
        return instance;
    }
    
    /**
     * Registers connection events.
     */
    private void registerEvents() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            loadPlayerData(player);
        });
        
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            savePlayerData(player);
            cachedData.remove(player.getUuid());
        });
    }
    
    /**
     * Gets player data, loading if necessary.
     * 
     * @param player The player
     * @return The player data
     */
    public PlayerData getPlayerData(ServerPlayerEntity player) {
        return cachedData.computeIfAbsent(player.getUuid(), uuid -> {
            PlayerData data = loadPlayerDataFromDisk(player.getUuid(), player.getName().getString());
            EventBus.post(new PlayerDataLoadedEvent(player, data));
            return data;
        });
    }
    
    /**
     * Loads player data from disk.
     */
    private PlayerData loadPlayerDataFromDisk(UUID playerId, String playerName) {
        File file = getPlayerDataFile(playerId);
        
        if (!file.exists()) {
            GrandLineMod.LOGGER.info("Creating new player data for {}", playerName);
            return new PlayerData(playerId, playerName);
        }
        
        try {
            NbtCompound nbt = NbtIo.readCompressed(file.toPath(), NbtIo.UNLIMITED_TRACKER);
            PlayerData data = PlayerData.deserializeNbt(nbt);
            data.setPlayerName(playerName); // Update name if changed
            GrandLineMod.LOGGER.info("Loaded player data for {}", playerName);
            return data;
        } catch (IOException e) {
            GrandLineMod.LOGGER.error("Failed to load player data for {}, using backup", playerName, e);
            return loadBackup(playerId, playerName);
        }
    }
    
    /**
     * Saves player data to disk.
     */
    private void savePlayerData(ServerPlayerEntity player) {
        PlayerData data = cachedData.get(player.getUuid());
        if (data == null || !data.isDirty()) {
            return;
        }
        
        data.updateLastSeen();
        
        File file = getPlayerDataFile(player.getUuid());
        
        // Create backup
        if (file.exists() && GrandLineMod.getConfigManager().getConfig().playerData.backupOnSave) {
            createBackup(file);
        }
        
        try {
            NbtCompound nbt = data.serializeNbt();
            Files.createDirectories(file.getParentFile().toPath());
            NbtIo.writeCompressed(nbt, file.toPath());
            data.markClean();
            
            EventBus.post(new PlayerDataSavedEvent(player, data));
            
            GrandLineMod.LOGGER.debug("Saved player data for {}", player.getName().getString());
        } catch (IOException e) {
            GrandLineMod.LOGGER.error("Failed to save player data for {}", 
                player.getName().getString(), e);
        }
    }
    
    /**
     * Loads player data.
     */
    private void loadPlayerData(ServerPlayerEntity player) {
        getPlayerData(player); // Triggers load if not cached
    }
    
    /**
     * Saves all cached player data.
     */
    public void saveAll() {
        for (Map.Entry<UUID, PlayerData> entry : cachedData.entrySet()) {
            ServerPlayerEntity player = server.getPlayerManager().getPlayer(entry.getKey());
            if (player != null) {
                savePlayerData(player);
            }
        }
        GrandLineMod.LOGGER.info("Saved all player data");
    }
    
    /**
     * Creates a backup of a player data file.
     */
    private void createBackup(File file) {
        try {
            File backup = new File(file.getParentFile(), file.getName() + ".backup");
            Files.copy(file.toPath(), backup.toPath(), 
                java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            GrandLineMod.LOGGER.warn("Failed to create backup", e);
        }
    }
    
    /**
     * Loads player data from backup.
     */
    private PlayerData loadBackup(UUID playerId, String playerName) {
        File backup = new File(getPlayerDataFile(playerId).getParentFile(), 
            playerId.toString() + ".dat.backup");
        
        if (backup.exists()) {
            try {
                NbtCompound nbt = NbtIo.readCompressed(backup.toPath(), NbtIo.UNLIMITED_TRACKER);
                PlayerData data = PlayerData.deserializeNbt(nbt);
                GrandLineMod.LOGGER.info("Loaded player data from backup for {}", playerName);
                return data;
            } catch (IOException e) {
                GrandLineMod.LOGGER.error("Failed to load backup for {}", playerName, e);
            }
        }
        
        GrandLineMod.LOGGER.warn("Creating new player data for {} after load failure", playerName);
        return new PlayerData(playerId, playerName);
    }
    
    /**
     * Gets the file for player data.
     */
    private File getPlayerDataFile(UUID playerId) {
        return dataDirectory.resolve(playerId.toString() + ".dat").toFile();
    }
    
    /**
     * Gets the experience manager.
     * 
     * @return The experience manager
     */
    public ExperienceManager getExperienceManager() {
        return experienceManager;
    }
}
