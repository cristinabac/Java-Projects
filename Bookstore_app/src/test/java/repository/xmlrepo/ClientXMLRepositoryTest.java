package repository.xmlrepo;

import domain.Client;
import domain.validators.ClientValidator;
import domain.validators.Validator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.Repository;
import repository.xml_repository.ClientXMLRepository;

import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;

public class ClientXMLRepositoryTest {
    private final Long id = 30L;
    private final String name = "ill be deleted";
    private final Integer moneySpent = 0;
    private final String fileName = "data/xml/clienttest.xml";
    private Client client;
    private Validator<Client> validator;
    private Repository<Long,Client> testRepository;

    @Before
    public void setUp(){
        client = new Client(name);
        client.setId(id);
        client.setMoneySpent(moneySpent);
        validator = new ClientValidator();
        testRepository = new ClientXMLRepository(validator,fileName);
    }

    @After
    public void tearDown(){
        testRepository=null;
        validator=null;
    }

    @Test
    public void testAll(){
        assertEquals("Size should be 3",3, StreamSupport.
                stream(testRepository.findAll().spliterator(),false).count());

        //Test add functions
        testRepository.save(client);
        assertEquals("Size should be 4",4,StreamSupport.
                stream(testRepository.findAll().spliterator(),false).count());

        //Test remove functions (super.remove and removeInternal)
        testRepository.delete(client.getId());
        assertEquals("Size should be 3",3,StreamSupport.
                stream(testRepository.findAll().spliterator(),false).count());

        //Test update functions (super.update and updateInternal)
        testRepository.save(client);
        assertEquals("Size should be 4",4,StreamSupport.
                stream(testRepository.findAll().spliterator(),false).count());
        client.setName("I've been updated");
        testRepository.update(client);
        assert "I've been updated".equals(testRepository.findOne(client.getId()).get().getName());
        testRepository.delete(client.getId());

    }
}
