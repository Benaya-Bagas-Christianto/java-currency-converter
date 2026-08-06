package com.mycompany.currencyconverter;

import com.mycompany.currencyconverter.db.DatabaseHelper;
import com.mycompany.currencyconverter.gui.MainFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        DatabaseHelper.initializeDatabase();
        
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
