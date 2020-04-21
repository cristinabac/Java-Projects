import domain.Book;
import domain.Client;
import domain.Sale;
import domain.validators.BookValidator;
import domain.validators.ClientValidator;
import domain.validators.SaleValidator;
import domain.validators.Validator;
import repository.Repository;
import repository.InMemoryRepository;
import repository.database_repository.BookDBRepository;
import repository.database_repository.ClientDBRepository;
import repository.database_repository.SaleDBRepository;
import repository.file_repository.BookFileRepository;
import repository.file_repository.ClientFileRepository;
import repository.file_repository.SalesFileRepository;
import repository.xml_repository.BookXMLRepository;
import repository.xml_repository.ClientXMLRepository;
import repository.xml_repository.SaleXMLRepository;
import service.BookService;
import service.ClientService;
import service.SaleService;
import ui.Console;

public class Main {

    public static void main(String[] args) {

        Validator<Client> clientValidator = new ClientValidator();
        Validator<Book> bookValidator = new BookValidator();
        Validator<Sale> saleValidator = new SaleValidator();

        Repository<Long, Client> clientRepository;
        Repository<Long,Book> bookRepository;
        Repository<Long,Sale> salesRepository;
        /*
        //InMemoryRepository
        clientRepository = new InMemoryRepository<>(clientValidator);
        bookRepository = new InMemoryRepository<>(bookValidator);
        salesRepository = new InMemoryRepository<Long, Sale>(saleValidator);
         */




        //File Repository
        clientRepository = new ClientFileRepository(clientValidator,"data/file/client.txt");
        bookRepository = new BookFileRepository(bookValidator,"data/file/book.txt");
        salesRepository = new SalesFileRepository(saleValidator,"data/file/sale.txt");

        //XML Repository
        clientRepository = new ClientXMLRepository(clientValidator,"data/xml/client.xml");
        bookRepository = new BookXMLRepository(bookValidator,"data/xml/book.xml");
        salesRepository = new SaleXMLRepository(saleValidator,"data/xml/sales.xml");

        //DB Repo
        // WE DID IT :D
        clientRepository = new ClientDBRepository(clientValidator,"postgres","admin");
        bookRepository = new BookDBRepository(bookValidator,"postgres","admin");
        salesRepository = new SaleDBRepository(saleValidator,"postgres","admin");


        ClientService clientService = new ClientService(clientRepository);
        BookService  bookService = new BookService(bookRepository);
        SaleService  salesService = new SaleService(bookRepository,clientRepository,salesRepository);

        Console console = new Console(clientService,bookService,salesService);
        console.runConsole();



    }
}
