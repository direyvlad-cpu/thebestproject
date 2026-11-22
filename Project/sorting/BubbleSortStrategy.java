package main.sorting;

import java.util.Comparator;
import java.util.List;

public class BubbleSortStrategy<T> implements SortStrategy<T> {

    @Override
    public void sort(List<T> list, Comparator<? super T> comparator) {
        if (list == null || list.size() <= 1) return;

        if (comparator instanceof OddEvenComparator<?> oddEven) {
            sortOddEven(list, oddEven);
            return;
        }

        // Обычная сортировка пузырьком
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (comparator.compare(list.get(j), list.get(j + 1)) > 0) {
                    swap(list, j, j + 1);
                }
            }
        }
    }

    private void sortOddEven(List<T> list, OddEvenComparator<?> oddEven) {
        // Логика для задания 6 (сортировка только чётных)
        // Мы будем реализовывать её через базовый метод, чтобы не дублировать код
        OddEvenSorterHelper.sort(list, oddEven, this::performStandardSort);
    }

    // Вспомогательный метод для обмена элементов (Core, перегрузка/массивы)
    private void swap(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    // Метод нужен для хелпера, чтобы отсортировать только чётные элементы
    public void performStandardSort(List<T> subList, Comparator<? super T> comparator) {
        int n = subList.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (comparator.compare(subList.get(j), subList.get(j + 1)) > 0) {
                    T temp = subList.get(j);
                    subList.set(j, subList.get(j + 1));
                    subList.set(j + 1, temp);
                }
            }
        }
    }
}