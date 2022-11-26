package quotes;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

import quotes.database.Database;
import quotes.database.Person;

/**
 * The model for the {@code Person} table
 */
public class PersonData extends AbstractTableModel {
    private Database db;

    private List<EditEvent> editEvents;

    private static final String[] columnNames = new String[] {
            "Teljes név",
            "Rövid név",
            "Megjegyzés",
            "Jóságok száma",
            "Szerkesztés" };
    private static final Class<?>[] columnClasses = new Class<?>[] {
            String.class,
            String.class,
            String.class,
            Integer.class,
            JButton.class
    };

    /**
     * Creates a {@code PersonData} object
     * 
     * @param db the database
     */
    public PersonData(Database db) {
        this.db = db;

        editEvents = new ArrayList<EditEvent>();

        db.onAllPersonUpdate(idx -> {
            fireTableDataChanged();
        });

        db.onPersonUpdate(idx -> {
            fireTableRowsUpdated(idx, idx);
        });

        db.onPersonInsert(idx -> {
            fireTableRowsInserted(idx, idx);
        });

        db.onPersonDelete(idx -> {
            fireTableStructureChanged();
        });
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public int getRowCount() {
        return db.getPersonCount();
    }

    @Override
    public Object getValueAt(int row, int column) {
        Person p = db.getPersonByIndex(row);
        if (p == null)
            return null;

        switch (column) {
            case 0:
                return p.getFullName();
            case 1:
                return p.getShortName();
            case 2:
                return p.getNotes();
            case 3:
                return p.getQuoteCount();
            case 4:
                JButton b = new JButton("Szerkeszt");
                b.addActionListener(e -> {
                    for (EditEvent ee : editEvents)
                        ee.onEdit(p);
                });
                return b;

            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnClasses[columnIndex];
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 4;
    }

    /**
     * Add a {@code Person} to the database
     * 
     * @param p the {@code Person} to add
     */
    public void addPerson(Person p) {
        db.addPerson(p);
    }

    /**
     * Remove a {@code Person} from the database
     * 
     * @param p the {@code Person} to remove
     */
    public void deletePerson(Person p) {
        db.deletePersonById(p.getId());
    }

    public interface EditEvent {
        /**
         * Called when a {@code Person} is edited
         * 
         * @param p the edited {@code Person}
         */
        public void onEdit(Person p);
    }

    /**
     * Add an {@code EditEvent} to the list
     * 
     * @param e the {@code EditEvent} to add
     */
    public void addEditEvent(EditEvent e) {
        editEvents.add(e);
    }
}
