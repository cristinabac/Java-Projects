package serverModule.entityService;

import commonModule.domain.Book;
import commonModule.domain.Client;
import commonModule.domain.Sale;
import serverModule.repository.Repository;
import serverModule.repository.database_repository.SortingRepository;
import serverModule.repository.database_repository.sort_pack.Sort;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SaleService {

    private Repository<Long, Book> bookRepository;
    private Repository<Long, Client> clientRepository;
    private Repository<Long, Sale> saleRepository;

    public SaleService(Repository<Long, Book> bookRepository, Repository<Long, Client> clientRepository,
                           Repository<Long, Sale> sales) {
        this.bookRepository = bookRepository;
        this.clientRepository = clientRepository;
        this.saleRepository = sales;
    }


    /**
     * returns all the sales from the repository.
     *
     * @return a list which contains all the sales
     */
    public List<Sale> getAllSales() {
        Iterable<Sale> sales = saleRepository.findAll();
        return StreamSupport.stream(sales.spliterator(),false).collect(Collectors.toList());
    }


    /**
     * A client buys a book, the amount of his spent money increases.
     *
     * @param idBook   is a Long
     * @param idClient is a Long
     */
    public boolean buy(Long idBook, Long idClient) {
        if(clientRepository.findOne(idClient).isEmpty())
            return false;
        Client oldClient = clientRepository.findOne(idClient).get();
        if(bookRepository.findOne(idBook).isEmpty())
            return false;
        Book book = bookRepository.findOne(idBook).get();

        Client updatedClient = new Client(oldClient.getName());
        updatedClient.setId(idClient);
        updatedClient.setMoneySpent(oldClient.getMoneySpent() + book.getPrice());

        clientRepository.update(updatedClient);
        saleRepository.save(new Sale(idBook, idClient));
        return  true;
    }


    /**
     * returns all the sales from the repository.
     *
     * @return a set which contains all the sales
     */
    private Set<Sale> getSalesSet() {
        Iterable<Sale> sales = saleRepository.findAll();
        return StreamSupport.stream(sales.spliterator(), false).collect(Collectors.toSet());
    }



    Repository<Long, Book> getBookRepository() {
        return bookRepository;
    }

    Repository<Long, Client> getClientRepository() {
        return clientRepository;
    }

    /**
     * Returns an Repository with all the saleRepository made, consisting of pairs (id of the book sold, id of the client who bought it ).
     *
     * @return saleRepository is an repository with the saleRepository (bookId, clientId)
     */
    Repository<Long,Sale> getSaleRepository(){
        return saleRepository;
    }

    public List<Sale> sortByBookIdAndClientId(){
        SortingRepository<Long,Sale> casted = (SortingRepository<Long,Sale>) this.saleRepository;
        Sort sortObject = new Sort("bookId","clientId");
        return StreamSupport.stream(casted.findAll(sortObject).spliterator(),false).collect(Collectors.toList());
    }
}
