package bookstore_pck.service;

import bookstore_pck.domain.Client;
import bookstore_pck.domain.validators.ClientValidator;
import bookstore_pck.domain.validators.ValidatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import bookstore_pck.repository.ClientRepository;
import org.springframework.transaction.annotation.Transactional;

/*
import bookstore.config.repository.Repository;
import bookstore.config.repository.database_repository.SortingRepository;
import bookstore.config.repository.database_repository.sort_pack.Sort;

 */

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class ClientService implements ClientServiceInterface{
    private static final Logger log = LoggerFactory.getLogger(
            ClientService.class);

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientValidator clientValidator;


    /**
     *Adds a new client to the bookstore.config.repository
     *
     * @param client is a Client
     * @throws ValidatorException if the client will
     *            not pass the validation step
     */
    @Override
    public void addClient(Client client) throws ValidatorException {
        log.trace("addClient: client={}", client);
        clientValidator.validate(client);
        clientRepository.save(client);
        log.trace("addClient --- method finished");
    }

    /**
     * Removes a client based on its ID
     *
     * @param id is of type Long and represents
     *          an identifier for the client
     */
    @Override
    public void deleteClient(Long id){
        log.trace("deleteClient: id={}", id);
        clientRepository.deleteById(id);
        log.trace("deleteClient --- method finished");
    }


    /**
     * Updates a client having the id corresponding to the
     * id of the client given as input parameter
     *
     * @param newClient is of type Client,
     *              replacement for the old client
     */
    @Override
    @Transactional
    public void updateClient(Client newClient){
        log.trace("updateClient: client={}", newClient);
        clientValidator.validate(newClient);
        clientRepository.findById(newClient.getId())
                .ifPresent(client1 -> {
                    client1.setName(newClient.getName());
                    client1.setMoneySpent(newClient.getMoneySpent());
                    log.debug("updateClient --- client updated --- " +
                            "client={}", client1);
                });
        log.trace("updateClient --- method finished");
    }


    /**
     * Function that returns all the clients from the bookstore.config.repository
     *
     * @return a List of Clients that contains all existing clients
     */
    @Override
    public List<Client> getAllClients(){
        log.trace("getAllClients --- method entered");
        List<Client> result = clientRepository.findAll();
        log.trace("getAllClients: result={}", result);
        return result;
    }


    /**
     * Function that sorts all clients alphabetically, by their names
     *
     * @return a list with clients sorted by name
     */
    @Override
    public List<Client> clientsSortedAlphabetically(){
        log.trace("clientsSortedAlphabetically -- method entered");
        Iterable<Client> clients = clientRepository.findAll();
        List<Client> result =  StreamSupport.stream(clients.spliterator(),false).sorted(Comparator.comparing(Client::getName)).collect(Collectors.toList());
        log.trace("clientsSortedAlphabetically: result={}", result);
        return result;

    }



    /**
     * Function that orders clients by the ammount of spent money
     *
     *
     * @return a list of clients, ordered by moneySpent
     */
    @Override
    public List<Client> clientsSortedByMoneySpent(){
        log.trace("clientsSortedByMoneySpent -- method entered");
        Iterable<Client> clients = clientRepository.findAll();
        List<Client> result = StreamSupport.stream(clients.spliterator(),false).sorted(Comparator.comparing(Client::getMoneySpent).reversed()).collect(Collectors.toList());
        log.trace("clientsSortedByMoneySpent: result={}", result);
        return result;
    }


    /**
     * Gets all clients that contain the searchString in their name
     *
     * @param searchString a String
     *
     * @return list of clients that meet filtering condition
     */
    @Override
    public List<Client>  filterByName(String searchString){
        log.trace("filterByName -- method entered");
        Iterable<Client> clients = clientRepository.findAll();
        List<Client> result =  StreamSupport.stream(clients.spliterator(),false).filter((c)->c.getName().contains(searchString)).collect(Collectors.toList());
        log.trace("filterByName: result = {}", result);
        return result;

    }

    @Override
    public List<Client> sortByNameAndID(){
        /*
        SortingRepository<Long,Client> caste = (SortingRepository<Long, Client>) this.clientRepository;
        Sort sortObject = new Sort(Boolean.FALSE,"name").and(new Sort("id"));
        return StreamSupport.stream(caste.findAll(sortObject).spliterator(),false).collect(Collectors.toList());

         */
        return null;
    }
}
