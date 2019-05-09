package hercerm.common.ds;

import java.util.List;

class AVLVertex<K extends Comparable<? super K>, V> extends BVertex<K, V> {
    private int height;
    private int balanceF;

    public AVLVertex(K key) {
        super(key);
        initHeight();
    }

    public AVLVertex(K key, List<V> values) {
        super(key, values);
        initHeight();
    }

    public AVLVertex(K key, BVertex<K, V> right, BVertex<K, V> left) {
        super(key, right, left);
        initHeight();
    }

    public AVLVertex(K key, List<V> values, BVertex<K, V> left, BVertex<K, V> right) {
        super(key, values, left, right);
        initHeight();
    }

    // A leaf is considered to have height one.
    // This is convenient for the calculation of balance factors.
    private void initHeight() {
        height = 1;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getBalanceF() {
        return balanceF;
    }

    public void setBalanceF(int balanceF) {
        this.balanceF = balanceF;
    }

    // Handle new vertex model
    @Override
    public AVLVertex<K, V> getLeft() {
        return (AVLVertex<K, V>) super.getLeft();
    }
    public void setLeft(AVLVertex<K, V> left) {
        super.setLeft(left);
    }
    @Override
    public AVLVertex<K, V> getRight() {
        return (AVLVertex<K, V>) super.getRight();
    }
    public void setRight(AVLVertex<K, V> right) {
        super.setRight(right);
    }

    @Override
    public String toString() {
        return "{" +
                "key=" + super.getKey() +
                ", height=" + height +
                ", balanceF=" + balanceF +
                '}';
    }
}
