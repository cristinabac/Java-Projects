package clientModule;

import clientModule.clientService.BookServiceClient;
import clientModule.clientService.ClientServiceClient;
import clientModule.clientService.SaleServiceClient;
import clientModule.tcp.TcpClient;
import clientModule.ui.Console;
import commonModule.service.BookServiceInterface;
import commonModule.service.ClientServiceInterface;
import commonModule.service.SaleServiceInterface;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientApp {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        TcpClient client = new TcpClient(ClientServiceInterface.SERVER_HOST, ClientServiceInterface.PORT_NUMBER);
        ClientServiceInterface clientServiceInterface = new ClientServiceClient(executorService,client);
        BookServiceInterface bookServiceInterface = new BookServiceClient(executorService,client);
        SaleServiceInterface saleServiceInterface = new SaleServiceClient(executorService,client);
        Console console = new Console(clientServiceInterface,bookServiceInterface,saleServiceInterface);
        console.runConsole();
        executorService.shutdownNow();
    }
}
