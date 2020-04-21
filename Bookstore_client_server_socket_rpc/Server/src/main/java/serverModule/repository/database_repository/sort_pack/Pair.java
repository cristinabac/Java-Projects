package serverModule.repository.database_repository.sort_pack;

public class Pair<TKey, TValue> {
    private  TKey key;
    private TValue value;

    public Pair(TKey key, TValue value) {
        this.key = key;
        this.value = value;
    }

    public TKey getKey(){
        return this.key;
    }

    public TValue getValue(){
        return this.value;
    }
}
