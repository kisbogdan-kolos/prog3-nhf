package quotes;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

import quotes.database.Database;
import quotes.database.Person;
import quotes.database.Quote;

public class QuoteData extends AbstractTableModel {
    private Database db;

    private List<ClickEvent> editEvents;
    private List<ClickEvent> viewEvents;

    private static final String[] columnNames = new String[] {
            "Kontextus",
            "Idézet",
            "Kontextus",
            "Szerző",
            "Szerkesztés",
            "Nézet" };
    private static final Class<?>[] columnClasses = new Class<?>[] {
            String.class,
            String.class,
            String.class,
            String.class,
            JButton.class,
            JButton.class
    };

    public QuoteData(Database db) {
        this.db = db;

        editEvents = new ArrayList<ClickEvent>();
        viewEvents = new ArrayList<ClickEvent>();

        db.onAllQuoteUpdate(idx -> {
            fireTableDataChanged();
        });

        db.onQuoteUpdate(idx -> {
            fireTableRowsUpdated(idx, idx);
        });

        db.onQuoteInsert(idx -> {
            fireTableRowsInserted(idx, idx);
        });

        db.onQuoteDelete(idx -> {
            fireTableStructureChanged();
        });
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public int getRowCount() {
        return db.getQuoteCount();
    }

    @Override
    public Object getValueAt(int row, int column) {
        Quote q = db.getQuoteByIndex(row);
        JButton b;
        if (q == null)
            return null;

        switch (column) {
            case 0:
                return q.getContextBefore();
            case 1:
                return q.getQuote();
            case 2:
                return q.getContextAfter();
            case 3:
                return q.getAuthor().getShortName();
            case 4:
                b = new JButton("Szerkeszt");
                b.addActionListener(e -> {
                    for (ClickEvent ce : editEvents)
                        ce.onClick(q);
                });
                return b;
            case 5:
                b = new JButton("Megnéz");
                b.addActionListener(e -> {
                    for (ClickEvent ce : viewEvents)
                        ce.onClick(q);
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
        return column == 4 || column == 5;
    }

    public void addQuote(Quote q) {
        db.addQuote(q);
    }

    public void deleteQuote(Quote q) {
        db.deleteQuoteById(q.getId());
    }

    public Quote getQuoteByIndex(int idx) {
        return db.getQuoteByIndex(idx);
    }

    public interface ClickEvent {
        public void onClick(Quote p);
    }

    public void addEditEvent(ClickEvent e) {
        editEvents.add(e);
    }

    public void addViewEvent(ClickEvent e) {
        viewEvents.add(e);
    }

    public List<Person> getAllPeople() {
        int len = db.getPersonCount();
        List<Person> list = new ArrayList<Person>();

        for (int i = 0; i < len; i++)
            list.add(db.getPersonByIndex(i));
        return list;
    }
}
