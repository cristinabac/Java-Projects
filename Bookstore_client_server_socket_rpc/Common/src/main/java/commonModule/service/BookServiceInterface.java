package commonModule.service;

import commonModule.domain.Book;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public interface BookServiceInterface {
    String SERVER_HOST="localhost";
    Integer PORT_NUMBER=1234;

    String ADD_BOOK="addBook";
    String REMOVE_BOOK="removeBook";
    String UPDATE_BOOK="updateBook";
    String GET_BOOKS="getBooks";
    String FILTER_BY_AUTHOR_BOOKS="filterBooksByAuthor";
    String FILTER_BY_TITLE_BOOKS="filterBooksByTitle";
    String SORT_AUTHOR_TITLE_ID="sortBookByAuthorTitleID";

    CompletableFuture<Boolean> addBook(Book bookToBeAdded);

    CompletableFuture<Boolean> removeBook(Long bookID);

    CompletableFuture<Boolean> updateBook(Book updatedBook);

    CompletableFuture<List<Book>> getBooks();

    CompletableFuture<List<Book>> booksFilteredByAuthor(String authorName);

    CompletableFuture<List<Book>> booksFilteredByTile(String bookTile);

    CompletableFuture<List<Book>> booksSortedByAuthorTitleID();
}
