package serverModule.entityService;

import commonModule.domain.BaseEntity;
import commonModule.domain.Client;
import commonModule.domain.validators.ClientValidator;
import commonModule.domain.validators.Validator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import serverModule.repository.database_repository.ClientDBRepository;
import serverModule.repository.database_repository.SortingRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ClientServiceTest {
    private Validator<Client> validator;
    private SortingRepository<Long, Client> repository;
    private ClientService service;

    @Before
    public void setUp(){
        validator = new ClientValidator();
        repository = new ClientDBRepository(validator,"postgres","admin");
        service = new ClientService(repository);
    }

    @After
    public  void tearDown(){
        service = null;
        repository = null;
        validator = null;
    }

    @Test
    public void testAddMethod1(){
        List<Client> clients = service.getAllClients();
        Client fakeClient = new Client();
        fakeClient.setId(clients.get(0).getId());
        fakeClient.setMoneySpent(0);
        fakeClient.setName("aaaa");
        Boolean result = service.addClient(fakeClient);
        assert result.equals(false);
    }

    @Test
    public void testAddMethodAndRemove(){
        Long id = Collections.max(service.getAllClients().stream().map(BaseEntity::getId).collect(Collectors.toList()));
        id += 1;
        Boolean result = service.deleteClient(id);
        assert result.equals(false);
        Client newClient = new Client();
        newClient.setId(id);
        newClient.setName("aaaa");
        newClient.setMoneySpent(0);
        result = service.updateClient(newClient);
        assert result.equals(false);
        result = service.addClient(newClient);
        assert result.equals(true);
        result = service.deleteClient(id);
        assert result.equals(true);
    }

    @Test
    public void testUpdate(){
        List<Client> clients = service.getAllClients();
        Client oldClient = clients.get(0);
        Client newClient = new Client();
        newClient.setId(oldClient.getId());
        newClient.setName("aaa");
        newClient.setMoneySpent(0);
        Boolean result = service.updateClient(newClient);
        assert result.equals(true);
        result = service.updateClient(oldClient);
        assert result.equals(true);
    }
}
