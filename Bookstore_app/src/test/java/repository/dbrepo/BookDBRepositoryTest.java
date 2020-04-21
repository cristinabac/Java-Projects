package repository.dbrepo;

import domain.Book;
import domain.validators.BookValidator;
import domain.validators.ClientValidator;
import domain.validators.Validator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.database_repository.BookDBRepository;
import repository.database_repository.SortingRepository;

import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;

public class BookDBRepositoryTest {

    Validator<Book> validator;
    SortingRepository<Long, Book> testRepository;
    //private String name1="this is fucked again";
    //private String name2="uptade -- do not use this name";
    private Book book1;
    private Book book2;


    @Before
    public void setUp(){
        validator = new BookValidator();
        testRepository = new BookDBRepository(validator,"postgres","admin");
        book1 = new Book();
        book1.setId(100L);
        book1.setTitle("titlu1");
        book1.setAuthor("autor1");
        book1.setPrice(11);
        book2 = new Book();
        book2.setId(100L);
        book2.setTitle("titlu2");
        book2.setAuthor("autor2");
        book2.setPrice(22);
    }

    @After
    public void tearDown(){
        testRepository=null;
        validator=null;
        book1=null;
        book2=null;
    }

    @Test
    public void findAllMethodTest(){
        assert  StreamSupport.stream(testRepository.findAll().spliterator(),false).count() > 0;
    }

    @Test
    public void findOneMethodTest1(){
        //id does exist
        Optional<Book> result = testRepository.findOne(1L);
        result.ifPresentOrElse(i->{assert  true;},()->{assert false;});
    }

    @Test
    public void findOneMethodTest2(){
        //id that does not exist
        Optional<Book> result = testRepository.findOne(111L);
        result.ifPresentOrElse(i->{assert  false;},()->{assert true;});
    }


    @Test
    public void testCrudOperations(){
        Integer size = (int) StreamSupport.stream(testRepository.findAll().spliterator(),false).count();
        testRepository.save(book1);
        assert  size + 1 == StreamSupport.stream(testRepository.findAll().spliterator(),false).count();

        size = (int) StreamSupport.stream(testRepository.findAll().spliterator(),false).count();
        testRepository.save(book1);
        assert  size  == StreamSupport.stream(testRepository.findAll().spliterator(),false).count();

        testRepository.update(book2);
        assert StreamSupport.stream(testRepository.findAll().spliterator(),false)
                .filter(i->i.getTitle().equals(book2.getTitle())).count() == 1;

        size = (int) StreamSupport.stream(testRepository.findAll().spliterator(),false).count();
        testRepository.delete(book2.getId());
        assert size - 1 == StreamSupport.stream(testRepository.findAll().spliterator(),false).count();
    }

}
