package Project.validation;

import Project.test.TestSupport;
import Project.model.Bus;
import Project.validation.DataValidator;
import Project.model.Student;
import Project.model.User;
import Project.validation.ValidationException;

public class ValidationTests {
    public static void main(String[] args) {
        TestSupport.test("Положительное целое число", () ->
                TestSupport.assertEquals(25, DataValidator.parsePositiveInt(" 25 ", "Размер"), "Парсинг int"));
        TestSupport.test("Пустое число отклоняется", () ->
                TestSupport.assertThrows(ValidationException.class,
                        () -> DataValidator.parsePositiveInt("  ", "Размер"), "Пустой ввод"));
        TestSupport.test("Ноль для положительного поля отклоняется", () ->
                TestSupport.assertThrows(ValidationException.class,
                        () -> DataValidator.parsePositiveInt("0", "Размер"), "Ноль"));
        TestSupport.test("Текст вместо числа отклоняется", () ->
                TestSupport.assertThrows(ValidationException.class,
                        () -> DataValidator.parsePositiveLong("abc", "Зачётка"), "Текст вместо числа"));

        TestSupport.test("Граничные оценки 0 и 5 принимаются", () -> {
            TestSupport.assertDoubleEquals(0.0, DataValidator.parseAverageGrade("0"), 0.000001, "Оценка 0");
            TestSupport.assertDoubleEquals(5.0, DataValidator.parseAverageGrade("5"), 0.000001, "Оценка 5");
        });
        TestSupport.test("Оценка выше 5 отклоняется", () ->
                TestSupport.assertThrows(ValidationException.class,
                        () -> DataValidator.parseAverageGrade("5.1"), "Оценка выше границы"));
        TestSupport.test("NaN в оценке отклоняется", () ->
                TestSupport.assertThrows(ValidationException.class,
                        () -> DataValidator.parseAverageGrade("NaN"), "NaN"));

        TestSupport.test("Студент создаётся с корректными значениями", () -> {
            Student student = Student.builder().groupNumber(101).averageGrade(4.5).gradeBookNumber(123456L).build();
            TestSupport.assertEquals(101, student.getGroupNumber(), "Номер группы");
            TestSupport.assertDoubleEquals(4.5, student.getAverageGrade(), 0.000001, "Средний балл");
        });
        TestSupport.test("Студент с отрицательной группой не создаётся", () ->
                TestSupport.assertThrows(ValidationException.class,
                        () -> Student.builder().groupNumber(-1).averageGrade(4).gradeBookNumber(123L).build(),
                        "Отрицательная группа"));
        TestSupport.test("Студент с нулевой зачёткой не создаётся", () ->
                TestSupport.assertThrows(ValidationException.class,
                        () -> Student.builder().groupNumber(101).averageGrade(4).gradeBookNumber(0).build(),
                        "Нулевая зачётка"));

        TestSupport.test("Автобус принимает нулевой пробег", () -> {
            Bus bus = Bus.builder().number("А123ВС").model("ЛиАЗ 5292").mileage(0).build();
            TestSupport.assertDoubleEquals(0, bus.getMileage(), 0.000001, "Пробег 0");
        });
        TestSupport.test("Автобус с отрицательным пробегом не создаётся", () ->
                TestSupport.assertThrows(ValidationException.class,
                        () -> Bus.builder().number("А123ВС").model("ЛиАЗ").mileage(-0.1).build(),
                        "Отрицательный пробег"));
        TestSupport.test("Автобус с бесконечным пробегом не создаётся", () ->
                TestSupport.assertThrows(ValidationException.class,
                        () -> Bus.builder().number("А123ВС").model("ЛиАЗ").mileage(Double.POSITIVE_INFINITY).build(),
                        "Бесконечный пробег"));

        TestSupport.test("Пользователь создаётся с корректными данными", () -> {
            User user = User.builder().name("Иван Петров").password("secret123").email("Ivan@example.com").build();
            TestSupport.assertEquals("ivan@example.com", user.getEmail(), "Нормализация email");
            TestSupport.assertTrue(user.checkPassword("secret123"), "Проверка пароля");
        });
        TestSupport.test("Короткий пароль отклоняется", () ->
                TestSupport.assertThrows(ValidationException.class,
                        () -> User.builder().name("Иван").password("123").email("i@example.com").build(),
                        "Короткий пароль"));
        TestSupport.test("Некорректная почта отклоняется", () ->
                TestSupport.assertThrows(ValidationException.class,
                        () -> User.builder().name("Иван").password("secret123").email("ivan@invalid").build(),
                        "Неправильный email"));

        TestSupport.printSummary();
    }
}
