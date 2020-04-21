package repository.database_repository;

import domain.Sale;
import domain.validators.BookstoreException;
import domain.validators.Validator;
import domain.validators.ValidatorException;
import repository.database_repository.sort_pack.Sort;
import repository.database_repository.sort_pack.SortAlgorithm;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

public class SaleDBRepository implements SortingRepository<Long, Sale> {
    private String URL = "jdbc:postgresql://localhost:5432/bookstore";
    private String username;
    private String password;
    private Validator<Sale> saleValidator;

    public SaleDBRepository(Validator<Sale> saleValidator,String username, String password) {
        this.username = username;
        this.password = password;
        this.saleValidator = saleValidator;
    }

    private Sale createSale(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        Long bookID = resultSet.getLong("bookid");
        Long clientID = resultSet.getLong("clientid");

        Sale sale = new Sale();
        sale.setId(id);
        sale.setBookId(bookID);
        sale.setClientId(clientID);

        return sale;
    }

    @Override
    public Iterable<Sale> findAll(Sort sort) throws BookstoreException {
        ArrayList<Sale> sales = new ArrayList<>();
        StreamSupport.stream(findAll().spliterator(),false).forEach(sales::add);
        SortAlgorithm<Long,Sale> algorithm = new SortAlgorithm<>(sales,sort);
        return algorithm.sort();
    }

    @Override
    public Optional<Sale> findOne(Long aLong) {
        try(Connection connection = DriverManager.getConnection(URL,username,password)){
            String sqlCommand = "select * from sales where id=?";
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setLong(1,aLong);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return Optional.of(createSale(resultSet));
            }
        } catch (SQLException e) {
            throw new BookstoreException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Sale> findAll() {
        Set<Sale> set =  new HashSet<>();
        try(Connection connection = DriverManager.getConnection(URL,username,password)){
            String sqlCommand = "select * from sales";
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                set.add(this.createSale(resultSet));
            }
        } catch (SQLException e) {
            throw new BookstoreException(e.getMessage());
        }
        return set;
    }

    @Override
    public Optional<Sale> save(Sale entity) throws ValidatorException,BookstoreException {
        saleValidator.validate(entity);
        Optional<Sale> result = this.findOne(entity.getId());
        if(result.isPresent())
            return Optional.of(entity);

        try(Connection connection = DriverManager.getConnection(URL,username,password)){
            String sqlCommand = "insert into sales(id,bookid,clientid) values(?,?,?)";
            PreparedStatement  statement = connection.prepareStatement(sqlCommand);
            statement.setLong(1,entity.getId());
            statement.setLong(2,entity.getBookId());
            statement.setLong(3,entity.getClientId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new BookstoreException(e.getMessage());
        }
        return result;
    }

    @Override
    public Optional<Sale> delete(Long aLong)throws BookstoreException {
        Optional<Sale> entity = findOne(aLong);
        try(Connection connection = DriverManager.getConnection(URL,username,password)){
            String sqlCommand = "delete from sales where id=?";
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setLong(1,aLong);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new BookstoreException(e.getMessage());
        }
        return entity;
    }

    @Override
    public Optional<Sale> update(Sale entity) throws ValidatorException,BookstoreException {
        saleValidator.validate(entity);
        Optional<Sale> result = findOne(entity.getId());
        if(result.isEmpty())
            return Optional.of(entity);
        try(Connection connection = DriverManager.getConnection(URL,username,password)){
            String sqlCommand = "update sales set bookid=?, clientid=? where id=?";
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setLong(1,entity.getBookId());
            statement.setLong(2,entity.getClientId());
            statement.setLong(3,entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new BookstoreException(e.getMessage());
        }
        return Optional.empty();
    }
}
