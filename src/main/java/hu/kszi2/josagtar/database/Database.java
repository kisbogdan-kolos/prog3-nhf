package hu.kszi2.josagtar.database;

import java.util.UUID;

public interface Database {
    /**
     * Initialize database. Tries to open default database file ({@code db.json}).
     */
    public void init();

    /**
     * @return number of {@code Person} records
     */
    public int getPersonCount();

    /**
     * Gets {@code Person} by index
     * 
     * @param idx index of required {@code Person}
     * @return {@code Person} at requested index
     */
    public Person getPersonByIndex(int idx);

    /**
     * Gets {@code Person} by id
     * 
     * @param id id of required {@code Person}
     * @return {@code Person} with requested id
     */
    public Person getPersonById(UUID id);

    /**
     * Adds {@code Person} to database
     * 
     * @param p {@code Person} to add
     */
    public void addPerson(Person p);

    /**
     * Deletes {@code Person} by index
     * 
     * @param idx index of {@code Person} to delete
     */
    public void deletePersonByIndex(int idx);

    /**
     * Deletes {@code Person} by id
     * 
     * @param id id of {@code Person} to delete
     */
    public void deletePersonById(UUID id);

    /**
     * {@code UpdateEvent} is called when a {@code Person} is modified
     * 
     * @param e {@code UpdateEvent} to add
     */
    public void onPersonUpdate(UpdateEvent e);

    /**
     * {@code UpdateEvent} is called when a {@code Person} is added
     * 
     * @param e {@code UpdateEvent} to add
     */
    public void onPersonInsert(UpdateEvent e);

    /**
     * {@code UpdateEvent} is called when a {@code Person} is deleted
     * 
     * @param e {@code UpdateEvent} to add
     */
    public void onPersonDelete(UpdateEvent e);

    /**
     * {@code UpdateEvent} is called when more than one {@code Person} is modified,
     * added or deleted
     * 
     * @param e {@code UpdateEvent} to add
     */
    public void onAllPersonUpdate(UpdateEvent e);

    /**
     * @return number of {@code Quote} records
     */
    public int getQuoteCount();

    /**
     * Gets {@code Quote} by index
     * 
     * @param idx index of required {@code Quote}
     * @return {@code Quote} at requested index
     */
    public Quote getQuoteByIndex(int idx);

    /**
     * Gets {@code Quote} by id
     * 
     * @param id id of required {@code Quote}
     * @return {@code Quote} with requested id
     */
    public Quote getQuoteById(UUID id);

    /**
     * Adds {@code Quote} to database
     * 
     * @param q {@code Quote} to add
     */
    public void addQuote(Quote q);

    /**
     * Deletes {@code Quote} by index
     * 
     * @param idx index of {@code Quote} to delete
     */
    public void deleteQuoteByIndex(int idx);

    /**
     * Deletes {@code Quote} by id
     * 
     * @param id id of {@code Quote} to delete
     */
    public void deleteQuoteById(UUID id);

    /**
     * {@code UpdateEvent} is called when a {@code Quote} is modified
     * 
     * @param e {@code UpdateEvent} to add
     */
    public void onQuoteUpdate(UpdateEvent e);

    /**
     * {@code UpdateEvent} is called when a {@code Quote} is added
     * 
     * @param e {@code UpdateEvent} to add
     */
    public void onQuoteInsert(UpdateEvent e);

    /**
     * {@code UpdateEvent} is called when a {@code Quote} is deleted
     * 
     * @param e {@code UpdateEvent} to add
     */
    public void onQuoteDelete(UpdateEvent e);

    /**
     * {@code UpdateEvent} is called when more than one {@code Quote} is modified,
     * added or deleted
     * 
     * @param e {@code UpdateEvent} to add
     */
    public void onAllQuoteUpdate(UpdateEvent e);

    /**
     * Configure the database. This is required, when the database file is required
     * to be changed
     */
    public void configure();

    public interface UpdateEvent {
        /**
         * @param idx index of {@code Person} or {@code Quote} that was modified, added
         *            or deleted
         */
        public void onEvent(int idx);
    }
}
