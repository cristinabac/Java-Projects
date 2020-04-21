package service;

import domain.Book;
import domain.validators.BookValidator;
import domain.validators.Validator;
import domain.validators.ValidatorException;
import repository.Repository;
import repository.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BookServiceTest {

    public static final Long id1=(long)1;
    public static final Long id2=(long)2;
    public static final Long id3=(long)3;
    public static final Long id4=(long)4;
    private Book book1;
    private Book book2;
    private Book book3;
    private Book invalidBook;
    private Validator<Book> validator;
    private Repository<Long, Book> repository;
    private BookService testBookService;

    @Before
    public void setUp() {
        book1 = new Book("Poezii", "Eminescu", 15);
        book1.setId(id1);
        book2 = new Book("Cu usile inchise", "Sartre", 10);
        book2.setId(id2);
        book3 = new Book("La rascruce de vanturi", "Emily Bronte", 20);
        book3.setId(id3);
        invalidBook = new Book();
        validator = new BookValidator();
        repository = new InMemoryRepository<>(validator);
        testBookService = new BookService(repository);
        testBookService.addBook(book1);
        testBookService.addBook(book2);
    }

    @After
    public void tearDown() {
        book1 = null;
        book2 = null;
        book3 = null;
        validator = null;
        repository = null;
        testBookService = null;
    }

    @Test
    public void testAddBook(){
        assertEquals("Size should be 2",2, testBookService.getAllBooks().size());
        testBookService.addBook(book3);
        assertEquals("Size should be 3",3, testBookService.getAllBooks().size());
    }

    @Test
    public void testGetAllBooks()
    {
        assertEquals("the length should be 2", 2, testBookService.getAllBooks().size());
    }

    @Test
    public void testUpdateBook() {
        assertEquals("Should work",0,
                testBookService.getAllBooks().stream().
                        map(Book::getAuthor).filter(e -> e.equals("Update")).count());
        book1.setAuthor("Update");
        testBookService.updateBook(book1);
        assertEquals("Should work",1,
                testBookService.getAllBooks().stream().
                        map(Book::getAuthor).filter(e -> e.equals("Update")).count());
    }

    @Test
    public void testErrorAddBook(){
        try{
            testBookService.addBook(invalidBook);
            assert false;
        }
        catch (ValidatorException exc){
            assert true;
        }
    }


    @Test
    public void testDeleteBook1(){
        testBookService.addBook(book3);
        assertEquals("Size should be 3",3, testBookService.getAllBooks().size());
        testBookService.deleteBook(id3);
        assertEquals("Size should be 2",2, testBookService.getAllBooks().size());
    }

    @Test
    public void testDeleteBook2(){
        //test for deleting an element that does not appear in repository
        testBookService.addBook(book3);
        assertEquals("Size should be 3",3, testBookService.getAllBooks().size());
        testBookService.deleteBook(id4);
        assertEquals("Size should be 3",3, testBookService.getAllBooks().size());
    }

    @Test
    public void testFilterBooksByTitle()
    {
        testBookService.addBook(book3);
        List<Book> filteredBooks = new ArrayList<>(testBookService.filterByTitle("Poezii"));
        assertEquals("the length should be 1", 1, filteredBooks.size());
        assertEquals("The price should be 15", 15, filteredBooks.get(0).getPrice());
    }

    @Test
    public void testFilterBooksByAuthor()
    {
        testBookService.addBook(book3);
        assertEquals("the length should be 3", 3, testBookService.getAllBooks().size());
        List<Book> filteredBooks = new ArrayList<>(testBookService.filterByAuthor("Sartre"));
        assertEquals("the length should be 1", 1, filteredBooks.size());
        assertEquals("The price should be 10", 10, filteredBooks.get(0).getPrice());
    }

}
