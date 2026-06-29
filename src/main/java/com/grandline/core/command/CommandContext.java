package com.grandline.core.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Context for command execution containing parsed arguments.
 */
public class CommandContext {
    
    private final String commandName;
    private final List<String> args;
    private final String rawInput;
    
    public CommandContext(String commandName, List<String> args, String rawInput) {
        this.commandName = commandName;
        this.args = new ArrayList<>(args);
        this.rawInput = rawInput;
    }
    
    /**
     * Gets the command name that was invoked.
     * 
     * @return The command name
     */
    public String getCommandName() {
        return commandName;
    }
    
    /**
     * Gets all arguments as an unmodifiable list.
     * 
     * @return The arguments list
     */
    public List<String> getArgs() {
        return Collections.unmodifiableList(args);
    }
    
    /**
     * Gets the number of arguments.
     * 
     * @return Argument count
     */
    public int getArgCount() {
        return args.size();
    }
    
    /**
     * Gets an argument by index.
     * 
     * @param index The argument index (0-based)
     * @return The argument value
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public String getArg(int index) {
        return args.get(index);
    }
    
    /**
     * Gets an argument by index, or returns a default if not present.
     * 
     * @param index The argument index (0-based)
     * @param defaultValue The default value
     * @return The argument value or default
     */
    public String getArg(int index, String defaultValue) {
        return index < args.size() ? args.get(index) : defaultValue;
    }
    
    /**
     * Gets an argument as an integer.
     * 
     * @param index The argument index (0-based)
     * @return The integer value
     * @throws IndexOutOfBoundsException if index is invalid
     * @throws NumberFormatException if argument is not a valid integer
     */
    public int getArgAsInt(int index) {
        return Integer.parseInt(getArg(index));
    }
    
    /**
     * Gets an argument as an integer, or returns default if invalid.
     * 
     * @param index The argument index (0-based)
     * @param defaultValue The default value
     * @return The integer value or default
     */
    public int getArgAsInt(int index, int defaultValue) {
        try {
            return index < args.size() ? Integer.parseInt(args.get(index)) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * Gets an argument as a double.
     * 
     * @param index The argument index (0-based)
     * @return The double value
     * @throws IndexOutOfBoundsException if index is invalid
     * @throws NumberFormatException if argument is not a valid double
     */
    public double getArgAsDouble(int index) {
        return Double.parseDouble(getArg(index));
    }
    
    /**
     * Gets an argument as a boolean.
     * Accepts: true/false, yes/no, on/off, 1/0
     * 
     * @param index The argument index (0-based)
     * @return The boolean value
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public boolean getArgAsBoolean(int index) {
        String arg = getArg(index).toLowerCase();
        return arg.equals("true") || arg.equals("yes") || 
               arg.equals("on") || arg.equals("1");
    }
    
    /**
     * Checks if an argument exists at the given index.
     * 
     * @param index The argument index (0-based)
     * @return true if argument exists, false otherwise
     */
    public boolean hasArg(int index) {
        return index < args.size();
    }
    
    /**
     * Gets the raw input string.
     * 
     * @return The raw command input
     */
    public String getRawInput() {
        return rawInput;
    }
    
    /**
     * Joins arguments from start index to end as a single string.
     * 
     * @param startIndex The starting index (inclusive)
     * @return The joined string
     */
    public String joinArgs(int startIndex) {
        return joinArgs(startIndex, args.size());
    }
    
    /**
     * Joins arguments from start to end index as a single string.
     * 
     * @param startIndex The starting index (inclusive)
     * @param endIndex The ending index (exclusive)
     * @return The joined string
     */
    public String joinArgs(int startIndex, int endIndex) {
        if (startIndex >= args.size()) {
            return "";
        }
        return String.join(" ", args.subList(startIndex, Math.min(endIndex, args.size())));
    }
}
