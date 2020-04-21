package serverModule.repository.database_repository;

import commonModule.domain.Client;
import commonModule.domain.validators.BookstoreException;
import commonModule.domain.validators.ClientValidator;
import commonModule.domain.validators.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import serverModule.repository.database_repository.sort_pack.Sort;
import serverModule.repository.database_repository.sort_pack.SortAlgorithm;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class ClientDBRepository implements SortingRepository<Long, Client> {

    @Autowired
    private JdbcOperations jdbcOperations;

    @Autowired
    private ClientValidator validator;

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
    public Iterable<Client> findAll(Sort sort) {
        ArrayList<Client> clients = new ArrayList<>();
        StreamSupport.stream(findAll().spliterator(),false).forEach(clients::add);
        SortAlgorithm<Long,Client> algorithm = new SortAlgorithm<>(clients,sort);
        return algorithm.sort();
    }


    @Override
    public Optional<Client> findOne(Long id) throws BookstoreException {
        String sqlCommandString = "select * from clients where id=?";
        Client client = jdbcOperations.queryForObject(sqlCommandString,
                new Object[] {id},
                (rs, rowNum) -> createClient(rs));
        return Optional.ofNullable(client);
    }

    @Override
    public Iterable<Client> findAll() throws BookstoreException{

        String sqlCommand = "select * from clients";
        return  jdbcOperations.query(sqlCommand, (rs, rowNum) -> createClient(rs));
    }

    @Override
    public Optional<Client> save(Client entity) throws ValidatorException,BookstoreException {
        validator.validate(entity);
        Optional<Client> exists = this.findOne(entity.getId());
        if(exists.isPresent())
            return Optional.of(entity);
        String sqlCommand = "insert into clients(id,name,moneySpent) values (?,?,?)";
        jdbcOperations.update(sqlCommand, entity.getId().intValue(), entity.getName(), entity.getMoneySpent());
        return Optional.empty();
    }

    @Override
    public Optional<Client> delete(Long aLong) throws BookstoreException {
        Optional<Client> result = this.findOne(aLong);
        String sqlCommand = "delete from clients where id=?";
        jdbcOperations.update(sqlCommand, aLong.intValue() );
        return result;
    }

    @Override
    public Optional<Client> update(Client entity) throws ValidatorException,BookstoreException {
        validator.validate(entity);
        Optional<Client> result = this.findOne(entity.getId());
        String sqlCommand = "update clients set name=?, moneyspent=? where id=?";
        jdbcOperations.update(sqlCommand, entity.getName(), entity.getMoneySpent(), entity.getId().intValue());
        return result;
    }
}
