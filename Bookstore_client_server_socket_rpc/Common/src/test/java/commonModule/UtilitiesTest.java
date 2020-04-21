package commonModule;

import commonModule.domain.Book;
import commonModule.domain.Client;
import commonModule.domain.Sale;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class UtilitiesTest {

    @Test
    public void testClientToString(){
        Client cl =  new Client();
        cl.setId(1L);
        cl.setName("a");
        cl.setMoneySpent(0);
        String strRepr = new Utilities().clientToString(cl);
        assert strRepr.equals("1,a,0");
    }

    @Test
    public void testStringToClient(){
        String str = "1,a,0";
        Client cl = new Utilities().stringToClient(str);
        assert cl.getId().equals(1L);
        assert cl.getName().equals("a");
        assert cl.getMoneySpent() == 0;
    }

    @Test
    public void testBookToString(){
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("a");
        book.setTitle("b");
        book.setPrice(0);
        String str = new Utilities().bookToString(book);
        assert str.equals("1,a,b,0");
    }

    @Test
    public  void testStringToBook(){
        String str = "1,a,b,0";
        Book book = new Utilities().stringToBook(str);
        assert book.getId().equals(1L);
        assert book.getAuthor().equals("a");
        assert book.getTitle().equals("b");
        assert book.getPrice() == 0;
    }

    @Test
    public void testSaleToString(){
        Sale sale = new Sale();
        sale.setId(1L);
        sale.setBookId(2L);
        sale.setClientId(3L);
        String str = new Utilities().saleToString(sale);
        assert str.equals("1,2,3");
    }

    @Test
    public void testStringToSale(){
        String str ="1,2,3";
        Sale sale = new Utilities().stringToSale(str);
        assert sale.getId().equals(1L);
        assert sale.getBookId().equals(2L);
        assert sale.getClientId().equals(3L);
    }

    @Test
    public void testCLientListToStrig(){
        List<Client> lst = new ArrayList<>();
        Client cl1 = new Client();
        cl1.setId(1L);
        cl1.setName("mihai");
        cl1.setMoneySpent(0);
        lst.add(cl1);
        Client cl2 = new Client();
        cl2.setId(2L);
        cl2.setName("miruna");
        cl2.setMoneySpent(3);
        lst.add(cl2);
        String str = Utilities.fromClientListToString(lst);
        assert str.equals("1,mihai,0;2,miruna,3;");
    }

    @Test
    public void testFromStringToClientList(){
        List<Client> lst = Utilities.formStringToClientList("1,mihai,0;2,miruna,3;");
        assert lst.get(0).getId().equals(1L);
        assert lst.get(0).getName().equals("mihai");
        assert lst.get(0).getMoneySpent() == 0;
        assert lst.get(1).getId().equals(2L);
        assert lst.get(1).getName().equals("miruna");
        assert lst.get(1).getMoneySpent() == 3;

    }
}
