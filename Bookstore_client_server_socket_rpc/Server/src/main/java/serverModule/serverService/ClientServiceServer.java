package serverModule.serverService;

import commonModule.domain.Client;
import commonModule.service.ClientServiceInterface;
import serverModule.entityService.ClientService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class ClientServiceServer implements ClientServiceInterface {
    private ClientService clientService;
    private ExecutorService executor;

    public ClientServiceServer(ClientService clientService, ExecutorService executor) {
        this.clientService = clientService;
        this.executor = executor;
    }

    @Override
    public CompletableFuture<Boolean> addClient(Client clientToBeAdded) {
        return CompletableFuture.supplyAsync(() -> {
            return this.clientService.addClient(clientToBeAdded);
        }, executor);
    }

    @Override
    public CompletableFuture<List<Client>> getClients() {
        return CompletableFuture.supplyAsync(() -> this.clientService.getAllClients(), executor);
    }

    @Override
    public CompletableFuture<List<Client>> showClientsOrderByMoney() {
        return CompletableFuture.supplyAsync(() -> this.clientService.clientsSortedByMoneySpent(), executor);
    }

    @Override
    public CompletableFuture<Boolean> updateClient(Client updatedClient) {
        return CompletableFuture.supplyAsync(() -> {
            return this.clientService.updateClient(updatedClient);
        }, executor);
    }

    @Override
    public CompletableFuture<Boolean> removeClient(Long clientID) {
        return CompletableFuture.supplyAsync(() -> {
            return this.clientService.deleteClient(clientID);
        }, executor);
    }

    @Override
    public CompletableFuture<List<Client>> clientsSortedByNameID() {
        return CompletableFuture.supplyAsync(() -> this.clientService.sortByNameAndID(), executor);
    }
}
