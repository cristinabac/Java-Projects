package repository.file_repository;

import domain.Book;
import domain.Sale;
import domain.validators.BookstoreException;
import domain.validators.Validator;
import domain.validators.ValidatorException;
import repository.InMemoryRepository;
import service.BookService;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SalesFileRepository extends InMemoryRepository<Long, Sale> {
    private String fileName;

    public SalesFileRepository(Validator<Sale> validator,String fileName){
        super(validator);
        this.fileName=fileName;
        loadData();
    }

    /**
     * Function that reads all the Sale entities saved in a text file
     *
     * @throws BookstoreException if reading from file fails, or
     * content of file is not valid
     */
    private void loadData() throws BookstoreException{
        Path filePath = Paths.get(fileName);
        try{
            Files.lines(filePath).forEach((line)->{
                List<String> content  = Arrays.asList(line.split(","));
                Sale  sale = new Sale();
                Long bookID = Long.parseLong(content.get(0));
                Long clientID = Long.parseLong(content.get(1));

                sale.setBookId(bookID);
                sale.setClientId(clientID);

                super.save(sale);
            });
        }
        catch (IOException | NumberFormatException exc){
            throw new BookstoreException(exc.getMessage());
        }
    }

    /**
     *
     * @param entity Sale
     *            must not be null.
     * @return an {@code Optional} null if the entity was saved otherwise (e.g. id already exists) returns the entity.
     * @throws ValidatorException if validation of entity fails
     * @throws BookstoreException if saving to file operation fails
     */
    @Override
    public Optional<Sale> save(Sale entity)throws ValidatorException, BookstoreException {
        Optional<Sale> result = super.save(entity);

        try(PrintWriter writer = new PrintWriter(fileName)) {
            writer.write("");
        } catch (FileNotFoundException e) {
            throw new BookstoreException(e.getMessage());
        }

        super.findAll().forEach(this::saveFile);
        return result;
    }


    /**
     *
     * @param id Integer
     *            must not be null.
     * @return an {@code Optional} - null if there is no entity with the given id, otherwise the removed entity.
     * @throws BookstoreException - if writing operation fails
     */
    @Override
    public Optional<Sale> delete(Long id)throws BookstoreException{
        Optional<Sale> result = super.delete(id);
        try(PrintWriter writer = new PrintWriter(fileName)) {
            writer.write("");
        } catch (FileNotFoundException e) {
            throw new BookstoreException(e.getMessage());
        }
        super.findAll().forEach(this::saveFile);
        return result;
    }

    /**
     *
     * @param sale: Sale
     *            must not be null.
     * @return an {@code Optional} - null if the entity was updated otherwise (e.g. id does not exist) returns the
     *      *         entity.
     * @throws BookstoreException if writing to file operation fails
     */
    @Override
    public Optional<Sale> update(Sale sale)throws ValidatorException, BookstoreException {
        Optional<Sale> result = super.update(sale);
        try(PrintWriter writer = new PrintWriter(fileName)) {
            writer.write("");
        } catch (FileNotFoundException e) {
            throw new BookstoreException(e.getMessage());
        }
        super.findAll().forEach(this::saveFile);
        return result;
    }

    /**
     * Function that writes to file a Sale object
     * @param sale Sale
     * @throws BookstoreException if write operation fails
     */
    private void saveFile(Sale sale) throws BookstoreException {
        Path filePath = Paths.get(fileName);
        try(BufferedWriter bufferedWriter = Files.newBufferedWriter(filePath, StandardOpenOption.APPEND)) {
            String stringFormat = sale.getBookId()+","+sale.getClientId();
            bufferedWriter.write(stringFormat);
            bufferedWriter.newLine();
        } catch (IOException e) {
            throw new BookstoreException(e.getMessage());
        }

    }


}
