package serverModule.repository.database_repository;

import commonModule.domain.Book;
import commonModule.domain.validators.BookValidator;
import commonModule.domain.validators.BookstoreException;
import commonModule.domain.validators.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import serverModule.repository.database_repository.sort_pack.Sort;
import serverModule.repository.database_repository.sort_pack.SortAlgorithm;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.StreamSupport;


public class BookDBRepository implements SortingRepository<Long,Book>{
    /*
    private static final String URL="jdbc:postgresql://localhost:5432/bookstore";
    private String userName = "postgres";
    private String password = "admin";

     */

    @Autowired
    private BookValidator validator;

    @Autowired
    private JdbcOperations jdbcOperations;

    /*
    public BookDBRepository(Validator<Book> validator,String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.validator = validator;
    }*/

    /*
    public BookDBRepository(){

    }

     */

    @Override
    public Iterable<Book> findAll(Sort sort) {
        ArrayList<Book> books = new ArrayList<>();
        StreamSupport.stream(findAll().spliterator(),false).forEach(books::add);
        SortAlgorithm<Long,Book> algorithm = new SortAlgorithm<>(books,sort);
        return algorithm.sort();
    }

    private Book createBook(ResultSet resultSet) throws SQLException {
        Book result = new Book();
        Long bookID = (long) resultSet.getInt("id");
        String bookTitle = resultSet.getString("title");
        String bookAuthor = resultSet.getString("author");
        Integer bookPrice = resultSet.getInt("price");
        result.setId(bookID);
        result.setTitle(bookTitle);
        result.setAuthor(bookAuthor);
        result.setPrice(bookPrice);
        return result;
    }

    @Override
    public Optional<Book> findOne(Long id) throws BookstoreException {
        String sqlCommand = "select * from books where id=?";
        Book book = jdbcOperations.queryForObject(sqlCommand, new Object[]{id}, (rs, rowNum) -> createBook(rs));
        return Optional.ofNullable(book);
    }

    @Override
    public Iterable<Book> findAll() throws BookstoreException{
        String sqlCommand = "select * from books";
        return jdbcOperations.query(sqlCommand, (rs, rowNum) -> createBook(rs));
    }

    @Override
    public Optional<Book> save(Book entity) throws ValidatorException,BookstoreException {
        validator.validate(entity);
        Optional<Book> exists = this.findOne(entity.getId());
        if (exists.isPresent())
            return Optional.of(entity);

        String sqlCommand = "insert into books(id,title,author,price) values (?,?,?,?)";
        jdbcOperations.update(sqlCommand, entity.getId(), entity.getTitle(), entity.getAuthor(), entity.getPrice());
        return Optional.empty();
    }

    @Override
    public Optional<Book> delete(Long aLong) throws BookstoreException {
        Optional<Book> result = this.findOne(aLong);
        String sqlCommand = "delete from books where id=?";
        jdbcOperations.update(sqlCommand, aLong);
        return result;
    }

    @Override
    public Optional<Book> update(Book entity) throws ValidatorException,BookstoreException {
        validator.validate(entity);
        Optional<Book> result = this.findOne(entity.getId());
        String sqlCommand = "update books set title=?, author=?,price=? where id=?";
        jdbcOperations.update(sqlCommand, entity.getTitle(), entity.getAuthor(), entity.getPrice(), entity.getId());
        return result;
    }
}
