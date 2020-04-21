package repository.dbrepo.sort;

import domain.BaseEntity;
import domain.Client;
import domain.Sale;
import org.junit.Test;
import repository.database_repository.sort_pack.Sort;
import repository.database_repository.sort_pack.SortAlgorithm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SortAlgorithmTest {

    @Test
    public  void minimalistTest(){
        Sort sort = new Sort("id");
        ArrayList<Client> arr = new ArrayList<>();
        Client  client1 = new Client("Arge");
        client1.setId(20L);
        Client client2 = new Client("ZDanciu");
        client2.setId(10L);
        Client client3 = new Client("Mircea");
        client3.setId(15L);
        arr.add(client1);
        arr.add(client2);
        arr.add(client3);

        SortAlgorithm<Long,Client> test = new SortAlgorithm<>(arr,sort);
        assert  10L == test.sort().get(0).getId();

        sort = new Sort("name");
        test = new SortAlgorithm<>(arr,sort);
        assert test.sort().get(0).getName().equals("Arge");
    }


    @Test
    public  void testCompoundedSort(){
        Client  client1 = new Client("Arge");
        client1.setId(20L);
        Client client2 = new Client("Zbeng");
        client2.setId(10L);
        Client client3 = new Client("Arge");
        client3.setId(15L);
        Client client4 = new Client("Arge");
        client4.setId(18L);
        Client client5 = new Client("Zbeng");
        client5.setId(39L);
        Client client6 = new Client("YAY");
        client6.setId(22L);
        ArrayList<Client> arr = new ArrayList<>();
        arr.add(client1);
        arr.add(client2);
        arr.add(client3);
        arr.add(client4);
        arr.add(client5);
        arr.add(client6);

        Sort  sort = new Sort("name").and(new Sort(Boolean.FALSE,"id"));
        SortAlgorithm<Long,Client> test = new SortAlgorithm<>(arr,sort);

    }

    @Test
    public void lastTest(){
        Client  client1 = new Client("Arge");
        client1.setId(20L);
        Client client2 = new Client("Zbeng");
        client2.setId(10L);
        Client client3 = new Client("Arge");
        client3.setId(15L);
        Client client4 = new Client("Arge");
        client4.setId(18L);
        Client client5 = new Client("Zbeng");
        client5.setId(39L);
        Client client6 = new Client("YAY");
        client6.setId(22L);
        ArrayList<Client> arr = new ArrayList<>();
        arr.add(client1);
        arr.add(client2);
        arr.add(client3);
        arr.add(client4);
        arr.add(client5);
        arr.add(client6);

        Sort sort = new Sort(Boolean.FALSE,"name","id");
        SortAlgorithm<Long,Client> test = new SortAlgorithm<>(arr,sort);
        List<Client> cl = test.sort();
        for(Client client: cl)
            System.out.println(client);
    }
}
