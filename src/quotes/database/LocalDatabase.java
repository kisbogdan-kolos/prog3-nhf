package quotes.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * {@code Database} implementation using JSON files
 */
public class LocalDatabase implements Database {
    private List<Person> people;
    private List<Quote> quotes;
    private String filename;

    private List<UpdateEvent> singlePersonUpdate;
    private List<UpdateEvent> singlePersonInsert;
    private List<UpdateEvent> singlePersonDelete;
    private List<UpdateEvent> allPeople;
    private List<UpdateEvent> singleQuoteUpdate;
    private List<UpdateEvent> singleQuoteInsert;
    private List<UpdateEvent> singleQuoteDelete;
    private List<UpdateEvent> allQuotes;

    private LocalDatabaseConfigurator cfg;

    /**
     * Creates a new {@code LocalDatabase} instance
     * 
     * @param cfg {@code LocalDatabaseConfigurator} to use
     */
    public LocalDatabase(LocalDatabaseConfigurator cfg) {
        people = new ArrayList<Person>();

        quotes = new ArrayList<Quote>();

        singlePersonUpdate = new ArrayList<UpdateEvent>();
        singlePersonInsert = new ArrayList<UpdateEvent>();
        singlePersonDelete = new ArrayList<UpdateEvent>();
        allPeople = new ArrayList<UpdateEvent>();
        singleQuoteUpdate = new ArrayList<UpdateEvent>();
        singleQuoteInsert = new ArrayList<UpdateEvent>();
        singleQuoteDelete = new ArrayList<UpdateEvent>();
        allQuotes = new ArrayList<UpdateEvent>();

        singlePersonUpdate.add(idx -> saveFile());
        singlePersonInsert.add(idx -> saveFile());
        singlePersonDelete.add(idx -> saveFile());
        allPeople.add(idx -> saveFile());
        singleQuoteUpdate.add(idx -> saveFile());
        singleQuoteInsert.add(idx -> saveFile());
        singleQuoteDelete.add(idx -> saveFile());
        allQuotes.add(idx -> saveFile());

        this.cfg = cfg;
        cfg.setDatabase(this);
    }

    /**
     * @return filename of the currently active database file
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Sets the filename of the new database file. If the file does not exist, the
     * database will run the configuration again.
     * 
     * @param filename
     */
    public void setFilename(String filename) {
        this.filename = filename;
        try {
            readFile();
        } catch (FileNotFoundException e) {
            cfg.fileAccessError();
        } catch (Exception e) {
            cfg.fileFormatError(e.toString());
        }
    }

    private void readFile() throws FileNotFoundException {
        InputStream is = new FileInputStream(filename);

        JSONTokener tokener = new JSONTokener(is);
        JSONObject object = new JSONObject(tokener);

        people.clear();
        quotes.clear();

        JSONArray peopleJA = object.getJSONArray("people");
        peopleJA.forEach(jp -> {
            JSONObject jo = (JSONObject) jp;
            Person p = new Person(
                    UUID.fromString(jo.getString("id")),
                    jo.getString("fullName"),
                    jo.optString("shortName"),
                    jo.optString("notes"));

            p.setDataChanged(person -> {
                for (UpdateEvent e : singlePersonUpdate)
                    e.onEvent(people.indexOf(person));
            });
            people.add(p);
        });

        for (UpdateEvent e : allPeople)
            e.onEvent(0);

        JSONArray quotesJA = object.getJSONArray("quotes");
        quotesJA.forEach(jq -> {
            JSONObject jo = (JSONObject) jq;

            JSONObject quoteJO = jo.getJSONObject("quote");
            Person owner = getPersonById(UUID.fromString(jo.getString("person")));
            if (owner == null) {
                throw new IllegalArgumentException("quote has invalid owner");
            }
            owner.setQuoteCount(owner.getQuoteCount() + 1);

            Quote q = new Quote(
                    UUID.fromString(jo.getString("id")),
                    owner,
                    quoteJO.optString("contextBefore"),
                    quoteJO.getString("quote"),
                    quoteJO.optString("contextAfter"));

            q.setDataChanged(quote -> {
                for (UpdateEvent e : singleQuoteUpdate)
                    e.onEvent(quotes.indexOf(quote));
            });
            quotes.add(q);
        });

        for (UpdateEvent e : allQuotes)
            e.onEvent(0);
    }

