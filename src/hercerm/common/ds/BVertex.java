package hercerm.common.ds;

import java.util.List;

class BVertex<K extends Comparable<? super K>, V> extends UVertex<K, V> {
    private BVertex<K, V> left;

    BVertex(K key) {
        super(key);
    }

    BVertex(K key, List<V> values) {
        super(key, values);
    }

    BVertex(K key, BVertex<K, V> right, BVertex<K, V> left) {
        super(key, right);
        this.left = left;
    }

    BVertex(K key, List<V> values, BVertex<K, V> left, BVertex<K, V> right) {
        super(key, values, right);
        this.left = left;
    }

    BVertex<K, V> getLeft() {
        return (BVertex<K, V>) left;
    }

    void setLeft(BVertex<K, V> left) {
        this.left = left;
    }

    @Override
    @SuppressWarnings("unchecked")
    BVertex<K, V> getRight() {
        return (BVertex<K, V>) super.getRight();
    }
    void setRight(BVertex<K, V> right) {
        super.setRight(right);
    }
}