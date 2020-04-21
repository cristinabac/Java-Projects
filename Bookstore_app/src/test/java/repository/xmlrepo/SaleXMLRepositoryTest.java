package repository.xmlrepo;

import domain.Sale;
import domain.validators.SaleValidator;
import domain.validators.Validator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.Repository;
import repository.xml_repository.SaleXMLRepository;

import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;

public class SaleXMLRepositoryTest {
    private Validator<Sale> validatorSale;
    private Repository<Long,Sale> testRepo;

    @Before
    public void setUp(){
        validatorSale = new SaleValidator();
        testRepo = new SaleXMLRepository(validatorSale,"data/xml/salestest.xml");
    }

    @After
    public  void tearDown(){
        testRepo = null;
        validatorSale = null;
    }

    @Test
    public void testAll(){
        assertEquals("Size should be 3",3, StreamSupport
                .stream(testRepo.findAll().spliterator(),false)
                .count());

        Sale sale = new Sale(55L,66L);
        testRepo.save(sale);

        assertEquals("Size should be 4",4, StreamSupport
                .stream(testRepo.findAll().spliterator(),false)
                .count());
        sale.setBookId(100L);
        testRepo.update(sale);
        assert StreamSupport.stream(testRepo.findAll().spliterator(),false)
                .filter(i->i.getId()==sale.getId())
                .map(i->i.getBookId()==100L)
                .count() == 1;
        testRepo.delete(sale.getId());

        assertEquals("Size should be 3",3, StreamSupport
                .stream(testRepo.findAll().spliterator(),false)
                .count());
    }
}
