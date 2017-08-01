import java.util.concurrent.ThreadLocalRandom;

public class BPlusTree<K extends Comparable<K>,V> {
	private Node<K,V> root;
	private LeafNode<K, V> head;
	private final int orderInternal; //max children for a node
	private final int orderLeaf;
	private final double fillFactor;
	private int height;

	//
	private final int minLeafKeys, maxLeafKeys, minIntKeys, maxIntKeys;

	public BPlusTree(int orderInternal, int orderLeaf) {
		this.orderInternal = orderInternal;
		this.orderLeaf = orderInternal;
		this.height = 0;
		this.fillFactor = 0.5;

		this.minIntKeys = (int) Math.ceil(fillFactor * orderInternal);
		this.minLeafKeys = (int) Math.ceil(fillFactor * orderLeaf);
		this.maxIntKeys = orderInternal;
		this.maxLeafKeys = orderLeaf;
		this.head = new LeafNode<K,V>();
		this.root = this.head;
	}

	public int minKeys(int order) {
		return (int) Math.ceil(fillFactor * order);
	}

	public int maxKeys(int order) {
		return order;
	}

	public void add(K key, V value) {
		Node<K,V> splitNode = root.put(key, value);
		if(splitNode != null) {
			System.out.println("************************ SPLIT ROOT NODE **************************");
			root = new IndexNode<K, V>(root, splitNode);
			height++;
		}
	}

	public V get(K key) {
		return root.get(key);
	}
	
	public void show() {
		root.show(height);
	}

	public void printLeaves() {
		LeafNode<K, V> cursor = this.head;
		
		while(cursor!= null) {
			cursor.show(0);
			cursor = cursor.right;
		}
	}
	
	private class IndexNode<K extends Comparable<K>, V> implements Node<K, V> {

		private SortedKeyList<K> keys;
		private SortedList<Node<K, V>> nodes;

		public IndexNode() {
			keys = new SortedKeyList<>();
			nodes = new SortedList<>();
		}

		public IndexNode(Node<K, V> rightChild, Node<K, V> leftChild) {
			keys = new SortedKeyList<>();
			nodes = new SortedList<>();
			keys.add(leftChild.getSplitKey());
			nodes.add(rightChild);
			nodes.add(leftChild);
		}

		private IndexNode(SortedKeyList<K> keys, SortedList<Node<K, V>> nodes) {
			this.keys = keys;
			this.nodes = nodes;
		}

		@Override
		public boolean isLeaf() {
			return false;
		}

		@Override
		public int nodeSize() {
			return keys.size();
		}

		public K getSplitKey() {
			return keys.getSplitKey(false);
		}

		@Override
		public Node<K, V> put(K key, V value) {
			Node<K, V> splitIndexNode = null;
			int idx = keys.findChildIndex(key);
			Node<K, V> splitChildNode = nodes.get(idx).put(key, value);
			if(splitChildNode != null && nodeSize() <= maxIntKeys) {
				System.out.println("************************ SPLIT INTERNAL NODE **************************");
				int index = keys.addKey(splitChildNode.getSplitKey());
				nodes.insert(index+1, splitChildNode);
				if(nodeSize() > maxIntKeys) splitIndexNode = new IndexNode<K, V>(keys.split(minLeafKeys), nodes.split(minLeafKeys+1));
			}
			
			return splitIndexNode;
		}

		@Override
		public void show(int height) {
			StringBuilder sb = new StringBuilder("");
			for(int i = 0; i <= height; i++) sb.append("\t");
			sb.append("[" + height + "]INT KEY");
			keys.print(sb.toString());
			for(int i = 0; i < nodes.size(); i++) nodes.get(i).show(height-1);
		}

		@Override
		public V get(K key) {
			return nodes.get(keys.findChildIndex(key)).get(key);
		}
		
	}




	private class LeafNode<K extends Comparable<K>, V> implements Node<K, V> {

		private SortedKeyList<K> keys;
		private SortedList<V> values;
		private LeafNode<K, V> right;
		//private LeafNode<K, V> left;

		public LeafNode() {
			keys = new SortedKeyList<>();
			values = new SortedList<>();
			right = null;
		}

		private LeafNode(SortedKeyList<K> keys, SortedList<V> values) {
			this.keys = keys;
			this.values = values;
			this.right = null;
		}

		@Override
		public boolean isLeaf() {
			return true;
		}

		@Override
		public int nodeSize() {
			return keys.size();
		}

		public K getSplitKey() {
			return keys.getSplitKey(true);
		}


		public Node<K, V> put(K key, V value) {
			if(nodeSize() < maxLeafKeys) {
				simplePut(key, value);
				return null;
			}
			else if(nodeSize() == maxLeafKeys) return splitPut(key, value);
			else throw new IllegalStateException();
		}

		private void simplePut(K key, V value) {
			int idx = keys.addKey(key);
			values.insert(idx, value);
			//show();
		}

		private Node<K, V> splitPut(K key, V value) {
			simplePut(key, value);
			LeafNode<K, V> newRight = new LeafNode<K, V>(keys.split(minLeafKeys), values.split(minLeafKeys));
			if(this.right != null) newRight.right = this.right;
			this.right = newRight;
			return newRight;
		}

		public void show(int height) {
			//keys.print("KEYS   ");
			values.print("VALUES ");
		}

		public String toString() {
			return keys.toString("LN");
		}

		@Override
		public V get(K key) {
			Integer i = keys.findKeyIndex(key);
			return (i != null)? values.get(i) : null;
		}
	}


	public static void main(String[] args) throws InterruptedException {
		int N = 30;
		Integer[] rnds = new Integer[N]; 
		BPlusTree<Integer, String> bt = new BPlusTree<>(3, 3);
		
		for (int i = 1; i < N; i++) {
			rnds[i] = ThreadLocalRandom.current().nextInt(0, 100);
			System.out.println("Adding..." + rnds[i]);
			try {
				bt.add(rnds[i], Integer.toString(rnds[i]));
			}
			catch(Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
		
		bt.show();
		System.out.println("@@@@@@@@@@@ Leaves @@@@@@@@@@@@@@@@@@");
		bt.printLeaves();
		
		for (int i = 1; i < N; i++) {
			System.out.println("GET: Key " + rnds[i] + " --> " + bt.get(rnds[i]-1));
		}

	}

}
