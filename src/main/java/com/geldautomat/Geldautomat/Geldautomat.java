package com.geldautomat.Geldautomat;

import com.geldautomat.controller.LoginController;
import com.geldautomat.model.BankManagementSystem;
import com.geldautomat.util.CSVImporter;
import com.geldautomat.view.LoginView;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;

public class Geldautomat {
    public static void main(String[] args) {
        JFileChooser fileChooser = new JFileChooser();


        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV-Dateien", "csv");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filepath = selectedFile.getAbsolutePath();

            try {
                BankManagementSystem bankManagementSystem = CSVImporter.loadFromCSV(filepath);
                new LoginController(new LoginView(), bankManagementSystem);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Fehler:\n" + e.getMessage(),
                        "Fehler", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, "Fehler:\n" + e.getMessage(),
                        "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        } else if (result == JFileChooser.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(null, "Keine Datei ausgewählt.",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
