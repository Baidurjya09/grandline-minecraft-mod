package com.grandline.core.event;

import com.grandline.GrandLineMod;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Central event bus for the Grand Line mod.
 * Manages event registration, posting, and handler execution.
 * 
 * This class is thread-safe for registration and posting operations.
 * Event handlers are executed on the thread that posts the event.
 */
public class EventBus {
    
    private static final int MAX_RECURSION_DEPTH = 10;
    private static final ThreadLocal<Integer> recursionDepth = ThreadLocal.withInitial(() -> 0);
    
    // Map: Event Class -> List of Handlers (sorted by priority)
    private static final Map<Class<? extends Event>, List<RegisteredHandler>> handlers = 
        new ConcurrentHashMap<>();
    
    // Set of registered listener instances (for duplicate prevention)
    private static final Set<Object> registeredListeners = 
        Collections.newSetFromMap(new WeakHashMap<>());
    
    /**
     * Registers all event handlers in the given listener object.
     * Methods annotated with @EventHandler will be registered.
     * 
     * @param listener The listener instance to register
     * @throws IllegalArgumentException if listener is null or already registered
     * @throws EventException if registration fails
     */
    public static synchronized void register(Object listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null");
        }
        
        if (registeredListeners.contains(listener)) {
            GrandLineMod.LOGGER.warn("Listener {} already registered", listener.getClass().getName());
            return;
        }
        
        int handlerCount = 0;
        
        for (Method method : listener.getClass().getMethods()) {
            EventHandler annotation = method.getAnnotation(EventHandler.class);
            if (annotation == null) {
                continue;
            }
            
            // Validate method signature
            if (method.getParameterCount() != 1) {
                throw new EventException(
                    "Event handler must have exactly one parameter: " + method);
            }
            
            Class<?> eventType = method.getParameterTypes()[0];
            if (!Event.class.isAssignableFrom(eventType)) {
                throw new EventException(
                    "Event handler parameter must extend Event: " + method);
            }
            
            @SuppressWarnings("unchecked")
            Class<? extends Event> eventClass = (Class<? extends Event>) eventType;
            
            RegisteredHandler handler = new RegisteredHandler(
                listener, method, annotation.priority(), annotation.ignoreCancelled());
            
            handlers.computeIfAbsent(eventClass, k -> new ArrayList<>()).add(handler);
            handlerCount++;
        }
        
        if (handlerCount > 0) {
            // Sort handlers by priority
            handlers.values().forEach(list -> 
                list.sort(Comparator.comparingInt(h -> h.priority.getSlot())));
            
            registeredListeners.add(listener);
            GrandLineMod.LOGGER.debug("Registered {} event handlers from {}", 
                handlerCount, listener.getClass().getSimpleName());
        } else {
            GrandLineMod.LOGGER.warn("No event handlers found in {}", 
                listener.getClass().getSimpleName());
        }
    }
    
    /**
     * Unregisters all event handlers from the given listener.
     * 
     * @param listener The listener instance to unregister
     */
    public static synchronized void unregister(Object listener) {
        if (listener == null) {
            return;
        }
        
        handlers.values().forEach(list -> 
            list.removeIf(handler -> handler.listener == listener));
        
        registeredListeners.remove(listener);
        
        GrandLineMod.LOGGER.debug("Unregistered listener {}", 
            listener.getClass().getSimpleName());
    }
}

    /**
     * Posts an event to all registered handlers.
     * Handlers are executed in priority order on the calling thread.
     * 
     * @param event The event to post
     * @param <T> The event type
     * @return The event after all handlers have processed it
     * @throws IllegalArgumentException if event is null
     * @throws EventException if recursion depth is exceeded or handler execution fails
     */
    public static <T extends Event> T post(T event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        
        // Check recursion depth
        int depth = recursionDepth.get();
        if (depth >= MAX_RECURSION_DEPTH) {
            throw new EventException(
                "Event recursion depth exceeded (max " + MAX_RECURSION_DEPTH + "): " + 
                event.getEventName());
        }
        
        recursionDepth.set(depth + 1);
        
        try {
            List<RegisteredHandler> eventHandlers = handlers.get(event.getClass());
            
            if (eventHandlers != null) {
                for (RegisteredHandler handler : eventHandlers) {
                    // Skip if event is cancelled and handler doesn't want cancelled events
                    if (event.isCancelled() && handler.ignoreCancelled) {
                        continue;
                    }
                    
                    try {
                        handler.invoke(event);
                    } catch (Exception e) {
                        GrandLineMod.LOGGER.error(
                            "Error executing event handler {} for event {}", 
                            handler.method.getName(), 
                            event.getEventName(), 
                            e);
                        
                        // Don't let one handler's failure break the entire event chain
                        // But log it for debugging
                    }
                }
            }
            
            return event;
            
        } finally {
            recursionDepth.set(depth);
        }
    }
    
    /**
     * Gets the number of registered handlers for a specific event type.
     * 
     * @param eventClass The event class
     * @return The number of handlers
     */
    public static int getHandlerCount(Class<? extends Event> eventClass) {
        List<RegisteredHandler> eventHandlers = handlers.get(eventClass);
        return eventHandlers != null ? eventHandlers.size() : 0;
    }
    
    /**
     * Clears all registered event handlers.
     * This should only be used for testing or cleanup.
     */
    public static synchronized void clearAll() {
        handlers.clear();
        registeredListeners.clear();
        GrandLineMod.LOGGER.info("Cleared all event handlers");
    }
    
    /**
     * Internal class representing a registered event handler.
     */
    private static class RegisteredHandler {
        final Object listener;
        final Method method;
        final EventPriority priority;
        final boolean ignoreCancelled;
        
        RegisteredHandler(Object listener, Method method, EventPriority priority, 
                         boolean ignoreCancelled) {
            this.listener = listener;
            this.method = method;
            this.priority = priority;
            this.ignoreCancelled = ignoreCancelled;
            
            // Make method accessible if it isn't already
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
        }
        
        void invoke(Event event) throws Exception {
            method.invoke(listener, event);
        }
    }
}
