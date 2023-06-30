package com.geldautomat.controller;

import com.geldautomat.model.*;
import com.geldautomat.view.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

/**
 * Diese Klasse stellt den Controller für die Überweisungsfunktion dar.
 * Sie verarbeitet Benutzereingaben aus der Überweisungsansicht und führt entsprechende Aktionen im Modell aus.
 */
public class UeberweisenController {
    private UeberweisenView view;
    private Konto senderKonto;
    KontoView kontoview; // Für View Update
    private BankManagementSystem managementSystem;

    /**
     * Konstruktor für die UeberweisenController Klasse.
     * Initialisiert den Controller mit der gegebenen Ansicht, dem Senderkonto und dem Bankverwaltungssystem.
     * Erhöhter Komfort für Benutzereingaben durch automtisches Wechseln des Fokus bei Entertaste.
     *
     * @param view Die Überweisungsansicht.
     * @param senderKonto Das Konto, von dem die Überweisung ausgeht.
     * @param bankManagementSystem Das Bankverwaltungssystem.
     */
    public UeberweisenController(UeberweisenView view, Konto senderKonto, KontoView kontoview, BankManagementSystem bankManagementSystem) {
        this.view = view;
        this.senderKonto = senderKonto;
        this.managementSystem = bankManagementSystem;
        this.kontoview = kontoview;
        
        view.getBlzTextField().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                	view.getKontonummerTextField().requestFocusInWindow();
                }
            }
        });
        view.getKontonummerTextField().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                	view.getBetragTextField().requestFocusInWindow();
                }
            }
        });
        view.getBetragTextField().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                	view.getUeberweisenButton().requestFocusInWindow();
                }
            }
        });
        view.getUeberweisenButton().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                	view.getUeberweisenButton().doClick();
                }
            }
        });        
        //
        this.view.getUeberweisenButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ueberweisen();
            }
        });

        this.view.getAbbrechenButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        });
    }

    /**
     * Führt eine Überweisung durch, basierend auf den Eingaben des Benutzers in der Überweisungsansicht.
     * Überprüft, ob das Empfängerkonto existiert und ob der Sender genug Geld auf dem Konto hat.
     * Fragt bei Girokontos, ob überzogen werden soll.
     * Führt die Überweisung durch, wenn alle Bedingungen erfüllt sind.
     */
    private void ueberweisen() {
        String blz = view.getBlzTextField().getText();
        String kontonummer = view.getKontonummerTextField().getText();
        double betrag = Double.parseDouble(view.getBetragTextField().getText());
        
        Bank empfaengerBank = managementSystem.getBankByBLZ(blz);
        Konto empfaengerKonto = null;
        if (empfaengerBank != null) { empfaengerKonto = empfaengerBank.getKontoByKontonummer(Integer.parseInt(kontonummer));}

        if (empfaengerBank == null || empfaengerKonto == null) {
            // Das Empfängerkonto existiert nicht
            JOptionPane.showMessageDialog(view, "Das Empfängerkonto existiert nicht.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (senderKonto.getKontostand() < betrag) {
            // Der Sender hat nicht genug Geld auf dem Konto
            if (senderKonto instanceof Girokonto && ((Girokonto) senderKonto).getUeberziehungsbetrag() >= betrag - senderKonto.getKontostand()) {
                // Der Sender kann das Konto überziehen
                int confirmResult = JOptionPane.showConfirmDialog(view, "Möchten Sie das Konto überziehen?", "Bestätigung", JOptionPane.YES_NO_OPTION);
                if (confirmResult != JOptionPane.YES_OPTION) {
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(view, "Sie haben nicht genug Geld auf dem Konto.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Führe die Überweisung durch
        senderKonto.setKontostand(senderKonto.getKontostand() - betrag);
        senderKonto.setKontostand(Math.round(senderKonto.getKontostand()*100000)/100000.0);
        empfaengerKonto.setKontostand(empfaengerKonto.getKontostand() + betrag);
        empfaengerKonto.setKontostand(Math.round(empfaengerKonto.getKontostand()*100000)/100000.0);

        JOptionPane.showMessageDialog(view, "Die Überweisung wurde erfolgreich durchgeführt.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
        view.dispose();
        kontoview.setKonto(senderKonto);
        
    }
}
