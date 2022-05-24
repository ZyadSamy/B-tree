import java.util.List;


public interface IBTreeNode<K extends Comparable<K>, V> {

	/**
	 * @return the numOfKeys return number of keys in this node.
	 */
	public int getNumOfKeys();
	
	/**
	 * @return isLeaf if the node is leaf or not.
	 */
	public boolean isLeaf();
	
	/**
	 * @return the keys return the list of keys of the given node.
	 */
	public List<K> getKeys();
	
	/**
	 * @param keys the keys to set
	 */
	public void setKeys(List<K> keys);
	
	/**
	 * @return the values return the list of values for the given node.
	 */
	public List<V> getValues();
	
	/**
	 * @param values the values to set
	 */
	public void setValues(List<V> values);
	
	/**
	 * @return the children return the list of children for the given node.
	 */
	public List<IBTreeNode<K, V>> getChildren();
	
	/**
	 * @param children the children to set
	 */
	public void setChildren(List<IBTreeNode<K, V>> children);


	public K getKey(int i);

	public V getValue(int i);

	public IBTreeNode<K,V> getChild(int i);

	public void insertValue(int i, V value);

	public void insertChild(int i, IBTreeNode<K,V> child);

	public void insertKey(int i, K key);

	public void removeKey(int i);
	public void removeValue(int i);
	public void removeChild(int i);
}
