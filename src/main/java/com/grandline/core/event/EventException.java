package com.grandline.core.event;

/**
 * Exception thrown when event system operations fail.
 */
public class EventException extends RuntimeException {
    
    public EventException(String message) {
        super(message);
    }
    
    public EventException(String message, Throwable cause) {
        super(message, cause);
    }
}
