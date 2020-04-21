package serverModule.repository.database_repository;

import commonModule.domain.Client;
import commonModule.domain.Sale;
import commonModule.domain.validators.BookstoreException;
import commonModule.domain.validators.SaleValidator;
import commonModule.domain.validators.Validator;
import commonModule.domain.validators.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import serverModule.repository.database_repository.sort_pack.Sort;
import serverModule.repository.database_repository.sort_pack.SortAlgorithm;

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

    @Autowired
    private JdbcOperations jdbcOperations;
    @Autowired
    private SaleValidator saleValidator;


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
        String sqlCommand = "select * from sales where id=?";
        Sale sale =  jdbcOperations.queryForObject(sqlCommand, new Object[] {aLong}, (rs, rowNum) -> createSale(rs));
        return Optional.empty();
    }

    @Override
    public Iterable<Sale> findAll() {
        String sqlCommand = "select * from sales";
        return jdbcOperations.query(sqlCommand,(rs, rowNum) -> createSale(rs));
    }

    @Override
    public Optional<Sale> save(Sale entity) throws ValidatorException,BookstoreException {
        saleValidator.validate(entity);
        Optional<Sale> result = this.findOne(entity.getId());
        if(result.isPresent())
            return Optional.of(entity);
        String sqlCommand = "insert into sales(id,bookid,clientid) values(?,?,?)";
        jdbcOperations.update(sqlCommand, entity.getId().intValue(), entity.getBookId().intValue(), entity.getClientId().intValue());
        return Optional.empty();
    }

    @Override
    public Optional<Sale> delete(Long aLong)throws BookstoreException {
        Optional<Sale> entity = findOne(aLong);
        String sqlCommand = "delete from sales where id=?";
        jdbcOperations.update(sqlCommand, aLong.intValue());
        return entity;
    }

    @Override
    public Optional<Sale> update(Sale entity) throws ValidatorException,BookstoreException {
        saleValidator.validate(entity);
        Optional<Sale> result = findOne(entity.getId());
        if(result.isEmpty())
            return Optional.of(entity);
        String sqlCommand = "update sales set bookid=?, clientid=? where id=?";
        jdbcOperations.update(sqlCommand, entity.getBookId().intValue(), entity.getClientId().intValue(), entity.getId().intValue());
        return Optional.empty();
    }
}
