package hercerm.common.ds;

class MVertex<K extends Comparable<? super K>, V> {
    private int m;                          // Number of children
    private MVertexData<K, V>[] children;   // The array of children

    // Create a node with k children
    MVertex(int m, int M) {
        this.m = m;
        children = new MVertexData[M];
    }

    int getM() {
        return m;
    }

    void setM(int m) {
        this.m = m;
    }

    MVertexData<K, V>[] getChildren() {
        return children;
    }

    public void setChildren(MVertexData<K, V>[] children) {
        this.children = children;
    }
}