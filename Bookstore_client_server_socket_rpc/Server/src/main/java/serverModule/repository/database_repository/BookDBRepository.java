package serverModule.repository.database_repository;

import commonModule.domain.Book;
import commonModule.domain.validators.BookstoreException;
import commonModule.domain.validators.Validator;
import commonModule.domain.validators.ValidatorException;
import serverModule.repository.database_repository.sort_pack.Sort;
import serverModule.repository.database_repository.sort_pack.SortAlgorithm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import java.sql.*;
import java.util.stream.StreamSupport;


public class BookDBRepository implements SortingRepository<Long, Book> {
    private static final String URL="jdbc:postgresql://localhost:5432/bookstore";
    private String userName;
    private String password;
    private Validator<Book> validator;


    public BookDBRepository(Validator<Book> validator,String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.validator = validator;
    }

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
        String sqlCommandString = "select * from books where id=?";
        Book result = new Book();

        try(Connection connection = DriverManager.getConnection(URL,userName,password)){
            PreparedStatement statement = connection.prepareStatement(sqlCommandString);
            statement.setLong(1,id);
            ResultSet resultSet = statement.executeQuery();
            //set up book, according to query result
            if(resultSet.next()){
                result = createBook(resultSet);
                return Optional.of(result);
            }

        } catch (SQLException e) {
            throw new BookstoreException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Book> findAll() throws BookstoreException{

        String sqlCommand = "select * from books";
        Set<Book> set = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(URL,userName,password)) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            ResultSet  resultSet = statement.executeQuery();
            while (resultSet.next()){
                set.add(createBook(resultSet));
            }
        } catch (SQLException e) {
            throw new BookstoreException(e.getMessage());
        }
        return set;
    }

    @Override
    public Optional<Book> save(Book entity) throws ValidatorException,BookstoreException {
        validator.validate(entity);
        Optional<Book> exists = this.findOne(entity.getId());
        if(exists.isPresent())
            return Optional.of(entity);
        try(Connection connection = DriverManager.getConnection(URL,userName,password)){
            String sqlCommand = "insert into books(id,title,author,price) values (?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setLong(1,entity.getId());
            statement.setString(2,entity.getTitle());
            statement.setString(3, entity.getAuthor());
            statement.setInt(4,entity.getPrice());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new BookstoreException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Book> delete(Long aLong) throws BookstoreException {
        Optional<Book> result = this.findOne(aLong);
        try(Connection connection = DriverManager.getConnection(URL,userName,password)) {
            String sqlCommand = "delete from books where id=?";
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setLong(1,aLong);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new BookstoreException(e.getMessage());
        }
        return result;
    }

    @Override
    public Optional<Book> update(Book entity) throws ValidatorException,BookstoreException {
        validator.validate(entity);
        Optional<Book> result = this.findOne(entity.getId());
        try(Connection connection = DriverManager.getConnection(URL,userName,password)){
            String sqlCommand = "update books set title=?, author=?,price=? where id=?";
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setString(1,entity.getTitle());
            statement.setString(2,entity.getAuthor());
            statement.setInt(3,entity.getPrice());
            statement.setLong(4,entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new BookstoreException(e.getMessage());
        }
        return result;
    }
}
