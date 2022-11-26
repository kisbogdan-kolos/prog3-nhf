package quotes.gui.person;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import quotes.PersonData;
import quotes.database.Person;

public class AddPerson extends JFrame {
    private PersonData pd;

    private JTextField fullName;
    private JTextField shortName;
    private JTextField notes;

    public AddPerson(PersonData pd) {
        super("Ember hozzáadása");
        this.pd = pd;

        initComponents();
    }

    public void showComponent() {
        setVisible(true);
    }

    private void initComponents() {
        setSize(new Dimension(350, 200));
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        fullName = generateInputPanel("Teljes név:", panel);
        shortName = generateInputPanel("Rövid név:", panel);
        notes = generateInputPanel("Jegyzet:", panel);

        JPanel buttonPanel = new JPanel();

        JButton add = new JButton("Hozzáad");
        add.addActionListener(e -> addPerson());

        JButton cancel = new JButton("Mégse");
        cancel.addActionListener(e -> clearFields());

        buttonPanel.add(add);
        buttonPanel.add(cancel);
        panel.add(buttonPanel);

        add(panel, BorderLayout.CENTER);
    }

    private JTextField generateInputPanel(String text, JPanel addTo) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel label = new JLabel(text);
        JTextField textField = new JTextField(20);

        panel.add(label);
        panel.add(textField);
        addTo.add(panel);

        return textField;
    }

    private void addPerson() {
        try {
            Person p = new Person(fullName.getText(), shortName.getText(), notes.getText());
            pd.addPerson(p);
            clearFields();
        } catch (IllegalArgumentException e) {
            String msg = e.getMessage();
            JOptionPane.showMessageDialog(this, msg, "Adat hiba", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        fullName.setText("");
        shortName.setText("");
        notes.setText("");
        setVisible(false);
    }
}
