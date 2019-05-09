package hercerm.common.ds;

import java.util.LinkedList;
import java.util.List;

class VertexData<K extends Comparable<? super K>, V> {
    private K key;
    private List<V> values;

    VertexData(K key) {
        this.key = key;
        values = new LinkedList<V>();
    }

    VertexData(K key, List<V> values) {
        this.key = key;

        if(values == null)
            this.values = new LinkedList<>();
        else
            this.values = values;
    }

    // Add values to key
    void add(V value) {
        values.add(value);
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public List<V> getValues() {
        return values;
    }

    void setValues(List<V> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return key.toString();
    }
}
