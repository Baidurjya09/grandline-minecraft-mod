package com.grandline.data.experience;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for XpCurve.
 */
class XpCurveTest {
    
    @Test
    void testLinearCurve() {
        XpCurve curve = new XpCurve(XpCurve.CurveType.LINEAR, 100, 1.0);
        
        assertEquals(0, curve.getXpForLevel(1));
        assertEquals(100, curve.getXpForLevel(2));
        assertEquals(200, curve.getXpForLevel(3));
    }
    
    @Test
    void testExponentialCurve() {
        XpCurve curve = new XpCurve(XpCurve.CurveType.EXPONENTIAL, 100, 2.0);
        
        assertEquals(0, curve.getXpForLevel(1));
        assertEquals(100, curve.getXpForLevel(2));
        assertEquals(200, curve.getXpForLevel(3));
        assertEquals(400, curve.getXpForLevel(4));
    }
    
    @Test
    void testGetLevelForXp() {
        XpCurve curve = new XpCurve(XpCurve.CurveType.LINEAR, 100, 1.0);
        
        assertEquals(1, curve.getLevelForXp(0));
        assertEquals(1, curve.getLevelForXp(50));
        assertEquals(2, curve.getLevelForXp(100));
        assertEquals(2, curve.getLevelForXp(150));
        assertEquals(3, curve.getLevelForXp(200));
    }
    
    @Test
    void testXpToNextLevel() {
        XpCurve curve = new XpCurve(XpCurve.CurveType.LINEAR, 100, 1.0);
        
        assertEquals(100, curve.getXpToNextLevel(0, 1));
        assertEquals(50, curve.getXpToNextLevel(50, 1));
        assertEquals(100, curve.getXpToNextLevel(100, 2));
    }
    
    @Test
    void testLevelProgress() {
        XpCurve curve = new XpCurve(XpCurve.CurveType.LINEAR, 100, 1.0);
        
        assertEquals(0.0f, curve.getLevelProgress(0, 1), 0.01f);
        assertEquals(0.5f, curve.getLevelProgress(50, 1), 0.01f);
        assertEquals(1.0f, curve.getLevelProgress(100, 1), 0.01f);
    }
}
