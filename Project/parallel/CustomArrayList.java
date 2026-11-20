package model;

import java.util.*;
import java.util.stream.Collector;

//может назвать по-другому?
public class CustomArrayList<T> extends AbstractList <T> {
    private Object[] elements = new Object[10];
    private int size = 0;

    @Override
    public boolean add(T element) {
        // Изюминка: Защита от дубликатов по номеру зачетки (для объектов Student)
        if (isDuplicate(element)) {
            System.out.println("Дубликат отклонен: студент с таким номером зачетки уже существует");
            return false;
        }

        // Автоматическое динамическое расширение массива при заполнении
        if(size == elements.length) {
            elements = Arrays.copyOf(elements, elements.length * 2);
        }
        elements[size++] = element; //тут увеличивается размер size
        return true;
    }

    // Вспомогательный метод проверки дубликатов по номеру зачетки (для объектов Student)
    private boolean isDuplicate(T element) {
        if (element instanceof Student newStudent) {
            for (int i = 0; i < size; i++) {
                if (elements[i] instanceof Student existing &&
                existing.getGradeBookNumber() == newStudent.getGradeBookNumber()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    //аннотация, чтобы сказать компилятору, что мы проконтролировали возвращаемые типы
    //если её не будет - return будет гореть жёлтым
    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException ("Индекс: " + index + ", Размер: " + size);
        }
        return (T) elements[index];
    }

    @Override
    public int size() {
        return size;
    }

    //реализовать собственный метод сортировки
    //или сохранить порядок заполнения коллекции, если где-то сортировка уже была?
    @SuppressWarnings("unchecked") //чтобы не горело жёлтым
    public void manualSort(Comparator<? super T> comparator) {
        if (size <= 1) return;
        mergeSort(elements, 0, size - 1, comparator);
        System.out.println("Коллекция успешно отсортирована сортировкой слияния!");
    }

    private void mergeSort(Object[] arr, int left, int right, Comparator<? super T> comparator) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            mergeSort(arr, left, mid, comparator);
            mergeSort(arr, mid + 1, right, comparator);

            merge(arr, left, mid, right, comparator);
        }
    }

    @SuppressWarnings("unchecked") //чтобы не горело жёлтым
    private void merge(Object[] arr, int left, int mid, int right, Comparator<? super T> comparator) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        Object[] leftArr = new Object[n1];
        Object[] rightArr = new Object[n2];

        for (int i = 0; i < n1; i++) leftArr[i] = arr[left + i];
        for (int j = 0; j < n2; j++) rightArr[j] = arr[mid + 1 + j];

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            if (comparator.compare((T) leftArr[i], (T) rightArr[j]) <= 0) {
                arr[k++] = leftArr[i++];
            } else {
                arr[k++] = rightArr[j++];
            }
        }

        while (i < n1) {
            arr[k++] = leftArr[i++];
        }

        while (j < n2) {
            arr[k++] = rightArr[j++];
        }
    }

    // Кастомный коллектор для интеграции со Stream API
    public static <T> Collector<T, ?, CustomArrayList<T>> toCustomList() {
        return Collector.of(() -> new CustomArrayList<T>(), //заменить лямбдой
                (container, element) -> container.add(element), //заменить лямбдой
                (left, right) -> {
                    for (int i = 0; i < right.size; i++) {
                        left.add(right.get(i));
                    }
                    return left;
                });

    }

}
