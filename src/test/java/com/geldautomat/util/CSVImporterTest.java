package com.geldautomat.util;

import com.geldautomat.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class CSVImporterTest {
    @Test
    public void testLoadFromCSV() throws IOException {
        String filepath = "src/test/resources/BankCSV.csv";
        BankManagementSystem bankManagementSystem = CSVImporter.loadFromCSV(filepath);

        Assert.assertEquals(3, bankManagementSystem.getBanks().size());

        // Überprüfe Bank 1
        Bank bank1 = bankManagementSystem.getBanks().get(0);
        Assert.assertEquals(2, bank1.getKonten().size());
        Assert.assertEquals("VR Bank Rhein-Neckar", bank1.getName());
        Assert.assertEquals("MA2424", bank1.getBlz());

        // Überprüfe Girokonto 1
        Girokonto girokonto1 = null;
        for (Konto konto : bank1.getKonten()) {
            if (konto instanceof Girokonto) {
                girokonto1 = (Girokonto) konto;
                break;
            }
        }
        Assert.assertEquals(8321, girokonto1.getKontonummer());
        Assert.assertEquals(1234, girokonto1.getPin());
        Assert.assertEquals(500.14, girokonto1.getKontostand(), 0.001);
        Assert.assertEquals("Mustermann", girokonto1.getKontoinhaber().getName());
        Assert.assertEquals("Max", girokonto1.getKontoinhaber().getVorname());
        Assert.assertEquals("VR Bank Rhein-Neckar", girokonto1.getBank().getName());
        Assert.assertEquals(1000.0, girokonto1.getUeberziehungsbetrag(), 0.001);

        // Überprüfe Sparkonto 1
        Sparkonto sparkonto1 = null;
        for (Konto konto : bank1.getKonten()) {
            if (konto instanceof Sparkonto) {
                sparkonto1 = (Sparkonto) konto;
                break;
            }
        }
        Assert.assertEquals(4711, sparkonto1.getKontonummer());
        Assert.assertEquals(1234, sparkonto1.getPin());
        Assert.assertEquals(50.14, sparkonto1.getKontostand(), 0.001);
        Assert.assertEquals("Mustermann", sparkonto1.getKontoinhaber().getName());
        Assert.assertEquals("Max", sparkonto1.getKontoinhaber().getVorname());
        Assert.assertEquals("VR Bank Rhein-Neckar", sparkonto1.getBank().getName());
        Assert.assertEquals(0.03, sparkonto1.getZins(), 0.001);

        // Überprüfe Bank 2
        Bank bank2 = bankManagementSystem.getBanks().get(1);
        Assert.assertEquals(2, bank2.getKonten().size());
        Assert.assertEquals("Berliner Bank", bank2.getName());
        Assert.assertEquals("19087", bank2.getBlz());

        // Überprüfe Sparkonto 2
        Sparkonto sparkonto2 = null;
        for (Konto konto : bank2.getKonten()) {
            if (konto instanceof Sparkonto) {
                sparkonto2 = (Sparkonto) konto;
                break;
            }
        }
        Assert.assertEquals(1717, sparkonto2.getKontonummer());
        Assert.assertEquals(1234, sparkonto2.getPin());
        Assert.assertEquals(500.14, sparkonto2.getKontostand(), 0.001);
        Assert.assertEquals("Mustermann", sparkonto2.getKontoinhaber().getName());
        Assert.assertEquals("Max", sparkonto2.getKontoinhaber().getVorname());
        Assert.assertEquals("Berliner Bank", sparkonto2.getBank().getName());
        Assert.assertEquals(0.010, sparkonto2.getZins(), 0.001);

        // Überprüfe Sparkonto 3
        Sparkonto sparkonto3 = null;
        for (Konto konto : bank2.getKonten()) {
            if (konto instanceof Sparkonto && konto != sparkonto2) {
                sparkonto3 = (Sparkonto) konto;
                break;
            }
        }
        Assert.assertEquals(1919, sparkonto3.getKontonummer());
        Assert.assertEquals(1234, sparkonto3.getPin());
        Assert.assertEquals(540.2, sparkonto3.getKontostand(), 0.001);
        Assert.assertEquals("Mustermann", sparkonto3.getKontoinhaber().getName());
        Assert.assertEquals("Max", sparkonto3.getKontoinhaber().getVorname());
        Assert.assertEquals("Berliner Bank", sparkonto3.getBank().getName());
        Assert.assertEquals(0.011, sparkonto3.getZins(), 0.001);

        // Überprüfe Bank 3
        Bank bank3 = bankManagementSystem.getBanks().get(2);
        Assert.assertEquals(1, bank3.getKonten().size());
        Assert.assertEquals("Zocker Bank", bank3.getName());
        Assert.assertEquals("Zock7777", bank3.getBlz());

        // Überprüfe Girokonto 2
        Girokonto girokonto2 = null;
        for (Konto konto : bank3.getKonten()) {
            if (konto instanceof Girokonto) {
                girokonto2 = (Girokonto) konto;
                break;
            }
        }
        Assert.assertEquals(4444, girokonto2.getKontonummer());
        Assert.assertEquals(7777, girokonto2.getPin());
        Assert.assertEquals(-8000.5, girokonto2.getKontostand(), 0.001);
        Assert.assertEquals("Hogo", girokonto2.getKontoinhaber().getName());
        Assert.assertEquals("Gruber", girokonto2.getKontoinhaber().getVorname());
        Assert.assertEquals("Zocker Bank", girokonto2.getBank().getName());
        Assert.assertEquals(50000.5, girokonto2.getUeberziehungsbetrag(), 0.001);
    }
}
