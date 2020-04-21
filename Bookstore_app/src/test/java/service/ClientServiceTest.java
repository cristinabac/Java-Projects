package service;

import domain.Client;
import domain.validators.ClientValidator;
import domain.validators.Validator;
import domain.validators.ValidatorException;
import repository.Repository;
import repository.InMemoryRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertEquals;


public class ClientServiceTest {

    private static final String name1="Hanga Catalin";
    private static final String name2="Tulbure Claudiu";
    public static final String name3="Andro Mihaela";
    public static final String name4="Ciota Ionel";
    public static final Long id1=(long)1;
    public static final Long id2=(long)2;
    public static final Long id3=(long)3;
    public static final Long id4=(long)4;
    private Client client1,client2,client3,client4,invalidClient;
    private Validator<Client> validator;
    private Repository<Long,Client> repository;
    private ClientService testClientService;


    @Before
    public void setUp(){
        client1 = new Client(name1);
        client2 = new Client(name2);
        client3 = new Client(name3);
        client4 = new Client(name4);
        invalidClient = new Client();
        client1.setId(id1);
        client2.setId(id2);
        client3.setId(id3);
        client4.setId(id4);
        client1.setMoneySpent(4646);
        client2.setMoneySpent(5000);
        client3.setMoneySpent(2000);

        validator = new ClientValidator();
        repository = new InMemoryRepository<Long, Client>(validator);
        testClientService = new ClientService(repository);

        testClientService.addClient(client1);
        testClientService.addClient(client2);
        testClientService.addClient(client3);
    }

    @After
    public void tearDown(){
        testClientService = null;
        repository = null;
        validator = null;
        client1 = null;
        client2 = null;
        client3 = null;
        client4 = null;
        invalidClient = null;
    }

    @Test
    public void testAddClientMethod(){
        assertEquals("Size should be 3",3, testClientService.getAllClients().size());
        testClientService.addClient(client4);
        assertEquals("Size should be 4",4, testClientService.getAllClients().size());
    }

    @Test
    public void testErrorAddClient(){
        try{
            testClientService.addClient(invalidClient);
            assert false;
        }
        catch (ValidatorException exc){
            assert true;
        }
    }

    @Test
    public void testSuccesDeleteClientMethod(){
        testClientService.addClient(client4);
        assertEquals("Size should be 4",4, testClientService.getAllClients().size());
        testClientService.deleteClient(id4);
        assertEquals("Size should be 3",3, testClientService.getAllClients().size());
    }

    @Test
    public void test2DeleteMethod(){
        //test for deleting an element that does not appear in repository
        assertEquals("Size should be 3",3, testClientService.getAllClients().size());
        testClientService.deleteClient(id4);
        assertEquals("Size should be 3",3, testClientService.getAllClients().size());
    }

    @Test
    public void testUpdateMethod(){
       assertEquals("Should work",0,
                testClientService.getAllClients().stream().
                        map(Client::getName).filter(e -> e.equals("Update")).count());
        client1.setName("Update");
        testClientService.updateClient(client1);
        assertEquals("Should work",1,
                testClientService.getAllClients().stream().
                        map(Client::getName).filter(e -> e.equals("Update")).count());
    }


    @Test
    public void testGetAllClientsMethod(){
        assertEquals("Size should be 3",3, testClientService.getAllClients().size());
    }

    @Test
    public void  testClientsSortedAlphaMethod(){
        List<Client> sorted = testClientService.clientsSortedAlphabetically();
        assert name3.equals(sorted.get(0).getName());
        assert name1.equals(sorted.get(1).getName());
        assert name2.equals(sorted.get(2).getName());
    }

    @Test
    public void testClientsSortedByMoneySpentMethod(){
        List<Client> moneySortedClients = testClientService.clientsSortedByMoneySpent();
        assert 5000 == moneySortedClients.get(0).getMoneySpent();
        assert 2000 == moneySortedClients.get(2).getMoneySpent();
    }

    @Test
    public void test1FilterByNameMethod(){
        List<Client> filtered = testClientService.filterByName("a");
        assertEquals("All Clients contain letter a, size should be 3",3,filtered.size());
    }

    @Test
    public void test2FilterByNameMethod(){
        List<Client> filtered = testClientService.filterByName("Han");
        assertEquals("Only one client should contain <Han> in the name",1,filtered.size());
    }

    @Test
    public void test3FilterByNameMethod(){
        List<Client> filtered = testClientService.filterByName("no results ");
        assertEquals("There should be no results, test3FilterByNameMethod",0,filtered.size());
    }
}
