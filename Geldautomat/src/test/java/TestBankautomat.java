import javax.swing.*;

import com.geldautomat.controller.LoginController;
import com.geldautomat.model.BankManagementSystem;
import com.geldautomat.util.CSVImporter;
import com.geldautomat.view.LoginView;

import java.io.File;
import java.io.IOException;

public class TestBankautomat {
    public static void main(String[] args) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filepath = selectedFile.getAbsolutePath();

            try {
                BankManagementSystem bankManagementSystem = CSVImporter.loadFromCSV(filepath);

                new LoginController(new LoginView(), bankManagementSystem);
                        


            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
    }
}
