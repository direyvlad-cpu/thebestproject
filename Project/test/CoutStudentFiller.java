package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** Ручное заполнение студентов. Старый дублирующий интерфейс DataFiller удалён. */
class CoutCreate implements DataFiller<Student> {
    @Override
    public List<Student> fill(int size) {
        DataValidator.validatePositiveInt(size, "Размер коллекции");
        List<Student> students = new ArrayList<>(size);
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < size; i++) {
            System.out.println("Студент " + (i + 1) + ":");
            students.add(EntityInput.readStudent(scanner));
        }
        return students;
    }
}
