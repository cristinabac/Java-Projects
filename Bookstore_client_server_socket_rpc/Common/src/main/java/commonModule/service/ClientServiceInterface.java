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


    CompletableFuture<Boolean> addClient(Client clientToBeAdded);

    CompletableFuture<List<Client>> getClients();

    CompletableFuture<List<Client>> showClientsOrderByMoney();

    CompletableFuture<Boolean> updateClient(Client updatedClient);

    CompletableFuture<Boolean> removeClient(Long clientID);

    CompletableFuture<List<Client>> clientsSortedByNameID();
}
