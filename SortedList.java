
public class SortedList<T> {
		protected Entry head;
		protected int size;

		protected class Entry {
			protected T t;
			protected Entry next;
			
			public Entry(T t, Entry next) {
				this.t = t;
				this.next = next;
			}
		}
		
		public SortedList(Entry head, int size){
			this.head = head;
			this.size = size;
			
		}

		public SortedList() {
			head = new Entry(null, null);
			size = 0;
		}
		
		

		public int size() {
			return this.size;
		}

		public T get(int idx) {
			if(size() == 0) throw new UnsupportedOperationException();
			if(idx < 0 || idx >= size()) throw new IndexOutOfBoundsException();
			Entry cursor = head;
			for(int i = 0; i < idx; i++) cursor = cursor.next;
			return cursor.t;
		}
		
		//inserts before the element at current idx;
		public void insert(int idx, T t) {
			if(size() == 0 && idx == 0) head = new Entry(t, null);
			else if(idx < 0 || idx > size()) throw new IndexOutOfBoundsException();
			else if(idx == 0) head = new Entry(t, head);
			else {
				Entry prev = head;
				Entry curr = head.next;
				
				while(--idx > 0) {
					curr = curr.next;
					prev = prev.next;
				}
				
				prev.next = new Entry(t, curr);
				
			}
			size++;

		}
		
		protected Entry newHead(int leftSize) {
			if(size() <= leftSize || leftSize <= 0) throw new UnsupportedOperationException("Can't do a split");
			int newSize = leftSize;
			Entry curr = head;
			while(--leftSize > 0){
				curr = curr.next;
			}
			
			Entry newHead = curr.next;
			curr.next = null;
			size = newSize;
			return newHead;
		}
		
		public final void add(T t) {
			if(t == null) throw new IllegalArgumentException();
			Entry newEntry = new Entry(t, null);
			
			if(size() == 0) head = newEntry;
			else {
				Entry cursor = head;
				while(cursor.next != null) cursor = cursor.next;
				cursor.next = newEntry;
			}
			size++;
		}
		
		//returns the right half part. Input is the size on the left half.
		public SortedList<T> split(int leftSize){
			int oldSize = size();
			return new SortedList<T>(newHead(leftSize), oldSize-leftSize);
		}
		
		public void print(String token) {
			
			System.out.println(toString(token));
		}
		
		public String toString(String token) {
			StringBuilder sb = new StringBuilder(token);
			sb.append("{");
			Entry cursor = head;
			for(int i = 0; i < size(); i++, cursor=cursor.next) {
				sb.append(cursor.t.toString() + ",");				
			}
			sb.append(" }");
			return sb.toString();
		}

	}
