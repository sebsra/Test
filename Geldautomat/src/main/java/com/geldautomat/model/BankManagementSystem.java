package com.geldautomat.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse repräsentiert das Bank Management System.
 * Es verwaltet eine Liste von Banken.
 */
public class BankManagementSystem {
    /**
     * Liste der Banken im Management System.
     */
    public List<Bank> banks;

    /**
     * Konstruktor für das Bank Management System.
     * Initialisiert die Liste der Banken.
     */
    public BankManagementSystem() {
        this.banks = new ArrayList<>();
    }

    /**
     * Fügt eine Bank zur Liste der Banken hinzu.
     *
     * @param bank Die hinzuzufügende Bank.
     */
    public void addBank(Bank bank) {
        banks.add(bank);
    }

    /**
     * Entfernt eine Bank aus der Liste der Banken.
     *
     * @param bank Die zu entfernende Bank.
     */
    public void removeBank(Bank bank) {
        banks.remove(bank);
    }

    /**
     * Gibt die Liste der Banken zurück.
     *
     * @return Die Liste der Banken.
     */
    public List<Bank> getBanks() {
        return banks;
    }

    /**
     * Gibt eine Bank mit einer bestimmten Bankleitzahl (BLZ) zurück.
     *
     * @param blz Die Bankleitzahl der gesuchten Bank.
     * @return Die Bank mit der angegebenen BLZ oder null, wenn keine solche Bank gefunden wurde.
     */
    public Bank getBankByBLZ(String blz) {
        for (Bank bank : this.banks) {
            if (bank.getBlz().equals(blz)) {
                return bank;
            }
        }
        return null;
    }
}
