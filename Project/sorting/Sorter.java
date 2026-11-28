package Project.sorting;

import java.util.Comparator;
import java.util.List;

public class Sorter<T> {
    private SortStrategy<T> strategy;

    public void setStrategy(SortStrategy<T> strategy) {
        this.strategy = strategy;
    }

    public void sort(List<T> list, Comparator<? super T> comparator) {
        if (strategy == null) {
            throw new IllegalStateException("Стратегия сортировки не выбрана!");
        }
        // Вызов полиморфного метода (Core 1-5)
        strategy.sort(list, comparator);
    }
}