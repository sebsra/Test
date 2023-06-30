package com.geldautomat.view;

import javax.swing.*;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Color;

/**
/**
 * Diese Klasse repräsentiert die Ansicht für die Einzahlungsfunktion.
 * Sie enthält ein Dialogfenster, in dem der Benutzer einen Betrag eingeben kann, der eingezahlt werden soll.
 */
public class EinzahlenView {
    private double betrag;

    /**
     * Konstruktor für die EinzahlenView.
     * Erstellt ein Dialogfenster, in dem der Benutzer einen Betrag eingeben kann, der eingezahlt werden soll.
     *
     * @param parent Das übergeordnete Fenster, zu dem dieses Dialogfenster gehört.
     */
    public EinzahlenView(JFrame parent) {
        betrag = -1;
        while (betrag == -1) {
            JPanel panel = new JPanel();
            JTextField betragField = new JTextField(10);
            panel.add(new JLabel("Betrag:"));
            panel.add(betragField);
            panel.add(new JLabel("€"));

            Object[] options = { "Abbrechen", "Einzahlen" };
            JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION, null, options, options[1]);

            //benutzerdefinierte Button-Konfiguration erstellen
            UIManager.put("Button.background", Color.WHITE);
            UIManager.put("Button.foreground", Color.BLACK);
            UIManager.put("Button.font", new Font("Arial", Font.BOLD, 12));

            // benutzerdefiniertes Button-Array dem JOptionPane übergeben
            optionPane.setOptions(options);

            JDialog dialog = optionPane.createDialog(parent, "Einzahlen");
            
            //Fokus auf Betragsfeld um direkt lostippen zu können
            dialog.addWindowFocusListener(new WindowAdapter() {
                public void windowGainedFocus(WindowEvent e) {
                    betragField.requestFocusInWindow();
                }
            });
            dialog.setVisible(true);

            if (optionPane.getValue() == options[1]) {
                try {
                    betrag = Double.parseDouble(betragField.getText());
                    if (betrag <= 0) {
                        throw new NumberFormatException();
                    }
                    int confirmResult = JOptionPane.showConfirmDialog(parent, "Sicher " + betrag + "€ einzahlen?",
                            "Bestätigung", JOptionPane.YES_NO_OPTION);
                    if (confirmResult == JOptionPane.YES_OPTION) {
                        break;
                    } else {
                        betrag = -1;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(parent, "Ungültiger Betrag", "Fehler", JOptionPane.ERROR_MESSAGE);
                    betrag = -1;
                }
            } else {
                break;
            }
        }
    }

    /**
     * Gibt den vom Benutzer eingegebenen Betrag zurück.
     *
     * @return Der vom Benutzer eingegebene Betrag.
     */
    public double getBetrag() {
        return betrag;
    }
}
