package service;

import domain.Book;
import domain.Client;
import domain.Sale;
import domain.validators.*;
import repository.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;

public class SaleServiceTest {
    private Book book;
    private Client client;
    private Validator<Book> bookValidator;
    private Validator<Client> clientValidator;
    private Validator<Sale> saleValidator;
    private Repository<Long, Book> bookRepository;
    private Repository<Long, Client> clientRepository;
    private Repository<Long, Sale> saleRepository;
    private SaleService testSaleService;

    @Before
    public void setUp() {
        book = new Book("Poezii", "Eminescu", 15);
        book.setId(1L);
        client = new Client("Andrei");
        client.setId(1L);
        bookValidator = new BookValidator();
        clientValidator = new ClientValidator();
        saleValidator = new SaleValidator();
        bookRepository = new InMemoryRepository<>(bookValidator);
        clientRepository = new InMemoryRepository<>(clientValidator);
        saleRepository = new InMemoryRepository<>(saleValidator);
        bookRepository.save(book);
        clientRepository.save(client);
        testSaleService = new SaleService(bookRepository, clientRepository, saleRepository);
    }

    @After
    public void tearDown() {
        book = null;
        client = null;
        bookValidator = null;
        clientValidator = null;
        saleValidator = null;
        bookRepository = null;
        clientRepository = null;
        saleRepository = null;
        testSaleService = null;
    }

    @Test
    public void testGetBookRepository()
    {
        Iterable<Book> books = testSaleService.getBookRepository().findAll();
        long cont = StreamSupport.stream(books.spliterator(), false).count();
        assertEquals("the length should be 1", 1, cont);
    }

    @Test
    public void testGetClientRepository() {
        Iterable<Client> clients = testSaleService.getClientRepository().findAll();
        long cont = StreamSupport.stream(clients.spliterator(), false).count();
        assertEquals("the length should be 1", 1, cont);
    }

    @Test
    public void testBuyAndGetSaleRepository()
    {
        assertEquals("the money spent by the first element should be 0", 0, testSaleService.getClientRepository().findAll().iterator().next().getMoneySpent());
        testSaleService.buy(1L, 1L); //client 1 buys bok 1
        Iterable<Sale> sales = testSaleService.getSaleRepository().findAll();
        long cont = StreamSupport.stream(sales.spliterator(), false).count();
        assertEquals("the length should be 1", 1, cont);
        assertEquals("the bookId of the first element should be 1", 1, testSaleService.getSaleRepository().findAll().iterator().next().getBookId().intValue());
        assertEquals("the money spent by the first element should be 15", 15, testSaleService.getClientRepository().findAll().iterator().next().getMoneySpent());
    }


    @Test
    public void testGetAllMethod(){
       assertEquals("No elements",0, testSaleService.getAllSales().size());
    }


}
