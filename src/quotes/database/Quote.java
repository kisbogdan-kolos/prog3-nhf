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

    Quote(UUID id, Person author, String contextBefore, String quote, String contextAfter) {
        this.id = id;
        dataChanged = new ArrayList<UpdateEvent>();

        setAuthor(author);
        setContextBefore(contextBefore);
        setQuote(quote);
        setContextAfter(contextAfter);
    }

    public Quote(Person author, String contextBefore, String quote, String contextAfter) {
        this(UUID.randomUUID(), author, contextBefore, quote, contextAfter);
    }

    public UUID getId() {
        return id;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        if (author == null) {
            throw new IllegalArgumentException("author cannot be null");
        }
        this.author = author;

        for (UpdateEvent e : dataChanged)
            e.onEvent(this);
    }

    public String getContextBefore() {
        if (contextBefore == null) {
            return "";
        }
        return contextBefore;
    }

    public void setContextBefore(String contextBefore) {
        this.contextBefore = contextBefore;

        for (UpdateEvent e : dataChanged)
            e.onEvent(this);
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        if (quote == null || quote.isEmpty()) {
            throw new IllegalArgumentException("quote cannot be null or empty");
        }
        this.quote = quote;

        for (UpdateEvent e : dataChanged)
            e.onEvent(this);
    }

    public String getContextAfter() {
        if (contextAfter == null) {
            return "";
        }
        return contextAfter;
    }

    public void setContextAfter(String contextAfter) {
        this.contextAfter = contextAfter;

        for (UpdateEvent e : dataChanged)
            e.onEvent(this);
    }

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
        public void onEvent(Quote q);
    }

    public void setDataChanged(UpdateEvent dataChanged) {
        this.dataChanged.add(dataChanged);
    }

}
