package com.grandline.core.serialization;

/**
 * Interface for data serialization implementations.
 * Supports converting between Java objects and various formats (JSON, NBT, etc.).
 * 
 * @param <T> The serialization format type (JsonObject, NbtCompound, etc.)
 */
public interface DataSerializer<T> {
    
    /**
     * Serializes an object to the format.
     * 
     * @param object The object to serialize
     * @return The serialized data
     * @throws SerializationException if serialization fails
     */
    T serialize(Object object) throws SerializationException;
    
    /**
     * Deserializes data to an object.
     * 
     * @param data The serialized data
     * @param type The target class type
     * @param <R> The result type
     * @return The deserialized object
     * @throws SerializationException if deserialization fails
     */
    <R> R deserialize(T data, Class<R> type) throws SerializationException;
    
    /**
     * Gets the format name this serializer handles.
     * 
     * @return The format name (e.g., "json", "nbt")
     */
    String getFormatName();
}
