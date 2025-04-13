package hu.kszi2.josagtar.database;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import hu.kszi2.josagtar.database.LocalDatabase.LocalDatabaseConfigurator;

public class TestLocalDatabase {
    static private String invalidFormat0;
    static private String invalidFormat1;
    static private String invalidFormat2;
    static private String invalidFormat3;
    static private String validFormat0;
    static private String validFormat1;
    static private String invalidQuoteId;

    static private LocalDatabase validDatabase;

    @BeforeClass
    static public void setupFiles() throws IOException {
        invalidFormat0 = UUID.randomUUID().toString() + ".json";
        invalidFormat1 = UUID.randomUUID().toString() + ".json";
        invalidFormat2 = UUID.randomUUID().toString() + ".json";
        invalidFormat3 = UUID.randomUUID().toString() + ".json";
        validFormat0 = UUID.randomUUID().toString() + ".json";
        validFormat1 = UUID.randomUUID().toString() + ".json";
        invalidQuoteId = UUID.randomUUID().toString() + ".json";

        FileWriter fw = new FileWriter(invalidFormat0);
        PrintWriter pw = new PrintWriter(fw);
        pw.println("invalid json");
        pw.close();

        fw = new FileWriter(invalidFormat1);
        pw = new PrintWriter(fw);
        pw.println("{}");
        pw.close();

        fw = new FileWriter(invalidFormat2);
        pw = new PrintWriter(fw);
        pw.println("{\"people\":[]");
        pw.close();

        fw = new FileWriter(invalidFormat3);
        pw = new PrintWriter(fw);
        pw.println("{\"quotes\":[]}");
        pw.close();

        fw = new FileWriter(validFormat0);
        pw = new PrintWriter(fw);
        pw.println("{\"quotes\":[],\"people\":[]}");
        pw.close();

        fw = new FileWriter(validFormat1);
        pw = new PrintWriter(fw);
        pw.println(
                "{\"people\":[{\"fullName\":\"AAA\",\"id\":\"b18b5755-e811-4866-8e99-9161e65b7937\",\"shortName\":\"BBB\"},{\"notes\":\"EEE\",\"fullName\":\"CCC\",\"id\":\"df30b7bd-9d41-406a-af01-971890b9256d\",\"shortName\":\"DDD\"}],\"quotes\":[{\"quote\":{\"contextBefore\":\"FFF\",\"quote\":\"GGG\"},\"person\":\"df30b7bd-9d41-406a-af01-971890b9256d\",\"id\":\"1442adfd-8e90-4ec0-8505-ba3ed197fff6\"},{\"quote\":{\"quote\":\"HHH\"},\"person\":\"b18b5755-e811-4866-8e99-9161e65b7937\",\"id\":\"2f32e147-0d53-400d-ad84-064351e7973a\"}]}");
        pw.close();

        fw = new FileWriter(invalidQuoteId);
        pw = new PrintWriter(fw);
        pw.println(
                "{\"people\":[{\"fullName\":\"AAA\",\"id\":\"b18b5755-e811-4866-8e99-9161e65b7937\",\"shortName\":\"BBB\"},{\"notes\":\"EEE\",\"fullName\":\"CCC\",\"id\":\"df30b7bd-9d41-406a-af01-971890b9256d\",\"shortName\":\"DDD\"}],\"quotes\":[{\"quote\":{\"contextBefore\":\"FFF\",\"quote\":\"GGG\"},\"person\":\"df30b7bd-9d41-406a-af01-971890b9256d\",\"id\":\"1442adfd-8e90-4ec0-8505-ba3ed197fff6\"},{\"quote\":{\"quote\":\"HHH\"},\"person\":\"b18b5755-e811-4866-8e99-9161e65b7938\",\"id\":\"2f32e147-0d53-400d-ad84-064351e7973a\"}]}");
        pw.close();
    }

