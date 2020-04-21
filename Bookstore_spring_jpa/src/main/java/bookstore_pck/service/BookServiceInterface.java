package bookstore_pck.service;

import bookstore_pck.domain.Book;

import java.util.List;

public interface BookServiceInterface {
    void addBook(Book book);

    void deleteBook(Long id);

    void updateBook(Book newBook);

    List<Book> getAllBooks();

    List<Book>  filterByTitle(String searchString);

    List<Book>  filterByAuthor(String author);

    List<Book> sortByAuthorAndTitleAndID();



}
