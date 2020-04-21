package serverModule.entityService;

import commonModule.domain.BaseEntity;
import commonModule.domain.Book;
import commonModule.domain.Client;
import commonModule.domain.Sale;
import commonModule.domain.validators.BookValidator;
import commonModule.domain.validators.ClientValidator;
import commonModule.domain.validators.SaleValidator;
import commonModule.domain.validators.Validator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import serverModule.repository.database_repository.BookDBRepository;
import serverModule.repository.database_repository.ClientDBRepository;
import serverModule.repository.database_repository.SaleDBRepository;
import serverModule.repository.database_repository.SortingRepository;
import serverModule.repository.database_repository.sort_pack.Sort;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SaleServiceTest {
    private Validator<Sale> validator;
    private Validator<Book> bookValidator;
    private Validator<Client> clientValidator;
    private SortingRepository<Long,Client> clientSortingRepository;
    private SortingRepository<Long,Book> bookSortingRepository;
    private SortingRepository<Long,Sale> repository;
    private SaleService saleService;
/*
    @Before
    public void setUp(){
        validator = new SaleValidator();
        bookValidator = new BookValidator();
        clientValidator = new ClientValidator();
        bookSortingRepository = new BookDBRepository(bookValidator,"postgres","admin");
        clientSortingRepository = new ClientDBRepository(clientValidator,"postgres","admin");
        repository = new SaleDBRepository(validator,"postgres","admin");
        saleService = new SaleService(bookSortingRepository,clientSortingRepository,repository);
    }

    @After
    public void tearDown(){
        saleService = null;
        repository = null;
        bookSortingRepository = null;
        clientSortingRepository = null;
        validator = null;
        clientValidator = null;
        bookValidator = null;
    }

    @Test
    public void testFails(){
        Long maxBookID = Collections.max(StreamSupport.stream(bookSortingRepository.findAll().spliterator(),false)
                .map(BaseEntity::getId).collect(Collectors.toList()));
        maxBookID += 1;
        Long maxClientID = Collections.max(StreamSupport.stream(clientSortingRepository.findAll().spliterator(),false)
                .map(BaseEntity::getId).collect(Collectors.toList()));
        maxClientID += 1;
        Boolean result = saleService.buy(maxBookID,maxClientID-1);
        assert result.equals(false);
        result = saleService.buy(maxBookID-1,maxClientID);
        assert result.equals(false);
        result = saleService.buy(maxBookID,maxClientID);
        assert result.equals(false);
    }*/
}
