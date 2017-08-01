
public interface Node<K extends Comparable<K>,V> {
	public boolean isLeaf();
	public int nodeSize();
	public Node<K, V> put(K key, V value);
	public V get(K key);
	public String toString();
	public K getSplitKey();
	public void show(int height);

}
