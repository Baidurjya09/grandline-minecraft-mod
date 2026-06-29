package com.grandline.core.serialization;

import net.minecraft.nbt.NbtCompound;

/**
 * NBT implementation of DataSerializer for Minecraft data.
 * This is a basic implementation - full NBT serialization will be
 * expanded in future phases as gameplay features are added.
 */
public class NbtDataSerializer implements DataSerializer<NbtCompound> {
    
    @Override
    public NbtCompound serialize(Object object) throws SerializationException {
        if (object instanceof NbtSerializable) {
            return ((NbtSerializable) object).serializeNbt();
        }
        throw new SerializationException(
            "Object does not implement NbtSerializable: " + object.getClass().getName());
    }
    
    @Override
    public <R> R deserialize(NbtCompound data, Class<R> type) throws SerializationException {
        if (!NbtSerializable.class.isAssignableFrom(type)) {
            throw new SerializationException(
                "Type does not implement NbtSerializable: " + type.getName());
        }
        
        try {
            // Create instance and deserialize
            @SuppressWarnings("unchecked")
            NbtSerializable instance = (NbtSerializable) type.getDeclaredConstructor().newInstance();
            instance.deserializeNbt(data);
            @SuppressWarnings("unchecked")
            R result = (R) instance;
            return result;
        } catch (Exception e) {
            throw new SerializationException("Failed to deserialize NBT to " + type.getName(), e);
        }
    }
    
    @Override
    public String getFormatName() {
        return "nbt";
    }
    
    /**
     * Interface for objects that can be serialized to/from NBT.
     */
    public interface NbtSerializable {
        /**
         * Serializes this object to NBT.
         * 
         * @return The NBT compound
         */
        NbtCompound serializeNbt();
        
        /**
         * Deserializes this object from NBT.
         * 
         * @param nbt The NBT compound
         */
        void deserializeNbt(NbtCompound nbt);
    }
}
