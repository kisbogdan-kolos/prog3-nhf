package quotes.gui.quote;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import quotes.QuoteData;
import quotes.database.Person;
import quotes.database.Quote;

public class AddQuote extends JFrame {
    private QuoteData qd;

    private DefaultComboBoxModel<Person> personModel;
    private JComboBox<Person> personSelector;
    private JTextField contextBefore;
    private JTextField quoteText;
    private JTextField contextAfter;

    /**
     * Creates an {@code AddQuote} object
     * 
     * @param qd the {@code QuoteData} object to use
     */
    public AddQuote(QuoteData qd) {
        super("Idézet hozzáadása");
        this.qd = qd;

        initComponents();
    }

    /**
     * Shows the component
     */
    public void showComponent() {
        personModel.removeAllElements();
        personModel.addAll(qd.getAllPeople());
        try {
            personSelector.setSelectedIndex(0);
        } catch (IllegalArgumentException e) {

        }

        setVisible(true);
    }

    /**
     * Initializes the components
     */
    private void initComponents() {
        setSize(new Dimension(350, 220));
        setResizable(false);

        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        personModel = new DefaultComboBoxModel<Person>();
        personSelector = new JComboBox<Person>(personModel);
        personSelector.setRenderer(new PersonSelectorRenderer());
        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        selectorPanel.add(new JLabel("Ember:"));
        selectorPanel.add(personSelector);

        panel.add(selectorPanel);

        contextBefore = generateInputPanel("Kontextus:", panel);
        quoteText = generateInputPanel("Idézet:", panel);
        contextAfter = generateInputPanel("Kontextus:", panel);

        JPanel buttonPanel = new JPanel();

        JButton add = new JButton("Hozzáad");
        add.addActionListener(e -> addQuote());

        JButton cancel = new JButton("Mégse");
        cancel.addActionListener(e -> clearFields());

        buttonPanel.add(add);
        buttonPanel.add(cancel);
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
     * Adds a quote to the database
     */
    private void addQuote() {
        try {
            Quote q = new Quote((Person) personSelector.getSelectedItem(), contextBefore.getText(), quoteText.getText(),
                    contextAfter.getText());
            qd.addQuote(q);
            clearFields();
        } catch (IllegalArgumentException e) {
            String msg = e.getMessage();
            JOptionPane.showMessageDialog(this, msg, "Adat hiba", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Clears the fields
     */
    private void clearFields() {
        contextBefore.setText("");
        quoteText.setText("");
        contextAfter.setText("");
        setVisible(false);
    }

}