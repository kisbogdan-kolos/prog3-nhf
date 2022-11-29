package quotes.database;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestQuote {
    private Quote quote;
    private Person author;

    @Before
    public void initQuote() {
        author = new Person("Teszt Elek", "TesztE", "tesztellek");
        quote = new Quote(author, "contextBefore", "quote", "contextAfter");
    }

    @Test
    public void testCreateWithId() {
        UUID id = UUID.randomUUID();
        Quote q = new Quote(id, author, "contextBefore", "quote", "contextAfter");
        Assert.assertEquals(id, q.getId());
        Assert.assertEquals(author, q.getAuthor());
        Assert.assertEquals("contextBefore", q.getContextBefore());
        Assert.assertEquals("quote", q.getQuote());
        Assert.assertEquals("contextAfter", q.getContextAfter());
    }

    @Test
    public void testCreateWithoutId() {
        Quote q = new Quote(author, "contextBefore", "quote", "contextAfter");
        Assert.assertNotNull(q.getId());
        Assert.assertEquals(author, q.getAuthor());
        Assert.assertEquals("contextBefore", q.getContextBefore());
        Assert.assertEquals("quote", q.getQuote());
        Assert.assertEquals("contextAfter", q.getContextAfter());
    }

    @Test
    public void testSetValidAuthor() {
        Person p = new Person("Teszt Béla", "TesztB", "teszt");
        quote.setAuthor(p);
        Assert.assertEquals(p, quote.getAuthor());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullAuthor() {
        quote.setAuthor(null);
    }

    @Test
    public void testSetValidContextBefore() {
        quote.setContextBefore("AAA");
        Assert.assertEquals("AAA", quote.getContextBefore());
    }

    @Test
    public void testSetNullContextBefore() {
        quote.setContextBefore(null);
        Assert.assertEquals("", quote.getContextBefore());
    }

    @Test
    public void testSetValidQuote() {
        quote.setQuote("AAA");
        Assert.assertEquals("AAA", quote.getQuote());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmptyQuote() {
        quote.setQuote("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullQuote() {
        quote.setQuote(null);
    }

    @Test
    public void testSetValidContextAfter() {
        quote.setContextAfter("AAA");
        Assert.assertEquals("AAA", quote.getContextAfter());
    }

    @Test
    public void testSetNullContextAfter() {
        quote.setContextAfter(null);
        Assert.assertEquals("", quote.getContextAfter());
    }

    @Test
    public void testSetAllValidParams() {
        Person p = new Person("Teszt Béla", "TesztB", "teszt");
        quote.setAllParams(p, "AAA", "AAA", "AAA");
        Assert.assertEquals(p, quote.getAuthor());
        Assert.assertEquals("AAA", quote.getContextBefore());
        Assert.assertEquals("AAA", quote.getQuote());
        Assert.assertEquals("AAA", quote.getContextAfter());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAllNullQuote() {
        quote.setAllParams(author, "AAA", null, "AAA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAllEmptyQuote() {
        quote.setAllParams(author, "AAA", "", "AAA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAllNullAuthor() {
        quote.setAllParams(null, "AAA", "AAA", "AAA");
    }

    @Test
    public void testSetAllNullContextBefore() {
        quote.setAllParams(author, null, "AAA", "AAA");
        Assert.assertEquals("", quote.getContextBefore());
    }

    @Test
    public void testSetAllNullContextAfter() {
        quote.setAllParams(author, "AAA", "AAA", null);
        Assert.assertEquals("", quote.getContextAfter());
    }

    @Test(expected = TestException.class)
    public void testDataChangedAuthor() {
        quote.setDataChanged(q -> {
            throw new TestException();
        });

        quote.setAuthor(author);
    }

    @Test(expected = TestException.class)
    public void testDataChangedContextBefore() {
        quote.setDataChanged(q -> {
            throw new TestException();
        });

        quote.setContextBefore("AAA");
    }

    @Test(expected = TestException.class)
    public void testDataChangedQuote() {
        quote.setDataChanged(q -> {
            throw new TestException();
        });

        quote.setQuote("AAA");
    }

    @Test(expected = TestException.class)
    public void testDataChangedContextAfter() {
        quote.setDataChanged(q -> {
            throw new TestException();
        });

        quote.setContextAfter("AAA");
    }

    private class TestException extends RuntimeException {
    }
}
