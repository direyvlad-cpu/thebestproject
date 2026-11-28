package Project.validation;

import Project.model.DataFillers;
import Project.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Случайное заполнение студентов с генерацией только допустимых значений. */
class RandomCreate implements DataFillers<Student> {
    @Override
    public List<Student> fill(int size) {
        DataValidator.validatePositiveInt(size, "Размер коллекции");
        List<Student> students = new ArrayList<>(size);
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int groupNumber = random.nextInt(900) + 100;
            double averageGrade = 2.0 + random.nextDouble() * 3.0;
            long gradeBookNumber = random.nextInt(900_000) + 100_000L;
            students.add(Student.builder()
                    .groupNumber(groupNumber)
                    .averageGrade(averageGrade)
                    .gradeBookNumber(gradeBookNumber)
                    .build());
        }
        return students;
    }
}
