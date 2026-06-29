package com.grandline.core.command;

import net.minecraft.text.Text;

/**
 * Represents the result of command execution.
 * Contains success status and optional feedback message.
 */
public class CommandResult {
    
    private final boolean success;
    private final Text message;
    
    private CommandResult(boolean success, Text message) {
        this.success = success;
        this.message = message;
    }
    
    /**
     * Creates a successful command result.
     * 
     * @param message Feedback message
     * @return Success result
     */
    public static CommandResult success(String message) {
        return new CommandResult(true, Text.literal(message));
    }
    
    /**
     * Creates a successful command result.
     * 
     * @param message Feedback message
     * @return Success result
     */
    public static CommandResult success(Text message) {
        return new CommandResult(true, message);
    }
    
    /**
     * Creates a successful command result with no message.
     * 
     * @return Success result
     */
    public static CommandResult success() {
        return new CommandResult(true, null);
    }
    
    /**
     * Creates a failed command result.
     * 
     * @param message Error message
     * @return Failure result
     */
    public static CommandResult failure(String message) {
        return new CommandResult(false, Text.literal(message));
    }
    
    /**
     * Creates a failed command result.
     * 
     * @param message Error message
     * @return Failure result
     */
    public static CommandResult failure(Text message) {
        return new CommandResult(false, message);
    }
    
    /**
     * Checks if the command succeeded.
     * 
     * @return true if successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }
    
    /**
     * Gets the feedback message.
     * 
     * @return The message, or null if none
     */
    public Text getMessage() {
        return message;
    }
    
    /**
     * Checks if this result has a message.
     * 
     * @return true if message exists, false otherwise
     */
    public boolean hasMessage() {
        return message != null;
    }
}
