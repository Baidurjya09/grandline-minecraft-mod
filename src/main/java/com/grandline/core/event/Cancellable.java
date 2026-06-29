package com.grandline.core.event;

/**
 * Marker interface for events that can be cancelled.
 * 
 * When an event is cancelled, subsequent listeners should respect
 * the cancellation and the action that triggered the event should
 * not proceed.
 * 
 * Example:
 * <pre>
 * public class PlayerDamageEvent extends Event implements Cancellable {
 *     // Event implementation
 * }
 * 
 * // In a listener:
 * if (event.isCancelled()) {
 *     return; // Don't process cancelled events
 * }
 * </pre>
 */
public interface Cancellable {
    
    /**
     * Checks if this event has been cancelled.
     * 
     * @return true if cancelled, false otherwise
     */
    boolean isCancelled();
    
    /**
     * Sets the cancelled state of this event.
     * 
     * @param cancelled The new cancelled state
     */
    void setCancelled(boolean cancelled);
    
    /**
     * Cancels this event.
     * Convenience method equivalent to setCancelled(true).
     */
    default void cancel() {
        setCancelled(true);
    }
}
