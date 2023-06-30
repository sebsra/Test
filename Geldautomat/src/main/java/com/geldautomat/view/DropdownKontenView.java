package com.geldautomat.view;

import java.awt.Component;

import javax.swing.*;

import com.geldautomat.model.*;

/**
 * Diese Klasse ist ein benutzerdefinierter Renderer für die Dropdown-Liste der Konten.
 * Sie bestimmt, wie die Konten in der Dropdown-Liste angezeigt werden.
 */
public class DropdownKontenView extends DefaultListCellRenderer {
    private static final long serialVersionUID = 1L;
    /**
     * Diese Methode wird aufgerufen, um eine Komponente zu erstellen, die in der Dropdown-Liste angezeigt wird.
     * Sie konfiguriert den Text, der für jedes Konto in der Liste angezeigt wird.
     *
     * @param list Die Liste, die diese Zelle rendert.
     * @param value Das Objekt, das in der Zelle dargestellt wird.
     * @param index Der Index der Zelle in der Liste.
     * @param isSelected Gibt an, ob die Zelle ausgewählt ist.
     * @param cellHasFocus Gibt an, ob die Zelle den Fokus hat.
     * @return Die Komponente, die in der Dropdown-Liste angezeigt wird.
     */
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof Konto) {
            Konto konto = (Konto) value;

            // Den Namen der Ausprägung von Konto ermitteln
            String kontoTypName;
            if (konto instanceof Girokonto) {
                kontoTypName = "Girokonto";
            } else if (konto instanceof Sparkonto) {
                kontoTypName = "Sparkonto";
            } else {
                kontoTypName = "Unbekannt";
            }

            // Den Text festlegen, der in der Dropdown-Liste für das jeweilige Konto angezeigt wird
            String displayText = konto.getKontonummer() + "  (" + kontoTypName + ")  ";
            setText(displayText);

            if (isSelected) {
                list.setSelectionBackground(getBackground());
                list.setSelectionForeground(getForeground());
            }
        }

        return this;
    }
}

