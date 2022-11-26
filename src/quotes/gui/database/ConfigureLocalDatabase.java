package quotes.gui.database;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import quotes.database.LocalDatabase;
import quotes.database.LocalDatabase.LocalDatabaseConfigurator;

public class ConfigureLocalDatabase extends JFrame implements LocalDatabaseConfigurator {
    private LocalDatabase db;

    private JTextField tf;

    public ConfigureLocalDatabase() {
        super("Fájlnév megadása");
        setSize(new Dimension(300, 100));

        tf = new JTextField(10);
        tf.setMaximumSize(new Dimension(250, 25));
        add(tf, BorderLayout.CENTER);

        JButton bt = new JButton("OK");
        add(bt, BorderLayout.SOUTH);
        bt.addActionListener(e -> {
            setVisible(false);
            if (db != null)
                db.setFilename(tf.getText());
        });
    }

    public void configure() {
        setVisible(true);
        if (db != null)
            tf.setText(db.getFilename());
    }

    @Override
    public void fileAccessError() {
        JOptionPane.showMessageDialog(this, "A fájl nem nyitható meg, vagy nem megfelelő a jogosultság.",
                "Fájl elérés hiba", JOptionPane.ERROR_MESSAGE);
        configure();
    }

    @Override
    public void fileFormatError(String msg) {
        JOptionPane.showMessageDialog(this, "A fájl nem megfelelő formátumú: " + msg, "Fájl formátum hiba",
                JOptionPane.ERROR_MESSAGE);
        configure();
    }

    @Override
    public void fileSaveError(String msg) {
        JOptionPane.showMessageDialog(this, "A fájl nem sikerült menteni: " + msg, "Fájl mentés hiba",
                JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void setDatabase(LocalDatabase db) {
        this.db = db;
    }
}
