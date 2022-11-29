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

    /**
     * Creates a new {@code Person} with a fixed id
     * 
     * @param id
     * @param fullName
     * @param shortName
     * @param notes
     */
    Person(UUID id, String fullName, String shortName, String notes) {
        this.id = id;
        dataChanged = new ArrayList<UpdateEvent>();

        setFullName(fullName);
        setShortName(shortName);
        setNotes(notes);
    }

    /**
     * Creates a new {@code Person} with a random id
     * 
     * @param fullName
     * @param shortName
     * @param notes
     */
    public Person(String fullName, String shortName, String notes) {
        this(UUID.randomUUID(), fullName, shortName, notes);
    }

    /**
     * @return id of {@code Person}
     */
    public UUID getId() {
        return id;
    }

    /**
     * @return full name of {@code Person}
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets full name of {@code Person}
     * 
     * @param fullName
     * @throws IllegalArgumentException if {@code fullName} is empty or null
     */
    public void setFullName(String fullName) {
        if (fullName == null || fullName.isEmpty()) {
            throw new IllegalArgumentException("fullName cannot be null or empty");
        }
        this.fullName = fullName;

        for (UpdateEvent e : dataChanged)
            e.onEvent(this);
    }

    /**
     * @return short name of {@code Person}
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * Sets short name of {@code Person}
     * 
     * @param shortName
     * @throws IllegalArgumentException if {@code shortName} is empty or null
     */
    public void setShortName(String shortName) {
        if (shortName == null || shortName.isEmpty()) {
            throw new IllegalArgumentException("shortName cannot be null or empty");
        }
        this.shortName = shortName;

        for (UpdateEvent e : dataChanged)
            e.onEvent(this);
    }

    /**
     * @return notes of {@code Person}, never null
     */
    public String getNotes() {
        if (notes == null) {
            return "";
        }
        return notes;
    }

    /**
     * Sets notes of {@code Person}
     * 
     * @param notes
     */
    public void setNotes(String notes) {
        this.notes = notes;

        for (UpdateEvent e : dataChanged)
            e.onEvent(this);
    }

    /**
     * Set all parameters of {@code Person}
     * 
     * @param fullName
     * @param shortName
     * @param notes
     * @throws IllegalArgumentException if {@code fullName} or {@code shortName} is
     *                                  empty or null
     */
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

    /**
     * @return number of quotes of {@code Person}
     */
    public int getQuoteCount() {
        return quoteCount;
    }

    /**
     * Sets number of quotes of {@code Person}
     * 
     * @param quoteCount
     */
    void setQuoteCount(int quoteCount) {
        this.quoteCount = quoteCount;

        for (UpdateEvent e : dataChanged)
            e.onEvent(this);
    }

    public interface UpdateEvent {
        /**
         * Called when {@code Person} is updated
         * 
         * @param person
         */
        public void onEvent(Person p);
    }

    /**
     * Adds an event to be called when {@code Person} is updated
     * 
     * @param e
     */
    void setDataChanged(UpdateEvent dataChanged) {
        this.dataChanged.add(dataChanged);
    }
}
