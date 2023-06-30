package com.geldautomat.model;

import static org.junit.Assert.*;
import org.junit.Test;

public class GirokontoTest {
    
    @Test
    public void testEinzahlen() {
        Kontoinhaber kontoinhaber = new Kontoinhaber(123456, "Mustermann", "Max", "Bahnhofstraße 1", "68159", "Mannheim");
        Bank bank = new Bank("VR Bank Rhein-Neckar", "MA2424", null);
        Girokonto girokonto = new Girokonto(4711, 1234, 100.0, kontoinhaber, bank, 1000.0);
        
        girokonto.einzahlen(50.0);
        
        assertEquals(150.0, girokonto.getKontostand(), 0.001);
    }
    
    @Test
    public void testAuzzahlenInGrenzen() {
        Kontoinhaber kontoinhaber = new Kontoinhaber(123456, "Mustermann", "Max", "Bahnhofstraße 1", "68159", "Mannheim");
        Bank bank = new Bank("VR Bank Rhein-Neckar", "MA2424", null);
        Girokonto girokonto = new Girokonto(4711, 1234, 100.0, kontoinhaber, bank, 1000.0);
        
        assertFalse(girokonto.istKontoüberzogen());
        
        girokonto.auszahlen(500.0);
        
        assertEquals(-400.0, girokonto.getKontostand(), 0.001);
        assertTrue(girokonto.istKontoüberzogen());
    }
    
    @Test
    public void testAuzzahlenImUebermaß() {
        Kontoinhaber kontoinhaber = new Kontoinhaber(123456, "Mustermann", "Max", "Bahnhofstraße 1", "68159", "Mannheim");
        Bank bank = new Bank("VR Bank Rhein-Neckar", "MA2424", null);
        Girokonto girokonto = new Girokonto(4711, 1234, 100.0, kontoinhaber, bank, 1000.0);
        
        assertFalse(girokonto.istKontoüberzogen());
        
        try {
            girokonto.auszahlen(1500.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Kontostand zu niedrig", e.getMessage());
        }
        
        assertEquals(100.0, girokonto.getKontostand(), 0.001);
        assertFalse(girokonto.istKontoüberzogen());
    }
}
