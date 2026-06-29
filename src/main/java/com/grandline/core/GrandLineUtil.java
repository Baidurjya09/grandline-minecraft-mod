package com.grandline.core;

import com.grandline.GrandLineMod;
import net.minecraft.util.Identifier;

/**
 * Utility class for common operations used throughout the mod.
 * This class provides helper methods for:
 * - Resource identification
 * - String manipulation
 * - Common calculations
 * - Validation
 */
public final class GrandLineUtil {
    
    private GrandLineUtil() {
        throw new AssertionError("Utility class should not be instantiated");
    }
    
    /**
     * Creates a properly namespaced identifier for this mod.
     * 
     * @param path The resource path
     * @return An identifier with the mod's namespace
     * @throws IllegalArgumentException if path is null or empty
     */
    public static Identifier id(String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Path cannot be null or empty");
        }
        return Identifier.of(GrandLineMod.MOD_ID, path);
    }
    
    /**
     * Validates that a value is not null.
     * 
     * @param value The value to check
     * @param name The name of the value (for error messages)
     * @param <T> The type of the value
     * @return The value if not null
     * @throws IllegalArgumentException if value is null
     */
    public static <T> T requireNonNull(T value, String name) {
        if (value == null) {
            throw new IllegalArgumentException(name + " cannot be null");
        }
        return value;
    }
    
    /**
     * Clamps a value between a minimum and maximum.
     * 
     * @param value The value to clamp
     * @param min The minimum value
     * @param max The maximum value
     * @return The clamped value
     */
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
    
    /**
     * Clamps a value between a minimum and maximum.
     * 
     * @param value The value to clamp
     * @param min The minimum value
     * @param max The maximum value
     * @return The clamped value
     */
    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
    
    /**
     * Clamps a value between a minimum and maximum.
     * 
     * @param value The value to clamp
     * @param min The minimum value
     * @param max The maximum value
     * @return The clamped value
     */
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
    
    /**
     * Linearly interpolates between two values.
     * 
     * @param start The start value
     * @param end The end value
     * @param progress The interpolation progress (0.0 to 1.0)
     * @return The interpolated value
     */
    public static float lerp(float start, float end, float progress) {
        return start + (end - start) * progress;
    }
    
    /**
     * Linearly interpolates between two values.
     * 
     * @param start The start value
     * @param end The end value
     * @param progress The interpolation progress (0.0 to 1.0)
     * @return The interpolated value
     */
    public static double lerp(double start, double end, double progress) {
        return start + (end - start) * progress;
    }
}
