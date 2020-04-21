package serverModule.entityService;


import commonModule.domain.Book;
import commonModule.domain.validators.BookValidator;
import commonModule.domain.validators.Validator;

import serverModule.repository.database_repository.SortingRepository;

import java.util.List;

public class BookServiceTest {
    private Validator<Book> validator;
    private SortingRepository<Long,Book> repository;
    private BookService service;

    /*
    @Before
    public void setUp(){
        Validator<Book> validator = new BookValidator();
        SortingRepository<Long,Book> repository = new BookDBRepository(validator, "postgres","admin");
        service = new BookService(repository);
    }

    @After
    public void tearDown(){
        service = null;
        repository = null;
        validator = null;
    }

    @Test
    public void testAddMethod1(){
        List<Book> lst = service.getAllBooks();
        Long existingID = lst.get(0).getId();
        Book fakeBook = new Book();
        fakeBook.setId(existingID);
        fakeBook.setAuthor("aaa");
        fakeBook.setTitle("bbb");
        fakeBook.setPrice(10);
        Boolean result = service.addBook(fakeBook);
        assert  result.equals(false);

    }

    @Test
    public void testAddMethodAndRemoveMethod(){
        Book fakeBook = new Book();
        fakeBook.setId(1000L);
        fakeBook.setAuthor("aaa");
        fakeBook.setTitle("bbb");
        fakeBook.setPrice(10);
        Boolean result = service.addBook(fakeBook);
        assert  result.equals(true);
        result = service.deleteBook(1000L);
        assert result.equals(true);
    }

    @Test
    public void testUpdate(){
        List<Book> lst = service.getAllBooks();
        Long existingID = lst.get(0).getId();
        Book oldBook = lst.get(0);
        Book fakeBook = new Book();
        fakeBook.setId(oldBook.getId());
        fakeBook.setAuthor("aaa");
        fakeBook.setTitle("bbb");
        fakeBook.setPrice(10);
        Boolean result = service.updateBook(fakeBook);
        assert  result.equals(true);
        result = service.updateBook(oldBook);
        assert  result.equals(true);
    }

*/

}
