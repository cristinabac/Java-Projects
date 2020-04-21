package bookstore_pck.ui;

import bookstore_pck.domain.Book;
import bookstore_pck.domain.Client;
import bookstore_pck.domain.Sale;
import bookstore_pck.domain.validators.BookstoreException;
import bookstore_pck.domain.validators.ValidatorException;
import bookstore_pck.service.SaleServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import bookstore_pck.service.BookServiceInterface;
import bookstore_pck.service.ClientServiceInterface;

import javax.crypto.spec.PSource;
import java.lang.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


//TODO: PLEASE ADD YOUR COMMANDS HERE
enum Commands{
    c1{
        @Override
        public void execCommand(Console console){
            console.addClientCommand();
        }
    },
    c2{
        @Override
        public void execCommand(Console console){
            console.printClientsOrderedCommand();
        }
    },
    c3{
        @Override
        public void execCommand(Console console){
            console.searchClientsCommand();
        }
    },
    c4{
        @Override
        public void execCommand(Console console){
            console.showClientsSortedByMoneySpentCommand();
        }
    },
    c5{
        @Override
        public void execCommand(Console console){
            console.updateClientCommand();
        }
    },
    c6{
        @Override
        public void execCommand(Console console){
            console.deleteClientCommand();
        }
    },
    x{
        @Override
        public void execCommand(Console console){
            System.exit(0);
        }
    },
    c7{
        @Override
        public void execCommand(Console console) {
            console.addBookCommand();
        }
    },
    c8{
        @Override
        public void execCommand(Console console) {
            console.deleteBookCommand();
        }
    },
    c9{
        @Override
        public void execCommand(Console console) {
            console.updateBookCommand();
        }
    },
    c10{
        @Override
        public void execCommand(Console console) {
            console.printBooksCommand();
        }
    },
    c11{
        @Override
        public void execCommand(Console console) {
            console.filterByAuthorCommand();
        }
    },
    c12{
        @Override
        public void execCommand(Console console) {
            console.filterByTitleCommand();
        }
    },
    c13{
        @Override
        public void execCommand(Console console) {
            console.sortBooks();
        }
    },
    c14{
        @Override
        public void execCommand(Console console) {
            console.sortClients();
        }
    },
    c15{
        @Override
        public void execCommand(Console console) {
            console.getSales();
        }
    },
    c16{
        @Override
        public void execCommand(Console console) {
            console.addSale();
        }
    };
    
    public abstract void execCommand(Console console);
}

@Component
public class Console {
    @Autowired
    private ClientServiceInterface clientService;
    @Autowired
    private BookServiceInterface bookService;
    @Autowired
    private SaleServiceInterface saleService;


    private void ShowMenu(){
        System.out.println("c1 - Add client.");
        System.out.println("c2 - Show all clients ordered alphabetically.");
        System.out.println("c3 - Search for clients.");
        System.out.println("c4 - Show clients sorted by money spent.");
        System.out.println("c5 - Update client");
        System.out.println("c6 - Delete client");
        System.out.println("c7 - Add book");
        System.out.println("c8 - Remove book");
        System.out.println("c9 - Update book");
        System.out.println("c10 - All books");
        System.out.println("c11 - Filter by Author");
        System.out.println("c12 - Filter by Title");
        System.out.println("c13 - Sort Books by Author,Title and ID");
        System.out.println("c14 - Sort Clients by Name and ID");
        System.out.println("c15 - Show Sales");
        System.out.println("c16 - Add Sale");
        System.out.println("x - Exit");
    }


    public void runConsole(){
        chooseCommands();
        runConsole();
    }

    private void chooseCommands(){
        ShowMenu();
        BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
        try{
            String inputCommand = buff.readLine();
            Commands.valueOf(inputCommand).execCommand(this);
        }
        catch (IOException exc){
            System.out.println("Reading failed! Try Again...");
        }
        catch (BookstoreException exc){
            System.out.println(exc.getMessage());
        }
        catch (IllegalArgumentException illExc){
            System.out.println("WRONG COMMAND!");
        }


    }

