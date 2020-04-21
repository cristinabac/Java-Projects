package repository.filerepo;

import domain.BaseEntity;
import domain.Sale;
import domain.validators.SaleValidator;
import domain.validators.Validator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.Repository;
import repository.file_repository.SalesFileRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;

public class SaleFileRepositoryTests {
    private final Long bookID1 = 20L;
    private final Long  clientID1 = 30L;
    private Sale sale1;
    private final String filePath = "data/file/saletest.txt";
    private Validator<Sale> saleValidator;
    private Repository<Long,Sale> testRepository;

    @Before
    public void setUP(){
        saleValidator = new SaleValidator();
        testRepository = new SalesFileRepository(saleValidator,filePath);
    }

    @After
    public void tearDown(){
        testRepository = null;
        saleValidator = null;
    }

    @Test
    public void testAllMethods(){
        assertEquals("We should have 6 objects",6,StreamSupport.
                stream(testRepository.findAll().spliterator(),false).count());


        List<Long> ids = (List<Long>) StreamSupport.stream(testRepository.findAll().spliterator(),false)
                .map(BaseEntity::getId).collect(Collectors.toList());


        Long id1 = testRepository.findOne(ids.get(1)).get().getBookId();
        Long id2 = testRepository.findOne(ids.get(1)).get().getClientId();

        Sale sale = new Sale(bookID1,clientID1);
        sale.setId(ids.get(1));
        testRepository.update(sale);
        Long expID = testRepository.findOne(ids.get(1)).get().getBookId();
        assert  20L == expID;

        testRepository.delete(ids.get(1));
        assertEquals("We should have 5 objects",5,StreamSupport.
                stream(testRepository.findAll().spliterator(),false).count());

        Sale sale1 = new Sale(id1,id2);
        sale1.setId(ids.get(1));
        testRepository.save(sale1);

        assertEquals("We should have 6 objects",6,StreamSupport.
                stream(testRepository.findAll().spliterator(),false).count());


    }
}
