import java.util.Arrays;

@SuppressWarnings("unchecked")

public class BinaryHeap<T extends Comparable<T>> {
	private static final int CAPACITY = Main.nodeNumber+1;
	private T[] heap;
	private int length;

	public BinaryHeap(T[] array) {
		heap = (T[]) new Comparable[CAPACITY];
		length = 0;
		long startTime = System.nanoTime();
		for (T each : array) {
			insert(each);
		}
		long endTime = System.nanoTime();
		System.out.println("Insert binary heap time= " + (endTime - startTime) + "ns");
	}

	public void insert(T value) {
		// if (this.length >= heap.length - 1) {heap = this.resize();}
		length++;
		heap[length] = value;
		bubbleUp();
	}

	public T extract() {
		T result = min();
		swap(1, length);
		heap[length] = null;
		length--;
		bubbleDown();
		return result;
	}

	public boolean search(T value) {
		for (int i = 0; i < length; i++) {
			if (value.equals(heap[i])) {
				System.out.println("index = " + i);
				return true;
			}
		}
		return false;
	}

	public T min() {
		if (length == 0)
			throw new IllegalStateException();
		return heap[1];
	}

	public T max() {
		if (length == 0)
			throw new IllegalStateException();
		int index = length;
		for (int i = length; i > 1; i--) {
			if (heap[i - 1].compareTo(heap[index]) > 0) {
				index = i - 1;
			}
		}
		return heap[index];
	}

	public int length() {
		return length;
	}

	private T[] resize() {
		return Arrays.copyOf(heap, heap.length + CAPACITY);
	}

	private void bubbleUp() {
		int index = length;
		while (index > 1 && (heap[parentIndex(index)].compareTo(heap[index]) > 0)) {
			swap(index, parentIndex(index));
			index = parentIndex(index);
		}
	}

	private void bubbleDown() {
		int index = 1;
		while (hasLeftChild(index)) {
			int smaller = leftIndex(index);
			if (hasRightChild(index) && heap[leftIndex(index)].compareTo(heap[rightIndex(index)]) > 0) {
				smaller = rightIndex(index);
			}
			if (heap[index].compareTo(heap[smaller]) > 0) {
				swap(index, smaller);
			} else
				break;
			index = smaller;
		}
	}

	private int leftIndex(int i) {
		return i * 2;
	}

	private int rightIndex(int i) {
		return i * 2 + 1;
	}

	private boolean hasLeftChild(int i) {
		return leftIndex(i) <= length;
	}

	private boolean hasRightChild(int i) {
		return rightIndex(i) <= length;
	}

	private int parentIndex(int i) {
		return i / 2;
	}

	private void swap(int index1, int index2) {
		T temp = heap[index1];
		heap[index1] = heap[index2];
		heap[index2] = temp;
	}

	@Override
	public String toString() {
		String retval = "";
		int i = 1;
		for (T each : heap) {
			if (each != null) {			
				if ((Math.log(i) / Math.log(2)) % 1 == 0) {
					retval += "\n";
				}
				retval += each + " ";
				i++;
			}
		}
		return retval + "\n";
	}
}