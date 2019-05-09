package hercerm.common.ds;

import java.util.List;

class MVertexData<K extends Comparable<? super K>, V> extends VertexData<K, V> {
    private MVertex<K, V> next;     // Helper field to iterate over array entries

    MVertexData(K key, List<V> values, MVertex<K, V> next) {
        super(key, values);
        this.next = next;
    }

    MVertex<K, V> getNext() {
        return next;
    }

    void setNext(MVertex<K, V> next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return getKey().toString();
    }
}