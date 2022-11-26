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

    public AddQuote(QuoteData qd) {
        super("Idézet hozzáadása");
        this.qd = qd;

        initComponents();
    }

    public void showComponent() {
        personModel.removeAllElements();
        personModel.addAll(qd.getAllPeople());

        setVisible(true);
    }

    private void initComponents() {
        setSize(new Dimension(350, 200));
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        personModel = new DefaultComboBoxModel<Person>();
        personModel.addAll(qd.getAllPeople());
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

    private JTextField generateInputPanel(String text, JPanel addTo) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel label = new JLabel(text);
        JTextField textField = new JTextField(20);

        panel.add(label);
        panel.add(textField);
        addTo.add(panel);

        return textField;
    }

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

    private void clearFields() {
        contextBefore.setText("");
        quoteText.setText("");
        contextAfter.setText("");
        setVisible(false);
    }

}