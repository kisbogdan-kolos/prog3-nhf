package quotes.database;

import java.util.UUID;

public interface Database {
    public void init();

    public int getPersonCount();

    public Person getPersonByIndex(int idx);

    public Person getPersonById(UUID id);

    public void addPerson(Person p);

    public void deletePersonByIndex(int idx);

    public void deletePersonById(UUID id);

    public void onPersonUpdate(UpdateEvent e);

    public void onPersonInsert(UpdateEvent e);

    public void onPersonDelete(UpdateEvent e);

    public void onAllPersonUpdate(UpdateEvent e);

    public int getQuoteCount();

    public Quote getQuoteByIndex(int idx);

    public Quote getQuoteById(UUID id);

    public void addQuote(Quote q);

    public void deleteQuoteByIndex(int idx);

    public void deleteQuoteById(UUID id);

    public void onQuoteUpdate(UpdateEvent e);

    public void onQuoteInsert(UpdateEvent e);

    public void onQuoteDelete(UpdateEvent e);

    public void onAllQuoteUpdate(UpdateEvent e);

    public void configure();

    public interface UpdateEvent {
        public void onEvent(int idx);
    }
}
