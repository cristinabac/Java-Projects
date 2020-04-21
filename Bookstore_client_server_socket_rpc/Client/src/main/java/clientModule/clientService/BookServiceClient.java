package clientModule.clientService;

import clientModule.tcp.TcpClient;
import commonModule.Message;
import commonModule.Utilities;
import commonModule.domain.Book;
import commonModule.domain.validators.BookstoreException;
import commonModule.service.BookServiceInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class BookServiceClient implements BookServiceInterface {
    private ExecutorService executorService;
    private TcpClient tcpClient;


    public BookServiceClient(ExecutorService executorService, TcpClient tcpClient) {
        this.executorService = executorService;
        this.tcpClient = tcpClient;
    }

    @Override
    public CompletableFuture<Boolean> addBook(Book bookToBeAdded) {
        return CompletableFuture.supplyAsync(()->{
            Message request = Message.builder()
                    .header(BookServiceInterface.ADD_BOOK)
                    .body(new Utilities().bookToString(bookToBeAdded))
                    .build();
            Message response = tcpClient.sendAndReceive(request);
            if(response.getHeader().equals(Message.ERROR))
                throw new BookstoreException(response.getBody());
            return response.getBody().equals("added");
        }, executorService);
    }

    @Override
    public CompletableFuture<Boolean> removeBook(Long bookID) {
        return CompletableFuture.supplyAsync(()->{
            Message request = Message.builder()
                    .header(BookServiceInterface.REMOVE_BOOK)
                    .body(bookID.toString())
                    .build();
            Message response = tcpClient.sendAndReceive(request);
            if(response.getHeader().equals(Message.ERROR))
                throw new BookstoreException(response.getBody());
            return response.getBody().equals("removed");
        }, executorService);
    }

    @Override
    public CompletableFuture<Boolean> updateBook(Book updatedBook) {
        return CompletableFuture.supplyAsync(()->{
            Message request = Message.builder()
                    .header(BookServiceInterface.UPDATE_BOOK)
                    .body(new Utilities().bookToString(updatedBook))
                    .build();
            Message  response = tcpClient.sendAndReceive(request);
            if(response.getHeader().equals(Message.ERROR))
                throw new BookstoreException(response.getBody());
            return response.getBody().equals("updated");
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Book>> getBooks() {
        return CompletableFuture.supplyAsync(()->{
            Message request = Message.builder()
                    .header(BookServiceInterface.GET_BOOKS)
                    .body("")
                    .build();
            Message response = tcpClient.sendAndReceive(request);
            if(response.getHeader().equals(Message.ERROR))
                throw new BookstoreException(response.getBody());
            if(!response.getBody().contains(","))
                return new ArrayList<>();
            return Utilities.fromStringToBookList(response.getBody());
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Book>> booksFilteredByAuthor(String authorName) {
        return CompletableFuture.supplyAsync(()->{
            Message request = Message.builder()
                    .header(BookServiceInterface.FILTER_BY_AUTHOR_BOOKS)
                    .body(authorName)
                    .build();
            Message response = tcpClient.sendAndReceive(request);
            if(response.getHeader().equals(Message.ERROR))
                throw new BookstoreException(response.getBody());
            if(!response.getBody().contains(","))
                return new ArrayList<>();
            return Utilities.fromStringToBookList(response.getBody());
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Book>> booksFilteredByTile(String bookTile) {
        return CompletableFuture.supplyAsync(()->{
            Message request = Message.builder()
                    .header(BookServiceInterface.FILTER_BY_TITLE_BOOKS)
                    .body(bookTile)
                    .build();
            Message response = tcpClient.sendAndReceive(request);
            if(response.getHeader().equals(Message.ERROR))
                throw new BookstoreException(response.getBody());
            if(!response.getBody().contains(","))
                return new ArrayList<>();
            return Utilities.fromStringToBookList(response.getBody());
        }, executorService);
    }

    @Override
    public CompletableFuture<List<Book>> booksSortedByAuthorTitleID() {
        return CompletableFuture.supplyAsync(()->{
            Message request = Message.builder()
                    .header(BookServiceInterface.SORT_AUTHOR_TITLE_ID)
                    .body("")
                    .build();
            Message response = tcpClient.sendAndReceive(request);
            if(response.getHeader().equals(Message.ERROR))
                throw new BookstoreException(response.getBody());
            if(!response.getBody().contains(","))
                return new ArrayList<>();
            return Utilities.fromStringToBookList(response.getBody());
        }, executorService);
    }
}
