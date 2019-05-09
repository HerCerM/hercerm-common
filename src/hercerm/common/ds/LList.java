package hercerm.common.ds;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class LList<K extends Comparable<? super K>, V>
        implements Iterable<K>, SortedMultimap<K, V> {

    private UVertex<K, V> root;

    @Override
    public boolean insert(K key, List<V> values) {
        if(key == null || search(key, root) != null)
            return false;

        root = insert(key, values, root);
        return true;
    }

    private UVertex<K, V> insert(K key, List<V> values, UVertex<K, V> vertex) {
        if(vertex == null) // Null root or end of list
            return new UVertex<K, V>(key, values);

        if(key.compareTo(vertex.getKey()) < 0) {
            UVertex<K, V> newVx =  new UVertex<>(key, values);
            newVx.setRight(vertex);
            return newVx;
        }

        vertex.setRight(insert(key, values, vertex.getRight()));

        return vertex;
    }

    @Override
    public List<V> get(K key) {
        if(key == null)
            return null;

        UVertex<K, V> vertex =  search(key, root);
        return vertex != null ? vertex.getvalues() : null;
    }

    @Override
    public boolean addValue(K key, V value) {
        if(key == null || value == null)
            return false;

        UVertex<K, V> vertex = search(key, root);

        if(vertex == null)
            return false;

        vertex.addValue(value);
        return true;
    }
    @Override
    public List<K> keys() {
        List<K> keys = new LinkedList<>();

        for(K key : this)
            keys.add(key);

        return keys;
    }

    private UVertex<K, V> search(K key, UVertex<K, V> vertex) {

        if(vertex == null)
            return null;

        if(vertex.getKey().compareTo(key) == 0)
            return vertex;

        return search(key, vertex.getRight());
    }

    @Override
    public int countComparisons(K key) {
        return countComparisons(key, root, 1);
    }

    private int countComparisons(K key, UVertex<K, V> vertex, int count) {
        if(vertex == null)
            return 0; // Not found on list

        if(vertex.getKey().compareTo(key) == 0)
            return count; // Found

        // Not found on current vertex
        return countComparisons(key, vertex.getRight(), ++count);
    }

    @Override
    public Iterator<K> iterator() {
        return new Iterator<K>() {

            private UVertex<K, V> currentVertex = root;

            @Override
            public boolean hasNext() {
                return currentVertex != null;
            }

            @Override
            public K next() {
                VertexData<K, V> data = currentVertex.getData();
                currentVertex = currentVertex.getRight();
                return data.getKey();
            }
        };
    }
}
