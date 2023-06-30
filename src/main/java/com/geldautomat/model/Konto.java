package com.geldautomat.model;

/**
 * Diese abstrakte Klasse repräsentiert ein Konto.
 * Sie enthält Informationen über das Konto und Methoden zur Kontoverwaltung.
 */
public abstract class Konto {
    private int kontonummer;
    private int pin;
    private double kontostand;
    private Kontoinhaber kontoinhaber;
    private Bank bank;

    /**
     * Konstruktor für ein Konto.
     *
     * @param kontonummer Die Kontonummer des Kontos.
     * @param pin Die PIN des Kontos.
     * @param kontostand Der aktuelle Kontostand.
     * @param kontoinhaber Der Inhaber des Kontos.
     * @param bank Die Bank, bei der das Konto geführt wird.
     */
    public Konto(int kontonummer, int pin, double kontostand, Kontoinhaber kontoinhaber, Bank bank) {
        this.kontonummer = kontonummer;
        this.pin = pin;
        this.kontostand = kontostand;
        this.kontoinhaber = kontoinhaber;
        this.bank = bank;
    }

    public int getKontonummer() {
        return kontonummer;
    }

    public void setKontonummer(int kontonummer) {
        this.kontonummer = kontonummer;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public double getKontostand() {
        return kontostand;
    }

    public void setKontostand(double kontostand) {
        this.kontostand = kontostand;
    }

    public Kontoinhaber getKontoinhaber() {
        return kontoinhaber;
    }

    public void setKontoinhaber(Kontoinhaber kontoinhaber) {
        this.kontoinhaber = kontoinhaber;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }
    
    /**
     * Überprüft, ob das Konto überzogen ist.
     *
     * @return true, wenn das Konto überzogen ist, sonst false.
     */
    public boolean istKontoüberzogen() {
        return getKontostand() < 0;
    }

    /**
     * Zahlt einen Betrag auf das Konto ein.
     *
     * @param betrag Der einzuzahlende Betrag.
     */
    public void einzahlen(double betrag) {
        kontostand += betrag;
        kontostand = Math.round(kontostand*100000)/100000.0;
    }

    /**
     * Zahlt einen Betrag vom Konto aus.
     * Diese Methode muss von den Unterklassen implementiert werden.
     *
     * @param betrag Der auszuzahlende Betrag.
     */
    public abstract void auszahlen(double betrag);

    /**
     * Überprüft, ob eine gegebene PIN mit der PIN des Kontos übereinstimmt.
     *
     * @param pin Die zu überprüfende PIN.
     * @return true, wenn die PIN übereinstimmt, sonst false.
     */
    public boolean checkPassword(int pin) {
        return pin == this.pin;
    }
}