    /**
     * Function that reads and creates a new client
     *
     * @return a client
     * @throws BookstoreException if read fails, an error
     *           occurs during read operations
     */
     Client readClient()throws BookstoreException{
        Client client;
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.println("Enter client ID: ");
            Long clientID = Long.parseLong(buffer.readLine());
            System.out.println("Enter client name: ");
            String clientName = buffer.readLine();
            client = new Client(clientName);
            client.setId(clientID);
            return client;
        }
        catch (IOException exc){
            throw new BookstoreException("Read client command failed!");
        }
        catch (NumberFormatException exci){
            throw new BookstoreException("Invalid input!");
        }
    }

    /**
     * Reads a book from the user
     * @return a Book
     * @throws BookstoreException in case read fails or
     *             gives faulty input
     */
    Book readBook() throws BookstoreException{
         Book book;
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.println("Enter book ID: ");
            Long bookID = Long.parseLong(buffer.readLine());
            System.out.println("Enter tile: ");
            String bookTitle = buffer.readLine();
            System.out.println("Enter author: ");
            String bookAuthor = buffer.readLine();
            System.out.println("Enter price");
            Integer price = Integer.parseInt(buffer.readLine());
            book = new Book(bookTitle,bookAuthor,price);
            book.setId(bookID);
            return book;
        }
        catch (IOException exc){
            throw new BookstoreException("Read client command failed!");
        }
        catch (NumberFormatException exci){
            throw new BookstoreException("Invalid input!");
        }
    }

    /**
     *
     * @throws BookstoreException  if readClient fails
     * @throws ValidatorException if addClient from
     *         client service fails
     */
    void addClientCommand()throws BookstoreException, ValidatorException {
        Client newClient = readClient();
        try {
            clientService.addClient(newClient);
        } catch (ValidatorException e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    /**
     * Adds a new book to the book service
     * @throws BookstoreException in case readBook() fails
     * @throws ValidatorException in case addBook() in service fails
     */
    void addBookCommand() throws BookstoreException,ValidatorException{
        Book bookToBeAdded = readBook();
        try {
            bookService.addBook(bookToBeAdded);
        } catch (ValidatorException e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    /**
     * Prints all the clients sorted alphabetically
     */
    void printClientsOrderedCommand(){
        System.out.println("\t**********CLIENTS**********");
        List<Client> ordered = clientService.clientsSortedAlphabetically();
        ordered.forEach(System.out::println);

        System.out.println("\t***************************");
    }

    /**
     * Print all existing books from Service
     */
    void printBooksCommand(){
        System.out.println("\t**********BOOKS**********");
        bookService.getAllBooks().forEach(System.out::println);
        System.out.println("\t*************************");
    }

    /**
     * Prints all clients that contain a substring in their name
     *  substring - read from user
     *
     * @throws BookstoreException if read operation fails
     */
    void searchClientsCommand()throws BookstoreException{
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.println("Enter search key: ");
            String searchKey = buffer.readLine();
            clientService.filterByName(searchKey).forEach(System.out::println);
        }
        catch (IOException exc){
            throw new BookstoreException("Failed to read search key!");
        }
    }

    /**
     * prints all clients, in decr. order of money spent
     * on books.
     */
    void showClientsSortedByMoneySpentCommand(){
        System.out.println("********CLIENTS*********");
        clientService.clientsSortedByMoneySpent().forEach(System.out::println);
    }

    /**
     * Gets input from the user and updates a client
     */

    void updateClientCommand(){
        System.out.println("Enter the ID of the client you want to update\nand the new details!");
        Client client = readClient();
        clientService.updateClient(client);
    }

    /**
     * Updates a book
     */
    void updateBookCommand(){
        System.out.println("Enter the ID of the book you want to update\nand the new information:");
        Book updatedVersion = readBook();
        bookService.updateBook(updatedVersion);
    }
    /**
     * Deletes a client, by id given as  user input
     */
    public void deleteClientCommand() {
        BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
        Long id;
        String userInput;
        try{
            System.out.println("Enter the ID of the client you want to remove: ");
            userInput = buff.readLine();
            id = Long.parseLong(userInput);
            clientService.deleteClient(id);
        }
        catch (NumberFormatException exc){
            throw new BookstoreException("Invalid value for ID!");
        }
        catch (IOException ex){
            throw new BookstoreException("Read Failed!");
        }
    }

    /**
     * Deletes a book, based on the id given as input from the keyboard
     */
    public void deleteBookCommand(){
        BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
        Long id;
        String userInput;
        try{
            System.out.println("Enter the ID of the client you want to remove: ");
            userInput = buff.readLine();
            id = Long.parseLong(userInput);
            bookService.deleteBook(id);
        }
        catch (NumberFormatException exc){
            throw new BookstoreException("Invalid value for ID!");
        }
        catch (IOException ex){
            throw new BookstoreException("Read Failed!");
        }
    }

    /**
     * Prints all books filtered by the author name
     * @throws BookstoreException if read opartion fails
     */
    void filterByAuthorCommand() throws BookstoreException{
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.println("Enter search key: ");
            String searchKey = buffer.readLine();
            bookService.filterByAuthor(searchKey).forEach(System.out::println);
        }
        catch (IOException exc){
            throw new BookstoreException("Failed to read search key!");
        }
    }


    void filterByTitleCommand() throws BookstoreException{
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.println("Enter search key: ");
            String searchKey = buffer.readLine();
            bookService.filterByTitle(searchKey).forEach(System.out::println);
        }
        catch (IOException exc){
            throw new BookstoreException("Failed to read search key!");
        }
    }

    public void sortBooks() {
        try{
            System.out.println("\t****RESULT****");
            bookService.sortByAuthorAndTitleAndID().forEach(System.out::println);
            System.out.println("\t***************");
        }
        catch (BookstoreException e) {
            System.out.println(e.getMessage());
        }
    }

    public void sortClients() {
        try{
            System.out.println("\t****RESULT****");
            clientService.sortByNameAndID().forEach(System.out::println);
            System.out.println("\t***************");
        }
        catch (BookstoreException e) {
            System.out.println(e.getMessage());
        }
    }

    private Sale readSale(){
        Sale sale = new Sale();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.println("Enter book ID: ");
            Long bookID = Long.parseLong(buffer.readLine());
            System.out.println("Enter client ID: ");
            Long clientID = Long.parseLong(buffer.readLine());
            sale.setClientId(clientID);
            sale.setBookId(bookID);
            return sale;
        }
        catch (IOException exc){
            throw new BookstoreException("Read client command failed!");
        }
        catch (NumberFormatException exci){
            throw new BookstoreException("Invalid input!");
        }
    }

    public void getSales() {
        System.out.println("\t**********SALES**********");
        saleService.getAllSales().forEach(System.out::println);
        System.out.println("\t*************************");
    }

    public void addSale() {
        Sale sale = readSale();
        try {
            saleService.buy(sale.getBookId(), sale.getClientId());
        }
        catch (BookstoreException e){
            System.out.println(e.getMessage());
        }
    }
}
