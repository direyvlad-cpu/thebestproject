package main.sorting;

import java.util.Comparator;
import java.util.function.ToLongFunction;

public interface OddEvenComparator<T> extends Comparator<T> {
    ToLongFunction<T> getFieldExtractor();
    Comparator<T> getBaseComparator();
}