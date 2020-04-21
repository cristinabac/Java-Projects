package repository.dbrepo;

import domain.Sale;
import domain.validators.SaleValidator;
import domain.validators.Validator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.database_repository.SaleDBRepository;
import repository.database_repository.SortingRepository;

import java.util.stream.StreamSupport;

public class SaleDBRepositoryTest {
    private Validator<Sale> validator;
    private SortingRepository<Long,Sale> testRepository;

    @Before
    public void setUp(){
        validator = new SaleValidator();
        testRepository = new SaleDBRepository(validator,"postgres","admin");
    }

    @After
    public void tearDown(){
        testRepository = null;
        validator = null;
    }

    @Test
    public  void testCrudOperations(){
        Sale sale = new Sale();
        sale.setId(1L);
        sale.setBookId(1L);
        sale.setClientId(1L);
        int size = (int) StreamSupport.stream(testRepository.findAll().spliterator(),false)
                .count();
        testRepository.save(sale);
        assert size + 1 == (int) StreamSupport.stream(testRepository.findAll().spliterator(),false).count();
        sale.setClientId(2L);
        assert StreamSupport.stream(testRepository.findAll().spliterator(),false)
                .filter(i->i.getId().equals(sale.getId()) && i.getClientId().equals(2L)).count() == 0;
        testRepository.update(sale);
        assert StreamSupport.stream(testRepository.findAll().spliterator(),false)
                .filter(i->i.getId().equals(sale.getId()) && i.getClientId().equals(2L)).count() == 1;

        size = (int) StreamSupport.stream(testRepository.findAll().spliterator(),false)
                .count();
        testRepository.delete(sale.getId());
        assert size - 1 == (int) StreamSupport.stream(testRepository.findAll().spliterator(),false)
                .count();
    }
}
