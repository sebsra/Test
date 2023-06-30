package com.geldautomat.controller;

import com.geldautomat.model.*;
import com.geldautomat.view.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

/**
 * Diese Klasse stellt den Controller für die Konto-Funktion dar.
 * Sie verarbeitet Benutzereingaben aus der Konto-Ansicht und führt entsprechende Aktionen im Modell aus.
 */
public class KontoController {
    private KontoView view;
    private Konto konto;
    private BankManagementSystem managementSystem;

    /**
     * Konstruktor für die KontoController Klasse.
     * Initialisiert den Controller mit der gegebenen Ansicht, dem Konto und dem Bankverwaltungssystem.
     *
     * @param kontoView Die Konto-Ansicht.
     * @param konto Das Konto.
     * @param managementSystem Das Bankverwaltungssystem des Kontos.
     */
    public KontoController(KontoView kontoView, Konto konto, BankManagementSystem managementSystem) {
        this.view = kontoView;
        this.konto = konto;
        this.managementSystem = managementSystem;
        kontoView.setKonto(konto);

        this.view.getLogoutButton().addActionListener(new LogoutListener());
        this.view.getEinzahlenButton().addActionListener(new EinzahlenListener());
        this.view.getAuszahlenButton().addActionListener(new AuszahlenListener());
        this.view.getUeberweisenButton().addActionListener(new UeberweisenListener());
        this.view.getKontoDropDown().addActionListener(new KontoDropDownListener());
    }
    
    

    //Innere Klassen, führen entsprechende Prozesse durch, wenn Nuttons gedrückt werden.


    class LogoutListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	view.dispose();
        	new LoginController(new LoginView(), managementSystem);
        }
    }

	class EinzahlenListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			EinzahlenView auszahlenView = new EinzahlenView(view);
			double betrag = auszahlenView.getBetrag();
			if (betrag == -1) {
				JOptionPane.showMessageDialog(view, "Einzahlung abgebrochen.", "Fehler", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			konto.einzahlen(betrag);
			JOptionPane.showMessageDialog(view, "Die Einzahlung wurde erfolgreich durchgeführt.", "Erfolg",
					JOptionPane.INFORMATION_MESSAGE);
			view.setKonto(konto); // Aktualisiert die Anzeige
		}
	}

	class AuszahlenListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			AuszahlenView auszahlenView = new AuszahlenView(view);
			double betrag = auszahlenView.getBetrag();
			if (betrag == -1) {
				JOptionPane.showMessageDialog(view, "Auszahlung abgebrochen.", "Fehler",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if (konto.getKontostand() < betrag) {
				if (konto instanceof Girokonto) {
					Girokonto girokonto = (Girokonto) konto;
					if (girokonto.getUeberziehungsbetrag() >= betrag - konto.getKontostand()) {
						int confirmResult = JOptionPane.showConfirmDialog(view, "Möchten Sie das Konto überziehen?",
								"Bestätigung", JOptionPane.YES_NO_OPTION);
						if (confirmResult != JOptionPane.YES_OPTION) {
							return;
						}
					}
				} else {
					JOptionPane.showMessageDialog(view, "Sie haben nicht genug Geld auf dem Konto.", "Fehler",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			konto.auszahlen(betrag);
			JOptionPane.showMessageDialog(view, "Die Auszahlung wurde erfolgreich durchgeführt.", "Erfolg",
					JOptionPane.INFORMATION_MESSAGE);
			view.setKonto(konto); // Aktualisiert die Anzeige
		}
	}

    

    class UeberweisenListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	UeberweisenView ueberweisenView = new UeberweisenView();
            new UeberweisenController(ueberweisenView, konto, view, managementSystem);
        }
    }

    class KontoDropDownListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Konto selectedKonto = (Konto) view.getKontoDropDown().getSelectedItem();
            if (selectedKonto != null) {
            konto = selectedKonto;
            }
            view.setKonto(konto);
        }
    }
}
