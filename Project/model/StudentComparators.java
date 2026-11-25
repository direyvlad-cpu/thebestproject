package model;

import java.util.Comparator;

public final class StudentComparators {

    private StudentComparators() { }

    public static final Comparator<Student> BY_GROUP =
            Comparator.comparingInt(Student::getGroupNumber);

    public static final Comparator<Student> BY_GRADE =
            Comparator.comparingDouble(Student::getAverageGrade);

    public static final Comparator<Student> BY_BOOK =
            Comparator.comparingLong(Student::getGradeBookNumber);
}

