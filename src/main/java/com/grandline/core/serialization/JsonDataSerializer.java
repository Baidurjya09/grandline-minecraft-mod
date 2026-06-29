package com.grandline.core.serialization;

import com.google.gson.*;

/**
 * JSON implementation of DataSerializer using Gson.
 */
public class JsonDataSerializer implements DataSerializer<JsonElement> {
    
    private final Gson gson;
    
    public JsonDataSerializer() {
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    }
    
    public JsonDataSerializer(Gson gson) {
        this.gson = gson;
    }
    
    @Override
    public JsonElement serialize(Object object) throws SerializationException {
        try {
            return gson.toJsonTree(object);
        } catch (Exception e) {
            throw new SerializationException("Failed to serialize object to JSON", e);
        }
    }
    
    @Override
    public <R> R deserialize(JsonElement data, Class<R> type) throws SerializationException {
        try {
            return gson.fromJson(data, type);
        } catch (JsonSyntaxException e) {
            throw new SerializationException("Failed to deserialize JSON to " + type.getName(), e);
        }
    }
    
    @Override
    public String getFormatName() {
        return "json";
    }
    
    /**
     * Gets the Gson instance used by this serializer.
     * 
     * @return The Gson instance
     */
    public Gson getGson() {
        return gson;
    }
}
