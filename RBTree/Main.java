import java.util.*;

class Main {
    public static void main(String[] args) {
        RBTree t1 = new RBTree();
        Integer[] nums1 = new Integer[] { 20, 19, 15, 13, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 };
        for (Integer n : nums1) {
            t1.Insert(n);
        }
        t1.print();
        t1.max();
        t1.min();
        t1.median();
        System.out.println();

        RBTree t2 = new RBTree();
        Integer[] nums2 = new Integer[] { 1, 2, 3, 4, 5, 7, 8, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
        for (Integer n : nums2) {
            t2.Insert(n);
        }
        t2.print();
        t2.max();
        t2.min();
        t2.median();
        System.out.println();
        
        System.out.println("Find intersection t1 t2:");
        ArrayList<Integer> same = intersection(t1, t2);
        System.out.println(same);

        System.out.println("delete 7");
        t1.delete(7);
        t1.print();

        System.out.println("delete 12");
        t1.delete(12);
        t1.print();

        System.out.println("search 10");
        t1.search(10);

        System.out.println("search 7");
        t1.search(7);

    }

    public static ArrayList<Integer> intersection(RBTree t1, RBTree t2) {
        ArrayList<Integer> list1 = t1.getList();
        ArrayList<Integer> list2 = t2.getList();
        ArrayList<Integer> list = new ArrayList<>();
        if (list1 == null || list2 == null || list1.size() == 0 || list2.size() == 0) {
            return list = null;
        }
        int i = 0, j = 0;
        while (i < list1.size() && j < list2.size()) {
            if (list1.get(i) == list2.get(j)) {
                list.add((int) list1.get(i));
                i++;
                j++;
            } else if ((int) list1.get(i) < (int) list2.get(j))
                i++;
            else
                j++;
        }
        return list;
    }
}