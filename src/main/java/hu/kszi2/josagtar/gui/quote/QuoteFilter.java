package hu.kszi2.josagtar.gui.quote;

import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

import hu.kszi2.josagtar.QuoteData;
import hu.kszi2.josagtar.database.Person;
import hu.kszi2.josagtar.database.Quote;

public class QuoteFilter extends JPanel {
    private QuoteData qd;

    private TableRowSorter<QuoteData> sorter;

    private Person allPerson;
    private DefaultComboBoxModel<Person> personModel;
    private JComboBox<Person> personSelector;
    private JTextField filterTextSelector;

    /**
     * Creates a {@code QuoteFilter} object
     * 
     * @param qd    the {@code QuoteData} object to use
     * @param table the {@code JTable} object to use
     */
    public QuoteFilter(QuoteData qd, JTable table) {
        super(new FlowLayout(FlowLayout.LEFT));
        this.qd = qd;

        sorter = new TableRowSorter<QuoteData>(qd);
        table.setRowSorter(sorter);

        initComponents();
    }

    /**
     * Updates the list of people
     */
    public void updateList() {
        personModel.removeAllElements();
        personModel.addElement(allPerson);
        personModel.addAll(qd.getAllPeople());
    }

    /**
     * Initializes the components
     */
    private void initComponents() {
        personModel = new DefaultComboBoxModel<Person>();
        allPerson = new Person("MINDEN", " ", "");
        updateList();

        personSelector = new JComboBox<Person>(personModel);
        personSelector.setRenderer(new PersonSelectorRenderer());
        personSelector.addActionListener(e -> applyFilter());

        filterTextSelector = new JTextField(20);
        filterTextSelector.getDocument().addDocumentListener(new InputFieldKeyListener());

        add(new JLabel("Szűrés: "));
        add(personSelector);
        add(filterTextSelector);
    }

    /**
     * Creates and applies the filter
     */
    private void applyFilter() {
        final Person filterPerson = (Person) personSelector.getSelectedItem();
        final String filterText = filterTextSelector.getText().toUpperCase();

        if (filterPerson == allPerson && filterText.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(new RowFilter<QuoteData, Integer>() {
                public boolean include(Entry<? extends QuoteData, ? extends Integer> entry) {
                    QuoteData qd = entry.getModel();
                    Quote q = qd.getQuoteByIndex(entry.getIdentifier());

                    boolean person = false;
                    if (filterPerson == allPerson || q.getAuthor() == filterPerson)
                        person = true;

                    boolean quote = false;
                    if (filterText.isEmpty()
                            || q.getContextBefore().toUpperCase().contains(filterText)
                            || q.getQuote().toUpperCase().contains(filterText)
                            || q.getContextAfter().toUpperCase().contains(filterText))
                        quote = true;

                    return person && quote;
                }
            });
        }
    }

    private class InputFieldKeyListener implements DocumentListener {

        @Override
        public void changedUpdate(DocumentEvent e) {
            applyFilter();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            applyFilter();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            applyFilter();
        }

    }
}