    @Before
    public void initDatabase() throws IOException {
        FileWriter fw = new FileWriter(validFormat1);
        PrintWriter pw = new PrintWriter(fw);
        pw.println(
                "{\"people\":[{\"fullName\":\"AAA\",\"id\":\"b18b5755-e811-4866-8e99-9161e65b7937\",\"shortName\":\"BBB\"},{\"notes\":\"EEE\",\"fullName\":\"CCC\",\"id\":\"df30b7bd-9d41-406a-af01-971890b9256d\",\"shortName\":\"DDD\"}],\"quotes\":[{\"quote\":{\"contextBefore\":\"FFF\",\"quote\":\"GGG\"},\"person\":\"df30b7bd-9d41-406a-af01-971890b9256d\",\"id\":\"1442adfd-8e90-4ec0-8505-ba3ed197fff6\"},{\"quote\":{\"quote\":\"HHH\"},\"person\":\"b18b5755-e811-4866-8e99-9161e65b7937\",\"id\":\"2f32e147-0d53-400d-ad84-064351e7973a\"}]}");
        pw.close();
        validDatabase = new LocalDatabase(new TestConfigurator(false));
        validDatabase.init(validFormat1);
    }

    @Test(expected = OnSetDatabase.class)
    public void testConfiguratorSetup() {
        new LocalDatabase(new TestConfigurator(true));
    }

    @Test(expected = OnFileAccessError.class)
    public void testNoInputFile() {
        LocalDatabase db = new LocalDatabase(new TestConfigurator(false));

        db.init(UUID.randomUUID().toString() + ".json");
    }

    @Test(expected = OnFileFormatError.class)
    public void testInvalidFormat0() {
        LocalDatabase db = new LocalDatabase(new TestConfigurator(false));

        db.init(invalidFormat0);
    }

    @Test(expected = OnFileFormatError.class)
    public void testInvalidFormat1() {
        LocalDatabase db = new LocalDatabase(new TestConfigurator(false));

        db.init(invalidFormat1);
    }

    @Test(expected = OnFileFormatError.class)
    public void testInvalidFormat2() {
        LocalDatabase db = new LocalDatabase(new TestConfigurator(false));

        db.init(invalidFormat2);
    }

    @Test(expected = OnFileFormatError.class)
    public void testInvalidFormat3() {
        LocalDatabase db = new LocalDatabase(new TestConfigurator(false));

        db.init(invalidFormat3);
    }

    @Test
    public void testValidFormat0() {
        LocalDatabase db = new LocalDatabase(new TestConfigurator(false));

        db.init(validFormat0);
    }

    @Test
    public void testValidFormat1() {
        LocalDatabase db = new LocalDatabase(new TestConfigurator(false));

        db.init(validFormat1);
    }

    @Test(expected = OnFileFormatError.class)
    public void testInvalidQuoteId() {
        LocalDatabase db = new LocalDatabase(new TestConfigurator(false));

        db.init(invalidQuoteId);
    }

    @Test
    public void testQuoteCount() {
        Assert.assertEquals(2, validDatabase.getQuoteCount());
    }

    @Test
    public void testPersonCount() {
        Assert.assertEquals(2, validDatabase.getPersonCount());
    }

    @Test
    public void testGetQuote() {
        Quote quote = validDatabase.getQuoteByIndex(0);

        Assert.assertEquals("GGG", quote.getQuote());
        Assert.assertEquals("FFF", quote.getContextBefore());
        Assert.assertEquals("CCC", quote.getAuthor().getFullName());
        Assert.assertEquals("DDD", quote.getAuthor().getShortName());
        Assert.assertEquals("EEE", quote.getAuthor().getNotes());
    }

    @Test
    public void testGetPerson() {
        Person person = validDatabase.getPersonByIndex(0);

        Assert.assertEquals("AAA", person.getFullName());
        Assert.assertEquals("BBB", person.getShortName());
        Assert.assertEquals("", person.getNotes());
        Assert.assertEquals(1, person.getQuoteCount());
    }

    @Test
    public void testAddPerson() {
        Person person = new Person("III", "JJJ", "KKK");

        validDatabase.addPerson(person);

        Assert.assertEquals(3, validDatabase.getPersonCount());
        Assert.assertEquals("III", validDatabase.getPersonByIndex(2).getFullName());
        Assert.assertEquals("JJJ", validDatabase.getPersonByIndex(2).getShortName());
        Assert.assertEquals("KKK", validDatabase.getPersonByIndex(2).getNotes());
    }