    private void saveFile() {
        try {
            FileWriter fw = new FileWriter(filename);

            JSONArray peopleJA = new JSONArray();
            for (Person p : people) {
                JSONObject personJO = new JSONObject();

                personJO.put("id", p.getId());
                personJO.put("shortName", p.getShortName());
                personJO.put("fullName", p.getFullName());
                if (!p.getNotes().isEmpty())
                    personJO.put("notes", p.getNotes());

                peopleJA.put(personJO);
            }

            JSONArray quotesJA = new JSONArray();
            for (Quote q : quotes) {
                JSONObject quoteJO = new JSONObject();
                JSONObject quoteDataJO = new JSONObject();

                quoteJO.put("id", q.getId());
                quoteJO.put("person", q.getAuthor().getId());
                if (!q.getContextBefore().isEmpty())
                    quoteDataJO.put("contextBefore", q.getContextBefore());
                quoteDataJO.put("quote", q.getQuote());
                if (!q.getContextAfter().isEmpty())
                    quoteDataJO.put("contextAfter", q.getContextAfter());
                quoteJO.put("quote", quoteDataJO);

                quotesJA.put(quoteJO);
            }

            JSONObject out = new JSONObject();

            out.put("people", peopleJA);
            out.put("quotes", quotesJA);

            fw.write(out.toString(4));
            fw.close();
        } catch (IOException e) {
            cfg.fileSaveError(e.toString());
        }
    }

    @Override
    public void init() {
        setFilename("db.json");
    }

    @Override
    public int getPersonCount() {
        return people.size();
    }

    @Override
    public Person getPersonByIndex(int idx) {
        return people.get(idx);
    }

    @Override
    public Person getPersonById(UUID id) {
        for (Person p : people)
            if (p.getId().equals(id))
                return p;
        return null;
    }

    @Override
    public void addPerson(Person p) {
        p.setDataChanged(person -> {
            for (UpdateEvent e : singlePersonUpdate)
                e.onEvent(people.indexOf(person));
        });
        people.add(p);

        for (UpdateEvent e : singlePersonInsert)
            e.onEvent(people.size() - 1);
    }

    @Override
    public void deletePersonByIndex(int idx) {
        if (people.get(idx).getQuoteCount() != 0) {
            throw new IllegalArgumentException("person has quotes");
        }
        people.remove(idx);

        for (UpdateEvent e : singlePersonDelete)
            e.onEvent(idx);
    }

    @Override
    public void deletePersonById(UUID id) {
        int idx = people.indexOf(getPersonById(id));

        if (idx == -1)
            return;

        deletePersonByIndex(idx);
    }

    @Override
    public int getQuoteCount() {
        return quotes.size();
    }

    @Override
    public Quote getQuoteByIndex(int idx) {
        return quotes.get(idx);
    }

    @Override
    public Quote getQuoteById(UUID id) {
        for (Quote q : quotes)
            if (q.getId().equals(id))
                return q;
        return null;
    }

    @Override
    public void addQuote(Quote q) {
        q.setDataChanged(quote -> {
            for (UpdateEvent e : singleQuoteUpdate)
                e.onEvent(quotes.indexOf(quote));
        });
        quotes.add(q);
        q.getAuthor().setQuoteCount(q.getAuthor().getQuoteCount() + 1);

        for (UpdateEvent e : singleQuoteInsert)
            e.onEvent(quotes.size() - 1);
    }

    @Override
    public void deleteQuoteByIndex(int idx) {
        Quote q = quotes.remove(idx);
        q.getAuthor().setQuoteCount(q.getAuthor().getQuoteCount() - 1);

        for (UpdateEvent e : singleQuoteDelete)
            e.onEvent(idx);
    }

    @Override
    public void deleteQuoteById(UUID id) {
        int idx = quotes.indexOf(getQuoteById(id));

        if (idx == -1)
            return;

        deleteQuoteByIndex(idx);
    }

    @Override
    public void configure() {
        cfg.configure();
    }

    @Override
    public void onPersonUpdate(UpdateEvent e) {
        singlePersonUpdate.add(e);
    }

    @Override
    public void onPersonInsert(UpdateEvent e) {
        singlePersonInsert.add(e);
    }

    @Override
    public void onPersonDelete(UpdateEvent e) {
        singlePersonDelete.add(e);
    }

    @Override
    public void onAllPersonUpdate(UpdateEvent e) {
        allPeople.add(e);
    }

    @Override
    public void onQuoteUpdate(UpdateEvent e) {
        singleQuoteUpdate.add(e);
    }

    @Override
    public void onQuoteInsert(UpdateEvent e) {
        singleQuoteInsert.add(e);
    }

    @Override
    public void onQuoteDelete(UpdateEvent e) {
        singleQuoteDelete.add(e);
    }

    @Override
    public void onAllQuoteUpdate(UpdateEvent e) {
        allQuotes.add(e);
    }

    /**
     * Interface for configuring the database.
     */
    public interface LocalDatabaseConfigurator {
        /**
         * Sets the database for the configuration
         * 
         * @param db
         */
        public void setDatabase(LocalDatabase db);

        /**
         * Called when the database is configured.
         */
        public void configure();

        /**
         * Called when the database is unable to save the file.
         */
        public void fileAccessError();

        /**
         * Called when the database is unable to parse the file.
         * 
         * @param reason
         */
        public void fileFormatError(String msg);

        /**
         * Called when the database is unable to save the file.
         * 
         * @param error
         */
        public void fileSaveError(String msg);
    }
}
