package repository.database_repository;

import domain.Client;
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

public class ClientDBRepository implements SortingRepository<Long, Client> {
    private static final String URL="jdbc:postgresql://localhost:5432/bookstore";
    private String userName;
    private String password;
    private Validator<Client> validator;


    public ClientDBRepository(Validator<Client> validator,String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Iterable<Client> findAll(Sort sort) {
        ArrayList<Client> clients = new ArrayList<>();
        StreamSupport.stream(findAll().spliterator(),false).forEach(clients::add);
        SortAlgorithm<Long,Client> algorithm = new SortAlgorithm<>(clients,sort);
        return algorithm.sort();
    }

    private Client createClient(ResultSet resultSet) throws SQLException {

        Client result = new Client();
        Long clientID = (long) resultSet.getInt("id");
        String clientName = resultSet.getString("name");
        Integer moneySpent = resultSet.getInt("moneyspent");
        result.setId(clientID);
        result.setName(clientName);
        result.setMoneySpent(moneySpent);
        return result;
    }

    @Override
    public Optional<Client> findOne(Long id) throws BookstoreException{
        String sqlCommandString = "select * from clients where id=?";
        Client result = new Client();

        try(Connection connection = DriverManager.getConnection(URL,userName,password)){
            PreparedStatement statement = connection.prepareStatement(sqlCommandString);
            statement.setLong(1,id);
            ResultSet resultSet = statement.executeQuery();
            //set up client, according to querry result
            if(resultSet.next()){
                result = createClient(resultSet);
                return Optional.of(result);
            }

        } catch (SQLException e) {
            throw new BookstoreException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Client> findAll() throws BookstoreException{

        String sqlCommand = "select * from clients";
        Set<Client> set = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(URL,userName,password)) {
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            ResultSet  resultSet = statement.executeQuery();
            while (resultSet.next()){
                set.add(createClient(resultSet));
            }
        } catch (SQLException e) {
            throw new BookstoreException(e.getMessage());
        }
        return set;
    }

    @Override
    public Optional<Client> save(Client entity) throws ValidatorException,BookstoreException {
        validator.validate(entity);
        Optional<Client> exists = this.findOne(entity.getId());
        if(exists.isPresent())
            return Optional.of(entity);
        try(Connection connection = DriverManager.getConnection(URL,userName,password)){
            String sqlCommand = "insert into clients(id,name,moneySpent) values (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setLong(1,entity.getId());
            statement.setString(2,entity.getName());
            statement.setInt(3,entity.getMoneySpent());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new BookstoreException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Client> delete(Long aLong) throws BookstoreException {
        Optional<Client> result = this.findOne(aLong);
        try(Connection connection = DriverManager.getConnection(URL,userName,password)) {
            String sqlCommand = "delete from clients where id=?";
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setLong(1,aLong);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new BookstoreException(e.getMessage());
        }
        return result;
    }

    @Override
    public Optional<Client> update(Client entity) throws ValidatorException,BookstoreException {
        validator.validate(entity);
        Optional<Client> result = this.findOne(entity.getId());
        try(Connection connection = DriverManager.getConnection(URL,userName,password)){
            String sqlCommand = "update clients set name=?, moneyspent=? where id=?";
            PreparedStatement statement = connection.prepareStatement(sqlCommand);
            statement.setString(1,entity.getName());
            statement.setInt(2,entity.getMoneySpent());
            statement.setLong(3,entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new BookstoreException(e.getMessage());
        }
        return result;
    }
}
