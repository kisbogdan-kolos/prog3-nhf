package hu.kszi2.josagtar.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class RemoteDatabase implements Database {
    private List<Person> people;
    private List<Quote> quotes;

    private List<UpdateEvent> singlePersonUpdate;
    private List<UpdateEvent> allPeople;
    private List<UpdateEvent> singleQuoteUpdate;
    private List<UpdateEvent> allQuotes;

    private String url;
    private String user;
    private String password;

    private Map<UUID, Integer> peopleIdMap;
    private Map<UUID, Integer> quotesIdMap;

    private RemoteDatabaseConfigurator cfg;

    public RemoteDatabase(RemoteDatabaseConfigurator cfg) {
        people = new ArrayList<Person>();
        quotes = new ArrayList<Quote>();

        singlePersonUpdate = new ArrayList<UpdateEvent>();
        allPeople = new ArrayList<UpdateEvent>();
        singleQuoteUpdate = new ArrayList<UpdateEvent>();
        allQuotes = new ArrayList<UpdateEvent>();

        url = "";
        user = "";
        password = "";

        peopleIdMap = new HashMap<UUID, Integer>();
        quotesIdMap = new HashMap<UUID, Integer>();

        cfg.setDatabase(this);
        this.cfg = cfg;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    private JSONArray httpGetArray(String path) {
        try {
            URI uri = URI.create(url + path);
            URL connectionUrl = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) connectionUrl.openConnection();
            connection.setRequestMethod("GET");
            String auth = user + ":" + password;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            connection.setRequestProperty("Authorization", "Basic " + encodedAuth);

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                cfg.connectionError("Failed to connect to the database. Response code: " + responseCode);
            }

            JSONTokener tokener = new JSONTokener(connection.getInputStream());
            JSONArray jsonArray = new JSONArray(tokener);
            return jsonArray;
        } catch (Exception e) {
            cfg.connectionError("Error while connecting to the database: " + e.getMessage());
        }

        return null;
    }

    public void updateDatabase() {
        JSONArray peopleArray = httpGetArray("people");
        if (peopleArray == null) {
            return;
        }

        JSONArray quotesArray = httpGetArray("quotes");
        if (quotesArray == null) {
            return;
        }

        people.clear();
        peopleIdMap.clear();
        quotes.clear();
        quotesIdMap.clear();

        try {
            for (int i = 0; i < peopleArray.length(); i++) {
                JSONObject personObject = peopleArray.getJSONObject(i);

                int id = personObject.getInt("id");
                String fullName = personObject.getString("fullname");
                String shortName = personObject.getString("displayname");
                String notes = personObject.isNull("notes") ? null : personObject.getString("notes");

                UUID idUUID = UUID.randomUUID();
                peopleIdMap.put(idUUID, id);

                Person person = new Person(idUUID, fullName, shortName, notes);
                person.setQuoteCount(personObject.getInt("count"));
                person.setDataChanged(p -> updatePerson(p));
                people.add(person);
            }

            for (int i = 0; i < quotesArray.length(); i++) {
                JSONObject quoteObject = quotesArray.getJSONObject(i);

                int id = quoteObject.getInt("id");
                String contextBefore = quoteObject.isNull("contextbefore") ? null
                        : quoteObject.getString("contextbefore");
                String quoteStr = quoteObject.getString("quote");
                String contextAfter = quoteObject.isNull("contextafter") ? null : quoteObject.getString("contextafter");
                JSONObject author = quoteObject.getJSONObject("author");
                int authorId = author.getInt("id");

                UUID idUUID = UUID.randomUUID();
                quotesIdMap.put(idUUID, id);

                UUID authorUUID = peopleIdMap.entrySet().stream()
                        .filter(entry -> entry.getValue() == authorId)
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .orElse(null);
                if (authorUUID == null) {
                    cfg.databaseError("Author not found for quote with ID: " + id);
                    throw new IllegalStateException("Author not found for quote with ID: " + id);
                }

                Quote quote = new Quote(idUUID, getPersonById(authorUUID), contextBefore, quoteStr, contextAfter);
                quote.setDataChanged(q -> updateQuote(q));
                quotes.add(quote);
            }

            for (UpdateEvent e : allPeople) {
                e.onEvent(0);
            }

            for (UpdateEvent e : allQuotes) {
                e.onEvent(0);
            }
        } catch (Exception e) {
            cfg.databaseError("Error while updating database: " + e.getMessage());
        }

        System.out.println("Database updated.");
    }

    private void updatePerson(Person p) {
        try {
            int personId = peopleIdMap.get(p.getId());
            URI uri = URI.create(url + "people/" + personId);
            URL connectionUrl = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) connectionUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            String auth = user + ":" + password;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            connection.setRequestProperty("Authorization", "Basic " + encodedAuth);
            connection.setDoOutput(true);

            JSONObject personData = new JSONObject();
            personData.put("fullname", p.getFullName());
            personData.put("displayname", p.getShortName());
            if (p.getNotes().isEmpty()) {
                personData.put("notes", JSONObject.NULL);
            } else {
                personData.put("notes", p.getNotes());
            }

            connection.getOutputStream().write(personData.toString().getBytes());

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                cfg.databaseError("Failed to update person. Response code: " + responseCode);
            } else {
                for (UpdateEvent e : singlePersonUpdate) {
                    e.onEvent(people.indexOf(p));
                }
            }
        } catch (Exception e) {
            cfg.databaseError("Error while updating person: " + e.getMessage());
        }
    }

    private void updateQuote(Quote q) {
        try {
            int quoteId = quotesIdMap.get(q.getId());
            URI uri = URI.create(url + "quotes/" + quoteId);
            URL connectionUrl = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) connectionUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            String auth = user + ":" + password;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            connection.setRequestProperty("Authorization", "Basic " + encodedAuth);
            connection.setDoOutput(true);

            JSONObject quoteData = new JSONObject();
            quoteData.put("author", peopleIdMap.get(q.getAuthor().getId()));
            if (q.getContextBefore().isEmpty()) {
                quoteData.put("contextbefore", JSONObject.NULL);
            } else {
                quoteData.put("contextbefore", q.getContextBefore());
            }
            quoteData.put("quote", q.getQuote());
            if (q.getContextAfter().isEmpty()) {
                quoteData.put("contextafter", JSONObject.NULL);
            } else {
                quoteData.put("contextafter", q.getContextAfter());
            }

            connection.getOutputStream().write(quoteData.toString().getBytes());

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                cfg.databaseError("Failed to update quote. Response code: " + responseCode);
            } else {
                for (UpdateEvent e : singleQuoteUpdate) {
                    e.onEvent(quotes.indexOf(q));
                }
            }
        } catch (Exception e) {
            cfg.databaseError("Error while updating quote: " + e.getMessage());
        }
    }

    public void setConnection(String url, String user, String password) {
        if (url == null || user == null || password == null) {
            cfg.connectionError("No database was given.");
            return;
        }

        if (url.isEmpty() || user.isEmpty() || password.isEmpty()) {
            cfg.connectionError("No database was given.");
            return;
        }

        if (url.charAt(url.length() - 1) != '/') {
            url += '/';
        }

        this.url = url;
        this.user = user;
        this.password = password;

        try {
            JSONObject connectionDetails = new JSONObject();
            connectionDetails.put("url", url);
            connectionDetails.put("user", user);
            connectionDetails.put("password", password);

            FileWriter file = new FileWriter("connection_details.json");
            file.write(connectionDetails.toString(4));
            file.close();

        } catch (Exception e) {
            cfg.databaseError("Error while saving connection details: " + e.getMessage());
        }

        updateDatabase();
    }

    @Override
    public void init() {
        try {
            InputStream inputStream = new FileInputStream("connection_details.json");
            JSONTokener tokener = new JSONTokener(inputStream);
            JSONObject connectionDetails = new JSONObject(tokener);

            String url = connectionDetails.getString("url");
            String user = connectionDetails.getString("user");
            String password = connectionDetails.getString("password");

            setConnection(url, user, password);
            return;
        } catch (FileNotFoundException e) {
        } catch ( Exception e) {
            cfg.databaseError("Error while loading connection details: " + e.getMessage());
            return;
        }

        cfg.connectionError("No database was given.");
    }

    public void init(String url, String user, String password) {
        setConnection(url, user, password);
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
        try {
            URI uri = URI.create(url + "people/new");
            URL connectionUrl = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) connectionUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            String auth = user + ":" + password;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            connection.setRequestProperty("Authorization", "Basic " + encodedAuth);
            connection.setDoOutput(true);

            JSONObject personData = new JSONObject();
            personData.put("fullname", p.getFullName());
            personData.put("displayname", p.getShortName());
            if (p.getNotes().isEmpty()) {
                personData.put("notes", JSONObject.NULL);
            } else {
                personData.put("notes", p.getNotes());
            }

            connection.getOutputStream().write(personData.toString().getBytes());

            int responseCode = connection.getResponseCode();
            if (responseCode < 200 || responseCode >= 300) {
                cfg.databaseError("Failed to add person. Response code: " + responseCode);
            } else {
                updateDatabase();
            }
        } catch (Exception e) {
            cfg.databaseError("Error while adding person: " + e.getMessage());
        }
    }

    @Override
    public void deletePersonByIndex(int idx) {
        cfg.databaseError("Deleting person is not supported.");
    }

    @Override
    public void deletePersonById(UUID id) {
        cfg.databaseError("Deleting person is not supported.");
    }

    @Override
    public void onPersonUpdate(UpdateEvent e) {
        singlePersonUpdate.add(e);
    }

    @Override
    public void onPersonInsert(UpdateEvent e) {
    }

    @Override
    public void onPersonDelete(UpdateEvent e) {
    }

    @Override
    public void onAllPersonUpdate(UpdateEvent e) {
        allPeople.add(e);
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
        try {
            URI uri = URI.create(url + "quotes/new");
            URL connectionUrl = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) connectionUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            String auth = user + ":" + password;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            connection.setRequestProperty("Authorization", "Basic " + encodedAuth);
            connection.setDoOutput(true);

            JSONObject quoteData = new JSONObject();
            quoteData.put("author", peopleIdMap.get(q.getAuthor().getId()));
            if (q.getContextBefore().isEmpty()) {
                quoteData.put("contextbefore", JSONObject.NULL);
            } else {
                quoteData.put("contextbefore", q.getContextBefore());
            }
            quoteData.put("quote", q.getQuote());
            if (q.getContextAfter().isEmpty()) {
                quoteData.put("contextafter", JSONObject.NULL);
            } else {
                quoteData.put("contextafter", q.getContextAfter());
            }

            connection.getOutputStream().write(quoteData.toString().getBytes());

            int responseCode = connection.getResponseCode();
            if (responseCode < 200 || responseCode >= 300) {
                cfg.databaseError("Failed to add quote. Response code: " + responseCode);
            } else {
                updateDatabase();
            }
        } catch (Exception e) {
            cfg.databaseError("Error while adding quote: " + e.getMessage());
        }
    }

    @Override
    public void deleteQuoteByIndex(int idx) {
        cfg.databaseError("Deleting quote is not supported.");
    }

    @Override
    public void deleteQuoteById(UUID id) {
        cfg.databaseError("Deleting quote is not supported.");
    }

    @Override
    public void onQuoteUpdate(UpdateEvent e) {
        singleQuoteUpdate.add(e);
    }

    @Override
    public void onQuoteInsert(UpdateEvent e) {
    }

    @Override
    public void onQuoteDelete(UpdateEvent e) {
    }

    @Override
    public void onAllQuoteUpdate(UpdateEvent e) {
        allQuotes.add(e);
    }

    @Override
    public void configure() {
        cfg.configure();
    }

    /**
     * Interface for configuring the database.
     */
    public interface RemoteDatabaseConfigurator {
        /**
         * Sets the database for the configuration
         * 
         * @param db
         */
        public void setDatabase(RemoteDatabase db);

        /**
         * Called when the database is configured.
         */
        public void configure();

        /**
         * Called when the database is unable to connect to remote server.
         */
        public void connectionError();

        /**
         * Called when the database is unable to connect to remote server.
         * 
         * @param msg
         */
        public void connectionError(String msg);

        /**
         * Called when there is an error within the database
         * 
         * @param msg
         */
        public void databaseError(String msg);
    }
}
