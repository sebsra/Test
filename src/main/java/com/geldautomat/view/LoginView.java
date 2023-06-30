package com.geldautomat.view;

import javax.swing.*;
import java.awt.*;

/**
 * Diese Klasse stellt die Benutzeroberfläche für die Login-Funktion dar.
 * Sie enthält Textfelder für die BLZ, Kontonummer und das Passwort,
 * sowie einen Button zum Durchführen des Logins.
 * 
 */
public class LoginView extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel panel;
    private JLabel blzLabel, kontonummerLabel, passwortLabel;
    private JTextField blzField, kontonummerField;
    private JPasswordField passwortField;
    private JButton loginButton;
    
    /**
     * Konstruktor für die LoginView Klasse.
     * Initialisiert die Benutzeroberfläche.
     * Erlaubt bei BLZ nur alphanumerische Werte und bei Kontonummer nur Zahlen.
     * Erhöhterkomfort der Benutzereingaben durch automtisches Wechseln des Fokus bei Entertaste.
     */
    public LoginView() {
        initializeUI();
    }
    
    /**
     * Initialisiert die Benutzeroberfläche, indem sie die benötigten UI-Komponenten erstellt und konfiguriert.
     */
    private void initializeUI() {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(null);

        blzLabel = new JLabel("BLZ");
        blzLabel.setBounds(0, 20, 100, 20);
        blzLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(blzLabel);
        blzField = new CustomTextFields().BlzField();
        blzField.setBounds(120, 20, 150, 20);
        panel.add(blzField);

        kontonummerLabel = new JLabel("Kontonummer");
        kontonummerLabel.setBounds(0, 50, 100, 20);
        kontonummerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(kontonummerLabel);
        kontonummerField = new CustomTextFields().KontonummerField();
        kontonummerField.setBounds(120, 50, 150, 20);
        panel.add(kontonummerField);


        passwortLabel = new JLabel("Passwort");
        passwortLabel.setBounds(0, 80, 100, 20);
        passwortLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(passwortLabel);
        passwortField = new JPasswordField();
        passwortField.setBounds(120, 80, 150, 20);
        panel.add(passwortField);

        
        loginButton = new JButton("Login");
        loginButton.setBounds(100, 120, 100, 20);
        Font buttonFont = new Font("Arial", Font.BOLD, 12);
        Color buttonColor = Color.WHITE;
        loginButton.setFont(buttonFont);
        loginButton.setBackground(buttonColor);
        panel.add(loginButton);

        add(panel);
        setVisible(true);
    }

    // Getter für die Eingabefelder und den Login-Button
    public JTextField getBlzField() {
        return blzField;
    }

    public JTextField getKontonummerField() {
        return kontonummerField;
    }

    public JPasswordField getPasswortField() {
        return passwortField;
    }

    public JButton getLoginButton() {
        return loginButton;
    }
}
