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
        BankManagementSystem importiertesSystem = new BankManagementSystem();

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            boolean isFirstLine = true;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                // Überspringe die Kopfzeile
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                try {
                    String[] values = line.split(";");
                    if (values.length < 14) {
                        throw new IOException("Ungültiges Format in Zeile " + lineNumber);
                    }
                    // Parsen und Validierung der Werte
                    String bankName = values[0];
                    
                    String blz = values[1];
                    
                    int kontonummer = Integer.parseInt(values[2]);

                    int pin = Integer.parseInt(values[3]);

                    double kontostand = Double.parseDouble(values[4].replaceAll("[€.�]", "").replace(",", ".").trim());
                    
                    String kontoart = values[5];
                    
                    String zinsString = values[6];
                    
                    String ueberziehungsbetragString = values[7];
                    
                    int kundennummer = Integer.parseInt(values[8]);
                    
                    String name = values[9];
                    
                    String vorname = values[10];
                    
                    String strasse = values[11].replace("�", "ß").trim();
                    
                    String plz = values[12];
                    
                    String ort = values[13];

                    Bank bank = importiertesSystem.getBankByBLZ(blz);
                    if (bank == null) {
                        bank = new Bank(blz, bankName, importiertesSystem);
                        importiertesSystem.banks.add(bank);
                    }
                    else {
                    	if (!bank.getName().equals(bankName)) {
                    		    throw new IOException("Inkonsistente Banknamen für BLZ '" + blz + "'\nIst: " + bankName +  ", soll: " + bank.getName() + " \n(Zeile:" + lineNumber + ")");
                    		}
					}

                    Kontoinhaber kontoinhaber = bank.findKundenByKundennummer(kundennummer);
                    if (kontoinhaber == null) {
                        kontoinhaber = new Kontoinhaber(kundennummer, vorname, name, strasse, plz, ort);
                    } else {
                        if (!kontoinhaber.getName().equals(name) ||
                            !kontoinhaber.getVorname().equals(vorname) ||
                            !kontoinhaber.getStrasse().equals(strasse) ||
                            !kontoinhaber.getPlz().equals(plz) ||
                            !kontoinhaber.getOrt().equals(ort)) {

                            throw new IOException("Inkonsistente Kundendaten für Kundennummer: " + kundennummer +
                                "\nBank BLZ: " + bank.getBlz() +
                                "\nName: Ist: '" + kontoinhaber.getName() + "', Soll: '" + name +
                                "'\nVorname: Ist: '" + kontoinhaber.getVorname() + "', Soll: '" + vorname +
                                "'\nStraße: Ist: '" + kontoinhaber.getStrasse() + "', Soll: '" + strasse +
                                "'\nPLZ: Ist: '" + kontoinhaber.getPlz() + "', Soll: '" + plz +
                                "'\nOrt: Ist: '" + kontoinhaber.getOrt() + "', Soll: '" + ort +
                                "'\n(Zeile: " + lineNumber + ")");
                        }
                    }

                    
                    Konto konto = bank.getKontoByKontonummer(kontonummer);
                    if (konto == null) {
                        if ("Girokonto".equalsIgnoreCase(kontoart)) {
                            double ueberziehungsbetrag = Double.parseDouble(ueberziehungsbetragString.replaceAll("[€.�]", "").replace(",", ".").trim());
                            konto = new Girokonto(kontonummer, pin, kontostand, kontoinhaber, bank, ueberziehungsbetrag);
                        } else if ("Sparkonto".equalsIgnoreCase(kontoart)) {
                            double zins = Double.parseDouble(zinsString.replaceAll("[%.�]", "").replace(",", ".").trim()) / 100.0;
                            konto = new Sparkonto(kontonummer, pin, kontostand, kontoinhaber, bank, zins);
                        } else {
                            throw new IOException("Unbekannte Kontoart in Zeile " + lineNumber);
                        }
                        kontoinhaber.addKonto(konto);
                        bank.addKonto(konto);
                    }
                    else {
                    	throw new IOException("Gedoppelte Kontonummer in Zeile " + lineNumber + " \n(BLZ, Kontonummer: " + bank.getBlz() +", " + kontonummer + ")");
                    }


                } catch (NumberFormatException ex) {
                    throw new IOException("Fehler beim Parsen der Daten in Zeile " + lineNumber + " \n(Err: " + ex.getMessage()+")");
                }
            }
        }
        return importiertesSystem;
    }
}
