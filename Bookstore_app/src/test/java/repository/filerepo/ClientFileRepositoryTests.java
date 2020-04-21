package repository.filerepo;

import domain.Client;
import domain.validators.ClientValidator;
import domain.validators.Validator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.Repository;
import repository.file_repository.ClientFileRepository;

import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;

public class ClientFileRepositoryTests {
    private static final String fileName ="data/file/clienttest.txt";
    private static final String clientName ="Miclea Mihai";
    private static final Long clientID = 4L;
    private static final Integer moneySPent =2000;
    private Client client;
    private Validator<Client> clientValidator;
    private Repository<Long, Client> testRepository;

    @Before
    public void setUp(){
        client = new Client();
        client.setId(clientID);
        client.setName(clientName);
        client.setMoneySpent(moneySPent);
        clientValidator = new ClientValidator();
        this.testRepository = new ClientFileRepository(clientValidator,fileName);
    }

    @After
    public void tearDown(){
        clientValidator=null;
        testRepository=null;
        client=null;
    }

    @Test
    public void testAll(){
        assertEquals("We should have 3 clients in repo",3, StreamSupport.stream(testRepository.findAll().spliterator(),false).count());

        assertEquals("We should have 3 clients in repo",3, StreamSupport.stream(testRepository.findAll().spliterator(),false).count());
        testRepository.save(client);
        assertEquals("We should have 4 clients in repo",4, StreamSupport.stream(testRepository.findAll().spliterator(),false).count());
        testRepository.delete(clientID);
        assertEquals("We should have 3 clients in repo",3, StreamSupport.stream(testRepository.findAll().spliterator(),false).count());

        assertEquals("We should have 3 clients in repo",3, StreamSupport.stream(testRepository.findAll().spliterator(),false).count());
        testRepository.delete(1L);
        assertEquals("size should be 2",2,StreamSupport.stream(testRepository.findAll().spliterator(),false).count());

        client.setId(1L);
        testRepository.save(client);
        assertEquals("ss",3,StreamSupport.stream(testRepository.findAll().spliterator(),false).count());

        client.setName("updated");
        testRepository.update(client);

    }
}
