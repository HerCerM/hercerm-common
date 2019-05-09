package hercerm.common.ds;

import java.util.LinkedList;
import java.util.List;

/**
 *  The {@code BTree} class is a modification of the B tree implementation
 *  by Robert Sedgewick and Kevin Wayne. Tha aim of the modification is to provide a multi
 *  map API to the B tree.
 *
 *  For the original implementation, see
 *  <a href="https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/BTree.java.html">
 */
public class BTree<K extends Comparable<? super K>, V> implements SortedMultimap<K, V> {
    // max children per B-tree node = M-1
    // (must be even and greater than 2)
    private static final int M = 4;

    private MVertex<K, V> root;       // root of the B-tree
    private int height;               // height of the B-tree
    private int n;                    // number of key-value pairs in the B-tree

    @Override
    public List<K> keys() {
        return traverse(root, height, new LinkedList<>());
    }
    private List<K> traverse(MVertex<K, V> vertex, int ht, List<MVertexData<K, V>> entries) {
        List<K> keys = new LinkedList<>();
        MVertexData<K, V>[] children = vertex.getChildren();

        if (ht == 0) {
            for (int j = 0; j < vertex.getM(); j++) {
                keys.add(children[j].getKey());
            }
        }
        else {
            for (int j = 0; j < vertex.getM(); j++) {
                keys.addAll(traverse(children[j].getNext(), ht-1, entries));
            }
        }
        return keys;
    }

    /**
     * Initializes an empty B-tree.
     */
    public BTree() {
        root = new MVertex<>(0, M);
    }

    /**
     * Returns true if this symbol table is empty.
     * @return {@code true} if this symbol table is empty; {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return n;
    }

    /**
     * Returns the height of this B-tree (for debugging).
     *
     * @return the height of this B-tree
     */
    public int height() {
        return height;
    }


    /**
     * Returns the value associated with the given key.
     *
     * @param  key the key
     * @return the value associated with the given key if the key is in the symbol table
     *         and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    @Override
    public List<V> get(K key) {
        if (key == null)
            return null;

        return search(key, root, height);
    }

    private List<V> search(K key, MVertex<K, V> vertex, int ht) {
        MVertexData<K, V>[] children = vertex.getChildren();

        // External node
        if (ht == 0) {
            for (int j = 0; j < vertex.getM(); j++) {
                if (eq(key, children[j].getKey()))
                    return children[j].getValues();
            }
        }

        // Internal node
        else {
            for (int j = 0; j < vertex.getM(); j++) {
                if (j+1 == vertex.getM() || less(key, children[j+1].getKey()))
                    return search(key, children[j].getNext(), ht-1);
            }
        }
        return null;
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is {@code null}, this effectively deletes the key from the symbol table.
     *
     * @param  key the key
     * @param  val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    @Override
    public boolean insert(K key, List<V> val) {
        if (key == null) return false;
        if(get(key) != null) return false;

        MVertex<K, V> u = insertNode(root, key, val, height);
        n++;
        if (u == null) return true;

        // need to split root
        MVertex<K, V> t = new MVertex<>(2, M);
        t.getChildren()[0] = new MVertexData<>(root.getChildren()[0].getKey(), null, root);
        t.getChildren()[1] = new MVertexData<>(u.getChildren()[0].getKey(), null, u);
        root = t;
        height++;
        return true;
    }

    @Override
    public boolean addValue(K key, V value) {
        if(key == null || value == null)
            return false;

        List<V> values = search(key, root, height);

        if(values == null)
            return false;

        values.add(value);
        return true;
    }

    private MVertex<K, V> insertNode(MVertex<K, V> h, K key, List<V> val, int ht) {
        int j;
        MVertexData<K, V> t = new MVertexData<>(key, val, null);

        // external node
        if (ht == 0) {
            for (j = 0; j < h.getM(); j++) {
                if (less(key, h.getChildren()[j].getKey())) break;
            }
        }

        // internal node
        else {
            for (j = 0; j < h.getM(); j++) {
                if ((j+1 == h.getM()) || less(key, h.getChildren()[j+1].getKey())) {
                    MVertex u = insertNode(h.getChildren()[j++].getNext(), key, val, ht-1);
                    if (u == null) return null;
                    t.setKey((K) u.getChildren()[0].getKey());
                    t.setNext(u);
                    break;
                }
            }
        }

        for (int i = h.getM(); i > j; i--)
            h.getChildren()[i] = h.getChildren()[i-1];
        h.getChildren()[j] = t;
        h.setM(h.getM() + 1);
        if (h.getM() < M) return null;
        else         return split(h);
    }

    // Split node in half
    private MVertex<K, V> split(MVertex h) {
        MVertex<K, V> t = new MVertex<>(M/2, M);
        h.setM(M/2);
        for (int j = 0; j < M/2; j++)
            t.getChildren()[j] = h.getChildren()[M/2+j];
        return t;
    }

    @Override
    public int countComparisons(K key) {
        return countComparisons(key, root, height, 1);
    }

    private int countComparisons(K key, MVertex<K, V> vertex, int ht, int count) {
        MVertexData<K, V>[] children = vertex.getChildren();

        // External node
        if (ht == 0) {
            for (int j = 0; j < vertex.getM(); j++) {

                count++;
                if (eq(key, children[j].getKey()))
                    return count;
            }
        }

        // Internal node
        else {
            //count++;
            for (int j = 0; j < vertex.getM(); j++) {
                if (j+1 == vertex.getM() || less(key, children[j+1].getKey())) {
                    return countComparisons(key, children[j].getNext(), ht-1, count);
                }
            }
        }

        return 0;
    }

    @Override
    public String toString() {
        return toString(root, height, "") + "\n";
    }

    private String toString(MVertex<K, V> h, int ht, String indent) {
        StringBuilder s = new StringBuilder();
        MVertexData<K, V>[] children = h.getChildren();

        if (ht == 0) {
            for (int j = 0; j < h.getM(); j++) {
                s.append(indent + children[j].getKey() + " " + children[j].getValues() + "\n");
            }
        }
        else {
            for (int j = 0; j < h.getM(); j++) {
                if (j > 0) s.append(indent + "(" + children[j].getKey() + ")\n");
                s.append(toString(children[j].getNext(), ht-1, indent + "     "));
            }
        }
        return s.toString();
    }

    private boolean less(K k1, K k2) {
        return k1.compareTo(k2) < 0;
    }
    private boolean eq(K k1, K k2) {
        return k1.compareTo(k2) == 0;
    }
}
