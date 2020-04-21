package repository.database_repository.sort_pack;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class Sort{
    List<Pair<Boolean,String>> fields;

    public Sort(String...args){
        fields = new ArrayList<>();
        for(String a:args)
            fields.add(new Pair<>(Boolean.TRUE,a));
    }

    public Sort(Boolean direction,String...args){
        fields = new ArrayList<>();
        for(String a:args)
            fields.add(new Pair<>(direction,a));
    }

    public  Sort and(Sort sort){
        for(Pair<Boolean,String> pair:sort.getAll())
            fields.add(new Pair<>(pair.getKey(),pair.getValue()));
        return this;
    }

    public List<Pair<Boolean,String>> getAll(){
        return this.fields;
    }
}
