package repository;

import domain.Client;
import domain.validators.ClientValidator;
import domain.validators.Validator;
import domain.validators.ValidatorException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.StreamSupport;
import static org.junit.Assert.assertEquals;

public class InMemoryRepositoryTests {

    private static final Long id1 = (long) 1;
    private static final Long id2 = (long) 2;
    private static final String name1 = "Geogre Zakor";
    private static final String name2 = "Miruna Calin";
    private static final String name3 = "Update Name";
    private Client client1;
    private Client client2;
    private Client client3;
    private Client invalidClient1;
    private Client invalidClient2;
    private Validator<Client> clientValidator;
    private Repository<Long,Client> testRepository;


    @Before
    public void setUp(){
        client1 = new Client(name1);
        client1.setId(id1);

        client2 = new Client(name2);
        client2.setId(id2);

        client3 = new Client(name3);
        client3.setId(id1);

        invalidClient1 = new Client("");
        invalidClient2 = new Client(name1);

        clientValidator = new ClientValidator();

        testRepository = new InMemoryRepository<Long, Client>(clientValidator);
        testRepository.save(client1);
    }

    @After
    public void tearDown(){
        client1=null;
        client2=null;
        client3=null;
        invalidClient2=null;
        invalidClient1=null;
        clientValidator=null;
        testRepository=null;
    }

    @Test
    public void testFindOneMethod(){
        assertEquals("The name of the client should be: George Zakor",name1,testRepository.findOne(id1).get().getName());
    }

    @Test
    public void testFindAllMethod(){
        assertEquals("Size should be equal to 1",1 , StreamSupport.stream(testRepository.findAll().spliterator(), false).
                count());
    }

    @Test
    public void testSaveMethod(){
        testRepository.save(client2);
        assertEquals("Size should be equal to 2",2 , StreamSupport.stream(testRepository.findAll().spliterator(), false).
                count());
    }

    @Test
    public void testValidationErrorOnAddMethod(){
        try{
            testRepository.save(invalidClient1);
            assert false;
        }
        catch(ValidatorException exc){
            assert true;
        }

        try{
            testRepository.save(invalidClient2);
            assert false;
        }
        catch(ValidatorException exc){
            assert true;
        }

    }

    @Test
    public void testDeleteMethod(){
        testRepository.delete(id2);
        assertEquals("Size should be equal to 1",1 , StreamSupport.stream(testRepository.findAll().spliterator(), false).
                count());
    }

    @Test
    public void testErrorOnDeleteMethod(){
        try {
            testRepository.delete(null);
            assert false;
        }
        catch (IllegalArgumentException exc){
            assert true;
        }
    }

    @Test
    public void testUpdateMethod(){
        testRepository.update(client3);
        assertEquals("Name should be equal to: Update Name",name3,testRepository.findOne(id1).get().getName());
    }

    @Test
    public void testValidationErrorUpdateMethod(){
        try{
            testRepository.update(invalidClient1);
            assert false;
        }
        catch(ValidatorException exc){
            assert true;
        }

        try{
            testRepository.update(invalidClient2);
            assert false;
        }
        catch(ValidatorException exc){
            assert true;
        }
    }
}
