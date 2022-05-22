import java.util.List;
import java.util.ListIterator;

public class BTree<K extends Comparable<K>, V> implements IBTree<K, V> {
    private int minimumDegree;
    private int maxSize;
    private IBTreeNode<K,V> root;

    @Override
    public int getMinimumDegree() {
        return minimumDegree;
    }

    public void setMinimumDegree(int minimumDegree) {
        this.minimumDegree = minimumDegree;
    }

    @Override
    public IBTreeNode<K, V> getRoot() {
        return root;
    }

    public void setRoot(IBTreeNode<K, V> root) {
        this.root = root;
    }

    @Override
    public void insert(K key, V value) {
        IBTreeNode<K,V> node = root;

        while (!node.isLeaf()){
            boolean nodeIsFull = node.getKeys().size() == (2 * minimumDegree) - 1;
            if (nodeIsFull) {
                splitChild(node,);
            }
        }



            if ()
                if (nodeIsFull) {
                    splitChild(node, 0);
                } else {

                }
    }

    @Override
    public V search(K key) {
        return search(root, key);
    }

    private V search(IBTreeNode<K,V> node, K key) {
        List<K> keys = node.getKeys();
        int i = 0;
        while (i <= node.getNumOfKeys() && key.compareTo(keys.get(i)) > 0)
            i++;

        // check if key is one of the nodes keys
        if (i <= node.getNumOfKeys() && key == keys.get(i)) {
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

    private void splitRoot() {
        IBTreeNode<K,V> root = this.root;
        List<K> keys = root.getKeys();
        int medianIndex = root.getKeys().size() / 2;

        IBTreeNode<K,V> newSibling = new BTreeNode<>();
        int i = medianIndex + 1;
        while (keys.get(i) != null) {
            newSibling.insertKey(i - medianIndex, root.getKey(i));
            newSibling.insertValue(i - medianIndex, root.getValue(i));
            if (!root.isLeaf()) {
                newSibling.insertChild(i - medianIndex, root.getChild(i));
                newSibling.insertChild(i - medianIndex, root.getChild(i+1));
            }
            i++;
        }

        i = medianIndex + 1;
        while (keys.get(i) != null) {
            root.getKeys().remove(i);
            root.getValues().remove(i);
            if (!root.isLeaf()) {
                root.getChildren().remove()
            }
        }

        IBTreeNode<K,V> newRoot = new BTreeNode<>();
        newRoot.insertKey(0, root.getKey(medianIndex));
        newRoot.insertValue(0, root.getValue(medianIndex));
        root.getChildren().remove()
    }

    private void splitChild(IBTreeNode<K,V> node, int i) {

        // assume no full parent child for now (TODO)

        int medianIndex = node.getKeys().size() / 2;
        IBTreeNode newRoot =
        node.insert(median);

        /*
        1- find median
        2- insert median into parent node
        3- put all elements before median into a new node and all elements after median into a new node
        4- link the place smaller than the median to the first elements and link the the place greater to the latter elements
        */

        return;
    }

    // left bias
    private int findNodeMedianIndex(IBTreeNode<K,V> node) {
        int nodeLength = node.getKeys().size();
        return nodeLength / 2;
    }
}
