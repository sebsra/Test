package com.geldautomat.view;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Diese Klasse stellt benutzerdefinierte Textfelder mit speziellen Filtern bereit.
 */
public class CustomTextFields {
	
    /**
     * Erstellt ein benutzerdefiniertes BLZ-Textfeld.
     * Das Textfeld erlaubt nur alphanumerische Zeichen.
     *
     * @return Das benutzerdefinierte BLZ-Textfeld.
     */
	public JTextField BlzField() {
	JTextField blzField = new JTextField();
    String ALPHANUMERIC_PATTERN = "^[A-Za-z0-9]*$";
    
    ((AbstractDocument) blzField.getDocument()).setDocumentFilter(new DocumentFilter() {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches(ALPHANUMERIC_PATTERN)) {
                super.insertString(fb, offset, string, attr);
            }
        }
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text.matches(ALPHANUMERIC_PATTERN)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    });
    return blzField;
}
    /**
     * Erstellt ein benutzerdefiniertes Kontonummer-Textfeld.
     * Das Textfeld erlaubt nur numerische Zeichen.
     *
     * @return Das benutzerdefinierte Kontonummer-Textfeld.
     */
	public JTextField KontonummerField() {
	JTextField kontonummerField = new JTextField();
    String NUMERIC_PATTERN = "^[0-9]*$";
    ((AbstractDocument) kontonummerField.getDocument()).setDocumentFilter(new DocumentFilter() {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches(NUMERIC_PATTERN)) {
                super.insertString(fb, offset, string, attr);
            }
        }
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text.matches(NUMERIC_PATTERN)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    });
    return kontonummerField;
	}
}
