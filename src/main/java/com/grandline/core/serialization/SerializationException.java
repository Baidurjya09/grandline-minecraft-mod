package com.grandline.core.serialization;

/**
 * Exception thrown when serialization or deserialization fails.
 */
public class SerializationException extends Exception {
    
    public SerializationException(String message) {
        super(message);
    }
    
    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
