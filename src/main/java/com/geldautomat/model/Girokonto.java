package com.geldautomat.model;

/**
 * Diese Klasse repräsentiert ein Girokonto.
 * Sie erbt von der abstrakten Klasse Konto und fügt einen Überziehungsbetrag hinzu.
 */

/**
 * Konstruktor für ein Girokonto.
 *
 * @param kontonummer Die Kontonummer des Kontos.
 * @param pin Die PIN des Kontos.
 * @param kontostand Der aktuelle Kontostand.
 * @param kontoinhaber Der Inhaber des Kontos.
 * @param bank Die Bank, bei der das Konto geführt wird.
 * @param ueberziehungsbetrag Der maximale Betrag, um den das Konto überzogen werden kann.
 */
public class Girokonto extends Konto {
    private double ueberziehungsbetrag;

    public Girokonto(int kontonummer, int pin, double kontostand, Kontoinhaber kontoinhaber, Bank bank, double ueberziehungsbetrag) {
        super(kontonummer, pin, kontostand, kontoinhaber, bank);
        this.ueberziehungsbetrag = ueberziehungsbetrag;
    }

    public double getUeberziehungsbetrag() {
        return ueberziehungsbetrag;
    }

    public void setUeberziehungsbetrag(double ueberziehungsbetrag) {
        this.ueberziehungsbetrag = ueberziehungsbetrag;
    }

    /**
     * Zahlt einen Betrag vom Konto aus.
     * Wenn der Betrag größer ist als der Kontostand plus der Überziehungsbetrag (mehr überzogen werden würde als erlaubt) wird eine Ausnahme ausgelöst.
     *
     * @param betrag Der auszuzahlende Betrag.
     * @throws IllegalArgumentException wenn der Betrag größer ist als der Kontostand plus der Überziehungsbetrag.
     */
    @Override
    public void auszahlen(double betrag) {
        if (betrag <= getKontostand() + ueberziehungsbetrag) {
            setKontostand(getKontostand() - betrag);
            setKontostand(Math.round(getKontostand()*100000)/100000.0);
        } else {
        	throw new IllegalArgumentException("Kontostand zu niedrig");
        }

    }    

}

