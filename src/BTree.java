import java.util.List;

public class BTree<K extends Comparable<K>, V> implements IBTree<K, V> {
    private final int minimumDegree;
    private IBTreeNode<K,V> root;

    public BTree(int minimumDegree) {
        this.minimumDegree = minimumDegree;
        this.root = null;
    }

    @Override
    public int getMinimumDegree() {
        return minimumDegree;
    }

    @Override
    public IBTreeNode<K, V> getRoot() {
        return root;
    }

    private void setRoot(IBTreeNode<K, V> root) {
        this.root = root;
    }

    @Override
    public void insert(K key, V value) {
        if(root == null) this.root = new BTreeNode<K,V>();

        if (isNodeFull(root))
            splitRoot();

        IBTreeNode<K,V> node = root;
        /* Go down the tree, but while going down if we encounter any full node split that node first. */
        while (!node.isLeaf()) {
            int i = 0;
            while (i < node.getNumOfKeys() && key.compareTo(node.getKey(i)) > 0)
                i++;

            // check if the key is already in the tree
            if (i < node.getNumOfKeys() && key.compareTo(node.getKey(i)) == 0)
                return;

            if (isNodeFull(node.getChild(i))) {
                splitChild(node, i);
                if (key.compareTo(node.getKey(i)) > 0)
                    i++;
            }

            node = node.getChild(i);
        }

        int i = 0;
        while(i < node.getNumOfKeys() && key.compareTo(node.getKey(i)) > 0)
            i++;

        node.insertKey(i, key);
        node.insertValue(i, value);
    }

    private boolean isNodeFull(IBTreeNode<K,V> node) {
        return node.getKeys().size() == (2 * minimumDegree) - 1;
    }

    private void splitRoot() {
        int medianIndex = root.getKeys().size() / 2;

        IBTreeNode<K,V> newRoot = new BTreeNode<K,V>();

        Pair<IBTreeNode<K,V>> pair = splitNodeAroundMedian(root);
        IBTreeNode<K,V> leftChild = pair.getFirst();
        IBTreeNode<K,V> rightChild = pair.getSecond();

        newRoot.insertKey(0, root.getKey(medianIndex));
        newRoot.insertValue(0, root.getValue(medianIndex));
        newRoot.insertChild(0, leftChild);
        newRoot.insertChild(1, rightChild);

        setRoot(newRoot);
    }

    /* This function assumes that it is guaranteed that the parent of that node is not full.
        1- find median
        2- insert median into parent node
        3- put all elements before median into a new node and all elements after median into a new node
        4- link the block smaller than the median to the first elements and link the the place greater to the latter elements
    */
    private void splitChild(IBTreeNode<K,V> parent, int childIndex) {
        IBTreeNode<K,V> node = parent.getChild(childIndex);
        int medianIndex = node.getKeys().size() / 2;

        Pair<IBTreeNode<K,V>> pair = splitNodeAroundMedian(node);
        IBTreeNode<K,V> leftChild = pair.getFirst();
        IBTreeNode<K,V> rightChild = pair.getSecond();

        parent.insertKey(childIndex, node.getKey(medianIndex));
        parent.insertValue(childIndex, node.getValue(medianIndex));
        parent.getChildren().remove(node);
        parent.insertChild(childIndex, leftChild);
        parent.insertChild(childIndex + 1, rightChild);
    }

    private Pair<IBTreeNode<K,V>> splitNodeAroundMedian(IBTreeNode<K,V> node) {
        int medianIndex = node.getKeys().size() / 2;

        IBTreeNode<K,V> leftNode = new BTreeNode<K,V>();
        IBTreeNode<K,V> rightNode = new BTreeNode<K,V>();

        // add keys and values to left child
        // add keys and values to right child
        List<K> keys = node.getKeys();
        List<V> values = node.getValues();
        int i;
        for (i = 0; i < medianIndex; i++) {
            leftNode.insertKey(i, keys.get(i));
            leftNode.insertValue(i, values.get(i));
        }
        for (i += 1; i < keys.size(); i++) {
            rightNode.insertKey(i - medianIndex - 1, keys.get(i));
            rightNode.insertValue(i - medianIndex - 1, values.get(i));
        }

        // add children to left and to right
        List<IBTreeNode<K,V>> children = node.getChildren();
        if (!node.isLeaf()) {
            for (i = 0; i < medianIndex; i++) {
                leftNode.insertChild(i, children.get(i));
                leftNode.insertChild(i + 1, children.get(i+1));
            }
            for (i += 1; i < keys.size(); i++) {
                rightNode.insertChild(i - medianIndex - 1, children.get(i));
                rightNode.insertChild(i - medianIndex, children.get(i+1));
            }
        }

        return new Pair<>(leftNode, rightNode);
    }


    @Override
    public V search(K key) {
        return search(root, key);
    }

    private V search(IBTreeNode<K,V> node, K key) {
        List<K> keys = node.getKeys();
        int i = 0;
        while (i < node.getNumOfKeys() && key.compareTo(keys.get(i)) > 0)
            i++;

        // check if key is one of the nodes keys
        if (i < node.getNumOfKeys() && key == keys.get(i)) {
            return node.getValues().get(i);
        }
        else if (node.isLeaf()) {
            // key is not one of the node's keys
            // and the node doesn't have any children
            // therefore the key doesn't exist
            return null;
        }
        else {
            IBTreeNode<K,V> child = node.getChildren().get(i);
            return search(child, key);
        }
    }

    /*
     * Deletion from a leaf node:
     * Case1: Leaf node have 1 more key than the minimum. >> Simply delete the key.
     * Case2: Leaf node has num of keys = minimum. >> Borrow
     *
     * Deletion from an internal node:
     * Case1:
     * */
    @Override
    public boolean delete(K key) {
        return false;
    }
}
