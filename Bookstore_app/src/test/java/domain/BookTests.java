package domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BookTests {

    private static final Long id = 1L;
    private static final Long new_id = 2L;
    private static final String title = "title1";
    private static final String new_title = "title2";
    private static final String author = "author1";
    private static final String new_author = "author2";
    private static final int price = 123;
    private static final int new_price = 999;

    private Book book;

    @Before
    public void setUp() {
        book = new Book(title, author, price);
        book.setId(id);
    }

    @After
    public void tearDown() {
        book = null;
    }

    @Test
    public void testGetTitle() {
        assertEquals("Titles are not equal", title, book.getTitle());
    }

    @Test
    public void testSetTitle() {
        book.setTitle(new_title);
        assertEquals("Titles are not equal", new_title, book.getTitle());
    }

    @Test
    public void testGetId() {
        assertEquals("Ids are not equal", id, book.getId());
    }

    @Test
    public void testSetId() {
        book.setId(new_id);
        assertEquals("Ids are not equal", new_id, book.getId());
    }

    @Test
    public void testGetAuthor() {
        assertEquals("Authors are not equal", author, book.getAuthor());
    }

    @Test
    public void testSetAuthor() {
        book.setAuthor(new_author);
        assertEquals("Authors should be equal", new_author, book.getAuthor());
    }

    @Test
    public void testGetPrice() {
        assertEquals("Prices should be equal", price, book.getPrice());
    }

    @Test
    public void testSetPrice() {
        book.setPrice(new_price);
        assertEquals("Prices should be equal", new_price, book.getPrice());
    }

    @Test
    public void testToStringMethod(){
        assert book.toString() != null;
    }
}
