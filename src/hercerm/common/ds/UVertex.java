package hercerm.common.ds;

import java.util.List;

class UVertex<K extends Comparable<? super K>, V> {
    private final VertexData<K, V> data;
    private UVertex<K, V> right;

    UVertex(K key) {
        data = new VertexData<K, V>(key);
    }

    UVertex(K key, List<V> values) {
        data = new VertexData<K, V>(key, values);
    }

    UVertex(K key, UVertex<K, V> right) {
        data = new VertexData<K, V>(key);
        this.right = right;
    }

    UVertex(K key, List<V> values, UVertex<K, V> right) {
        data = new VertexData<K, V>(key, values);
        this.right = right;
    }

    void addValue(V value) {
        data.add(value);
    }

    K getKey() {
        return data.getKey();
    }

    List<V> getvalues() {
        return data.getValues();
    }

    void setvalues(List<V> values) {
        data.setValues(values);
    }

    UVertex<K, V> getRight() {
        return right;
    }

    void setRight(UVertex<K, V> right) {
        this.right = right;
    }

    VertexData<K, V> getData() {
        return data;
    }
}
