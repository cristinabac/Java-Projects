package clientModule.clientService;

import commonModule.domain.Client;
import commonModule.service.ClientServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ClientServiceClient implements ClientServiceInterface {
    @Autowired
    private ClientServiceInterface clientService;

    @Override
    public Boolean addClient(Client clientToBeAdded) {
        return clientService.addClient(clientToBeAdded);
    }

    @Override
    public List<Client> getClients() {
        return clientService.getClients();
    }

    @Override
    public List<Client> showClientsOrderByMoney() {
        return clientService.showClientsOrderByMoney();
    }

    @Override
    public Boolean updateClient(Client updatedClient) {
        return clientService.updateClient(updatedClient);
    }

    @Override
    public Boolean removeClient(Long clientID) {
        return clientService.removeClient(clientID);
    }

    @Override
    public List<Client> clientsSortedByNameID() {
        return clientService.clientsSortedByNameID();
    }
}
