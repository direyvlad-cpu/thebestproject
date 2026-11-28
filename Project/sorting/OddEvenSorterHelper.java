package Project.sorting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;

public class OddEvenSorterHelper {

    public static <T> void sort(List<T> list, OddEvenComparator<T> comparator,
                                BiConsumer<List<T>, Comparator<T>> standardSortAlgorithm) {


            List<T> evenElements = new ArrayList<>();
            List<Integer> evenIndices = new ArrayList<>();

            // 1. Проходим по списку и находим чётные элементы
            for (int i = 0; i < list.size(); i++) {
                T element = list.get(i);
                long value = comparator.getFieldExtractor().applyAsLong(element);

                // Задание 6: если значение чётное - запоминаем индекс и сам элемент
                if (value % 2 == 0) {
                    evenElements.add(element);
                    evenIndices.add(i);
                }
            }

            if (evenElements.size() <= 1) return; // Сортировать нечего

            // 2. Сортируем только вытащенные чётные элементы твоим алгоритмом
            standardSortAlgorithm.accept(evenElements, comparator.getBaseComparator());

            // 3. Вставляем отсортированные чётные элементы обратно по их индексам
            for (int i = 0; i < evenIndices.size(); i++) {
                list.set(evenIndices.get(i), evenElements.get(i));
            }




    }

}