package com.grandline.data;

import com.grandline.data.stats.PlayerStats;
import com.grandline.data.stats.StatType;
import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

/**
 * Container for all player-specific data.
 * This class holds all persistent and runtime data for a player.
 * 
 * Thread Safety: This class is NOT thread-safe. All access should be
 * synchronized through PlayerDataManager.
 */
public class PlayerData {
    
    public static final int CURRENT_DATA_VERSION = 1;
    
    private final UUID playerId;
    private String playerName;
    
    // Experience & Leveling
    private int experience;
    private int level;
    private int skillPoints;
    
    // Statistics
    private final PlayerStats stats;
    
    // Metadata
    private long firstJoinTime;
    private long lastSeenTime;
    private int dataVersion;
    
    // Runtime flags
    private transient boolean dirty;
    
    /**
     * Creates new player data with defaults.
     * 
     * @param playerId The player's UUID
     * @param playerName The player's name
     */
    public PlayerData(UUID playerId, String playerName) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.experience = 0;
        this.level = 1;
        this.skillPoints = 0;
        this.stats = new PlayerStats();
        this.firstJoinTime = System.currentTimeMillis();
        this.lastSeenTime = this.firstJoinTime;
        this.dataVersion = CURRENT_DATA_VERSION;
        this.dirty = true;
    }
    
    /**
     * Gets the player's UUID.
     * 
     * @return The UUID
     */
    public UUID getPlayerId() {
        return playerId;
    }
    
    /**
     * Gets the player's name.
     * 
     * @return The player name
     */
    public String getPlayerName() {
        return playerName;
    }
    
    /**
     * Sets the player's name (for name changes).
     * 
     * @param playerName The new name
     */
    public void setPlayerName(String playerName) {
        if (!this.playerName.equals(playerName)) {
            this.playerName = playerName;
            markDirty();
        }
    }
    
    /**
     * Gets the player's experience points.
     * 
     * @return The experience
     */
    public int getExperience() {
        return experience;
    }
    
    /**
     * Sets the player's experience points.
     * 
     * @param experience The new experience
     */
    public void setExperience(int experience) {
        if (this.experience != experience) {
            this.experience = Math.max(0, experience);
            markDirty();
        }
    }
    
    /**
     * Adds experience points.
     * 
     * @param amount The amount to add
     */
    public void addExperience(int amount) {
        setExperience(this.experience + amount);
    }
    
    /**
     * Gets the player's level.
     * 
     * @return The level
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Sets the player's level.
     * 
     * @param level The new level
     */
    public void setLevel(int level) {
        if (this.level != level) {
            this.level = Math.max(1, level);
            markDirty();
        }
    }
    
    /**
     * Gets the player's skill points.
     * 
     * @return The skill points
     */
    public int getSkillPoints() {
        return skillPoints;
    }
    
    /**
     * Sets the player's skill points.
     * 
     * @param skillPoints The new skill points
     */
    public void setSkillPoints(int skillPoints) {
        if (this.skillPoints != skillPoints) {
            this.skillPoints = Math.max(0, skillPoints);
            markDirty();
        }
    }
    
    /**
     * Adds skill points.
     * 
     * @param amount The amount to add
     */
    public void addSkillPoints(int amount) {
        setSkillPoints(this.skillPoints + amount);
    }
    
    /**
     * Spends skill points.
     * 
     * @param amount The amount to spend
     * @return true if points were spent, false if not enough points
     */
    public boolean spendSkillPoints(int amount) {
        if (this.skillPoints >= amount) {
            setSkillPoints(this.skillPoints - amount);
            return true;
        }
        return false;
    }
    
    /**
     * Gets the player's statistics.
     * 
     * @return The stats object
     */
    public PlayerStats getStats() {
        return stats;
    }
    
    /**
     * Gets a specific stat value.
     * 
     * @param type The stat type
     * @return The stat value
     */
    public long getStat(StatType type) {
        return stats.getStat(type);
    }
    
    /**
     * Sets a specific stat value.
     * 
     * @param type The stat type
     * @param value The new value
     */
    public void setStat(StatType type, long value) {
        stats.setStat(type, value);
        markDirty();
    }
    
    /**
     * Adds to a specific stat value.
     * 
     * @param type The stat type
     * @param amount The amount to add
     */
    public void addStat(StatType type, long amount) {
        stats.addStat(type, amount);
        markDirty();
    }
    
    /**
     * Gets the first join timestamp.
     * 
     * @return The timestamp in milliseconds
     */
    public long getFirstJoinTime() {
        return firstJoinTime;
    }
    
    /**
     * Gets the last seen timestamp.
     * 
     * @return The timestamp in milliseconds
     */
    public long getLastSeenTime() {
        return lastSeenTime;
    }
    
    /**
     * Updates the last seen timestamp to now.
     */
    public void updateLastSeen() {
        this.lastSeenTime = System.currentTimeMillis();
        markDirty();
    }
    
    /**
     * Gets the data version.
     * 
     * @return The version number
     */
    public int getDataVersion() {
        return dataVersion;
    }
    
    /**
     * Sets the data version (for migrations).
     * 
     * @param dataVersion The version number
     */
    public void setDataVersion(int dataVersion) {
        this.dataVersion = dataVersion;
    }
    
    /**
     * Checks if this data has been modified.
     * 
     * @return true if dirty, false otherwise
     */
    public boolean isDirty() {
        return dirty;
    }
    
    /**
     * Marks this data as modified.
     */
    public void markDirty() {
        this.dirty = true;
    }
    
    /**
     * Marks this data as clean (saved).
     */
    public void markClean() {
        this.dirty = false;
    }
    
    /**
     * Serializes this data to NBT.
     * 
     * @return The NBT compound
     */
    public NbtCompound serializeNbt() {
        NbtCompound nbt = new NbtCompound();
        
        nbt.putString("playerId", playerId.toString());
        nbt.putString("playerName", playerName);
        nbt.putInt("experience", experience);
        nbt.putInt("level", level);
        nbt.putInt("skillPoints", skillPoints);
        nbt.put("stats", stats.serializeNbt());
        nbt.putLong("firstJoinTime", firstJoinTime);
        nbt.putLong("lastSeenTime", lastSeenTime);
        nbt.putInt("dataVersion", dataVersion);
        
        return nbt;
    }
    
    /**
     * Deserializes player data from NBT.
     * 
     * @param nbt The NBT compound
     * @return The deserialized player data
     */
    public static PlayerData deserializeNbt(NbtCompound nbt) {
        UUID playerId = UUID.fromString(nbt.getString("playerId"));
        String playerName = nbt.getString("playerName");
        
        PlayerData data = new PlayerData(playerId, playerName);
        data.experience = nbt.getInt("experience");
        data.level = nbt.getInt("level");
        data.skillPoints = nbt.getInt("skillPoints");
        data.stats.deserializeNbt(nbt.getCompound("stats"));
        data.firstJoinTime = nbt.getLong("firstJoinTime");
        data.lastSeenTime = nbt.getLong("lastSeenTime");
        data.dataVersion = nbt.getInt("dataVersion");
        data.dirty = false;
        
        return data;
    }
    
    @Override
    public String toString() {
        return "PlayerData{" +
                "playerId=" + playerId +
                ", playerName='" + playerName + '\'' +
                ", level=" + level +
                ", experience=" + experience +
                ", skillPoints=" + skillPoints +
                '}';
    }
}
