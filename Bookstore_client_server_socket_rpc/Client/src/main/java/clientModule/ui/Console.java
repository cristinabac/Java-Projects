package clientModule.ui;


import commonModule.domain.Sale;
import commonModule.domain.validators.BookstoreException;
import commonModule.domain.Book;
import commonModule.domain.Client;
import commonModule.domain.validators.ValidatorException;
import commonModule.service.BookServiceInterface;
import commonModule.service.ClientServiceInterface;
import commonModule.service.SaleServiceInterface;

import java.lang.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

public class Console {
    private ClientServiceInterface clientServiceInterface;
    private BookServiceInterface bookServiceInterface;
    private SaleServiceInterface saleServiceInterface;


    public Console(ClientServiceInterface clientServiceInterface, BookServiceInterface bookServiceInterface, SaleServiceInterface saleServiceInterface) {
        this.clientServiceInterface = clientServiceInterface;
        this.bookServiceInterface = bookServiceInterface;
        this.saleServiceInterface = saleServiceInterface;
    }

    private void ShowMenu(){
        System.out.println("c1 - Add client.");
        System.out.println("c2 - Show all clients ordered alphabetically.");
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


    public void runConsole() {
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
        clientServiceInterface.addClient(newClient).thenAcceptAsync((res)->{
            if(res)
                System.out.println("Client added!");
            else
                System.out.println("Failed to add client!\nMake the used ID is unique!");
        });
    }

    /**
     * Adds a new book to the book service
     * @throws BookstoreException in case readBook() fails
     * @throws ValidatorException in case addBook() in service fails
     */
    void addBookCommand() throws BookstoreException,ValidatorException{
        Book bookToBeAdded = readBook();
        bookServiceInterface.addBook(bookToBeAdded).thenAcceptAsync((res)->{
            if(res)
                System.out.println("Book added!");
            else
                System.out.println("Failed to add book!\nMake the used ID is unique!");
        });
    }

    /**
     * Prints all the clients sorted alphabetically
     */
    void printClientsOrderedCommand(){

        CompletableFuture<List<Client>> ls = clientServiceInterface.getClients();
        ls.thenAcceptAsync((clientList -> {
            if(clientList.size()==0)
                System.out.println("+-+-+ NO CLIENTS REGISTERD +-+-+");
            else{
                System.out.println("\t**********CLIENTS**********");
                clientList.forEach(System.out::println);
                System.out.println("\t***************************");
            }
        }));

    }

    /**
     * Print all existing books from Service
     */
    void printBooksCommand(){
        CompletableFuture<List<Book>> compBooks = bookServiceInterface.getBooks();
        compBooks.thenAcceptAsync((books -> {
            if(books.size()==0){
                System.out.println("+-+-+ NO BOOKS REGISTERED +-+-+");
                return;
            }
            System.out.println("\t**********BOOKS**********");
            books.forEach(System.out::println);
            System.out.println("\t*************************");
        }));
    }



    /**
     * prints all clients, in decr. order of money spent
     * on books.
     */
    void showClientsSortedByMoneySpentCommand(){
        CompletableFuture<List<Client>> clientCompl = clientServiceInterface.showClientsOrderByMoney();
        clientCompl.thenAcceptAsync((clients->{
            if(clients.size() == 0){
                System.out.println("+-+-+ NO RESULTS +-+-+");
                return;
            }
            System.out.println("********CLIENTS*********");
            clients.forEach(System.out::println);
        }));

    }

    /**
     * Gets input from the user and updates a client
     */

    void updateClientCommand(){
        System.out.println("Enter the ID of the client you want to update\nand the new details!");
        Client client = readClient();
        clientServiceInterface.updateClient(client).thenAcceptAsync((res)->{
            if(res)
                System.out.println("Client updated!");
            else
                System.out.println("Update failed!\nMake sure client ID is valid!");
        });
    }

    /**
     * Updates a book
     */
    void updateBookCommand(){
        System.out.println("Enter the ID of the book you want to update\nand the new information:");
        Book updatedVersion = readBook();
        bookServiceInterface.updateBook(updatedVersion).thenAcceptAsync((res)->{
            if(res)
                System.out.println("Book updated.");
            else
                System.out.println("Update failed!\nMake sure you use a valid ID!");
        });
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
            clientServiceInterface.removeClient(id).thenAcceptAsync((res)->{
                if(res)
                    System.out.println("Client removed!");
                else
                    System.out.println("Failed to remove client!\nMake sure client ID is valid!");
            });
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
            bookServiceInterface.removeBook(id).thenAcceptAsync((res)->{
                if(res)
                    System.out.println("Book removed");
                else
                    System.out.println("Remove failed!\nMake sure used ID is valid!");
            });
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
            CompletableFuture<List<Book>> bookCompl = bookServiceInterface.booksFilteredByAuthor(searchKey);
            bookCompl.thenAcceptAsync((books)->{
                if(books.size()==0){
                    System.out.println("No results!");
                    return;
                }
                System.out.println("RESULTS:");
                books.forEach(System.out::println);

            }
            );
        }
        catch (IOException exc) {
            throw new BookstoreException("Failed to read search key!");
        }
    }


    void filterByTitleCommand() throws BookstoreException{
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.println("Enter search key: ");
            String searchKey = buffer.readLine();
            bookServiceInterface.booksFilteredByTile(searchKey).thenAcceptAsync((books)->{
                if(books.size() == 0){
                    System.out.println("No results!");
                    return;
                }
                System.out.println("Results:");
                books.forEach(System.out::println);
            });
        }
        catch (IOException exc){
            throw new BookstoreException("Failed to read search key!");
        }
    }

    public void sortBooks() {
        try{
            bookServiceInterface.booksSortedByAuthorTitleID().thenAcceptAsync((books -> {
                if(books.size() == 0){
                    System.out.println("No data registered!");
                    return;
                }
                System.out.println("\t****RESULT****");
                books.forEach(System.out::println);
                System.out.println("\t***************");
            }));
        }
        catch (BookstoreException e) {
            System.out.println(e.getMessage());
        }
    }

    public void sortClients() {
        clientServiceInterface.clientsSortedByNameID().thenAcceptAsync((clients)->{
            try {

                if (clients.size() == 0) {
                    System.out.println("No data registered!");
                    return;
                }
                System.out.println("\t****RESULT****");
                clients.forEach(System.out::println);
                System.out.println("\t***************");
            }
            catch (BookstoreException e) {
                System.out.println(e.getMessage());
            }
        });

    }

    public void getSales() {
        saleServiceInterface.getSales().thenAcceptAsync((saleList -> {
            if(saleList.size() == 0){
                System.out.println("No data!");
                return;
            }
            System.out.println("\t****Sales*****");
            saleList.forEach(System.out::println);
        }));
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

    public void addSale() {
        Sale sale = readSale();
        saleServiceInterface.addSale(sale).thenAcceptAsync((res)->{
            if(res)
                System.out.println("Sale added!");
            else
                System.out.println("Failed to add sale!\nMake sure");
        });
    }

}
