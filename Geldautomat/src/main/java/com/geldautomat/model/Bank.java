package com.geldautomat.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse repräsentiert eine Bank.
 * Sie enthält Informationen über die Bank und verwaltet eine Liste von Konten.
 */
public class Bank {
    private String blz;
    private String name;
    private BankManagementSystem managementSystem;
    private List<Konto> konten;

    /**
     * Konstruktor für die Bank.
     *
     * @param blz Die Bankleitzahl der Bank.
     * @param name Der Name der Bank.
     * @param managementSystem Das Bank Management System, zu dem die Bank gehört.
     */
    public Bank(String blz, String name, BankManagementSystem managementSystem) {
        this.blz = blz;
        this.name = name;
        this.managementSystem = managementSystem;
        this.konten = new ArrayList<>();
    }
    public String getBlz() {
        return blz;
    }

    public void setBlz(String blz) {
        this.blz = blz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public BankManagementSystem getBankManagementSystem() {
        return managementSystem;
    }
    
    /**
     * Fügt ein Konto zur Liste der Konten hinzu.
     *
     * @param konto Das hinzuzufügende Konto.
     */
    public void addKonto(Konto konto) {
        konten.add(konto);
    }
    
    /**
     * Entfernt ein Konto aus der Liste der Konten.
     *
     * @param konto Das zu entfernende Konto.
     */
    public void removeKonto(Konto konto) {
        konten.remove(konto);
    }

    /**
     * Gibt die Liste der Konten zurück.
     *
     * @return Die Liste der Konten.
     */
    public List<Konto> getKonten() {
        return konten;
    }

    /**
     * Sucht einen Kunden anhand der Kundennummer.
     *
     * @param Kundennummer Die Kundennummer des gesuchten Kunden.
     * @return Der Kunde mit der angegebenen Kundennummer oder null, wenn kein solcher Kunde gefunden wurde.
     */
    public Kontoinhaber findKundenByKundennummer(int Kundennummer) {
    	for (Konto k : this.konten) {
    		if (k.getKontoinhaber().getKundennummer() == Kundennummer) {
    			return k.getKontoinhaber();
    			}
        }
        return null;
    }
    
    /**
     * Gibt ein Konto anhand der Kontonummer zurück.
     *
     * @param Kontonummer Die Kontonummer des gesuchten Kontos.
     * @return Das Konto mit der angegebenen Kontonummer oder null, wenn kein solches Konto gefunden wurde.
     */
    public Konto getKontoByKontonummer(int Kontonummer) {
    	for (Konto k : this.konten) {
    		if (k.getKontonummer() == Kontonummer) {
    			return k;
    			}
        }
        return null;
    }
}

