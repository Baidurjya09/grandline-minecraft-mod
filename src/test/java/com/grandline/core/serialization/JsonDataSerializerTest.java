package com.grandline.core.serialization;

import com.google.gson.JsonElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for JsonDataSerializer.
 */
class JsonDataSerializerTest {
    
    private JsonDataSerializer serializer;
    
    @BeforeEach
    void setUp() {
        serializer = new JsonDataSerializer();
    }
    
    @Test
    void testSerializeSimpleObject() throws SerializationException {
        TestData data = new TestData("test", 42);
        
        JsonElement json = serializer.serialize(data);
        
        assertNotNull(json);
        assertTrue(json.isJsonObject());
        assertEquals("test", json.getAsJsonObject().get("name").getAsString());
        assertEquals(42, json.getAsJsonObject().get("value").getAsInt());
    }
    
    @Test
    void testDeserializeSimpleObject() throws SerializationException {
        TestData original = new TestData("test", 42);
        JsonElement json = serializer.serialize(original);
        
        TestData deserialized = serializer.deserialize(json, TestData.class);
        
        assertNotNull(deserialized);
        assertEquals("test", deserialized.name);
        assertEquals(42, deserialized.value);
    }
    
    @Test
    void testRoundTrip() throws SerializationException {
        TestData original = new TestData("roundtrip", 99);
        
        JsonElement json = serializer.serialize(original);
        TestData restored = serializer.deserialize(json, TestData.class);
        
        assertEquals(original.name, restored.name);
        assertEquals(original.value, restored.value);
    }
    
    @Test
    void testFormatName() {
        assertEquals("json", serializer.getFormatName());
    }
    
    // Test Data Class
    static class TestData {
        String name;
        int value;
        
        TestData(String name, int value) {
            this.name = name;
            this.value = value;
        }
        
        // Required for Gson deserialization
        TestData() {}
    }
}
