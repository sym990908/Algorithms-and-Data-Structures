import java.util.Arrays;

@SuppressWarnings("unchecked")

public class Beap<T extends Comparable<T>> {
    private static final int CAPACITY = 10001;
    private T[] heap;
    private int length;
    private int height;

    public Beap(T[] array) {
        heap = (T[]) new Comparable[CAPACITY];
        length = 0;
        height = 0;
        long startTime = System.nanoTime();
        for (T each : array) {
            insert(each);
        }
        long endTime = System.nanoTime();
        System.out.println("Insert beap time= " + (endTime - startTime) + "ns");
    }

    public T[] getHeap() {
        return Arrays.copyOfRange(heap, 1, length + 1);
    }

    public void insert(T value) {
        // if (this.length >= heap.length - 1) {heap = this.resize();}
        length++;
        if (length == first(height() + 1)) {
            ++height;
        }
        heap[length] = value;
        bubbleUp();
    }

    private int first(int h) {
        return ((h - 1) * (h)) / 2 + 1;
    }

    private int last(int h) {
        return ((h) * (h + 1)) / 2;
    }

    public T extract() {
        T result = min();
        swap(1, length);
        heap[length] = null;
        length--;
        if (length == last(height() - 1))
            height--;
        bubbleDown();
        return result;
    }

    public boolean search(T value) {
        int h = height;
        int i = first(height);
        while (i <= last(h)) {
            if (value.equals(heap[i])) {
                System.out.println("index = " + i);
                return true;
            }
            if (value.compareTo(heap[i]) > 0 && (i + h + 1) <= length) {
                i += ++h;
            } else {
                i -= --h;
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
        for (int i = length; i > length-height; i--) {
            if (heap[i - 1].compareTo(heap[index]) > 0)
                index = i - 1;
        }
        return heap[index];
    }

    public int length() {
        return length;
    }

    public int height() {
        return height;
    }

    private T[] resize() {
        return Arrays.copyOf(heap, heap.length + CAPACITY);
    }

    private void bubbleUp() {
        int index = length;
        for (int h = height; h > 1; h--) {
            if (index == first(h)
                    && (heap[leftParentIndex(index, h)].compareTo(heap[index]) > 0)) {
                swap(index, leftParentIndex(index, h));
                index = leftParentIndex(index, h);
            } else if (index == last(h)
                    && (heap[rightParentIndex(index, h)].compareTo(heap[index]) > 0)) {
                swap(index, rightParentIndex(index, h));
                index = rightParentIndex(index, h);
            } else if (index != first(h)
                    && index != last(h)
                    && heap[rightParentIndex(index, h)].compareTo(heap[index]) > 0
                    && heap[rightParentIndex(index, h)].compareTo(heap[leftParentIndex(index, h)]) > 0) {
                swap(index, rightParentIndex(index, h));
                index = rightParentIndex(index, h);
            } else if (index != first(h)
                    && index != last(h)
                    && heap[leftParentIndex(index, h)].compareTo(heap[index]) > 0) {
                swap(index, leftParentIndex(index, h));
                index = leftParentIndex(index, h);
            } else
                break;
        }
    }

    private void bubbleDown() {
        int index = 1;
        int h = 1;
        while (hasLeftChild(index, h)) {
            int smaller = leftIndex(index, h);
            if (hasRightChild(index, h) && heap[leftIndex(index, h)].compareTo(heap[rightIndex(index, h)]) > 0) {
                smaller = rightIndex(index, h);
            }
            if (heap[index].compareTo(heap[smaller]) > 0) {
                swap(index, smaller);
            } else
                break;
            index = smaller;
            h++;
        }
    }

    private int leftIndex(int i, int h) {
        return i + h;
    }

    private int rightIndex(int i, int h) {
        return i + h + 1;
    }

    private int leftParentIndex(int i, int h) {
        return i - h + 1;
    }

    private int rightParentIndex(int i, int h) {
        return i - h;
    }

    private boolean hasLeftChild(int i, int h) {
        return i + h <= length;
    }

    private boolean hasRightChild(int i, int h) {
        return i + h + 1 <= length;
    }

    private void swap(int index1, int index2) {
        T temp = heap[index1];
        heap[index1] = heap[index2];
        heap[index2] = temp;
    }

    @Override
    public String toString() {
        String retval = "";
        String str = "";
        int i = 0;
        int h = 0;
        for (T each : heap) {
            if (each != null) {
                str = String.format("%04d", each);

                if (i == last(h)) {
                    retval += "\n";
                    for (int j = 0; j < (height - h); j++)
                        retval += "   ";
                    h++;
                }
                retval += str + "    ";
                i++;
            }
        }
        return retval + "\n";
    }
}