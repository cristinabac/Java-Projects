package repository.dbrepo.sort;

import domain.BaseEntity;
import domain.Book;
import domain.Sale;
import org.junit.Test;
import repository.database_repository.sort_pack.Sort;

import java.lang.reflect.Field;

public class SortTest {

    @Test
    public void constructorTests(){
        Sort testSort = new Sort("argument1");
        assert 1 == testSort.getAll().size();

        testSort = new Sort("argument1", "argument2","argument3");
        assert  3 == testSort.getAll().size();
        assert  testSort.getAll().get(0).getValue().equals("argument1");
        assert testSort.getAll().get(0).getKey().equals(Boolean.TRUE);

        testSort = new Sort(Boolean.FALSE,"argument1", "argument2","argument3");
        assert  3 == testSort.getAll().size();
        assert  testSort.getAll().get(0).getValue().equals("argument1");
        assert testSort.getAll().get(0).getKey().equals(Boolean.FALSE);

        testSort = new Sort(Boolean.FALSE,"argument1", "argument2").and(new Sort("argument3"));
        assert  3 == testSort.getAll().size();
        assert  testSort.getAll().get(0).getValue().equals("argument1");
        assert testSort.getAll().get(0).getKey().equals(Boolean.FALSE);
        assert testSort.getAll().get(2).getValue().equals("argument3");
        assert testSort.getAll().get(2).getKey().equals(Boolean.TRUE);

    }

}
