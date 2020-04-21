package repository.file_repository;

import domain.Book;
import domain.Client;
import domain.validators.BookstoreException;
import domain.validators.Validator;
import domain.validators.ValidatorException;
import repository.InMemoryRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ClientFileRepository extends InMemoryRepository<Long, Client> {

    private String fileName;
    public ClientFileRepository(Validator<Client> validator,String fileName) {
        super(validator);
        this.fileName=fileName;

        loadData();


    }

    /**
     * Function that reads all client entities from the
     * file specified by the private field fileName
     * @throws BookstoreException  if reading from file fails
     */
    private void loadData() throws BookstoreException{
        Path filePath = Paths.get(fileName);
        try{
            Files.lines(filePath).forEach(line->{
                List<String> items = Arrays.asList(line.split(","));

                Long clientID = Long.valueOf(items.get(0));
                String clientName = items.get(1);
                Integer moneySpent = Integer.valueOf(items.get(2));

                Client client = new Client();
                client.setId(clientID);
                client.setName(clientName);
                client.setMoneySpent(moneySpent);

                super.save(client);
            });
        }
        catch (IOException exc){
            throw new BookstoreException("Loading Clients Failed - File Repository");
        }
    }

    /**
     *
     * @param entity Client
     *            must not be null.
     * @return an {@code Optional} null if the entity was saved otherwise (e.g. id already exists) returns the entity.
     * @throws ValidatorException if validation of entity fails
     * @throws BookstoreException if saving to file operation fails
     */
    @Override
    public Optional<Client> save(Client entity) throws ValidatorException,BookstoreException {
        Optional<Client> ent = super.save(entity);

        try(PrintWriter printWriter = new PrintWriter(fileName)){
            printWriter.write("");
        } catch (FileNotFoundException e) {
            throw new BookstoreException(e.getMessage());
        }
        super.findAll().forEach(this::saveInFile);
        return ent;
    }

    /**
     *
     * @param id Integer
     *            must not be null.
     * @return an {@code Optional} - null if there is no entity with the given id, otherwise the removed entity.
     * @throws BookstoreException - if writing operation fails
     */
    @Override
    public Optional<Client> delete(Long id) throws BookstoreException{
        Optional<Client> optionalClient = super.delete(id);

        try(PrintWriter printWriter = new PrintWriter(fileName)) {
            printWriter.write("");
        } catch (FileNotFoundException e) {
            throw new BookstoreException(e.getMessage());
        }
        super.findAll().forEach(this::saveInFile);
        return optionalClient;
    }


    /**
     *
     * @param entity: Client
     *            must not be null.
     * @return an {@code Optional} - null if the entity was updated otherwise (e.g. id does not exist) returns the
     *      *         entity.
     * @throws BookstoreException if writing to file operation fails
     */
    @Override
    public Optional<Client> update(Client entity)throws BookstoreException{
        Optional<Client> optionalClient = super.update(entity);
        try(PrintWriter printWriter = new PrintWriter(fileName)){
            printWriter.write("");
        } catch (FileNotFoundException e) {
            throw new BookstoreException(e.getMessage());
        }
        super.findAll().forEach(this::saveInFile);
        return optionalClient;
    }


    /**
     * @param entity - Client
     * @throws BookstoreException - if write operation fails
     */
    private void saveInFile(Client entity) throws BookstoreException {
        Path filePath = Paths.get(fileName);

        try(BufferedWriter buff = Files.newBufferedWriter(filePath, StandardOpenOption.APPEND)){
            buff.write(entity.getId()+","+entity.getName()+","+entity.getMoneySpent());
            buff.newLine();
        } catch (IOException e) {
            throw new BookstoreException(e.getMessage());
        }
    }

}
