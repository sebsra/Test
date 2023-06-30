package com.geldautomat.util;

import com.geldautomat.model.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Diese Klasse ermöglicht das Laden von Daten aus einer CSV-Datei in ein BankManagementSystem.
 */
public class CSVImporter {

    /**
     * Lädt ein BankManagementSystem aus einer CSV-Datei.
     *
     * @param filepath Der Dateipfad zur CSV-Datei.
     * @return Das geladene BankManagementSystem.
     * @throws IOException Wenn beim Lesen der Datei ein Fehler auftritt.
     */
    public static BankManagementSystem loadFromCSV(String filepath) throws IOException {
    	BankManagementSystem ImportiertesSystem1 = new BankManagementSystem();

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                // Überspringe die Kopfzeile
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] values = line.split(";");
                if (values.length < 14) {
                    continue; 
                }
                
                String bankName = values[0];
                String blz = values[1];
                int kontonummer = Integer.parseInt(values[2]);
                int pin = Integer.parseInt(values[3]);
                double kontostand = Double.parseDouble(values[4].replaceAll("[€.�]", "").replace(",", ".").trim());
                kontostand = Math.round(kontostand * 100000) / 100000.0;
                String kontoart = values[5];
                String zinsString = values[6];
                String ueberziehungsbetragString = values[7];
                int kundennummer = Integer.parseInt(values[8]);
                String name = values[9];
                String vorname = values[10];
                String strasse = values[11].replace("�", "ß").trim();
                String plz = values[12];
                String ort = values[13];

                Bank bank = ImportiertesSystem1.getBankByBLZ(blz);
                if (bank == null) {
                    bank = new Bank(blz, bankName, ImportiertesSystem1);
                    ImportiertesSystem1.banks.add(bank);
                }

                Kontoinhaber kontoinhaber = bank.findKundenByKundennummer(kundennummer);
                if (kontoinhaber == null) {
                	kontoinhaber = new Kontoinhaber(kundennummer, vorname, name, strasse, plz, ort);
                }

                Konto konto = null;
                
                if ("Girokonto".equalsIgnoreCase(kontoart)) {
                	double ueberziehungsbetrag = Double.parseDouble(ueberziehungsbetragString.replaceAll("[€.�]", "").replace(",", ".").trim());
                    konto = new Girokonto(kontonummer, pin, kontostand, kontoinhaber, bank, ueberziehungsbetrag);
                } else if ("Sparkonto".equalsIgnoreCase(kontoart)) {
                	double zins = Double.parseDouble(zinsString.replaceAll("[%.�]", "").replace(",", ".").trim())/100.0;
                	zins = Math.round(zins * 100000) / 100000.0;
                    konto = new Sparkonto(kontonummer, pin, kontostand, kontoinhaber, bank, zins);
                }
                
                

                kontoinhaber.addKonto(konto);

                if (!bank.getKonten().contains(konto)) {
                    bank.addKonto(konto);
                }

                konto.setBank(bank);
            }
        }

        return ImportiertesSystem1;
    }
}
