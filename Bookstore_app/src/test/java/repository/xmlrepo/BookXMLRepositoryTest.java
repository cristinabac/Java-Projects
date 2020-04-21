package repository.xmlrepo;

import domain.Book;
import domain.Client;
import domain.validators.BookValidator;
import domain.validators.ClientValidator;
import domain.validators.Validator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.Repository;
import repository.xml_repository.BookXMLRepository;
import repository.xml_repository.ClientXMLRepository;

import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;

public class BookXMLRepositoryTest {

    private final Long id = 5L;
    private final String title = "titlu";
    private final String author = "autor";
    private final Integer price = 1;
    private final String fileName = "data/xml/booktest.xml";
    private Book book;
    private Validator<Book> validator;
    private Repository<Long,Book> testRepository;

    @Before
    public void setUp(){
        book = new Book(title,author,price);
        book.setId(id);
        validator = new BookValidator();
        testRepository = new BookXMLRepository(validator,fileName);
    }

    @After
    public void tearDown(){
        book = null;
        testRepository=null;
        validator=null;
    }

    @Test
    public void testAll(){
        assertEquals("Size should be 3",3, StreamSupport.
                stream(testRepository.findAll().spliterator(),false).count());

        //Test add functions
        testRepository.save(book);
        assertEquals("Size should be 4",4,StreamSupport.
                stream(testRepository.findAll().spliterator(),false).count());

        //Test remove functions (super.remove and removeInternal)
        testRepository.delete(book.getId());
        assertEquals("Size should be 3",3,StreamSupport.
                stream(testRepository.findAll().spliterator(),false).count());

        //Test update functions (super.update and updateInternal)
        testRepository.save(book);
        assertEquals("Size should be 4",4,StreamSupport.
                stream(testRepository.findAll().spliterator(),false).count());
        book.setTitle("I've been updated");
        testRepository.update(book);
        assert "I've been updated".equals(testRepository.findOne(book.getId()).get().getTitle());

        testRepository.delete(book.getId()); //ca sa ramana fisierul de test la fel ca la inceput
    }
}
