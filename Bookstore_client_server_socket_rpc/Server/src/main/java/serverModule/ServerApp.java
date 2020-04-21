package serverModule;

import commonModule.Message;
import commonModule.Utilities;
import commonModule.domain.Book;
import commonModule.domain.Client;
import commonModule.domain.Sale;
import commonModule.domain.validators.*;
import commonModule.service.BookServiceInterface;
import commonModule.service.ClientServiceInterface;
import commonModule.service.SaleServiceInterface;
import serverModule.entityService.BookService;
import serverModule.entityService.ClientService;
import serverModule.entityService.SaleService;
import serverModule.repository.database_repository.BookDBRepository;
import serverModule.repository.database_repository.ClientDBRepository;
import serverModule.repository.database_repository.SaleDBRepository;
import serverModule.repository.database_repository.SortingRepository;
import serverModule.serverService.BookServiceServer;
import serverModule.serverService.ClientServiceServer;
import serverModule.serverService.SaleServiceServer;
import serverModule.tcp.TcpServer;

import java.util.List;
import java.util.concurrent.*;

public class ServerApp {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        TcpServer server = new TcpServer(executorService, ClientServiceInterface.PORT_NUMBER);

        Validator<Client> clientValidator = new ClientValidator();
        Validator<Book> bookValidator = new BookValidator();
        Validator<Sale> saleValidator = new SaleValidator();

        SortingRepository<Long,Client> clientSortingRepository = new ClientDBRepository(clientValidator,"postgres","admin");
        SortingRepository<Long,Book> bookSortingRepository = new BookDBRepository(bookValidator,"postgres","admin");
        SortingRepository<Long,Sale> saleSortingRepository = new SaleDBRepository(saleValidator,"postgres","admin");

        ClientService clientService = new ClientService(clientSortingRepository);
        BookService bookService = new BookService(bookSortingRepository);
        SaleService saleService = new SaleService(bookSortingRepository,clientSortingRepository,saleSortingRepository);

        SaleServiceServer saleServiceServer = new SaleServiceServer(saleService,executorService);
        BookServiceServer bookServiceServer = new BookServiceServer(bookService,executorService);
        ClientServiceServer clientServiceServer = new ClientServiceServer(clientService,executorService);

        setHandlers(server,clientServiceServer);
        setHandlers(server,bookServiceServer);
        setHandlers(server,saleServiceServer);

