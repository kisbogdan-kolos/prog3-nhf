package quotes.gui.database;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JTextField;

import quotes.database.RemoteDatabase;
import quotes.database.RemoteDatabase.RemoteDatabaseConfigurator;

public class ConfigureRemoteDatabase extends JFrame implements RemoteDatabaseConfigurator {
    private RemoteDatabase db;

    private JTextField url;
    private JTextField user;
    private JTextField password;

    public ConfigureRemoteDatabase() {
        super("Távoli adatbázis beállítása");
        setSize(new Dimension(350, 200));

        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        url = generateInputPanel("URL:", panel);
        user = generateInputPanel("Felhasználó:", panel);
        password = generateInputPanel("Jelszó:", panel);

        JPanel buttonPanel = new JPanel();

        JButton add = new JButton("Mentés");
        add.addActionListener(e -> {
            db.setConnection(
                    url.getText(),
                    user.getText(),
                    password.getText());
            setVisible(false);
        });

        JButton cancel = new JButton("Mégse");
        cancel.addActionListener(e -> setVisible(false));

        JButton update = new JButton("Frissítés");
        update.addActionListener(e -> {
            db.updateDatabase();
            setVisible(false);
        });

        buttonPanel.add(add);
        buttonPanel.add(cancel);
        buttonPanel.add(update);
        panel.add(buttonPanel);

        add(panel, BorderLayout.CENTER);
    }

    /**
     * Generates an input panel
     * 
     * @param labelText the label text
     * @param panel     the panel to add the input panel to
     * @return the text field
     */
    private JTextField generateInputPanel(String text, JPanel addTo) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel label = new JLabel(text);
        JTextField textField = new JTextField(20);

        panel.add(label);
        panel.add(textField);
        addTo.add(panel);

        return textField;
    }

    @Override
    public void setDatabase(RemoteDatabase db) {
        this.db = db;
    }

    @Override
    public void configure() {
        setVisible(true);
        if (db != null) {
            url.setText(db.getUrl());
            user.setText(db.getUser());
            password.setText(db.getPassword());
        }
    }

    @Override
    public void connectionError() {
        JOptionPane.showMessageDialog(this, "Nem sikerült csatlakozni a szerverhez.",
                "Csatlakozási hiba", JOptionPane.ERROR_MESSAGE);
        configure();
    }

    @Override
    public void connectionError(String msg) {
        JOptionPane.showMessageDialog(this, "Nem sikerült csatlakozni a szerverhez: " + msg,
                "Csatlakozási hiba", JOptionPane.ERROR_MESSAGE);
        configure();
    }

    @Override
    public void databaseError(String msg) {
        JOptionPane.showMessageDialog(this, "Hiba történt az adatbázisban: " + msg,
                "Adatbázis hiba", JOptionPane.ERROR_MESSAGE);
    }

}
