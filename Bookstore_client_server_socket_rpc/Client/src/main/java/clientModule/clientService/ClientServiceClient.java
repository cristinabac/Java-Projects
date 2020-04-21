package clientModule.clientService;

import clientModule.tcp.TcpClient;
import commonModule.Message;
import commonModule.Utilities;
import commonModule.domain.Client;
import commonModule.domain.validators.BookstoreException;
import commonModule.service.ClientServiceInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class ClientServiceClient implements ClientServiceInterface {
    private ExecutorService executorService;
    private TcpClient tcpClient;

    public ClientServiceClient(ExecutorService executorService, TcpClient tcpClient) {
        this.executorService = executorService;
        this.tcpClient = tcpClient;
    }

    @Override
    public CompletableFuture<Boolean> addClient(Client clientToBeAdded) {
        return  CompletableFuture.supplyAsync(() ->{
            Message request = Message.builder()
                    .header(ClientServiceInterface.ADD_CLIENT)
                    .body(new Utilities().clientToString(clientToBeAdded))
                    .build();
            Message response = tcpClient.sendAndReceive(request);
            if(response.getHeader().equals(Message.ERROR))
                throw new BookstoreException(response.getBody());
            return response.getBody().equals("added");
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Client>> getClients() {
        return CompletableFuture.supplyAsync(()->{
            Message request = Message.builder()
                    .header(ClientServiceInterface.GET_CLIENTS)
                    .body("")
                    .build();
            Message response = tcpClient.sendAndReceive(request);
            if(response.getHeader().equals(Message.ERROR))
                throw new BookstoreException(response.getBody());
            if(response.getBody().equals(""))
                return new ArrayList<>();
            return Utilities.formStringToClientList(response.getBody());
        },executorService);
    }

    @Override
    public CompletableFuture<List<Client>> showClientsOrderByMoney() {
        return CompletableFuture.supplyAsync(()->{
            Message request = Message.builder()
                    .header(ClientServiceInterface.SHOW_CLIENT_ORDER_MONEY)
                    .body("")
                    .build();
            Message response  = tcpClient.sendAndReceive(request);
            if(response.getHeader().equals(Message.ERROR))
                throw new BookstoreException(response.getBody());
            if (response.getBody().equals(""))
                return new ArrayList<>();
            return Utilities.formStringToClientList(request.getBody());
        }, executorService);
    }

    @Override
    public CompletableFuture<Boolean> updateClient(Client updatedClient) {
        return CompletableFuture.supplyAsync(()->{
            Message request = Message.builder()
                    .header(ClientServiceInterface.UPDATE_CLIENT)
                    .body(new Utilities().clientToString(updatedClient))
                    .build();
            Message response = tcpClient.sendAndReceive(request);
            if(response.getHeader().equals(Message.ERROR))
                throw new BookstoreException(response.getBody());
            return response.getBody().equals("updated");
        }, executorService);
    }

    @Override
    public CompletableFuture<Boolean> removeClient(Long clientID) {
        return CompletableFuture.supplyAsync(()->{
            Message request = Message.builder()
                    .header(ClientServiceInterface.REMOVE_CLIENT)
                    .body(clientID.toString())
                    .build();
            Message response = tcpClient.sendAndReceive(request);
            if(response.getHeader().equals(Message.ERROR))
                throw new BookstoreException(response.getBody());
            return response.getBody().equals("removed");
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Client>> clientsSortedByNameID() {
        return CompletableFuture.supplyAsync(()->{
            Message request = Message.builder()
                    .header(ClientServiceInterface.SORT_CLIENT_NAME_ID)
                    .body("")
                    .build();
            Message response = tcpClient.sendAndReceive(request);
            if(response.getHeader().equals(Message.ERROR))
                throw new BookstoreException("ERROR: "+response.getBody());
            if(response.getBody().equals(""))
                return new ArrayList<>();
            return Utilities.formStringToClientList(response.getBody());
        }, executorService);
    }
}
