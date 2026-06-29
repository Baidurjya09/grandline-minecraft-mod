package com.grandline.core.event;

/**
 * Base class for all events in the Grand Line mod.
 * Events are used to decouple systems and allow for extensibility.
 * 
 * To create a custom event:
 * 1. Extend this class
 * 2. Add relevant data fields
 * 3. Implement Cancellable if the event can be cancelled
 * 4. Post the event using EventBus.post()
 * 
 * Thread Safety: Events are not thread-safe and should only be posted
 * from the main game thread unless explicitly documented otherwise.
 */
public abstract class Event {
    
    private boolean cancelled = false;
    private final long timestamp;
    
    protected Event() {
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * Gets the timestamp when this event was created.
     * 
     * @return The creation timestamp in milliseconds
     */
    public long getTimestamp() {
        return timestamp;
    }
    
    /**
     * Checks if this event has been cancelled.
     * Only applicable for events implementing {@link Cancellable}.
     * 
     * @return true if cancelled, false otherwise
     */
    public boolean isCancelled() {
        return cancelled;
    }
    
    /**
     * Sets the cancelled state of this event.
     * This method should only be called by cancellable events.
     * 
     * @param cancelled The new cancelled state
     * @throws UnsupportedOperationException if the event is not cancellable
     */
    public void setCancelled(boolean cancelled) {
        if (!(this instanceof Cancellable)) {
            throw new UnsupportedOperationException(
                "Cannot cancel event " + getClass().getSimpleName() + " - not cancellable");
        }
        this.cancelled = cancelled;
    }
    
    /**
     * Convenience method to cancel this event.
     * Equivalent to setCancelled(true).
     * 
     * @throws UnsupportedOperationException if the event is not cancellable
     */
    public void cancel() {
        setCancelled(true);
    }
    
    /**
     * Gets the name of this event type.
     * Used for debugging and logging.
     * 
     * @return The simple class name
     */
    public String getEventName() {
        return getClass().getSimpleName();
    }
    
    @Override
    public String toString() {
        return getEventName() + "{cancelled=" + cancelled + ", timestamp=" + timestamp + "}";
    }
}
