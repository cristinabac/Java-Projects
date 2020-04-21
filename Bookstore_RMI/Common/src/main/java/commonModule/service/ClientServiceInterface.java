package commonModule.service;

import commonModule.domain.Client;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ClientServiceInterface {
    String SERVER_HOST="localhost";
    Integer PORT_NUMBER=1234;

    String ADD_CLIENT="addClient";
    String GET_CLIENTS="getClients";
    String SHOW_CLIENT_ORDER_MONEY="showClientsOrderByMoney";
    String UPDATE_CLIENT="updateClient";
    String REMOVE_CLIENT="removeClient";
    String SORT_CLIENT_NAME_ID="sortClientNameID";


    Boolean addClient(Client clientToBeAdded);

    List<Client> getClients();

    List<Client> showClientsOrderByMoney();

    Boolean updateClient(Client updatedClient);

    Boolean removeClient(Long clientID);

    List<Client> clientsSortedByNameID();
}
