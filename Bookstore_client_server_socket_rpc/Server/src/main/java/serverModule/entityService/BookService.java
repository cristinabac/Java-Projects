package serverModule.entityService;

import commonModule.domain.Book;
import commonModule.domain.validators.ValidatorException;
import serverModule.repository.Repository;
import serverModule.repository.database_repository.SortingRepository;
import serverModule.repository.database_repository.sort_pack.Sort;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class BookService {
    private Repository<Long, Book> bookRepository;

    public BookService(Repository<Long, Book> bookRepository) {
        this.bookRepository = bookRepository;
    }


    /**
     *Adds a new book to the repository
     *
     * @param book is a Book
     * @throws ValidatorException if the book will
     *            not pass the validation step
     * @return boolean - True if added, false otherwise
     */
    public boolean addBook(Book book) throws ValidatorException {
        AtomicBoolean bool = new AtomicBoolean(true);
        bookRepository.save(book).ifPresentOrElse((v)->{bool.set(false);},()->{bool.set(true);});
        return bool.get();
    }

    /**
     * Removes a book based on its ID
     *
     * @param id is of type Long and represents
     *          an identifier for the book
     * @return  True if deleted, false otherwise
     */
    public boolean deleteBook(Long id){
        AtomicBoolean bool = new AtomicBoolean(true);
        bookRepository.delete(id).ifPresentOrElse((v)->{bool.set(true);},()->bool.set(false));
        return bool.get();
    }


    /**
     * Updates a book having the id corresponding to the
     * id of the book given as input parameter
     *
     * @param newBook is of type Book,
     *              replacement for the old book
     * @return True if updated, false otherwise
     */
    public boolean updateBook(Book newBook){
        AtomicBoolean bool = new AtomicBoolean(true);
        bookRepository.update(newBook).ifPresentOrElse((v)->bool.set(true),()->bool.set(false));
        return bool.get();
    }


    /**
     * Function that returns all the books from the repository
     *
     * 
     * @return a List of Books that contains all existing books
     */

    public List<Book> getAllBooks(){
        Iterable<Book> books = bookRepository.findAll();
        return StreamSupport.stream(books.spliterator(),false).collect(Collectors.toList());

    }


    /**
     * Gets all books that contain the searchString in their title
     *
     * @param searchString a String
     *
     * @return list of books that meet filtering condition
     */
    public List<Book>  filterByTitle(String searchString){
        Iterable<Book> books = bookRepository.findAll();
        return StreamSupport.stream(books.spliterator(),false).filter((c)->c.getTitle().contains(searchString)).collect(Collectors.toList());
    }

    /**
     * Gets all books that have the specified author
     *
     * @param author a String
     *
     * @return list of books that meet filtering condition
     */
    public List<Book>  filterByAuthor(String author){
        Iterable<Book> books = bookRepository.findAll();
        return StreamSupport.stream(books.spliterator(),false).filter((c)->c.getAuthor().equals(author)).collect(Collectors.toList());
    }

    public List<Book> sortByAuthorAndTitleAndID(){
        SortingRepository<Long,Book> caste = (SortingRepository<Long, Book>) this.bookRepository;
        Sort sortObject = new Sort("author").and(new Sort(Boolean.FALSE,"title").and(new Sort("id")));
        return StreamSupport.stream(caste.findAll(sortObject).spliterator(),false).collect(Collectors.toList());
    }
}