    @Test
    public void testAddQuote() {
        Person person = validDatabase.getPersonByIndex(1);
        Quote quote = new Quote(person, "LLL", "MMM", "NNN");

        validDatabase.addQuote(quote);

        Assert.assertEquals(3, validDatabase.getQuoteCount());
        Assert.assertEquals(quote, validDatabase.getQuoteByIndex(2));
        Assert.assertEquals("LLL", validDatabase.getQuoteByIndex(2).getContextBefore());
        Assert.assertEquals("NNN", validDatabase.getQuoteByIndex(2).getContextAfter());
        Assert.assertEquals("MMM", validDatabase.getQuoteByIndex(2).getQuote());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemovePersonWithQuotes() {

        validDatabase.deletePersonByIndex(0);
    }

    @Test
    public void testRemovePersonWithoutQuotes() {
        validDatabase.deleteQuoteByIndex(0);
        validDatabase.deletePersonByIndex(1);

        Assert.assertEquals(1, validDatabase.getPersonCount());
        Assert.assertEquals("AAA", validDatabase.getPersonByIndex(0).getFullName());
        Assert.assertEquals("BBB", validDatabase.getPersonByIndex(0).getShortName());
        Assert.assertEquals("", validDatabase.getPersonByIndex(0).getNotes());
        Assert.assertEquals(1, validDatabase.getPersonByIndex(0).getQuoteCount());
    }

    @Test
    public void testRemoveQuote() {

        validDatabase.deleteQuoteByIndex(0);

        Assert.assertEquals(1, validDatabase.getQuoteCount());
        Assert.assertEquals("HHH", validDatabase.getQuoteByIndex(0).getQuote());
        Assert.assertEquals("", validDatabase.getQuoteByIndex(0).getContextBefore());
        Assert.assertEquals("", validDatabase.getQuoteByIndex(0).getContextAfter());
        Assert.assertEquals("AAA", validDatabase.getQuoteByIndex(0).getAuthor().getFullName());
        Assert.assertEquals("BBB", validDatabase.getQuoteByIndex(0).getAuthor().getShortName());
        Assert.assertEquals("", validDatabase.getQuoteByIndex(0).getAuthor().getNotes());
    }

    @Test(expected = TestException.class)
    public void testOnPersonUpdate() {
        validDatabase.onPersonUpdate(idx -> {
            throw new TestException();
        });

        validDatabase.getPersonByIndex(0).setFullName("XXX");
    }

    @Test(expected = TestException.class)
    public void testOnPersonInsert() {
        validDatabase.onPersonInsert(idx -> {
            throw new TestException();
        });

        validDatabase.addPerson(new Person("XXX", "YYY", "ZZZ"));
    }

    @Test(expected = TestException.class)
    public void testOnPersonDelete() {
        validDatabase.onPersonDelete(idx -> {
            throw new TestException();
        });

        validDatabase.deleteQuoteByIndex(1);

        validDatabase.deletePersonByIndex(0);
    }

    @Test(expected = TestException.class)
    public void testOnQuoteUpdate() {
        validDatabase.onQuoteUpdate(idx -> {
            throw new TestException();
        });

        validDatabase.getQuoteByIndex(0).setQuote("XXX");
    }

    @Test(expected = TestException.class)
    public void testOnQuoteInsert() {
        validDatabase.onQuoteInsert(idx -> {
            throw new TestException();
        });

        validDatabase.addQuote(new Quote(validDatabase.getPersonByIndex(0), "XXX", "YYY", "ZZZ"));
    }

    @Test(expected = TestException.class)
    public void testOnQuoteDelete() {
        validDatabase.onQuoteDelete(idx -> {
            throw new TestException();
        });

        validDatabase.deleteQuoteByIndex(0);
    }

    private class TestConfigurator implements LocalDatabaseConfigurator {
        private boolean throwOnSetDatabase = false;

        public TestConfigurator(boolean throwOnSetDatabase) {
            this.throwOnSetDatabase = throwOnSetDatabase;
        }

        @Override
        public void setDatabase(LocalDatabase db) {
            if (throwOnSetDatabase) {
                throw new OnSetDatabase();
            }
        }

        @Override
        public void configure() {
            throw new OnConfigure();
        }

        @Override
        public void fileAccessError() {
            throw new OnFileAccessError();
        }

        @Override
        public void fileFormatError(String msg) {
            throw new OnFileFormatError();
        }

        @Override
        public void fileSaveError(String msg) {
            throw new OnFileSaveError();
        }

    }

    private class OnSetDatabase extends RuntimeException {
    }

    private class OnConfigure extends RuntimeException {
    }

    private class OnFileAccessError extends RuntimeException {
    }

    private class OnFileFormatError extends RuntimeException {
    }

    private class OnFileSaveError extends RuntimeException {
    }

    private class TestException extends RuntimeException {
    }
}
