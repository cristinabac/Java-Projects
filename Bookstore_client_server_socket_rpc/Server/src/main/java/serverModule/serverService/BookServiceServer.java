package serverModule.serverService;

import commonModule.domain.Book;
import commonModule.service.BookServiceInterface;
import serverModule.entityService.BookService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class BookServiceServer implements BookServiceInterface {
    private BookService bookService;
    private ExecutorService executor;

    public BookServiceServer(BookService bookService, ExecutorService executor) {
        this.bookService = bookService;
        this.executor = executor;
    }

    @Override
    public CompletableFuture<Boolean> addBook(Book bookToBeAdded) {
        return CompletableFuture.supplyAsync(() -> {
            return this.bookService.addBook(bookToBeAdded);
        }, executor);
    }

    @Override
    public CompletableFuture<Boolean> removeBook(Long bookID) {
        return CompletableFuture.supplyAsync(() -> {
            return this.bookService.deleteBook(bookID);
        }, executor);
    }

    @Override
    public CompletableFuture<Boolean> updateBook(Book updatedBook) {
        return CompletableFuture.supplyAsync(() -> {
            return  this.bookService.updateBook(updatedBook);
        }, executor);
    }

    @Override
    public CompletableFuture<List<Book>> getBooks() {
        return CompletableFuture.supplyAsync(() -> this.bookService.getAllBooks(), executor);
    }

    @Override
    public CompletableFuture<List<Book>> booksFilteredByAuthor(String authorName) {
        return CompletableFuture.supplyAsync(() -> this.bookService.filterByAuthor(authorName), executor);
    }

    @Override
    public CompletableFuture<List<Book>> booksFilteredByTile(String bookTile) {
        return CompletableFuture.supplyAsync(() -> this.bookService.filterByTitle(bookTile), executor);
    }

    @Override
    public CompletableFuture<List<Book>> booksSortedByAuthorTitleID() {
        return CompletableFuture.supplyAsync(() -> this.bookService.sortByAuthorAndTitleAndID(), executor);
    }
}
