package commonModule.service;

import commonModule.domain.Book;

import java.util.List;


public interface BookServiceInterface {


    Boolean addBook(Book bookToBeAdded);

    Boolean removeBook(Long bookID);

    Boolean updateBook(Book updatedBook);

    List<Book> getBooks();

    List<Book> booksFilteredByAuthor(String authorName);

    List<Book> booksFilteredByTile(String bookTile);

    List<Book> booksSortedByAuthorTitleID();
}
