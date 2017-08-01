import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class SortedKeyList<K extends Comparable<K>> extends SortedList<K> {
	public SortedKeyList() {
		super();
	}
	
	public SortedKeyList(Entry head, int size) {
		super(head, size);
	}
	
	public Integer findKeyIndex(K k) {
		if(size() == 0) throw new IllegalStateException();
		int i;
		Entry cursor = head;
		for(i = 0; i < size();) {

			if(k.compareTo(cursor.t) == 0) return i;
			cursor = cursor.next;
			i++;
		}
		return i==size()? null : new Integer(i);
		
	}

	public int findChildIndex(K k) {
		if(size() == 0) throw new IllegalStateException();
		int i;
		Entry cursor = head;
		for(i = 0; i < size();) {

			if(k.compareTo(cursor.t) < 0) return i;
			cursor = cursor.next;
			i++;
		}
		return i;
	}

	public int addKey(K k) {
		if(size() == 0) {
			head.t = k;
			size++;
			return 0;
		}

		if(!(k.compareTo(head.t) >= 0)) {
			if(k.compareTo(head.t) == 0) throw new UnsupportedOperationException();
			head = new Entry(k, head);
			size++;
			return 0;
		}

		Entry prev = head;
		Entry curr = head.next;
		int idx = 1;

		while(curr!= null && !(k.compareTo(curr.t) < 0)) {
			curr = curr.next;
			prev = prev.next;
			idx++;
		}

		if(k.compareTo(prev.t) == 0) throw new UnsupportedOperationException("Duplicate keys not allowed; skipping add");
		prev.next = new Entry(k,curr);
		size++;
		return idx;
	}
	
	public SortedKeyList<K> split(int leftSize){
		int oldSize = size();
		return new SortedKeyList<K>(newHead(leftSize), oldSize-leftSize);
	}
	
	public K getSplitKey(boolean isLeaf) {
		K splitKey = head.t;
		if(!isLeaf) {
			head = head.next;
			size--;
		}
		return splitKey;
	}
	
	
	public static void main(String[] args) {
		/*SortedKeyList<Integer> cl = new SortedKeyList<>();
		int sz = 15;
		Integer[] rnds = new Integer[15];
		for (int i = 0; i < sz; i++) rnds[i] = new Integer(ThreadLocalRandom.current().nextInt(0, 50));
		
		for (int i = 0; i < sz; i++) {
			try{
				System.out.print("Adding " + rnds[i] + " at " + cl.addKey(rnds[i]) + " ");
				cl.print("KEYS");
			}
			catch(Exception e) {
				System.out.println("XX " + rnds[i]);
			}
		}
		
		//for (int i = 0; i < sz; i++) System.out.println("Index of " + rnds[i] + " is " + cl.findChildIndex(rnds[i]-1));
		System.out.println("SIZE " + cl.size());
		for (int i = 0; i < cl.size(); i++) System.out.println("Element at index " + i + " is " + cl.get(i));*/
		
		/*
		int n = 5;
		SortedKeyList<Integer> keys = new SortedKeyList<>();
		SortedList<ArrayList<Integer>> nodes = new SortedList<>();
		for (int i = 0; i < n; i++) {
			int idx = keys.addKey(new Integer(ThreadLocalRandom.current().nextInt(0, 50)));
			nodes.insert(idx, new ArrayList<Integer>());
		}
		nodes.insert(0, new ArrayList<Integer>());
		
		
		for (int i = 0; i < 50; i++) {
			Integer data = ThreadLocalRandom.current().nextInt(0, 50);
			int idx = keys.findChildIndex(data);
			nodes.get(idx).add(data);
		}
		keys.print("KEYS ");
		nodes.print("NODES ");
		*/
		
		SortedKeyList<Integer> sl = new SortedKeyList<>();
		for (int i = 0; i < 5; i++) sl.insert(i, ThreadLocalRandom.current().nextInt(0, 50));
		sl.print("node ");
		
		SortedKeyList<Integer> sl2 = sl.split(3);
		sl.print("Sl ");
		sl2.print("Sl2 ");
		
	}
}
