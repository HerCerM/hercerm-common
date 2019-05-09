package hercerm.common.ds;

import java.util.List;

public interface SortedMultimap<K extends Comparable<? super K>, V> {
    /**
     * Allows insertion of key-values pairs into the multimap. If values
     * is null, then no values are associated with the given key. Attempting
     * {@link #get(Comparable)} on this key will return a list of size zero.
     *
     * @param key key to be inserted.
     * @param values values associated with the key.
     * @return true if the insertion was possible. False if the key is null or the
     * key is already present in the multimap.
     */
    boolean insert(K key, List<V> values);

    /**
     * Allows retrieval of values associated with a key.
     *
     * @param key key associated with a list of values.
     * @return list of values associated with the input key. Returns null if the
     * key is null or not present in the multimap.
     */
    List<V> get(K key);

    /**
     * Allows adding a value to a given list of values associated with a key.
     *
     * @param key target key.
     * @param value value to add to the list of values.
     * @return true if the insertion was possible. False if the key or the value
     * is null or if the key is not present in the multimap.
     */
    boolean addValue(K key, V value);

    /**
     * @return ascending sorted list of keys in the multimap.
     */
    List<K> keys();

    /**
     * Counts the number of comparison made in the process of searching a key.
     *
     * @return number of keys compared until desired key is found. Returns 0 if
     * the key is not present on the multimap.
     */
    int countComparisons(K key);
}