        server.startServer();
    }

    private static  Message generateMessage(String header,String body){
        return Message.builder()
                .header(header)
                .body(body)
                .build();
    }

    private static void setHandlers(TcpServer tcpServer, ClientServiceServer service){
        tcpServer.addHandler(ClientServiceInterface.ADD_CLIENT, (request)->{
            String clientCSV = request.getBody();
            Client client = new Utilities().stringToClient(clientCSV);
            try {
                Boolean res = service.addClient(client).get();
                if(res)
                    return generateMessage(Message.OK,"added");
                return generateMessage(Message.OK,"failed");
            } catch (InterruptedException | ExecutionException e) {
                return generateMessage(Message.ERROR,"Operation failed inside server");
            }
        });

        tcpServer.addHandler(ClientServiceInterface.GET_CLIENTS, (request)->{
            Future<List<Client>> clients = service.getClients();
            try{
                return generateMessage(Message.OK, Utilities.fromClientListToString(clients.get()));
            } catch (InterruptedException | ExecutionException e) {
                return generateMessage(Message.ERROR,e.getMessage());
            }
        });

        tcpServer.addHandler(ClientServiceInterface.SHOW_CLIENT_ORDER_MONEY, (request)->{
            Future<List<Client>> clients = service.showClientsOrderByMoney();
            try {
                return generateMessage(Message.OK,Utilities.fromClientListToString(clients.get()));
            } catch (InterruptedException | ExecutionException e) {
                return generateMessage(Message.ERROR,e.getMessage());
            }
        });

        tcpServer.addHandler(ClientServiceInterface.UPDATE_CLIENT,(request)->{
            String body = request.getBody();
            Client client = new Utilities().stringToClient(body);
            try {
                Boolean result = service.updateClient(client).get();
                if(result)
                    return generateMessage(Message.OK,"updated");
                return generateMessage(Message.OK,"failed");
            } catch (InterruptedException | ExecutionException e) {
                return generateMessage(Message.ERROR,"Operation failed inside server!");
            }
        });

        tcpServer.addHandler(ClientServiceInterface.REMOVE_CLIENT,(request)->{
            String body = request.getBody();
            Long clientID = Long.parseLong(body);
            try {
                Boolean res = service.removeClient(clientID).get();
                if(res)
                    return generateMessage(Message.OK,"removed");
                return generateMessage(Message.OK,"failed");
            } catch (InterruptedException | ExecutionException e) {
                return generateMessage(Message.ERROR,e.getMessage());
            }

        });

        tcpServer.addHandler(ClientServiceInterface.SORT_CLIENT_NAME_ID, (request)->{
            Future<List<Client>> result = service.clientsSortedByNameID();
            try{
                return generateMessage(Message.OK, Utilities.fromClientListToString(result.get()));
            } catch (InterruptedException | ExecutionException e) {
                return generateMessage(Message.ERROR, e.getMessage());
            }
        });

    }


    public static void setHandlers(TcpServer tcpServer, BookServiceServer service){

        tcpServer.addHandler(BookServiceInterface.ADD_BOOK, (request)->{
            try {
                String body = request.getBody();
                Book book = new Utilities().stringToBook(body);
                Boolean res = service.addBook(book).get();
                if(res)
                    return generateMessage(Message.OK, "added");
                return generateMessage(Message.OK, "failed");
            } catch (InterruptedException | ExecutionException e) {
                return generateMessage(Message.ERROR,"Operation failed on server");
            }
        });

        tcpServer.addHandler(BookServiceInterface.REMOVE_BOOK, (request)->{
            String body = request.getBody();
            Long id = Long.parseLong(body);
            try {
                Boolean res = service.removeBook(id).get();
                if(res)
                    return generateMessage(Message.OK,"removed");
            } catch (InterruptedException | ExecutionException e) {
                return generateMessage(Message.ERROR,e.getMessage());
            }
            return generateMessage(Message.OK,"failed");
        });

        tcpServer.addHandler(BookServiceInterface.UPDATE_BOOK, (request)->{
            String body = request.getBody();
            Book book = new Utilities().stringToBook(body);

            try {
                Boolean res = service.updateBook(book).get();
                if(res)
                    return generateMessage(Message.OK,"updated");
            } catch (InterruptedException | ExecutionException e) {
                return generateMessage(Message.ERROR,e.getMessage());
            }
            return generateMessage(Message.OK,"");
        });

        tcpServer.addHandler(BookServiceInterface.GET_BOOKS, (request)->{
            Future<List<Book>> books = service.getBooks();
            try{
                return generateMessage(Message.OK, Utilities.fromBookListToString(books.get()));
            } catch (InterruptedException | ExecutionException e) {
                return generateMessage(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler(BookServiceInterface.FILTER_BY_AUTHOR_BOOKS,(request)->{
            String authorName = request.getBody();
            Future<List<Book>> result = service.booksFilteredByAuthor(authorName);
            try{
                return generateMessage(Message.OK, Utilities.fromBookListToString(result.get()));
            } catch (InterruptedException | ExecutionException e) {
                return generateMessage(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler(BookServiceInterface.FILTER_BY_TITLE_BOOKS, (request)->{
            String title = request.getBody();
            Future<List<Book>> result = service.booksFilteredByTile(title);
            try{
                return generateMessage(Message.OK, Utilities.fromBookListToString(result.get()));
            } catch (InterruptedException | ExecutionException e) {
                return generateMessage(Message.ERROR, e.getMessage());
            }
        });

        tcpServer.addHandler(BookServiceInterface.SORT_AUTHOR_TITLE_ID,(request)->{
            Future<List<Book>> result = service.booksSortedByAuthorTitleID();
            try{
                return generateMessage(Message.OK, Utilities.fromBookListToString(result.get()));
            } catch (InterruptedException | ExecutionException e) {
                return generateMessage(Message.ERROR, e.getMessage());
            }
        });

    }


    public static  void setHandlers(TcpServer tcpServer, SaleServiceServer service){
        tcpServer.addHandler(SaleServiceInterface.GET_SALES, (request)->{
            Future<List<Sale>> result = service.getSales();
            try {
                return generateMessage(Message.OK, Utilities.fromSalesListToString(result.get()));
            } catch (InterruptedException | ExecutionException e) {
                return generateMessage(Message.ERROR,e.getMessage());
            }
        });

        tcpServer.addHandler(SaleServiceInterface.ADD_SALE,(request)->{
            String body = request.getBody();
            Sale sale = new Utilities().stringToSale(body);
            try {
                Boolean res = service.addSale(sale).get();
                if(res)
                    return generateMessage(Message.OK,"added");
            } catch (InterruptedException | ExecutionException e) {
                return generateMessage(Message.ERROR,"Server operations failed, detailed error:\n "+e.getMessage());
            }
            return generateMessage(Message.OK,"failed");
        });

        tcpServer.addHandler(SaleServiceInterface.REMOVE_SALE,(request)->{
            String body = request.getBody();
            Long id = Long.parseLong(body);
            try {
                Boolean res = service.removeSale(id).get();
                if(res)
                    return  generateMessage(Message.OK,"removed");
            } catch (InterruptedException | ExecutionException e) {
                return generateMessage(Message.ERROR,"Server operations failed, detailed error:\n "+e.getMessage());
            }
            return generateMessage(Message.OK,"failed");
        });
    }

}
