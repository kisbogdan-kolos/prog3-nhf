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

public class EditQuote extends JFrame {
    private QuoteData qd;
    private Quote quote;

    private DefaultComboBoxModel<Person> personModel;
    private JComboBox<Person> personSelector;
    private JTextField contextBefore;
    private JTextField quoteText;
    private JTextField contextAfter;

    /**
     * Creates an {@code EditQuote} object
     * 
     * @param qd the {@code QuoteData} object to use
     */
    public EditQuote(QuoteData qd) {
        super("Idézet hozzáadása");
        this.qd = qd;

        initComponents();
    }

    /**
     * Shows the component
     */
    public void showComponent(Quote q) {
        quote = q;

        personModel.removeAllElements();
        personModel.addAll(qd.getAllPeople());

        loadFields();

        setVisible(true);
    }

    /**
     * Initializes the components
     */
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

        JButton save = new JButton("Mentés");
        save.addActionListener(e -> saveQuote());

        JButton cancel = new JButton("Mégse");
        cancel.addActionListener(e -> clearFields());

        JButton delete = new JButton("Törlés");
        delete.addActionListener(e -> deleteQuote());

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

    /**
     * Saves the quote
     */
    private void saveQuote() {
        try {
            quote.setAllParams(
                    (Person) personSelector.getSelectedItem(),
                    contextBefore.getText(),
                    quoteText.getText(),
                    contextAfter.getText());

            clearFields();
        } catch (IllegalArgumentException e) {
            String msg = e.getMessage();
            JOptionPane.showMessageDialog(this, msg, "Adat hiba", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Deletes the quote
     */
    private void deleteQuote() {
        String[] options = { "Igen", "Nem" };
        int result = JOptionPane.showOptionDialog(this, "Biztosan törlöd: " + quote.getQuote() + "?", "Törlés",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (result == 0) {
            try {
                qd.deleteQuote(quote);
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
        contextBefore.setText("");
        quoteText.setText("");
        contextAfter.setText("");
        setVisible(false);
    }

    /**
     * Loads the fields
     */
    private void loadFields() {
        if (quote == null)
            return;

        contextBefore.setText(quote.getContextBefore());
        quoteText.setText(quote.getQuote());
        contextAfter.setText(quote.getContextAfter());
        personSelector.setSelectedItem(quote.getAuthor());
    }

}