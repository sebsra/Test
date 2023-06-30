package com.geldautomat.view;

import com.geldautomat.model.*;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

import javax.swing.*;

/**
 * Diese Klasse repräsentiert die Ansicht für die Kontoverwaltung.
 * Sie enthält ein Fenster, in dem der Benutzer Informationen zu seinem Konto anzeigen und verschiedene Aktionen ausführen kann.
 */
public class KontoView extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JLabel kontonummerLabel, kontonummer, nameLabel, name, adresseLabel, adresse, bankLabel, bank, blzLabel,
			blz, kontotypLabel, kontotyp, kontostandLabel, kontostand, zinsOderUeberziehungLabel, zinsOderUeberziehung;
	private JComboBox<Konto> kontoDropDown;
	private JButton logoutButton, ueberweisenButton, einzahlenButton, auszahlenButton;
	
    /**
     * Konstruktor für die KontoView.
     * Erstellt das Fenster und initialisiert die UI-Komponenten.
     */
	public KontoView() {
		initializeUI();
	}
	
    /**
     * Initialisiert die UI-Komponenten.
     */
	private void initializeUI() {
		setTitle("Konto");
		setSize(600, 350);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		int spalte1 = 30;
		int spalte2 = 180;

		panel = new JPanel();
		panel.setLayout(null);

		kontonummerLabel = new JLabel("Kontonummer");
		kontonummerLabel.setBounds(spalte1, 10, 200, 20);
		panel.add(kontonummerLabel);
		kontonummer = new JLabel();
		kontonummer.setBounds(spalte2, kontonummerLabel.getY(), 200, 20);
		panel.add(kontonummer);

		nameLabel = new JLabel("Vorname / Name");
		nameLabel.setBounds(spalte1, kontonummerLabel.getY() + 30, 200, 20);
		panel.add(nameLabel);
		name = new JLabel();
		name.setBounds(spalte2, nameLabel.getY(), 350, 20);
		panel.add(name);

		adresseLabel = new JLabel("Adresse");
		adresseLabel.setBounds(spalte1, nameLabel.getY() + 30, 200, 20);
		panel.add(adresseLabel);
		adresse = new JLabel();
		adresse.setBounds(spalte2, adresseLabel.getY(), 350, 20);
		panel.add(adresse);

		bankLabel = new JLabel("Bank");
		bankLabel.setBounds(spalte1, adresseLabel.getY() + 30, 200, 20);
		panel.add(bankLabel);
		bank = new JLabel();
		bank.setBounds(spalte2, bankLabel.getY(), 350, 20);
		panel.add(bank);

		blzLabel = new JLabel("BLZ");
		blzLabel.setBounds(spalte1, bankLabel.getY() + 30, 200, 20);
		panel.add(blzLabel);
		blz = new JLabel();
		blz.setBounds(spalte2, blzLabel.getY(), 350, 20);
		panel.add(blz);

		blzLabel = new JLabel("BLZ");
		blzLabel.setBounds(spalte1, bankLabel.getY() + 30, 200, 20);
		panel.add(blzLabel);
		blz = new JLabel();
		blz.setBounds(spalte2, blzLabel.getY(), 350, 20);
		panel.add(blz);

		kontotypLabel = new JLabel("Konto");
		kontotypLabel.setBounds(spalte1, blzLabel.getY() + 30, 200, 20);
		panel.add(kontotypLabel);
		kontotyp = new JLabel();
		kontotyp.setBounds(spalte2, kontotypLabel.getY(), 350, 20);
		panel.add(kontotyp);

		zinsOderUeberziehungLabel = new JLabel();
		zinsOderUeberziehungLabel.setBounds(spalte1, kontotypLabel.getY() + 30, 200, 20);
		panel.add(zinsOderUeberziehungLabel);
		zinsOderUeberziehung = new JLabel();
		zinsOderUeberziehung.setBounds(spalte2, zinsOderUeberziehungLabel.getY(), 350, 20);
		panel.add(zinsOderUeberziehung);

		kontostandLabel = new JLabel("Kontostand");
		kontostandLabel.setBounds(spalte1, zinsOderUeberziehungLabel.getY() + 30, 200, 20);
		panel.add(kontostandLabel);
		kontostand = new JLabel();
		kontostand.setBounds(spalte2, kontostandLabel.getY(), 350, 20);
		panel.add(kontostand);

		logoutButton = new JButton("Logout");
		logoutButton.setBounds(10, getHeight() - 70, 80, 20);
		panel.add(logoutButton);

		ueberweisenButton = new JButton("Überweisen");
		ueberweisenButton.setBounds(logoutButton.getX() + 230, logoutButton.getY(), 105, 20);
		panel.add(ueberweisenButton);

		einzahlenButton = new JButton("Einzahlen");
		einzahlenButton.setBounds(ueberweisenButton.getX() + 115, logoutButton.getY(), 105, 20);
		panel.add(einzahlenButton);

		auszahlenButton = new JButton("Auszahlen");
		auszahlenButton.setBounds(einzahlenButton.getX() + 115, logoutButton.getY(), 105, 20);
		panel.add(auszahlenButton);

	    Font buttonFont = new Font("Arial", Font.BOLD, 12);
	    Color buttonColor = Color.WHITE;
	    for (Component component : panel.getComponents()) {
	        if (component instanceof JButton) {
	            JButton button = (JButton) component;
	            button.setFont(buttonFont);
	            button.setBackground(buttonColor);
	        }
	    }
	    
		kontoDropDown = new JComboBox<>();
		kontoDropDown.setBounds(auszahlenButton.getX()-60, 10, 160, 20);
		kontoDropDown.setFont(buttonFont);
		kontoDropDown.setBackground(buttonColor);
		kontoDropDown.setEnabled(true);
		panel.add(kontoDropDown);
	    
		
		add(panel);
		setVisible(true);
	}
	
	
    /**
     * Aktualisiert die Anzeige mit den Informationen des angegebenen Kontos.
     *
     * @param konto Das Konto, dessen Informationen angezeigt werden sollen.
     */
	public void setKonto(Konto konto) {
		kontonummer.setText(String.valueOf(konto.getKontonummer()));
		Kontoinhaber kontoinhaber = konto.getKontoinhaber();
		name.setText(kontoinhaber.getVorname() + " " + kontoinhaber.getName());
		adresse.setText(kontoinhaber.getStrasse() + ", " + kontoinhaber.getPlz() + " " + kontoinhaber.getOrt());
		bank.setText(konto.getBank().getName());
		blz.setText(konto.getBank().getBlz());
		kontotyp.setText(konto instanceof Girokonto ? "Girokonto" : "Sparkonto");
		kontostand.setText(String.valueOf(konto.getKontostand()) + " €");

		if (konto instanceof Girokonto) {
			zinsOderUeberziehungLabel.setText("Überziehung:");
			zinsOderUeberziehung.setText(String.valueOf(((Girokonto) konto).getUeberziehungsbetrag())+ " €");
		} else if (konto instanceof Sparkonto) {
			zinsOderUeberziehungLabel.setText("Zins:");
			zinsOderUeberziehung.setText(String.valueOf(Math.round(((Sparkonto) konto).getZins()*100000)/1000.0)+"%");
		}
		kontoDropDown.removeAllItems();
		kontoDropDown.setRenderer(new DropdownKontenView());
		List<Konto> konten = kontoinhaber.getKonten();
		if (konten.size() == 1) {
		    kontoDropDown.setVisible(false);
		}
		konten.sort(Comparator.comparing(Konto::getKontonummer));
		for (Konto k : konten) {
			kontoDropDown.addItem(k);

		}
		kontoDropDown.setSelectedItem(konto);
	}

	public JComboBox<Konto> getKontoDropDown() {
	    return kontoDropDown;
	}

	public JButton getLogoutButton() {
	    return logoutButton;
	}

	public JButton getUeberweisenButton() {
	    return ueberweisenButton;
	}

	public JButton getEinzahlenButton() {
	    return einzahlenButton;
	}

	public JButton getAuszahlenButton() {
	    return auszahlenButton;
	}

}

