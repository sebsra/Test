package com.geldautomat.model;

import static org.junit.Assert.*;
import org.junit.Test;

public class SparkontoTest {
    
    @Test
    public void testEinzahlen() {
        Kontoinhaber kontoinhaber = new Kontoinhaber(123456, "Mustermann", "Max", "Bahnhofstraße 1", "68159", "Mannheim");
        Bank bank = new Bank("VR Bank Rhein-Neckar", "MA2424", null);
        Sparkonto sparkonto = new Sparkonto(4711, 1234, 100.0, kontoinhaber, bank, 3.0);
        
        sparkonto.einzahlen(50.0);
        
        assertEquals(150.0, sparkonto.getKontostand(), 0.001);
    }
    
    @Test
    public void testAuszahlen() {
        Kontoinhaber kontoinhaber = new Kontoinhaber(123456, "Mustermann", "Max", "Bahnhofstraße 1", "68159", "Mannheim");
        Bank bank = new Bank("VR Bank Rhein-Neckar", "MA2424", null);
        Sparkonto sparkonto = new Sparkonto(4711, 1234, 100.0, kontoinhaber, bank, 3.0);
        
        sparkonto.auszahlen(100.0);
        
        assertEquals(0.0, sparkonto.getKontostand(), 0.001);
    }
    
    @Test
    public void testZuVielAuszahlen() {
        Kontoinhaber kontoinhaber = new Kontoinhaber(123456, "Mustermann", "Max", "Bahnhofstraße 1", "68159", "Mannheim");
        Bank bank = new Bank("VR Bank Rhein-Neckar", "MA2424", null);
        Sparkonto sparkonto = new Sparkonto(4711, 1234, 100.0, kontoinhaber, bank, 3.0);
        
        try {
        	sparkonto.auszahlen(1500.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Kontostand zu niedrig", e.getMessage());
        }
        
        assertEquals(100.0, sparkonto.getKontostand(), 0.001);
        
        sparkonto.auszahlen(50.0);
    }
    
    @Test
    public void testBerechneZins() {
        Kontoinhaber kontoinhaber = new Kontoinhaber(123456, "Mustermann", "Max", "Bahnhofstraße 1", "68159", "Mannheim");
        Bank bank = new Bank("VR Bank Rhein-Neckar", "MA2424", null);
        Sparkonto sparkonto = new Sparkonto(4711, 1234, 100.0, kontoinhaber, bank, 3.0);
        
        sparkonto.berechneZins();
        
        assertEquals(103.0, sparkonto.getKontostand(), 0.001);
    }
}

