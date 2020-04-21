package bookstore_pck.service;

import bookstore_pck.domain.Book;
import bookstore_pck.domain.Client;
import bookstore_pck.domain.Sale;
import bookstore_pck.domain.validators.BookstoreException;
import bookstore_pck.repository.BookRepository;
import bookstore_pck.repository.ClientRepository;
import bookstore_pck.repository.SaleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class SaleService implements SaleServiceInterface {

    private static final Logger log = LoggerFactory.getLogger(
            ClientService.class);

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private SaleRepository saleRepository;


    /**
     * returns all the sales from the repository.
     *
     * @return a list which contains all the sales
     */
    @Override
    public List<Sale> getAllSales() {
        log.trace("getAllSales --- method entered");
        List<Sale> result = saleRepository.findAll();
        log.trace("getAllSales: result={}", result);
        return result;

    }


    /**
     * A client buys a book, the amount of his spent money increases.
     *
     * @param idBook   is a Long
     * @param idClient is a Long
     */
    @Override
    @Transactional
    public void buy(Long idBook, Long idClient) {
        log.trace("buy -- entered method ");


        Optional<Client> opt = clientRepository.findById(idClient);

        if(opt.isPresent()) {

            Client oldClient = clientRepository.findById(idClient).get();

            Optional<Book> opt2 = bookRepository.findById(idBook);

            if(opt2.isPresent()) {
                Book book = bookRepository.findById(idBook).get();

                Client updatedClient = new Client(oldClient.getName());
                updatedClient.setId(idClient);
                updatedClient.setMoneySpent(oldClient.getMoneySpent() + book.getPrice());

                //clientRepository.update(updatedClient);
                clientRepository.findById(updatedClient.getId())
                        .ifPresent(client1 -> {
                            client1.setName(updatedClient.getName());
                            client1.setMoneySpent(updatedClient.getMoneySpent());
                            log.debug("buy --- client updated --- " +
                                    "client={}", client1);
                        });

                saleRepository.save(new Sale(idBook, idClient));
            }
            else
                throw new BookstoreException("No book with that id");
        }
        else
            throw new BookstoreException("No client with that id");
        log.trace("buy --- method finished");
    }


    /*

    public List<Sale> sortByBookIdAndClientId(){
        SortingRepository<Long,Sale> casted = (SortingRepository<Long,Sale>) this.saleRepository;
        Sort sortObject = new Sort("bookId","clientId");
        return StreamSupport.stream(casted.findAll(sortObject).spliterator(),false).collect(Collectors.toList());
    }

     */

}
