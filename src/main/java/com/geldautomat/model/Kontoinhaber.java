package com.geldautomat.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse repräsentiert einen Kontoinhaber.
 * Sie enthält Informationen über den Kontoinhaber und verwaltet eine Liste von Konten.
 */
public class Kontoinhaber {
    private int kundennummer;
    private String name;
    private String vorname;
    private String plz;
    private String ort;
    private String strasse;
    private List<Konto> konten;

    /**
     * Konstruktor für einen Kontoinhaber.
     *
     * @param kundennummer Die Kundennummer des Kontoinhabers.
     * @param vorname Der Vorname des Kontoinhabers.
     * @param name Der Nachname des Kontoinhabers.
     * @param strasse Die Straße, in der der Kontoinhaber wohnt.
     * @param plz Die Postleitzahl des Wohnorts des Kontoinhabers.
     * @param ort Der Wohnort des Kontoinhabers.
     */
    public Kontoinhaber(int kundennummer, String vorname, String name, String strasse, String plz, String ort) {
        this.kundennummer = kundennummer;
        this.name = name;
        this.vorname = vorname;
        this.plz = plz;
        this.ort = ort;
        this.strasse = strasse;
        this.konten = new ArrayList<>();
    }
    
    public int getKundennummer() {
        return kundennummer;
    }

    public void setKundennummer(int kontonummer) {
        this.kundennummer = kontonummer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }
    
    public List<Konto> getKonten() {
        return konten;
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
}

