package clientModule.clientService;

import commonModule.domain.Book;
import commonModule.service.BookServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BookServiceClient implements BookServiceInterface {
    @Autowired
    private BookServiceInterface bookService;

    @Override
    public Boolean addBook(Book bookToBeAdded) {
        return this.bookService.addBook(bookToBeAdded);
    }

    @Override
    public Boolean removeBook(Long bookID) {
        return this.bookService.removeBook(bookID);
    }

    @Override
    public Boolean updateBook(Book updatedBook) {
        return  this.bookService.updateBook(updatedBook);
    }

    @Override
    public List<Book> getBooks() {
        return this.bookService.getBooks();
    }

    @Override
    public List<Book> booksFilteredByAuthor(String authorName) {
        return this.bookService.booksFilteredByAuthor(authorName);
    }

    @Override
    public List<Book> booksFilteredByTile(String bookTile) {
        return this.bookService.booksFilteredByTile(bookTile);
    }

    @Override
    public List<Book> booksSortedByAuthorTitleID() {
        return this.bookService.booksSortedByAuthorTitleID();
    }
}
