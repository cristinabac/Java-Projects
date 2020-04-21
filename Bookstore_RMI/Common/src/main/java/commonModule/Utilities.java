package commonModule;

import commonModule.domain.Book;
import commonModule.domain.Client;
import commonModule.domain.Sale;

import java.util.ArrayList;
import java.util.List;


public class Utilities {
    /**
     * Function that creates the CSV representation of a Client
     * CSV format for client: "clientID,clientName,clientMoneySpent"
     * @param client - client
     * @return String - CSV representation of a Client object
     */
    public  String clientToString(Client client){
        return client.getId()+","+client.getName()+","+client.getMoneySpent();
    }

    /**
     * Creates a client given a client in CSV string representation
     * @param clientCSV - CSV string representation of a Client
     * @return Client - created client from CSV representation
     */
    public Client stringToClient(String clientCSV){
        String[] attributes = clientCSV.split(",");
        Client client = new Client();
        client.setId(Long.parseLong(attributes[0]));
        client.setName(attributes[1]);
        client.setMoneySpent(Integer.parseInt(attributes[2]));
        return client;
    }

    /**
     *  Function that creates the CSV representation of a Book
     * CSV format for client: "bookID,bookAuthor,bookTitle,bookPrice"
     * @param book - book to be transformed into CSV format
     * @return CSV representation of BOOK
     */
    public String bookToString(Book book){
        return book.getId()+","+book.getAuthor()+","+book.getTitle()+","+book.getPrice();
    }

    /**
     * Creates a book from a given book in CSV string format
     * @param bookCSV - book in CSV  string representation
     * @return - created book
     */
    public Book stringToBook(String bookCSV){
        String[] attributes = bookCSV.split(",");
        Book book = new Book();
        book.setId(Long.parseLong(attributes[0]));
        book.setAuthor(attributes[1]);
        book.setTitle(attributes[2]);
        book.setPrice(Integer.parseInt(attributes[3]));
        return book;
    }

    /**
     * Creates the CSV string representation of a Sale Object
     * CSV representation:"saleID,bookID,clientID"
     * @param sale - sale object to be transformed in CSV representation
     * @return CSV string representation
     */
    public String saleToString(Sale sale){
        return  sale.getId()+","+sale.getBookId()+","+sale.getClientId();
    }


    /**
     * Creates a Sale object from a given sale in CSV string format
     * @param saleCSV - csv string
     * @return - created Sale object
     */
    public Sale stringToSale(String saleCSV){
        String[] attributes = saleCSV.split(",");
        Sale sale = new Sale();
        sale.setId(Long.parseLong(attributes[0]));
        sale.setBookId(Long.parseLong(attributes[1]));
        sale.setClientId(Long.parseLong(attributes[2]));
        return sale;
    }

    public static String fromClientListToString(List<Client> clientList){
        String string = "";
        for(Client client: clientList)
            string += new Utilities().clientToString(client) + ";";
        return string;
    }

    public static  List<Client> formStringToClientList(String stringFormat){
        List<Client> list = new ArrayList<>();
        String[] components = stringFormat.split(";");
        for(String component: components){
            list.add(new Utilities().stringToClient(component));
        }
        return list;
    }

    public static String fromBookListToString(List<Book> bookList){
        String string = "";
        for(Book book: bookList){
            string += new Utilities().bookToString(book)+";";
        }
        return string;
    }

    public static List<Book> fromStringToBookList(String stringFormat){
        String[] components = stringFormat.split(";");
        List<Book> books = new ArrayList<>();
        for(String component: components){
            books.add(new Utilities().stringToBook(component));
        }
        return books;
    }

    public static String fromSalesListToString(List<Sale> saleList){
        String string = "";
        for(Sale sale: saleList){
            string += new Utilities().saleToString(sale)+";";
        }
        return string;
    }

    public static List<Sale> fromStringToSaleList(String stringFormat){
        String[] components = stringFormat.split(";");
        List<Sale> sales = new ArrayList<>();
        for(String component: components){
            sales.add(new Utilities().stringToSale(component));
        }
        return sales;
    }
}
