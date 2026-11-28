package Project.sorting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MergeSortStrategy<T> implements SortStrategy<T> {

    @Override
    public void sort(List<T> list, Comparator<? super T> comparator) {
        if (list == null || list.size() <= 1) return;

        if (comparator instanceof OddEvenComparator<?> oddEven) {
            @SuppressWarnings("unchecked")
            OddEvenComparator<T> typed = (OddEvenComparator<T>) oddEven;
            OddEvenSorterHelper.sort(list, typed, this::performStandardSort);
            return;
        }

        performStandardSort(list, comparator);
    }

    public void performStandardSort(List<T> subList, Comparator<? super T> comparator) {
        if (subList.size() <= 1) return;

        int mid = subList.size() / 2;
        List<T> left = new ArrayList<>(subList.subList(0, mid));
        List<T> right = new ArrayList<>(subList.subList(mid, subList.size()));

        performStandardSort(left, comparator);  // Рекурсивный вызов
        performStandardSort(right, comparator); // Рекурсивный вызов

        merge(subList, left, right, comparator);
    }

    private void merge(List<T> result, List<T> left, List<T> right, Comparator<? super T> comparator) {
        int i = 0, j = 0, k = 0;
        while (i < left.size() && j < right.size()) {
            if (comparator.compare(left.get(i), right.get(j)) <= 0) {
                result.set(k++, left.get(i++));
            } else {
                result.set(k++, right.get(j++));
            }
        }
        while (i < left.size()) result.set(k++, left.get(i++));
        while (j < right.size()) result.set(k++, right.get(j++));
    }
}