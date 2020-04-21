package repository.filerepo;

import domain.Book;
import domain.validators.BookValidator;
import domain.validators.Validator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.Repository;
import repository.file_repository.BookFileRepository;

import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;

public class BookFileRepositoryTests {
    private static final String filename = "data/file/booktest.txt";
    private Validator<Book> bookValidator;
    private Repository<Long,Book> bookRepository;
    private Book book;

    @Before
    public void setUp(){
        bookValidator = new BookValidator();
        bookRepository = new BookFileRepository(bookValidator, filename);
        book = new Book("title","author",20);
        book.setId(3L);
    }

    @After
    public void tearDowm(){
        bookValidator = null;
        bookRepository = null;
        book = null;
    }

    @Test
    public void loadFromFileTest(){
        assertEquals("size should be 2", 2, StreamSupport.stream(bookRepository.findAll().spliterator(), false).
                count());

        bookRepository.save(book);
        assertEquals("size should be 3",3, StreamSupport.stream(bookRepository.findAll().spliterator(),false).count());
        bookRepository.delete(3L);
        assertEquals("size should be 2",2, StreamSupport.stream(bookRepository.findAll().spliterator(),false).count());

        book.setId(1L);
        book.setAuthor("updated");
        bookRepository.update(book);
        assertEquals("size should be 2",2, StreamSupport.stream(bookRepository.findAll().spliterator(),false).count());
    }

}


