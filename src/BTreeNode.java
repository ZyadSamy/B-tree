import java.util.ArrayList;
import java.util.List;

public class BTreeNode<K extends Comparable<K>, V> implements IBTreeNode<K, V> {
    private List<K> keys = new ArrayList<>();
    private List<V> values = new ArrayList<>();
    private List<IBTreeNode<K,V>> children = new ArrayList<>();

    @Override
    public int getNumOfKeys() {
        return keys.size();
    }

    @Override
    public List<K> getKeys() {
        return keys;
    }

    @Override
    public void setKeys(List<K> keys) {
        this.keys = keys;
    }

    @Override
    public List<IBTreeNode<K, V>> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<IBTreeNode<K, V>> children) {
        this.children = children;
    }

    @Override
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @Override
    public List<V> getValues() {
        return values;
    }

    @Override
    public void setValues(List<V> values) {
        this.values = values;
    }

    @Override
    public K getKey(int i) {
        return keys.get(i);
    }

    @Override
    public IBTreeNode<K, V> getChild(int i) {
        return children.get(i);
    }

    @Override
    public V getValue(int i) {
        return values.get(i);
    }

    public void insertKey(int i, K key) {
        this.keys.add(i, key);
    }

    public void insertValue(int i, V value) {
        this.values.add(i, value);
    }

    public void insertChild(int i, IBTreeNode<K,V> child) {
        this.children.add(i, child);
    }

    @Override
    public void removeKey(int i) {
        keys.remove(i);
    }

    @Override
    public void removeValue(int i) {
        values.remove(i);
    }

    @Override
    public void removeChild(int i) {
        children.remove(i);
    }
}
