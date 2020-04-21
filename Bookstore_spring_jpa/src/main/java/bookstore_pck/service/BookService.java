package bookstore_pck.service;

import bookstore_pck.domain.Book;
import bookstore_pck.domain.validators.BookValidator;
import bookstore_pck.domain.validators.ValidatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import bookstore_pck.repository.BookRepository;
import org.springframework.transaction.annotation.Transactional;
/*
import bookstore.config.repository.Repository;
import bookstore.config.repository.database_repository.SortingRepository;
import bookstore.config.repository.database_repository.sort_pack.Sort;

 */
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class BookService implements BookServiceInterface{
    private static final Logger log = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookValidator bookValidator;


    /**
     *Adds a new book to the bookstore.config.repository
     *
     * @param book is a Book
     * @throws ValidatorException if the book will
     *            not pass the validation step
     */
    @Override
    public void addBook(Book book) throws ValidatorException {
        log.trace("addBook: book={}", book);
        bookValidator.validate(book);
        bookRepository.save(book);
        log.trace("addBook --- method finished");
    }

    /**
     * Removes a book based on its ID
     *
     * @param id is of type Long and represents
     *          an identifier for the book
     */
    @Override
    public void deleteBook(Long id){
        log.trace("deleteBook: id={}", id);
        bookRepository.deleteById(id);
        log.trace("deleteBook --- method finished");
    }


    /**
     * Updates a book having the id corresponding to the
     * id of the book given as input parameter
     *
     * @param newBook is of type Book,
     *              replacement for the old book
     */
    @Override
    @Transactional
    public void updateBook(Book newBook){
        log.trace("updateBook: book={}", newBook);
        bookValidator.validate(newBook);
        bookRepository.findById(newBook.getId())
                .ifPresent(book1 -> {
                    book1.setTitle(newBook.getTitle());
                    book1.setAuthor(newBook.getAuthor());
                    book1.setPrice(newBook.getPrice());
                    log.debug("updateBook --- book updated --- " +
                            "book={}", book1);
                });
        log.trace("updateBook --- method finished");
    }


    /**
     * Function that returns all the books from the bookstore.config.repository
     *
     * @return a List of Books that contains all existing books
     */
    @Override
    public List<Book> getAllBooks(){
        log.trace("getAllBooks --- method entered");
        List<Book> result = bookRepository.findAll();
        log.trace("getAllBooks: result={}", result);
        return result;
    }


    /**
     * Gets all books that contain the searchString in their title
     *
     * @param searchString a String
     *
     * @return list of books that meet filtering condition
     */
    @Override
    public List<Book>  filterByTitle(String searchString){
        log.trace("filterByTitle --- method entered");
        List<Book> result = bookRepository.findAll()
                .stream()
                .filter(book -> book.getTitle().contains(searchString))
                .collect(Collectors.toList());
        log.trace("filterByTitle: result={}", result);
        return result;
    }

    /**
     * Gets all books that have the specified author
     *
     * @param author a String
     *
     * @return list of books that meet filtering condition
     */
    @Override
    public List<Book>  filterByAuthor(String author){
        /*
        Iterable<Book> books = bookRepository.findAll();
        return StreamSupport.stream(books.spliterator(),false).filter((c)->c.getAuthor().equals(author)).collect(Collectors.toList());

         */
        return null;
    }

    @Override
    public List<Book> sortByAuthorAndTitleAndID(){
        /*
        SortingRepository<Long,Book> caste = (SortingRepository<Long, Book>) this.bookRepository;
        Sort sortObject = new Sort("author").and(new Sort(Boolean.FALSE,"title").and(new Sort("id")));
        return StreamSupport.stream(caste.findAll(sortObject).spliterator(),false).collect(Collectors.toList());

         */
        return null;
    }
}
