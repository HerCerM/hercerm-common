package hercerm.common.ds;

import java.util.List;

public class AVLTree<K extends Comparable<? super K>, V> extends BSTree<K, V>
        implements SortedMultimap<K, V> {

    private AVLVertex<K, V> root; // New vertex model

    @Override
    public boolean insert(K key, List<V> values) {
        if(key == null || (root != null && search(key) != null))
            return false;

        // AVL insert helper
        root = insert(key, values, root);
        return true;
    }

    private AVLVertex<K, V> insert(K key, List<V> values, AVLVertex<K, V> vertex) {
        if(vertex == null)
            return new AVLVertex<K, V>(key, values); // Insert vertex in available space

        if(key.compareTo(vertex.getKey()) < 0)
            vertex.setLeft(insert(key, values, vertex.getLeft()));

        if(key.compareTo(vertex.getKey()) > 0)
            vertex.setRight(insert(key, values, vertex.getRight()));

        /* Given that this method is recursive, it is easy to update values of the vertices
        * that were traversed in the insertion path. The first time this section (after the
        * previous if statement) is reached, vertex holds the value of the parent node of
        * the inserted vertex.
        */

        // Update height of inserted vertex's ancestor
        vertex.setHeight(calculateVxHeight(vertex));

        // Update balance factor after standard BST insertion
        vertex.setBalanceF(calculateVxBalance(vertex));

        // Asses vertex for imbalance and fix if imbalanced

        // Left rotations (LL and LR)
        if(vertex.getBalanceF() > 1) {
            // LL rotation
            if(vertex.getLeft().getBalanceF() > 0) {
                vertex = LLRotate(vertex);
            }
            // LR rotation
            else if(vertex.getLeft().getBalanceF() < 0) {
                vertex.setLeft(RRRotate(vertex.getLeft()));
                vertex = LLRotate(vertex);
            }
        }
        // Right rotations (RR and RL)
        else if(vertex.getBalanceF() < -1) {
            // RR rotation
            if(vertex.getRight().getBalanceF() < 0) {
                vertex = RRRotate(vertex);
            }
            // RL rotation
            else if(vertex.getRight().getBalanceF() > 0) {
                vertex.setRight(LLRotate(vertex.getRight()));
                vertex = RRRotate(vertex);
            }
        }

        return vertex; // Return vertex with child inserted
    }

    /*
     * LL rotation is done on tree of this structure. The target of
     * the rotation is Z, where Z.balanceF > 1 && Z.left.balanceF > 0.
     *
     *                            Z
     *                           / \
     *                          Y   Zr
     *                         / \
     *                        X   Yr
     *                       / \
     *                     Xl  Xr
     */
    private AVLVertex<K, V> LLRotate(AVLVertex<K, V> vertex) {
        AVLVertex<K, V> tempZ = vertex; // Store Z
        AVLVertex<K, V> tempYr = vertex.getLeft().getRight(); // Store Yr

        vertex = vertex.getLeft(); // Swap Z for Y

        // Fix Yr
        vertex.setRight(tempZ);
        vertex.getRight().setLeft(tempYr);

        // Update balance factors and heights of moved vertices
        updateHeightBalanceRecursively(vertex);

        return vertex;
    }

    /*
     * RR rotation is done on tree of this structure. The target of
     * the rotation is Z, where Z.balanceF < -1 && Z.left.balanceF < 0.
     *
     *                            Z
     *                           / \
     *                         Zl   Y
     *                             / \
     *                            Yl  X
     *                               / \
     *                              Xl  Xr
     */
    private AVLVertex<K, V> RRRotate(AVLVertex<K, V> vertex) {
        AVLVertex<K, V> tempZ = vertex; // Store Z
        AVLVertex<K, V> tempYl = vertex.getRight().getLeft(); // Store Yl

        vertex = vertex.getRight(); // Swap Z for Y

        // Fix Yl
        vertex.setLeft(tempZ);
        vertex.getLeft().setRight(tempYl);

        // Update balance factors and heights of moved vertices
        updateHeightBalanceRecursively(vertex);

        return vertex;
    }

    private void updateHeightBalanceRecursively(AVLVertex<K, V> vertex) {
        if(vertex == null)
            return;

        updateHeightBalanceRecursively(vertex.getLeft());
        updateHeightBalanceRecursively(vertex.getRight());

        // Update height and balance factor
        vertex.setHeight(calculateVxHeight(vertex));
        vertex.setBalanceF(calculateVxBalance(vertex));
    }

    /**
     * Contrary to what the method's name may imply, this method calculates the
     * updated value of a vertex rather than its current height. Precondition:
     * vertex must not be null.
     *
     * @param vertex Target vertex to evaluate
     * @return New height for the vertex target
     */
    private int calculateVxHeight(AVLVertex<K, V> vertex) {
        return 1 + Math.max(getVxActualHeight(vertex.getLeft()),
                getVxActualHeight(vertex.getRight()));
    }

    /**
     * A null vertex is considered to have balance zero. Otherwise, the vertex's balance factor
     * is calculated accordingly: (vertex.leftChild.height - vertex.rightChild.height)
     *
     * @param vertex Target vertex to evaluate
     * @return Balance factor for vertex target
     */
    private int calculateVxBalance(AVLVertex<K, V> vertex) {
        return vertex == null ? 0 :
                getVxActualHeight(vertex.getLeft()) - getVxActualHeight(vertex.getRight());
    }

    /**
     * A null vertex is considered to have height of zero. Otherwise, the height is fetched.
     *
     * @param vertex Target vertex to evaluate
     * @return Logical height for vertex target
     */
    private int getVxActualHeight(AVLVertex<K, V> vertex) {
        if(vertex == null)
            return 0;

        return vertex.getHeight();
    }

    // Handle new vertex model
    AVLVertex<K, V> getRoot() {
        return root;
    }
    void setRoot(AVLVertex<K, V> root) {
        this.root = root;
    }
}
