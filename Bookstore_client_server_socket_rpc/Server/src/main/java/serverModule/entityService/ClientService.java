package serverModule.entityService;

import commonModule.domain.Client;
import commonModule.domain.validators.ValidatorException;
import serverModule.repository.Repository;
import serverModule.repository.database_repository.SortingRepository;
import serverModule.repository.database_repository.sort_pack.Sort;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ClientService {
    private Repository<Long, Client> clientRepository;


    public ClientService(Repository<Long, Client> clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     *Adds a new client to the repository
     *
     * @param client is a Client
     * @throws ValidatorException if the client will
     *            not pass the validation step
     * @return  true if added, false otherwise
     */
    public boolean addClient(Client client) throws ValidatorException {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        clientRepository.save(client).ifPresentOrElse((v)->atomicBoolean.set(false),()->atomicBoolean.set(true));
        return atomicBoolean.get();
    }


    /**
     * Removes a client based on its ID
     *
     * @param id is of type Long and represents
     *          an identifier for the client
     * @return true if removed from DB, false otherwise
     */
    public boolean deleteClient(Long id){
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        clientRepository.delete(id).ifPresentOrElse((v)->atomicBoolean.set(true),()->atomicBoolean.set(false));
        return atomicBoolean.get();
    }

    /**
     * Updates a client having the id corresponding to the
     * id of the client given as input parameter
     *
     * @param newClient is of type Client,
     *              replacement for the old client
     * @return true if updated, false otherwise
     */
    public boolean updateClient(Client newClient){
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        clientRepository.update(newClient).ifPresentOrElse((v)->atomicBoolean.set(true),()->atomicBoolean.set(false));
        return atomicBoolean.get();
    }


    /**
     * Function that returns all the clients from the repository
     *
     * @return a List of Clients that contains all existing clients
     */

    public List<Client> getAllClients(){
        Iterable<Client> clients = clientRepository.findAll();
        return StreamSupport.stream(clients.spliterator(),false).collect(Collectors.toList());

    }


    /**
     * Function that sorts all clients alphabetically, by their names
     *
     * @return a list with clients sorted by name
     */
    public List<Client> clientsSortedAlphabetically(){
        Iterable<Client> clients = clientRepository.findAll();
        return StreamSupport.stream(clients.spliterator(),false).sorted(Comparator.comparing(Client::getName)).collect(Collectors.toList());
    }


    /**
     * Function that orders clients by the ammount of spent money
     *
     *
     * @return a list of clients, ordered by moneySpent
     */
    public List<Client> clientsSortedByMoneySpent(){
        Iterable<Client> clients = clientRepository.findAll();
        return StreamSupport.stream(clients.spliterator(),false).sorted(Comparator.comparing(Client::getMoneySpent).reversed()).collect(Collectors.toList());
    }


    /**
     * Gets all clients that contain the searchString in their name
     *
     * @param searchString a String
     *
     * @return list of clients that meet filtering condition
     */
    public List<Client>  filterByName(String searchString){
        Iterable<Client> clients = clientRepository.findAll();
        return StreamSupport.stream(clients.spliterator(),false).filter((c)->c.getName().contains(searchString)).collect(Collectors.toList());
    }

    public List<Client> sortByNameAndID(){
        SortingRepository<Long,Client> caste = (SortingRepository<Long, Client>) this.clientRepository;
        Sort sortObject = new Sort(Boolean.FALSE,"name").and(new Sort("id"));
        return StreamSupport.stream(caste.findAll(sortObject).spliterator(),false).collect(Collectors.toList());
    }
}
