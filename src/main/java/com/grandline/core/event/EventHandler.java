package com.grandline.core.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as an event handler.
 * 
 * Requirements:
 * - Method must be public
 * - Method must have exactly one parameter (the event type)
 * - Method must return void
 * - Method's declaring class must be registered with EventBus
 * 
 * Example:
 * <pre>
 * public class MyListener {
 *     {@literal @}EventHandler(priority = EventPriority.HIGH)
 *     public void onPlayerDamage(PlayerDamageEvent event) {
 *         // Handle event
 *     }
 * }
 * 
 * // Register the listener
 * EventBus.register(new MyListener());
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventHandler {
    
    /**
     * The execution priority for this handler.
     * Handlers are executed in order from lowest to highest priority.
     * 
     * @return The priority level
     */
    EventPriority priority() default EventPriority.NORMAL;
    
    /**
     * Whether to receive cancelled events.
     * If true, this handler will be called even if the event is cancelled.
     * 
     * @return true to receive cancelled events, false otherwise
     */
    boolean ignoreCancelled() default true;
}
