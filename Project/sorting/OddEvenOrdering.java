package main.sorting;

import model.Student;
import java.util.Comparator;
import java.util.function.ToLongFunction;

public class OddEvenOrdering<T> implements OddEvenComparator<T> {

    private final ToLongFunction<T> fieldExtractor;
    private final Comparator<T> baseComparator;

    public OddEvenOrdering(ToLongFunction<T> fieldExtractor, Comparator<T> baseComparator) {
        this.fieldExtractor = fieldExtractor;
        this.baseComparator = baseComparator;
    }

    @Override
    public ToLongFunction<T> getFieldExtractor() {
        return fieldExtractor;
    }

    @Override
    public Comparator<T> getBaseComparator() {
        return baseComparator;
    }

    @Override
    public int compare(T o1, T o2) {
        return baseComparator.compare(o1, o2);
    }

    // Фабричный метод для обратной совместимости с Main.java (специально для Студентов)
    public static OddEvenComparator<Student> byAverageGrade() {
        return new OddEvenOrdering<>(
                student -> (long) student.getAverageGrade(),
                Comparator.comparingDouble(Student::getAverageGrade)
        );
    }
}