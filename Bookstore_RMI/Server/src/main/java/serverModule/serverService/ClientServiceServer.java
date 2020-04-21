package serverModule.serverService;

import commonModule.domain.Client;
import commonModule.service.ClientServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import serverModule.entityService.ClientService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class ClientServiceServer implements ClientServiceInterface {
    @Autowired
    private ClientService clientService;
    /*
    private ExecutorService executor;

    public ClientServiceServer(ClientService clientService, ExecutorService executor) {
        this.clientService = clientService;
        this.executor = executor;
    }*/

    @Override
    public Boolean addClient(Client clientToBeAdded) {
        return clientService.addClient(clientToBeAdded);
    }

    @Override
    public List<Client> getClients() {
       return clientService.getAllClients();
    }

    @Override
    public List<Client> showClientsOrderByMoney() {
        return clientService.clientsSortedByMoneySpent();
    }

    @Override
    public Boolean updateClient(Client updatedClient) {
        return clientService.updateClient(updatedClient);
    }

    @Override
    public Boolean removeClient(Long clientID) {
        return clientService.deleteClient(clientID);
    }

    @Override
    public List<Client> clientsSortedByNameID() {
        return clientService.sortByNameAndID();
    }
}
