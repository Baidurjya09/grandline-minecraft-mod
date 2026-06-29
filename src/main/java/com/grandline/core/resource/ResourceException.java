package com.grandline.core.resource;

/**
 * Exception thrown when resource loading fails.
 */
public class ResourceException extends Exception {
    
    public ResourceException(String message) {
        super(message);
    }
    
    public ResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
