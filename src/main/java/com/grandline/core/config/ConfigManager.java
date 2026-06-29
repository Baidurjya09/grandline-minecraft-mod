package com.grandline.core.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.grandline.GrandLineMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Manages configuration loading, saving, and access for the mod.
 * Configuration files are stored in the config directory and use JSON format.
 * 
 * This class handles:
 * - Loading configuration from disk
 * - Saving configuration to disk
 * - Providing default values
 * - Validating configuration values
 * - Hot-reloading configuration
 */
public class ConfigManager {
    
    private static final String CONFIG_FILE_NAME = "grandline.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
    private final Path configPath;
    private GrandLineConfig config;
    
    public ConfigManager() {
        this.configPath = FabricLoader.getInstance()
            .getConfigDir()
            .resolve(CONFIG_FILE_NAME);
        this.config = new GrandLineConfig();
    }
    
    /**
     * Loads the configuration from disk.
     * If the file doesn't exist, creates it with default values.
     * If the file is corrupted, backs it up and creates a new one.
     */
    public void load() {
        try {
            if (Files.exists(configPath)) {
                String json = Files.readString(configPath);
                JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
                config = GSON.fromJson(jsonObject, GrandLineConfig.class);
                
                // Validate loaded config
                if (!config.validate()) {
                    GrandLineMod.LOGGER.warn("Configuration validation failed, using defaults for invalid values");
                    config.applyDefaults();
                    save();
                }
                
                GrandLineMod.LOGGER.info("Configuration loaded successfully from {}", configPath);
            } else {
                // Create default config
                GrandLineMod.LOGGER.info("Configuration file not found, creating default configuration");
                config = new GrandLineConfig();
                save();
            }
        } catch (Exception e) {
            GrandLineMod.LOGGER.error("Failed to load configuration, using defaults", e);
            
            // Backup corrupted config
            try {
                if (Files.exists(configPath)) {
                    Path backup = configPath.resolveSibling(CONFIG_FILE_NAME + ".backup");
                    Files.move(configPath, backup);
                    GrandLineMod.LOGGER.info("Corrupted config backed up to {}", backup);
                }
            } catch (IOException backupError) {
                GrandLineMod.LOGGER.error("Failed to backup corrupted config", backupError);
            }
            
            config = new GrandLineConfig();
            save();
        }
    }
    
    /**
     * Saves the current configuration to disk.
     */
    public void save() {
        try {
            Files.createDirectories(configPath.getParent());
            String json = GSON.toJson(config);
            Files.writeString(configPath, json);
            GrandLineMod.LOGGER.debug("Configuration saved to {}", configPath);
        } catch (IOException e) {
            GrandLineMod.LOGGER.error("Failed to save configuration", e);
        }
    }
    
    /**
     * Reloads the configuration from disk.
     * Useful for applying changes made while the game is running.
     */
    public void reload() {
        GrandLineMod.LOGGER.info("Reloading configuration...");
        load();
    }
    
    /**
     * Gets the current configuration.
     * 
     * @return The configuration object
     */
    public GrandLineConfig getConfig() {
        return config;
    }
    
    /**
     * Gets the path to the configuration file.
     * 
     * @return The configuration file path
     */
    public Path getConfigPath() {
        return configPath;
    }
}
