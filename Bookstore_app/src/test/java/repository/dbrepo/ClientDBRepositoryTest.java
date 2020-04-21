package repository.dbrepo;

import domain.Client;
import domain.validators.ClientValidator;
import domain.validators.Validator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.database_repository.ClientDBRepository;
import repository.database_repository.SortingRepository;

import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;

public class ClientDBRepositoryTest {
    Validator<Client> validator;
    SortingRepository<Long, Client> testRepository;
    private String name1="this is fucked again";
    private String name2="uptade -- do not use this name";
    private Client client1;
    private Client client2;


    @Before
    public void setUp(){
        validator = new ClientValidator();
        testRepository = new ClientDBRepository(validator,"postgres","admin");
        client1 = new Client(name1);
        client1.setId(10L);
        client1.setMoneySpent(0);
        client2 = new Client(name2);
        client2.setId(10L);
        client2.setMoneySpent(0);
    }

    @After
    public void tearDown(){
        testRepository=null;
        validator=null;
        client1=null;
        client2=null;
    }

    @Test
    public void findAllMethodTest(){
       assert  StreamSupport.stream(testRepository.findAll().spliterator(),false).count() > 0;
    }

    @Test
    public void findOneMethodTest1(){
        Optional<Client> result = testRepository.findOne(1L);
        result.ifPresentOrElse(i->{assert  true;},()->{assert false;});
    }

    @Test
    public void findOneMethodTest2(){
        Optional<Client> result = testRepository.findOne(111L);
        result.ifPresentOrElse(i->{assert  false;},()->{assert true;});
    }


    @Test
    public void testCrudOperations(){
        Integer size = (int) StreamSupport.stream(testRepository.findAll().spliterator(),false).count();
        testRepository.save(client1);
        assert  size + 1 == StreamSupport.stream(testRepository.findAll().spliterator(),false).count();

        size = (int) StreamSupport.stream(testRepository.findAll().spliterator(),false).count();
        testRepository.save(client1);
        assert  size  == StreamSupport.stream(testRepository.findAll().spliterator(),false).count();



        testRepository.update(client2);
        assert StreamSupport.stream(testRepository.findAll().spliterator(),false)
               .filter(i->i.getName().equals(client2.getName())).count() == 1;


        size = (int) StreamSupport.stream(testRepository.findAll().spliterator(),false).count();
        testRepository.delete(client2.getId());
        assert size - 1 == StreamSupport.stream(testRepository.findAll().spliterator(),false).count();
    }

}
