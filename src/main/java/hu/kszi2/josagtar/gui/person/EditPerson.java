package hu.kszi2.josagtar.gui.person;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import hu.kszi2.josagtar.PersonData;
import hu.kszi2.josagtar.database.Person;

public class EditPerson extends JFrame {
    private PersonData pd;
    private Person person;

    private JTextField fullName;
    private JTextField shortName;
    private JTextField notes;

    /**
     * Creates an {@code EditPerson} object
     * 
     * @param pd the {@code PersonData} object to use
     */
    public EditPerson(PersonData pd) {
        super("Ember szerkesztése");

        this.pd = pd;

        initComponents();
    }

    /**
     * Shows the component
     * 
     * @param person the {@code Person} object to edit
     */
    public void showComponent(Person p) {
        person = p;
        loadFields();
        setVisible(true);
    }

    /**
     * Initializes the components
     */
    private void initComponents() {
        setSize(new Dimension(350, 200));
        setResizable(false);

        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        fullName = generateInputPanel("Teljes név:", panel);
        shortName = generateInputPanel("Rövid név:", panel);
        notes = generateInputPanel("Jegyzet:", panel);

        JPanel buttonPanel = new JPanel();

        JButton save = new JButton("Mentés");
        save.addActionListener(e -> savePerson());

        JButton cancel = new JButton("Mégse");
        cancel.addActionListener(e -> clearFields());

        JButton delete = new JButton("Törlés");
        delete.addActionListener(e -> deletePerson());

        buttonPanel.add(save);
        buttonPanel.add(cancel);
        buttonPanel.add(delete);
        panel.add(buttonPanel);

        add(panel, BorderLayout.CENTER);
    }

    /**
     * Generates an input panel
     * 
     * @param labelText the label text
     * @param panel     the panel to add the input panel to
     * @return the {@code JTextField} object
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

    /**
     * Saves the person
     */
    private void savePerson() {
        try {
            person.setAllParams(fullName.getText(), shortName.getText(), notes.getText());
            clearFields();
        } catch (IllegalArgumentException e) {
            String msg = e.getMessage();
            JOptionPane.showMessageDialog(this, msg, "Adat hiba", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Deletes the person
     */
    private void deletePerson() {
        String[] options = { "Igen", "Nem" };
        int result = JOptionPane.showOptionDialog(this, "Biztosan törlöd: " + person.getFullName() + "?", "Törlés",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (result == 0) {
            try {
                pd.deletePerson(person);
                clearFields();
            } catch (IllegalArgumentException e) {
                String msg = e.getMessage();
                JOptionPane.showMessageDialog(this, msg, "Törlés hiba", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Clears the fields
     */
    private void clearFields() {
        fullName.setText("");
        shortName.setText("");
        notes.setText("");
        setVisible(false);
    }

    /**
     * Loads the fields
     */
    private void loadFields() {
        if (person == null)
            return;
        fullName.setText(person.getFullName());
        shortName.setText(person.getShortName());
        notes.setText(person.getNotes());
    }
}
