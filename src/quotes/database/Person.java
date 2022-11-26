package quotes.database;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Person {
    private final UUID id;
    private String fullName;
    private String shortName;
    private String notes;
    private int quoteCount = 0;

    private List<UpdateEvent> dataChanged;

    Person(UUID id, String fullName, String shortName, String notes) {
        this.id = id;
        dataChanged = new ArrayList<UpdateEvent>();

        setFullName(fullName);
        setShortName(shortName);
        setNotes(notes);
    }

    public Person(String fullName, String shortName, String notes) {
        this(UUID.randomUUID(), fullName, shortName, notes);
    }

    public UUID getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if (fullName == null || fullName.isEmpty()) {
            throw new IllegalArgumentException("fullName cannot be null or empty");
        }
        this.fullName = fullName;

        for (UpdateEvent e : dataChanged)
            e.onEvent(this);
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        if (shortName == null || shortName.isEmpty()) {
            throw new IllegalArgumentException("shortName cannot be null or empty");
        }
        this.shortName = shortName;

        for (UpdateEvent e : dataChanged)
            e.onEvent(this);
    }

    public String getNotes() {
        if (notes == null) {
            return "";
        }
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;

        for (UpdateEvent e : dataChanged)
            e.onEvent(this);
    }

    public void setAllParams(String fullName, String shortName, String notes) {
        if (fullName == null || fullName.isEmpty()) {
            throw new IllegalArgumentException("fullName cannot be null or empty");
        }

        if (shortName == null || shortName.isEmpty()) {
            throw new IllegalArgumentException("shortName cannot be null or empty");
        }

        this.fullName = fullName;
        this.shortName = shortName;
        this.notes = notes;

        for (UpdateEvent e : dataChanged)
            e.onEvent(this);
    }

    public int getQuoteCount() {
        return quoteCount;
    }

    void setQuoteCount(int quoteCount) {
        this.quoteCount = quoteCount;

        for (UpdateEvent e : dataChanged)
            e.onEvent(this);
    }

    public interface UpdateEvent {
        public void onEvent(Person p);
    }

    void setDataChanged(UpdateEvent dataChanged) {
        this.dataChanged.add(dataChanged);
    }
}
