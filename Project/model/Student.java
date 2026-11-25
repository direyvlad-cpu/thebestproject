package model;


public class Student implements Comparable<Student> {

    private final int groupNumber;
    private final double averageGrade;
    private final long gradeBookNumber;

    private Student(Builder builder) {
        this.groupNumber = builder.groupNumber;
        this.averageGrade = builder.averageGrade;
        this.gradeBookNumber = builder.gradeBookNumber;
    }


    public static Builder builder() {
        return new Builder();
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public double getAverageGrade() {
        return averageGrade;
    }

    public long getGradeBookNumber() {
        return gradeBookNumber;
    }

    @Override
    public int compareTo(Student other) {
        return Integer.compare(this.groupNumber, other.groupNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student student = (Student) o;
        return groupNumber == student.groupNumber
                && Double.compare(student.averageGrade, averageGrade) == 0
                && gradeBookNumber == student.gradeBookNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupNumber, averageGrade, gradeBookNumber);
    }

    @Override
    public String toString() {
        return "Student{группа=" + groupNumber
                + ", средний балл=" + averageGrade
                + ", зачётка=" + gradeBookNumber + "}";
    }

    public String toFileString() {
        return groupNumber + ";" + averageGrade + ";" + gradeBookNumber;
    }

    public static class Builder {
        private int groupNumber;
        private double averageGrade = -1;
        private long gradeBookNumber = -1;

        private Builder() {
        }

        public Builder groupNumber(int value) {
            this.groupNumber = value;
            return this;
        }

        public Builder averageGrade(double value) {
            this.averageGrade = value;
            return this;
        }

        public Builder gradeBookNumber(long value) {
            this.gradeBookNumber = value;
            return this;
        }

        public Student build() {
            if (groupNumber <= 0 || averageGrade < 0 || gradeBookNumber < 0) {
                throw new IllegalStateException(
                        "Не заполнены обязательные поля: группа, средний балл и номер зачётки");
            }
            return new Student(this);
        }
    }
}