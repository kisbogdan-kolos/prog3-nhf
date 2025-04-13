package quotes.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import quotes.PersonData;
import quotes.QuoteData;
import quotes.database.Database;
// import quotes.database.LocalDatabase;
import quotes.database.RemoteDatabase;
// import quotes.gui.database.ConfigureLocalDatabase;
import quotes.gui.database.ConfigureRemoteDatabase;
import quotes.gui.person.PersonList;
import quotes.gui.quote.QuoteList;
import quotes.gui.quote.ViewQuote;

public class MainWindow extends JFrame {
    private ViewQuote vq;
    private Database db;
    private PersonData pd;
    private QuoteData qd;
    private PersonList pl;
    private QuoteList ql;
    // private ConfigureLocalDatabase dbc;
    private ConfigureRemoteDatabase dbc;

    /**
     * Creates a {@code MainWindow} object
     */
    public MainWindow() {
        super("Jóságtár");

        initComponents();
    }

    /**
     * Initializes the components
     */
    private void initComponents() {
        initMenuBar();

        // dbc = new ConfigureLocalDatabase();
        // db = new LocalDatabase(dbc);
        dbc = new ConfigureRemoteDatabase();
        db = new RemoteDatabase(dbc);

        Database.UpdateEvent updateRandomQuote = idx -> newRandomQuote();
        db.onAllPersonUpdate(updateRandomQuote);
        db.onAllQuoteUpdate(updateRandomQuote);
        db.onPersonDelete(updateRandomQuote);
        db.onPersonInsert(updateRandomQuote);
        db.onPersonUpdate(updateRandomQuote);
        db.onQuoteDelete(updateRandomQuote);
        db.onQuoteInsert(updateRandomQuote);
        db.onQuoteUpdate(updateRandomQuote);

        pd = new PersonData(db);
        qd = new QuoteData(db);

        pl = new PersonList(pd);
        ql = new QuoteList(qd);

        vq = new ViewQuote();

        pl.addComponentListener(new DisableFrame(this));
        ql.addComponentListener(new DisableFrame(this));
        dbc.addComponentListener(new DisableFrame(this));

        add(vq.getTextArea(), BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setMinimumSize(new Dimension(500, 200));

        setLocationRelativeTo(null);

        db.init();
    }

    /**
     * Initializes the menu bar
     */
    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Fájl");
        menuBar.add(fileMenu);

        JMenuItem peopleList = new JMenuItem("Emberek kezelése");
        peopleList.addActionListener(e -> pl.showComponent());
        fileMenu.add(peopleList);

        JMenuItem quoteList = new JMenuItem("Idézetek kezelése");
        quoteList.addActionListener(e -> ql.showComponent());
        fileMenu.add(quoteList);

        JMenuItem addQuote = new JMenuItem("Új idézet hozzáadása");
        addQuote.addActionListener(e -> ql.showComponent(true));
        fileMenu.add(addQuote);

        JMenu databaseMenu = new JMenu("Adatbázis");
        menuBar.add(databaseMenu);

        JMenuItem configDatabase = new JMenuItem("Adatbázis beállítása");
        configDatabase.addActionListener(e -> db.configure());
        databaseMenu.add(configDatabase);

        JMenu randomQuoteMenu = new JMenu("Random idézet");
        menuBar.add(randomQuoteMenu);

        JMenuItem randomQuote = new JMenuItem("Új random idézet");
        randomQuote.addActionListener(e -> newRandomQuote());
        randomQuoteMenu.add(randomQuote);

        setJMenuBar(menuBar);
    }

    /**
     * Shows a random quote
     */
    private void newRandomQuote() {
        int len = db.getQuoteCount();
        if (len > 0) {
            int random = new Random().nextInt(len);
            vq.setQuote(db.getQuoteByIndex(random));
        }
    }
}
