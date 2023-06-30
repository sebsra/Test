package com.geldautomat.model;

/**
 * Diese Klasse repräsentiert ein Sparkonto.
 * Sie erbt von der abstrakten Klasse Konto und fügt einen Zinssatz hinzu.
 */
public class Sparkonto extends Konto {
    private double zins;

    /**
     * Konstruktor für ein Sparkonto.
     *
     * @param kontonummer Die Kontonummer des Kontos.
     * @param pin Die PIN des Kontos.
     * @param kontostand Der aktuelle Kontostand.
     * @param kontoinhaber Der Inhaber des Kontos.
     * @param bank Die Bank, bei der das Konto geführt wird.
     * @param zins Der Zinssatz des Kontos.
     */
    public Sparkonto(int kontonummer, int pin, double kontostand, Kontoinhaber kontoinhaber, Bank bank, double zins) {
        super(kontonummer, pin, kontostand, kontoinhaber, bank);
        this.zins = zins;
    }

    public double getZins() {
        return zins;
    }

    public void setZins(double zins) {
        this.zins = zins;
    }

    /**
     * Berechnet die Zinsen auf dem Konto und zahlt sie auf das Konto ein. 
     */
    public void berechneZins() {
        double kontostand = getKontostand();
        double zinsbetrag = kontostand * (zins / 100);
        super.einzahlen(zinsbetrag);
    }
    
    /**
     * Zahlt einen Betrag vom Konto aus.
     * Wenn der Betrag größer ist als der Kontostand, wird eine Ausnahme ausgelöst.
     *
     * @param betrag Der auszuzahlende Betrag.
     * @throws IllegalArgumentException wenn der Betrag größer ist als der Kontostand.
     */
    @Override
    public void auszahlen(double betrag) {
        if (betrag <= getKontostand()) {
            setKontostand(getKontostand() - betrag);
            setKontostand(Math.round(getKontostand()*100000)/100000.0);
        } else {
        	throw new IllegalArgumentException("Kontostand zu niedrig");
        }

    }
    
}

