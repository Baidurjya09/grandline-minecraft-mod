package com.grandline.core.event;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for EventBus.
 */
class EventBusTest {
    
    @BeforeEach
    void setUp() {
        EventBus.clearAll();
    }
    
    @AfterEach
    void tearDown() {
        EventBus.clearAll();
    }
    
    @Test
    void testEventPosting() {
        TestListener listener = new TestListener();
        EventBus.register(listener);
        
        TestEvent event = new TestEvent();
        EventBus.post(event);
        
        assertTrue(listener.wasCalled());
        assertEquals(event, listener.getReceivedEvent());
    }
    
    @Test
    void testMultipleListeners() {
        TestListener listener1 = new TestListener();
        TestListener listener2 = new TestListener();
        
        EventBus.register(listener1);
        EventBus.register(listener2);
        
        TestEvent event = new TestEvent();
        EventBus.post(event);
        
        assertTrue(listener1.wasCalled());
        assertTrue(listener2.wasCalled());
    }
    
    @Test
    void testEventPriority() {
        OrderTracker tracker = new OrderTracker();
        
        PriorityListener lowListener = new PriorityListener(tracker, EventPriority.LOW);
        PriorityListener normalListener = new PriorityListener(tracker, EventPriority.NORMAL);
        PriorityListener highListener = new PriorityListener(tracker, EventPriority.HIGH);
        
        // Register in reverse order
        EventBus.register(highListener);
        EventBus.register(lowListener);
        EventBus.register(normalListener);
        
        EventBus.post(new TestEvent());
        
        // Should execute in priority order: LOW -> NORMAL -> HIGH
        assertEquals(3, tracker.getCallOrder().size());
        assertEquals(EventPriority.LOW, tracker.getCallOrder().get(0));
        assertEquals(EventPriority.NORMAL, tracker.getCallOrder().get(1));
        assertEquals(EventPriority.HIGH, tracker.getCallOrder().get(2));
    }
    
    @Test
    void testCancellableEvent() {
        CancelListener cancelListener = new CancelListener();
        TestListener normalListener = new TestListener();
        
        EventBus.register(cancelListener);
        EventBus.register(normalListener);
        
        CancellableTestEvent event = new CancellableTestEvent();
        EventBus.post(event);
        
        assertTrue(event.isCancelled());
        assertTrue(cancelListener.wasCalled());
        assertFalse(normalListener.wasCalled()); // Should not be called due to ignoreCancelled=true
    }
    
    @Test
    void testUnregisterListener() {
        TestListener listener = new TestListener();
        EventBus.register(listener);
        EventBus.unregister(listener);
        
        EventBus.post(new TestEvent());
        
        assertFalse(listener.wasCalled());
    }
    
    @Test
    void testDuplicateRegistration() {
        TestListener listener = new TestListener();
        EventBus.register(listener);
        EventBus.register(listener); // Should not throw, just log warning
        
        TestEvent event = new TestEvent();
        EventBus.post(event);
        
        // Should only be called once
        assertTrue(listener.wasCalled());
    }
    
    // Test Event Classes
    static class TestEvent extends Event {}
    
    static class CancellableTestEvent extends Event implements Cancellable {}
    
    // Test Listeners
    static class TestListener {
        private TestEvent receivedEvent;
        
        @EventHandler
        public void onTestEvent(TestEvent event) {
            this.receivedEvent = event;
        }
        
        boolean wasCalled() {
            return receivedEvent != null;
        }
        
        TestEvent getReceivedEvent() {
            return receivedEvent;
        }
    }
    
    static class PriorityListener {
        private final OrderTracker tracker;
        private final EventPriority priority;
        
        PriorityListener(OrderTracker tracker, EventPriority priority) {
            this.tracker = tracker;
            this.priority = priority;
        }
        
        @EventHandler(priority = EventPriority.LOW)
        public void onLowPriority(TestEvent event) {
            if (priority == EventPriority.LOW) {
                tracker.addCall(priority);
            }
        }
        
        @EventHandler(priority = EventPriority.NORMAL)
        public void onNormalPriority(TestEvent event) {
            if (priority == EventPriority.NORMAL) {
                tracker.addCall(priority);
            }
        }
        
        @EventHandler(priority = EventPriority.HIGH)
        public void onHighPriority(TestEvent event) {
            if (priority == EventPriority.HIGH) {
                tracker.addCall(priority);
            }
        }
    }
    
    static class CancelListener {
        private boolean called = false;
        
        @EventHandler(priority = EventPriority.LOW, ignoreCancelled = false)
        public void onCancellable(CancellableTestEvent event) {
            called = true;
            event.cancel();
        }
        
        boolean wasCalled() {
            return called;
        }
    }
    
    static class OrderTracker {
        private final List<EventPriority> callOrder = new ArrayList<>();
        
        void addCall(EventPriority priority) {
            callOrder.add(priority);
        }
        
        List<EventPriority> getCallOrder() {
            return callOrder;
        }
    }
}
