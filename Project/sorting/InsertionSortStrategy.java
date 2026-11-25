package main.sorting;

import java.util.Comparator;
import java.util.List;

public class InsertionSortStrategy<T> implements SortStrategy<T> {

    @Override
    public void sort(List<T> list, Comparator<? super T> comparator) {
        if (list == null || list.size() <= 1) return;

        if (comparator instanceof OddEvenComparator<?> oddEven) {
            OddEvenSorterHelper.sort(list, oddEven, this::performStandardSort);
            return;
        }

        performStandardSort(list, comparator);
    }

    public void performStandardSort(List<T> subList, Comparator<? super T> comparator) {
        int n = subList.size();
        for (int i = 1; i < n; i++) {
            T key = subList.get(i);
            int j = i - 1;
            while (j >= 0 && comparator.compare(subList.get(j), key) > 0) {
                subList.set(j + 1, subList.get(j));
                j--;
            }
            subList.set(j + 1, key);
        }
    }
}