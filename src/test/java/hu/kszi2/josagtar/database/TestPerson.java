package hu.kszi2.josagtar.database;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestPerson {
    private Person person;

    @Before
    public void initPerson() {
        person = new Person("Teszt Elek", "TesztT", "tesztellek");
    }

    @Test
    public void testCreateWithId() {
        UUID id = UUID.randomUUID();
        Person p = new Person(id, "Teszt Elek", "TesztE", "tesztellek");
        Assert.assertEquals(id, p.getId());
        Assert.assertEquals("Teszt Elek", p.getFullName());
        Assert.assertEquals("TesztE", p.getShortName());
        Assert.assertEquals("tesztellek", p.getNotes());
    }

    @Test
    public void testCreateWithoutId() {
        Person p = new Person("Teszt Elek", "TesztE", "tesztellek");
        Assert.assertNotNull(p.getId());
        Assert.assertEquals("Teszt Elek", p.getFullName());
        Assert.assertEquals("TesztE", p.getShortName());
        Assert.assertEquals("tesztellek", p.getNotes());
    }

    @Test
    public void testSetValidName() {
        person.setFullName("Teszt Béla");
        Assert.assertEquals("Teszt Béla", person.getFullName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmptyName() {
        person.setFullName("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullName() {
        person.setFullName(null);
    }

    @Test
    public void testSetValidShortName() {
        person.setShortName("TesztB");
        Assert.assertEquals("TesztB", person.getShortName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmptyShortName() {
        person.setShortName("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullShortName() {
        person.setShortName(null);
    }

    @Test
    public void testSetNotes() {
        person.setNotes("Teszt");
        Assert.assertEquals("Teszt", person.getNotes());
    }

    @Test
    public void testSetNullNotes() {
        person.setNotes(null);
        Assert.assertEquals("", person.getNotes());
    }

    @Test
    public void testSetAllValidParams() {
        person.setAllParams("Teszt Béla", "TesztB", "teszt");
        Assert.assertEquals("Teszt Béla", person.getFullName());
        Assert.assertEquals("TesztB", person.getShortName());
        Assert.assertEquals("teszt", person.getNotes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAllEmptyName() {
        person.setAllParams("", "TesztB", "teszt");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAllNullName() {
        person.setAllParams(null, "TesztB", "teszt");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAllEmptyShortName() {
        person.setAllParams("Teszt Béla", "", "teszt");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAllNullShortName() {
        person.setAllParams("Teszt Béla", null, "teszt");
    }

    @Test
    public void testSetAllNullNotes() {
        person.setAllParams("Teszt Béla", "TesztB", null);
        Assert.assertEquals("", person.getNotes());
    }

    @Test
    public void testInitalQuoteCount() {
        Assert.assertEquals(0, person.getQuoteCount());
    }

    @Test
    public void testSetQuoteCount() {
        person.setQuoteCount(5);
        Assert.assertEquals(5, person.getQuoteCount());
    }

    @Test(expected = TestException.class)
    public void testDataChangedName() {
        person.setDataChanged(p -> {
            throw new TestException();
        });

        person.setFullName("Teszt Béla");
    }

    @Test(expected = TestException.class)
    public void testDataChangedShortName() {
        person.setDataChanged(p -> {
            throw new TestException();
        });

        person.setShortName("TesztB");
    }

    @Test(expected = TestException.class)
    public void testDataChangedNotes() {
        person.setDataChanged(p -> {
            throw new TestException();
        });

        person.setNotes("teszt");
    }

    private class TestException extends RuntimeException {
    }
}
