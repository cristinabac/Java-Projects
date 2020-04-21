package repository.database_repository.sort_pack;

import domain.BaseEntity;
import domain.validators.BookstoreException;
import javafx.util.Pair;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SortAlgorithm<ID extends Serializable,T extends BaseEntity<ID>> {
    private List<T> objectList;
    private Sort sort;

    public SortAlgorithm(List<T> objectList, Sort sort) {
        this.objectList = objectList;
        this.sort = sort;
    }

    private int compareInternal(T obj1, T obj2, String fieldName) {
        try {
            Field field1;
            Field field2;
            try{
                field1 = obj1.getClass().getDeclaredField(fieldName);
                field2 = obj2.getClass().getDeclaredField(fieldName);
            }
            catch (NoSuchFieldException e){
                field1 = obj1.getClass().getSuperclass().getDeclaredField(fieldName);
                field2 = obj2.getClass().getSuperclass().getDeclaredField(fieldName);
            }

            field1.setAccessible(true);
            String val1 = field1.get(obj1).toString();
            field1.setAccessible(false);

            field2.setAccessible(true);
            String val2 = field2.get(obj2).toString();
            field2.setAccessible(false);

            if(!val1.matches(".*[a-zA-Z].*")){
                //Case  When Is Number
                Long lng1 = Long.parseLong(val1);
                Long lng2 = Long.parseLong(val2);
                return lng1.compareTo(lng2);
            }
            return val1.compareTo(val2);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new BookstoreException("INVALID FIELD: "+fieldName+"!");
        }
    }


    private  int compareWrapper(T obj1, T obj2){
        return compareMain(obj1,obj2,0);
    }

    private int compareMain(T obj1, T obj2, int index) {
        if(index == objectList.size())
            return 0;
        if(sort.getAll().get(index).getKey() == Boolean.TRUE){
            if(compareInternal(obj1,obj2,sort.getAll().get(index).getValue())==0)
                return compareMain(obj1,obj2,++index);
            else
                return compareInternal(obj1,obj2,sort.getAll().get(index).getValue());
        }
        if(compareInternal(obj1,obj2,sort.getAll().get(index).getValue())==0)
            return compareMain(obj1,obj2,++index);
        return -compareInternal(obj1,obj2,sort.getAll().get(index).getValue());
    }

    public List<T> sort(){
       return objectList.stream().sorted(this::compareWrapper).collect(Collectors.toList());
    }
}
