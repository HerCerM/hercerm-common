package hercerm.common.ds;

import java.util.LinkedList;
import java.util.List;

public class BSTree<K extends Comparable<? super K>, V> implements SortedMultimap<K, V> {
    private BVertex<K, V> root;

    public BSTree() {}

    public BSTree(K rootValue) {
        root = new BVertex<>(rootValue);
    }

    public BSTree(K rootValue, List<V> values) {
        root = new BVertex<>(rootValue, values);
    }

    @Override
    public boolean addValue(K key, V value) {
        if(key == null || value == null)
            return false;

        BVertex<K, V> tempVx = search(key);

        if(tempVx == null)
            return false;

        tempVx.addValue(value);
        return true;
    }

    @Override
    public boolean insert(K key, List<V> values) {
        if(key == null || (root != null && search(key) != null))
            return false;

        root = insert(key, values, root);
        return true;
    }

    @Override
    public List<V> get(K key) {
        if(key == null)
            return null;

        BVertex<K, V> vertex = search(key);
        return vertex != null ? vertex.getvalues() : null;
    }

    private BVertex<K, V> insert(K key, List<V> values, BVertex<K, V> vertex) {

        if(vertex == null) {
            vertex = new BVertex<K, V>(key, values);
            return vertex;
        }

        if(key.compareTo(vertex.getKey()) < 0)
            vertex.setLeft(insert(key, values, vertex.getLeft()));

        if(key.compareTo(vertex.getKey()) > 0)
            vertex.setRight(insert(key, values, vertex.getRight()));

        return vertex;
    }

    BVertex<K, V> search(K key) {
        return search(key, getRoot());
    }

    private BVertex<K, V> search(K key, BVertex<K, V> vertex) {
        if(vertex == null)
            return null;

        if(vertex.getKey().compareTo(key) == 0)
            return vertex;

        if(key.compareTo(vertex.getKey()) > 0)
            return search(key, vertex.getRight());

        return search(key, vertex.getLeft());
    }

    BVertex<K, V> getRoot() {
        return root;
    }

    void setRoot(BVertex<K, V> root) {
        this.root = root;
    }

    @Override
    public List<K> keys() {
        if(getRoot() == null)
            return new LinkedList<>();

        return traverse(getRoot(), new LinkedList<>());
    }
    private List<K> traverse(BVertex<K, V> vertex, List<K> data) {
        if(vertex == null)
            return null;

        traverse(vertex.getLeft(), data);

        data.add(vertex.getKey());

        traverse(vertex.getRight(), data);

        return data;
    }

    @Override
    public int countComparisons(K key) {
        return countComparisons(key, getRoot(), 1);
    }

    private int countComparisons(K key, BVertex<K, V> vertex, int count) {
        if(vertex == null)
            return 0; // Not found

        if(vertex.getKey().compareTo(key) == 0)
            return count; // Found

        // Not found on current vertex. Increment count by one
        if(key.compareTo(vertex.getKey()) > 0)
            return countComparisons(key, vertex.getRight(), ++count);
        return countComparisons(key, vertex.getLeft(), ++count);
    }
}
