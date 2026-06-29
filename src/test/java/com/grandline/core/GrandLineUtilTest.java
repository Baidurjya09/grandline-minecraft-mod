package com.grandline.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GrandLineUtil.
 */
class GrandLineUtilTest {
    
    @Test
    void testIdCreation() {
        var id = GrandLineUtil.id("test_item");
        assertEquals("grandline", id.getNamespace());
        assertEquals("test_item", id.getPath());
    }
    
    @Test
    void testIdCreationWithNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            GrandLineUtil.id(null);
        });
    }
    
    @Test
    void testIdCreationWithEmptyThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            GrandLineUtil.id("");
        });
    }
    
    @Test
    void testRequireNonNullWithValue() {
        String value = "test";
        String result = GrandLineUtil.requireNonNull(value, "value");
        assertEquals(value, result);
    }
    
    @Test
    void testRequireNonNullWithNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            GrandLineUtil.requireNonNull(null, "value");
        });
    }
    
    @Test
    void testClampInt() {
        assertEquals(5, GrandLineUtil.clamp(5, 0, 10));
        assertEquals(0, GrandLineUtil.clamp(-5, 0, 10));
        assertEquals(10, GrandLineUtil.clamp(15, 0, 10));
    }
    
    @Test
    void testClampFloat() {
        assertEquals(5.0f, GrandLineUtil.clamp(5.0f, 0.0f, 10.0f));
        assertEquals(0.0f, GrandLineUtil.clamp(-5.0f, 0.0f, 10.0f));
        assertEquals(10.0f, GrandLineUtil.clamp(15.0f, 0.0f, 10.0f));
    }
    
    @Test
    void testClampDouble() {
        assertEquals(5.0, GrandLineUtil.clamp(5.0, 0.0, 10.0));
        assertEquals(0.0, GrandLineUtil.clamp(-5.0, 0.0, 10.0));
        assertEquals(10.0, GrandLineUtil.clamp(15.0, 0.0, 10.0));
    }
    
    @Test
    void testLerpFloat() {
        assertEquals(0.0f, GrandLineUtil.lerp(0.0f, 10.0f, 0.0f));
        assertEquals(5.0f, GrandLineUtil.lerp(0.0f, 10.0f, 0.5f));
        assertEquals(10.0f, GrandLineUtil.lerp(0.0f, 10.0f, 1.0f));
    }
    
    @Test
    void testLerpDouble() {
        assertEquals(0.0, GrandLineUtil.lerp(0.0, 10.0, 0.0));
        assertEquals(5.0, GrandLineUtil.lerp(0.0, 10.0, 0.5));
        assertEquals(10.0, GrandLineUtil.lerp(0.0, 10.0, 1.0));
    }
}
