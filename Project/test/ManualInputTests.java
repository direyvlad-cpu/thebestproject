package Project.test;

import Project.validation.EntityInput;
import Project.model.Student;
import Project.validation.ValidationException;

import java.util.Scanner;

public class ManualInputTests {
    public static void main(String[] args) {
        TestSupport.test("Ручной ввод повторяется после некорректных значений", () -> {
            // Сначала неверная группа, затем верная; затем оценка выше допустимой и верная.
            Scanner scanner = new Scanner("abc\n101\n5.5\n4.25\n123456\n");
            Student student = EntityInput.readStudent(scanner);
            TestSupport.assertEquals(101, student.getGroupNumber(), "Группа после повторного ввода");
            TestSupport.assertDoubleEquals(4.25, student.getAverageGrade(), 0.000001, "Оценка после повторного ввода");
            TestSupport.assertEquals(123456L, student.getGradeBookNumber(), "Зачётка");
        });
        TestSupport.test("Завершение ввода до заполнения полей обрабатывается", () -> {
            Scanner scanner = new Scanner("101\n");
            TestSupport.assertThrows(ValidationException.class,
                    () -> EntityInput.readStudent(scanner), "Недостаточно введённых данных");
        });
        TestSupport.printSummary();
    }
}
