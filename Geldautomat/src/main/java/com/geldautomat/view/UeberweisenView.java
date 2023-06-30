package com.geldautomat.view;

import java.awt.Color;
import java.awt.Font;
import javax.swing.*;

/**
 * Diese Klasse stellt die Benutzeroberfläche für die Überweisungsfunktion dar.
 * Sie enthält Textfelder für die BLZ, Kontonummer und den Betrag der Überweisung,
 * sowie Buttons zum Durchführen der Überweisung oder zum Abbrechen der Aktion.
 */

public class UeberweisenView extends JFrame {
	private static final long serialVersionUID = 1L;
    private JPanel panel;
    private JLabel anLabel, blzLabel, kontonummerLabel, betragLabel;
    private JTextField blzTextField, kontonummerTextField, betragTextField;
    private JButton ueberweisenButton, abbrechenButton;

    /**
     * Konstruktor für die UeberweisenView Klasse.
     * Initialisiert die Benutzeroberfläche.
     */
    public UeberweisenView() {
        initializeUI();
    }
    /**
     * Initialisiert die Benutzeroberfläche, indem sie die benötigten UI-Komponenten erstellt und konfiguriert.
     */
    private void initializeUI() {
        setTitle("Überweisen");
        setSize(625, 190);
		setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(null);

        anLabel = new JLabel("An:");
        anLabel.setBounds(20, 10, 50, 20);
        anLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        anLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(anLabel);

        blzLabel = new JLabel("BLZ:");
        blzLabel.setBounds(20, 40, 50, 20);
        blzLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(blzLabel);

        blzTextField = new CustomTextFields().BlzField();
        blzTextField.setBounds(blzLabel.getWidth()+blzLabel.getX()+20, blzLabel.getY(), 180, 20);
        panel.add(blzTextField);

        kontonummerLabel = new JLabel("Kontonummer:");
        kontonummerLabel.setBounds(blzTextField.getWidth()+blzTextField.getX()+20, blzTextField.getY(), 100, 20);
        kontonummerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(kontonummerLabel);

        kontonummerTextField = new CustomTextFields().KontonummerField();
        kontonummerTextField.setBounds(kontonummerLabel.getWidth()+kontonummerLabel.getX()+20, kontonummerLabel.getY(), 180, 20);
        panel.add(kontonummerTextField);

        betragLabel = new JLabel("Betrag:");
        betragLabel.setBounds(blzLabel.getX(), 70, blzLabel.getWidth(), 20);
        betragLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(betragLabel);

        betragTextField = new JTextField();
        betragTextField.setBounds(betragLabel.getWidth()+betragLabel.getX()+20, betragLabel.getY(), 180, 20);
        panel.add(betragTextField);

        JLabel euroLabel = new JLabel("€");
        euroLabel.setBounds(betragTextField.getWidth()+betragTextField.getX()+10, betragTextField.getY(), 20, 20);
        panel.add(euroLabel);

        Font buttonFont = new Font("Arial", Font.BOLD, 12);
        Color buttonColor = Color.WHITE;
        
        ueberweisenButton = new JButton("Überweisen");
        ueberweisenButton.setBounds(kontonummerTextField.getWidth()+kontonummerTextField.getX()-120, 120, 120, 20);
        panel.add(ueberweisenButton);
        ueberweisenButton.setFont(buttonFont);
        ueberweisenButton.setBackground(buttonColor);


        abbrechenButton = new JButton("Abbrechen");
        abbrechenButton.setBounds(ueberweisenButton.getX()-120-15, 120, 120, 20);
        panel.add(abbrechenButton);
        abbrechenButton.setFont(buttonFont);
        abbrechenButton.setBackground(buttonColor);

        add(panel);
        setVisible(true);
    }
    
    public JTextField getBlzTextField() {
        return blzTextField;
    }

    public JTextField getKontonummerTextField() {
        return kontonummerTextField;
    }

    public JTextField getBetragTextField() {
        return betragTextField;
    }

    public JButton getUeberweisenButton() {
        return ueberweisenButton;
    }

    public JButton getAbbrechenButton() {
        return abbrechenButton;
    }

}
