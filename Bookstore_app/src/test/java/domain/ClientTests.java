package domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClientTests {
    private static final Long id = 1L;
    private static final Long newId = 2L;
    private static final String name = "name1";
    private static final String newName = "name2";
    private static final int newMoneySpent = 5;

    private Client client;


    @Before
    public void setUp(){
        client = new Client(name);
        client.setId(id);
        client.setMoneySpent(1);
    }


    @Test
    public void testGetName(){
        assertEquals("The names need to be equal",name,client.getName());
    }

    @Test
    public void testSetName(){
        client.setName(newName);
        assertEquals("Update on name failed",newName,client.getName());
    }

    @Test
    public void testGetId(){
        assertEquals("Failed to get Client ID",id,client.getId());
    }

    @Test
    public void testSetID(){
        client.setId(newId);
        assertEquals("Failed to update ID, in CLients",newId,client.getId());
    }

    @Test
    public void testGetMoney(){
        assertEquals("Failed to get money, in Clients",1,client.getMoneySpent());
    }

    @Test
    public void testSetMoney(){
        client.setMoneySpent(newMoneySpent);
        assertEquals("Failed to update sum of money spent, in Clients",newMoneySpent,client.getMoneySpent());
    }


    @Test
    public void testToStringMethod(){
        assert client.toString() != null;
    }
}
