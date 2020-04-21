package serverModule.serverService;

import commonModule.domain.Book;
import commonModule.service.BookServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import serverModule.entityService.BookService;

import java.util.List;

public class BookServiceServer implements BookServiceInterface {
    @Autowired
    private BookService bookService;

    @Override
    public Boolean addBook(Book bookToBeAdded) {
        return this.bookService.addBook(bookToBeAdded);
    }

    @Override
    public Boolean removeBook(Long bookID) {
        return this.bookService.deleteBook(bookID);
    }

    @Override
    public Boolean updateBook(Book updatedBook) {
        return  this.bookService.updateBook(updatedBook);
    }

    @Override
    public List<Book> getBooks() {
        return this.bookService.getAllBooks();
    }

    @Override
    public List<Book> booksFilteredByAuthor(String authorName) {
        return this.bookService.filterByAuthor(authorName);
    }

    @Override
    public List<Book> booksFilteredByTile(String bookTile) {
        return this.bookService.filterByTitle(bookTile);
    }

    @Override
    public List<Book> booksSortedByAuthorTitleID() {
        return this.bookService.sortByAuthorAndTitleAndID();
    }
}
