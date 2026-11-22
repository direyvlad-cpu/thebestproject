package main.sorting;

import model.Student;
import java.util.Comparator;
import java.util.function.ToLongFunction;

public class OddEvenOrdering {

    public static OddEvenComparator<Student> byAverageGrade() {
        return new OddEvenComparator<>() {
            @Override
            public ToLongFunction<Student> getFieldExtractor() {
                return student -> (long) student.getAverageGrade();
            }

            @Override
            public Comparator<Student> getBaseComparator() {
                return Comparator.comparingDouble(Student::getAverageGrade);
            }

            @Override
            public int compare(Student o1, Student o2) {
                return getBaseComparator().compare(o1, o2);
            }
        };
    }
}