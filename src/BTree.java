import java.util.List;

public class BTree<K extends Comparable<K>, V> implements IBTree<K, V> {
    private final int minimumDegree;
    private IBTreeNode<K,V> root;

    public BTree(int minimumDegree) {
        this.minimumDegree = minimumDegree;
        this.root = null;
    }

    private void setRoot(IBTreeNode<K, V> root) {
        this.root = root;
    }

    @Override
    public IBTreeNode<K, V> getRoot() {
        return root;
    }

    @Override
    public int getMinimumDegree() {
        return minimumDegree;
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


    @Override
    public void insert(K key, V value) {
        if(root == null)
            this.root = new BTreeNode<>();

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

        // check if the key is already in the tree
        if (i < node.getNumOfKeys() && key.compareTo(node.getKey(i)) == 0)
            return;

        node.insertKey(i, key);
        node.insertValue(i, value);
    }

    private boolean isNodeFull(IBTreeNode<K,V> node) {
        return node.getKeys().size() == (2 * minimumDegree) - 1;
    }

    private boolean isNodeMinimal(IBTreeNode<K,V> node) {
        return node.getNumOfKeys() == minimumDegree - 1;
    }

    private void splitRoot() {
        int medianIndex = root.getKeys().size() / 2;

        IBTreeNode<K,V> newRoot = new BTreeNode<>();

        Pair<IBTreeNode<K,V>> pair = splitNodeAroundMedian(root);
        IBTreeNode<K,V> leftChild = pair.getFirst();
        IBTreeNode<K,V> rightChild = pair.getSecond();

        newRoot.insertKey(0, root.getKey(medianIndex));
        newRoot.insertValue(0, root.getValue(medianIndex));
        newRoot.insertChild(0, leftChild);
        newRoot.insertChild(1, rightChild);

        setRoot(newRoot);
    }

    /* This function assumes that it is guaranteed that the parent of that node is not full. */
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

        IBTreeNode<K,V> leftNode = new BTreeNode<>();
        IBTreeNode<K,V> rightNode = new BTreeNode<>();

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
    public boolean delete(K key) {
        return delete(root, key);
    }

    /* Assumes that the called node is non minimal (has at least t nodes). */
    private boolean delete(IBTreeNode<K,V> node, K key) {
        int i = 0;
        while(i < node.getNumOfKeys() && key.compareTo(node.getKey(i)) > 0)
            i++;

        boolean keyFound = i < node.getNumOfKeys() && key.compareTo(node.getKey(i)) == 0;

        if (keyFound) {
            // Case 1: Leaf Node
            if(node.isLeaf()) {
                node.getKeys().remove(i);
                node.getValues().remove(i);
                return true;
            }
            // Case 2: internal node

            // case 2.a
            if (!isNodeMinimal(node.getChild(i-1))) {
                IBTreeNode<K,V> predecessor = getPredecessor(node.getChild(i));
                int replacementKeyIndex = predecessor.getNumOfKeys() - 1;
                node.getKeys().set(i, predecessor.getKey(replacementKeyIndex));
                node.getValues().set(i, predecessor.getValue(replacementKeyIndex));
                delete(node.getChild(i), predecessor.getKey(replacementKeyIndex));
                return true;
            }
            // case 2.b
            else if (!isNodeMinimal(node.getChild(i+1))) {
                IBTreeNode<K, V> successor = getSuccessor(node.getChild(i+1));
                int replacementKeyIndex = 0;
                node.getKeys().set(i, successor.getKey(0));
                node.getValues().set(i, successor.getValue(replacementKeyIndex));
                delete(node.getChild(i), successor.getKey(replacementKeyIndex));
                return true;
            }
            // case 2.c
            else {
                merge(node, i);
                delete(node, key);
                return true;
            }
        }

        if(node.isLeaf())
            return false;

        IBTreeNode<K,V> next = node.getChild(i);
        if (isNodeMinimal(next)) {
            if (i - 1 >= 0 && !isNodeMinimal(node.getChild(i-1)))
                borrowFromLeft(node, i);    // case 3.b
            else if (i + 1 <= node.getNumOfKeys() && !isNodeMinimal(node.getChild(i+1)))
                borrowFromRight(node, i);   // case 3.b
            else
                merge(node, i);   // case 3.a
        }

        return delete(next, key);
    }

    private IBTreeNode<K,V> getPredecessor(IBTreeNode<K,V> node) {
        if (node == null)  return null;
        if (node.isLeaf()) return node;
        return getSuccessor(node.getChild(node.getNumOfKeys()));
    }

    private IBTreeNode<K,V> getSuccessor(IBTreeNode<K,V> node) {
        if(node == null) return null;
        if (node.isLeaf()) return node;
        return getSuccessor(node.getChild(0));
    }

    private void borrowFromRight(IBTreeNode<K,V> parent, int nodeIndex) {
        IBTreeNode<K,V> node = parent.getChild(nodeIndex);
        IBTreeNode<K,V> sibling = parent.getChild(nodeIndex+1);

        // rotate the keys
        node.getKeys().add(parent.getKey(nodeIndex));
        node.getValues().add(parent.getValue(nodeIndex));
        parent.getKeys().set(nodeIndex, sibling.getKey(0));
        parent.getValues().set(nodeIndex, sibling.getValue(0));
        sibling.removeKey(0);
        sibling.removeValue(0);
        if (!sibling.isLeaf()) {
            node.getChildren().add(sibling.getChild(0));
            sibling.removeChild(0);
        }
    }

    private void borrowFromLeft(IBTreeNode<K,V> parent, int nodeIndex) {
        IBTreeNode<K,V> node = parent.getChild(nodeIndex);
        IBTreeNode<K,V> sibling = parent.getChild(nodeIndex-1);
        int siblingLastKeyIndex = sibling.getNumOfKeys() - 1;
        // rotate the keys
        node.insertKey(0, parent.getKey(nodeIndex-1));
        node.insertValue(0, parent.getValue(nodeIndex-1));

        parent.getKeys().set(nodeIndex-1, sibling.getKey(siblingLastKeyIndex));
        parent.getValues().set(nodeIndex-1, sibling.getValue(siblingLastKeyIndex));

        sibling.removeKey(siblingLastKeyIndex);
        sibling.removeValue(siblingLastKeyIndex);

        if(!sibling.isLeaf()) {
            node.insertChild(0, sibling.getChild(siblingLastKeyIndex+1));
            sibling.removeChild(siblingLastKeyIndex+1);
        }
    }

    private void merge(IBTreeNode<K,V> parent, int index) {
        IBTreeNode<K,V> first   = parent.getChild(index);
        IBTreeNode<K,V> second = parent.getChild(index + 1);

        List<K> keys   = first.getKeys();
        List<V> values = first.getValues();
        List<IBTreeNode<K,V>> children = first.getChildren();

        keys.add(parent.getKey(index));
        values.add(parent.getValue(index));
        parent.removeKey(index);
        parent.removeValue(index);

        for (int i = 0; i < second.getNumOfKeys(); i++) {
            keys.add(second.getKey(i));
            values.add(second.getValue(i));
            if (!second.isLeaf()) {
                children.add(second.getChild(i));
                children.add(second.getChild(i+1));
            }
        }

        parent.removeChild(index+1);
        parent.removeChild(index);
        parent.insertChild(index, first);

        if (parent == this.root && parent.getNumOfKeys() == 0)
            this.root = first;
    }


    // pre-order traversal
    public void print() {
        dfs(root);
        System.out.println("");
    }

    private void dfs(IBTreeNode<K,V> node) {
        for (K key : node.getKeys())
            System.out.print(key + " ");
        System.out.print("- ");

        if (!node.isLeaf()) {
            System.out.print("> ");
            for (IBTreeNode<K,V> child: node.getChildren())
                dfs(child);
        }
    }
}
