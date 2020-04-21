package domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SaleTests {
    private static final Long bookID1 = 1L;
    private static final Long bookID2 = 2L;
    private static final Long clientID1 = 1L;
    public static final Long clientID2 = 2L;
    private Sale testSale;

    @Before
    public void setUp(){
        testSale = new Sale(bookID1,clientID1);
    }

    @After
    public void tearDown(){
        testSale = null;
    }

    @Test
    public void testDefaultConstructor(){
        Sale sale = new Sale();
    }

    @Test
    public void testGetIDMethod(){
       assert 1 == testSale.getId();
    }

    @Test
    public void testGetBookIDMethod(){
        assert bookID1.equals(testSale.getBookId());
    }

    @Test
    public void testUpdateBookIDMethod(){
        assert bookID1.equals(testSale.getBookId());
        testSale.setBookId(bookID2);
        assert bookID2.equals(testSale.getBookId());
    }

    @Test
    public void testGetClientIDMethod(){
        assert clientID1.equals(testSale.getClientId());
    }

    @Test
    public void testSetClientIDMethod(){
        assert clientID1.equals(testSale.getClientId());
        testSale.setClientId(clientID2);
        assert clientID2.equals(testSale.getClientId());
    }

    @Test
    public void testToStringMethod(){
        assert testSale.toString() != null;
    }
}
