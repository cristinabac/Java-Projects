package repository.file_repository;

import domain.Book;
import domain.validators.BookstoreException;
import domain.validators.Validator;
import domain.validators.ValidatorException;
import repository.InMemoryRepository;

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

public class BookFileRepository extends InMemoryRepository<Long, Book> {

    private String fileName;

    public BookFileRepository(Validator<Book> validator, String fileName) {
        super(validator);
        this.fileName = fileName;

        loadData();
    }


    /**
     * Function that reads books from the file specified by the filename
     *
     * @throws BookstoreException if reading from file fails
     */
    private void loadData() throws BookstoreException {
        Path path = Paths.get(fileName);

        try {
            Files.lines(path).forEach(line -> {
                List<String> items = Arrays.asList(line.split(","));

                Long id = Long.valueOf(items.get(0));
                String title = items.get(1);
                String author = items.get((2));
                int price = Integer.parseInt(items.get(3));

                Book book = new Book(title, author, price);
                book.setId(id);

                try {
                    super.save(book);
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException ex) {
            //ex.printStackTrace();
            throw new BookstoreException("Loading books failed - Book file repo -" + ex.getMessage());
        }
    }

    /**
     * Saves the given entity.
     *
     * @param entity Book
     *            must not be null.
     * @return an {@code Optional}  - null if the entity was saved otherwise (e.g. id already exists) returns the entity.
     * @throws ValidatorException
     *              if the entity is not valid.
     */
    @Override
    public Optional<Book> save(Book entity) throws ValidatorException {
        Optional<Book> optional = super.save(entity);
        if (optional.isPresent()) {
            return optional;
        }
        saveToFile(entity);
        return Optional.empty();
    }


    /**
     * Fucntion that saves to the file specified by the filename
     *
     * @param entity - Book
     * @throws BookstoreException
     *              if saving to file fails
     */
    private void saveToFile(Book entity) throws BookstoreException{
        Path path = Paths.get(fileName);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            bufferedWriter.write(
                    entity.getId() + "," + entity.getTitle() + "," + entity.getAuthor() + "," + entity.getPrice());
            bufferedWriter.newLine();
        } catch (IOException e) {
            //e.printStackTrace();
            throw new BookstoreException("Saving books failed - Book file repo -" + e.getMessage());
        }
    }


    /**
     *  Updates the given entity.
     *
     * @param book - Book
     *             must not be null.
     * @return an {@code Optional} - null if the entity was updated otherwise (e.g. id does not exist) returns the
     *             entity.
     * @throws BookstoreException
     *               if saving to file fails
     */
    @Override
    public Optional<Book> update(Book book) throws BookstoreException{
        Optional<Book> optional = super.update(book); // try to update it
        PrintWriter pw = null;
        try {
            // empty the content of the file and rewrite the remaining entities after update
            pw = new PrintWriter(fileName);
            pw.write("");
            pw.close();
            findAll().forEach(this::saveToFile);
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            throw new BookstoreException(e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Removes the entity with the given id.
     *
     * @param aLong
     *       must not be null.
     * @return an {@code Optional} - null if there is no entity with the given id, otherwise the removed entity.
     * @throws BookstoreException
     *      if saving to file fails
     */
    @Override
    public Optional<Book> delete(Long aLong) throws BookstoreException{
        Optional<Book> optional = super.delete(aLong); // try to delete it
        PrintWriter pw = null;
        try {
            // empty the content of the file and rewrite the remaining entities after delete
            pw = new PrintWriter(fileName);
            pw.write("");
            pw.close();
            findAll().forEach(this::saveToFile);
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            throw new BookstoreException(e.getMessage());
        }
        return Optional.empty();
    }



}
