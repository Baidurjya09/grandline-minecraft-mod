package com.grandline.core.command;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CommandContext.
 */
class CommandContextTest {
    
    @Test
    void testBasicContext() {
        CommandContext ctx = new CommandContext("test", Arrays.asList("arg1", "arg2"), "test arg1 arg2");
        
        assertEquals("test", ctx.getCommandName());
        assertEquals(2, ctx.getArgCount());
        assertEquals("arg1", ctx.getArg(0));
        assertEquals("arg2", ctx.getArg(1));
        assertEquals("test arg1 arg2", ctx.getRawInput());
    }
    
    @Test
    void testEmptyArgs() {
        CommandContext ctx = new CommandContext("test", Collections.emptyList(), "test");
        
        assertEquals(0, ctx.getArgCount());
        assertFalse(ctx.hasArg(0));
    }
    
    @Test
    void testArgAsInt() {
        CommandContext ctx = new CommandContext("test", Arrays.asList("42", "invalid"), "test 42 invalid");
        
        assertEquals(42, ctx.getArgAsInt(0));
        assertEquals(99, ctx.getArgAsInt(1, 99)); // default for invalid
        assertEquals(99, ctx.getArgAsInt(5, 99)); // default for missing
    }
    
    @Test
    void testArgAsBoolean() {
        CommandContext ctx = new CommandContext("test", 
            Arrays.asList("true", "yes", "on", "1", "false", "no"), 
            "test true yes on 1 false no");
        
        assertTrue(ctx.getArgAsBoolean(0));
        assertTrue(ctx.getArgAsBoolean(1));
        assertTrue(ctx.getArgAsBoolean(2));
        assertTrue(ctx.getArgAsBoolean(3));
        assertFalse(ctx.getArgAsBoolean(4));
        assertFalse(ctx.getArgAsBoolean(5));
    }
    
    @Test
    void testJoinArgs() {
        CommandContext ctx = new CommandContext("test", 
            Arrays.asList("hello", "world", "test"), 
            "test hello world test");
        
        assertEquals("hello world test", ctx.joinArgs(0));
        assertEquals("world test", ctx.joinArgs(1));
        assertEquals("world", ctx.joinArgs(1, 2));
    }
    
    @Test
    void testArgWithDefault() {
        CommandContext ctx = new CommandContext("test", Arrays.asList("exists"), "test exists");
        
        assertEquals("exists", ctx.getArg(0, "default"));
        assertEquals("default", ctx.getArg(1, "default"));
    }
}
