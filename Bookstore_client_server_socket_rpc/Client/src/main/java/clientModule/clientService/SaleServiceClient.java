package clientModule.clientService;

import clientModule.tcp.TcpClient;
import commonModule.Message;
import commonModule.Utilities;
import commonModule.domain.Sale;
import commonModule.domain.validators.BookstoreException;
import commonModule.service.SaleServiceInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class SaleServiceClient implements SaleServiceInterface{
    private ExecutorService executorService;
    private TcpClient tcpClient;

    public SaleServiceClient(ExecutorService executorService, TcpClient tcpClient) {
        this.executorService = executorService;
        this.tcpClient = tcpClient;
    }

    @Override
    public CompletableFuture<Boolean> addSale(Sale sale) {
        return CompletableFuture.supplyAsync(()->{
            Message request = Message.builder()
                    .header(SaleServiceInterface.ADD_SALE)
                    .body(new Utilities().saleToString(sale))
                    .build();
            Message response = tcpClient.sendAndReceive(request);
            if(response.getHeader().equals(Message.ERROR))
                throw new BookstoreException(response.getBody());
            return response.getBody().equals("added");
        }, executorService);
    }

    @Override
    public CompletableFuture<Boolean> removeSale(Long id) {
        return CompletableFuture.supplyAsync(()->{
        Message request = Message.builder()
                .header(SaleServiceInterface.REMOVE_SALE)
                .body(id.toString())
                .build();
        Message response = tcpClient.sendAndReceive(request);
        if(response.getHeader().equals(Message.ERROR))
            throw new BookstoreException(response.getBody());
        return response.getBody().equals("removed");
    }, executorService);
    }

    @Override
    public CompletableFuture<List<Sale>> getSales() {
        return CompletableFuture.supplyAsync(()->{
            Message request = Message.builder()
                    .header(SaleServiceInterface.GET_SALES)
                    .body("")
                    .build();
            Message response = tcpClient.sendAndReceive(request);
            if(response.getHeader().equals(Message.ERROR))
                throw new BookstoreException(response.getBody());
            if(response.getBody().equals(""))
                return new ArrayList<>();
            return Utilities.fromStringToSaleList(response.getBody());
        }, executorService);
    }
}
