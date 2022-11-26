package quotes.database;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Quote {
    private final UUID id;
    private Person author;
    private String contextBefore;
    private String quote;
    private String contextAfter;

    private List<UpdateEvent> dataChanged;

    /**
     * Creates a new {@code Quote} with a fixed id
     * 
     * @param id
     * @param author
     * @param contextBefore
     * @param quote
     * @param contextAfter
     */
    Quote(UUID id, Person author, String contextBefore, String quote, String contextAfter) {
        this.id = id;
        dataChanged = new ArrayList<UpdateEvent>();

        setAuthor(author);
        setContextBefore(contextBefore);
        setQuote(quote);
        setContextAfter(contextAfter);
    }

    /**
     * Creates a new {@code Quote} with a random id
     * 
     * @param author
     * @param contextBefore
     * @param quote
     * @param contextAfter
     */
    public Quote(Person author, String contextBefore, String quote, String contextAfter) {
        this(UUID.randomUUID(), author, contextBefore, quote, contextAfter);
    }

    /**
     * @return id of {@code Quote}
     */
    public UUID getId() {
        return id;
    }

    /**
     * @return author of {@code Quote}
     */
    public Person getAuthor() {
        return author;
    }

    /**
     * Sets author of {@code Quote}
     * 
     * @param author
     * @throws IllegalArgumentException if {@code author} is null
     */
    public void setAuthor(Person author) {
        if (author == null) {
            throw new IllegalArgumentException("author cannot be null");
        }
        this.author = author;

        for (UpdateEvent e : dataChanged)
            e.onEvent(this);
    }

    /**
     * @return context before {@code Quote}
     */
    public String getContextBefore() {
        if (contextBefore == null) {
            return "";
        }
        return contextBefore;
    }

    /**
     * Sets context before {@code Quote}
     * 
     * @param contextBefore
     */
    public void setContextBefore(String contextBefore) {
        this.contextBefore = contextBefore;

        for (UpdateEvent e : dataChanged)
            e.onEvent(this);
    }

    /**
     * @return {@code Quote}
     */
    public String getQuote() {
        return quote;
    }

    /**
     * Sets {@code Quote}
     * 
     * @param quote
     * @throws IllegalArgumentException if {@code quote} is empty or null
     */
    public void setQuote(String quote) {
        if (quote == null || quote.isEmpty()) {
            throw new IllegalArgumentException("quote cannot be null or empty");
        }
        this.quote = quote;

        for (UpdateEvent e : dataChanged)
            e.onEvent(this);
    }

    /**
     * @return context after {@code Quote}
     */
    public String getContextAfter() {
        if (contextAfter == null) {
            return "";
        }
        return contextAfter;
    }

    /**
     * Sets context after {@code Quote}
     * 
     * @param contextAfter
     */
    public void setContextAfter(String contextAfter) {
        this.contextAfter = contextAfter;

        for (UpdateEvent e : dataChanged)
            e.onEvent(this);
    }

    /**
     * Set all parameters of {@code Quote}
     * 
     * @param author
     * @param contextBefore
     * @param quote
     * @param contextAfter
     */
    public void setAllParams(Person author, String contextBefore, String quote, String contextAfter) {
        if (author == null) {
            throw new IllegalArgumentException("author cannot be null");
        }
        if (quote == null || quote.isEmpty()) {
            throw new IllegalArgumentException("quote cannot be null or empty");
        }

        this.author = author;
        this.contextBefore = contextBefore;
        this.quote = quote;
        this.contextAfter = contextAfter;

        for (UpdateEvent e : dataChanged)
            e.onEvent(this);
    }

    public interface UpdateEvent {
        /**
         * Called when {@code Quote} is updated
         * 
         * @param q
         */
        public void onEvent(Quote q);
    }

    /**
     * Adds an {@code UpdateEvent} to the list of events to be called when
     * {@code Quote} is updated
     * 
     * @param e
     */
    public void setDataChanged(UpdateEvent dataChanged) {
        this.dataChanged.add(dataChanged);
    }

}
