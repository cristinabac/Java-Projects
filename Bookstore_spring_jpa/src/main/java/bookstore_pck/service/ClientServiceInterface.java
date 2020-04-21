package bookstore_pck.service;

import bookstore_pck.domain.Client;

import java.util.List;

public interface ClientServiceInterface {

    void addClient(Client client);

    void deleteClient(Long id);

    void updateClient(Client newClient);

    List<Client> getAllClients();

    List<Client> clientsSortedAlphabetically();

    List<Client> clientsSortedByMoneySpent();

    List<Client>  filterByName(String searchString);

    List<Client> sortByNameAndID();


}
