package com.geldautomat.controller;

import com.geldautomat.model.*;
import com.geldautomat.view.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


import javax.swing.JOptionPane;

/**
 * Diese Klasse stellt den Controller für die Login-Funktion dar.
 * Sie verarbeitet Benutzereingaben aus der Login-Ansicht und startet bei übereinstimmung eine KontoView.
 */
public class LoginController {
    private LoginView view;
    private BankManagementSystem managementSystem;

    /**
     * Konstruktor für die LoginController Klasse.
     * Initialisiert den Controller mit der gegebenen Ansicht und dem Bankverwaltungssystem.
     * Erhöhter Komfort für Benutzereingaben durch automtisches Wechseln des Fokus bei Entertaste.
     *
     * @param view Die Login-Ansicht.
     * @param managementSystem Das Bankverwaltungssystem.
     */
    public LoginController(LoginView view, BankManagementSystem managementSystem) {
        this.view = view;
        this.managementSystem = managementSystem;
        
        view.getBlzField().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                	view.getKontonummerField().requestFocusInWindow();
                }
            }
        });
        view.getKontonummerField().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                	view.getPasswortField().requestFocusInWindow();
                }
            }
        });
        view.getPasswortField().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                	view.getLoginButton().requestFocusInWindow();
                }
            }
        });
        view.getLoginButton().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                	view.getLoginButton().doClick();
                }
            }
        });
        //
        this.view.getLoginButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
    }

    /**
     * Führt den Login-Prozess durch, basierend auf den Eingaben des Benutzers in der Login-Ansicht.
     * Überprüft, ob die Bank existiert, ob das Konto existiert und ob das Passwort korrekt ist.
     * Leitet den Benutzer zur Kontenansicht weiter, wenn alle Bedingungen erfüllt sind.
     */
    private void login() {
        String blz = view.getBlzField().getText();
        String kontonummer = view.getKontonummerField().getText();
        String passwort = new String(view.getPasswortField().getPassword());


        if (blz.isEmpty() || kontonummer.isEmpty() || passwort.isEmpty()) {
            String fehlendeFelder = "";
            if (blz.isEmpty()) {
                fehlendeFelder += "BLZ ";
            }
            if (kontonummer.isEmpty()) {
                fehlendeFelder += "Kontonummer ";
            }
            if (passwort.isEmpty()) {
                fehlendeFelder += "Passwort ";
            }
            String fehlermeldung = "Bitte alle Felder ausfüllen. Fehlend:" + fehlendeFelder;
            JOptionPane.showMessageDialog(view, fehlermeldung, "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Existiert Bank?
        Bank bank = managementSystem.getBankByBLZ(blz);
        if (bank == null) {
            JOptionPane.showMessageDialog(view, "Bank nicht gefunden.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Existiert Account?
        Konto konto = bank.getKontoByKontonummer(Integer.parseInt(kontonummer));
        if (konto == null) {
            JOptionPane.showMessageDialog(view, "Konto nicht gefunden.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Passwort korrekt?
        if (!konto.checkPassword(Integer.parseInt(passwort))) {
            JOptionPane.showMessageDialog(view, "Falsches Passwort.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        new KontoController(new KontoView(), konto, managementSystem);
        view.dispose();
    }
}
