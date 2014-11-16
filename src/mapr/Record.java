package mapr;

import java.io.Serializable;
import java.security.Key;

/**
 * Created by wenhanl on 14-11-1.
 */
public class Record<K, V> implements Serializable {
    private K key;
    private V value;

    public Record(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public void set(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

}
