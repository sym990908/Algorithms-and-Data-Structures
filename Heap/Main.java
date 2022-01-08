public class Main {
    public static final int nodeNumber = 100;

    public static void main(String[] args) {
        long startTime = 0;
        long endTime = 0;
        int min = 0;
        int max = 0;
        int search = nodeNumber - 1;
        boolean result = false;

        Integer[] array = randomArray(0, 9999, nodeNumber);

        BinaryHeap<Integer> heap = new BinaryHeap<>(array);
        System.out.println("Length = " + heap.length());
        System.out.println(heap);
        System.out.println();

        Beap<Integer> beap = new Beap<>(array);
        System.out.println("Length = " + beap.length());
        System.out.println("Height = " + beap.height());
        System.out.println(beap);
        System.out.println();

        startTime = System.nanoTime();
        min = heap.min();
        endTime = System.nanoTime();
        System.out.println("Min = " + min);
        System.out.println("min binary heap time= " + (endTime - startTime) + "ns");

        startTime = System.nanoTime();
        min = beap.min();
        endTime = System.nanoTime();
        System.out.println("Min = " + min);
        System.out.println("min beap time= " + (endTime - startTime) + "ns");

        startTime = System.nanoTime();
        max = heap.max();
        endTime = System.nanoTime();
        System.out.println("max = " + max);
        System.out.println("max binary heap time= " + (endTime - startTime) + "ns");

        startTime = System.nanoTime();
        max = beap.max();
        endTime = System.nanoTime();
        System.out.println("max = " + max);
        System.out.println("max beap time= " + (endTime - startTime) + "ns");

        // System.out.print(heap);
        startTime = System.nanoTime();
        min = heap.extract();
        endTime = System.nanoTime();
        System.out.println("Extract:" + min);
        System.out.println("binary heap extract time= " + (endTime - startTime) + "ns");
        System.out.print(heap);

        startTime = System.nanoTime();
        min = beap.extract();
        endTime = System.nanoTime();
        System.out.println("Extract:" + min);
        System.out.println("Beap extract time= " + (endTime - startTime) + "ns");
        System.out.print(beap);

        startTime = System.nanoTime();
        result = heap.search(search);
        endTime = System.nanoTime();
        System.out.println("Search " + search + " result= " + result);
        System.out.println("binary heap search time= " + (endTime - startTime) + "ns");

        startTime = System.nanoTime();
        result = beap.search(search);
        endTime = System.nanoTime();
        System.out.println("Search " + search + " result= " + result);
        System.out.println("beap search time= " + (endTime - startTime) + "ns");
    }

    private static Integer[] randomArray(int min, int max, int n) {
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        Integer[] result = new Integer[n];
        Integer count = 0;
        while (count < n) {
            Integer num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if (num == result[j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                result[count] = num;
                count++;
            }
        }
        return result;
    }
}
