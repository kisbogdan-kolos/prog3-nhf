package quotes.gui.database;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import quotes.database.LocalDatabase;
import quotes.database.LocalDatabase.LocalDatabaseConfigurator;

public class ConfigureLocalDatabase extends JFrame implements LocalDatabaseConfigurator {
    private LocalDatabase db;

    private JLabel label;
    private JFileChooser fc;
    private String filename;
    private String path;

    public ConfigureLocalDatabase() {
        super("Helyi adatbázis beállítása");
        setSize(new Dimension(350, 100));

        setLocationRelativeTo(null);

        filename = "";
        path = System.getProperty("user.dir");

        initComponents();
    }

    private void initComponents() {
        fc = new JFileChooser();

        label = new JLabel("Jelenlegi fájl: -");
        label.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(label, BorderLayout.CENTER);

        JPanel p = new JPanel();

        JButton bt0 = new JButton("Mentés");
        bt0.addActionListener(e -> {
            setVisible(false);
            if (db != null)
                db.setFilename(filename);
        });
        p.add(bt0);

        JButton bt1 = new JButton("Fájl kiválasztása");
        bt1.addActionListener(e -> {
            int result = fc.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                filename = fc.getSelectedFile().getPath();
                if (filename.startsWith(path)) {
                    filename = filename.substring(path.length() + 1);
                }
                label.setText("Jelenlegi fájl: " + filename);
            }
        });
        p.add(bt1);

        JButton bt2 = new JButton("Mégse");
        bt2.addActionListener(e -> setVisible(false));
        p.add(bt2);

        add(p, BorderLayout.SOUTH);
    }

    public void configure() {
        setVisible(true);
        if (db != null) {
            filename = db.getFilename();
            label.setText("Jelenlegi fájl: " + filename);
            fc.setSelectedFile(new File(path, filename));
        }
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
