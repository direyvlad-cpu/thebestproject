package Project.validation;

import Project.model.Bus;
import Project.model.Student;
import Project.model.User;

import java.util.Scanner;
import java.util.function.Function;

/** Валидированный построчный ввод полей сущностей из консоли. */
public final class EntityInput {
    private EntityInput() {
    }

    public static Student readStudent(Scanner scanner) {
        int group = read(scanner, "Номер группы: ", value -> DataValidator.parsePositiveInt(value, "Номер группы"));
        double grade = read(scanner, "Средний балл (0–5): ", DataValidator::parseAverageGrade);
        long book = read(scanner, "Номер зачётки: ", value -> DataValidator.parsePositiveLong(value, "Номер зачётки"));
        return Student.builder().groupNumber(group).averageGrade(grade).gradeBookNumber(book).build();
    }

    public static Bus readBus(Scanner scanner) {
        String number = read(scanner, "Номер автобуса: ", DataValidator::validateBusNumber);
        String model = read(scanner, "Модель автобуса: ", DataValidator::validateBusModel);
        double mileage = read(scanner, "Пробег (км): ", value -> DataValidator.parseNonNegativeDouble(value, "Пробег"));
        return Bus.builder().number(number).model(model).mileage(mileage).build();
    }

    public static User readUser(Scanner scanner) {
        String name = read(scanner, "Имя: ", DataValidator::validateUserName);
        String password = read(scanner, "Пароль (не менее 8 символов): ", DataValidator::validatePassword);
        String email = read(scanner, "Электронная почта: ", DataValidator::validateEmail);
        return User.builder().name(name).password(password).email(email).build();
    }

    private static <T> T read(Scanner scanner, String prompt, Function<String, T> validator) {
        if (scanner == null) {
            throw new ValidationException("Источник ввода не должен быть null.");
        }
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                throw new ValidationException("Ввод завершён до получения всех обязательных данных.");
            }
            String value = scanner.nextLine();
            try {
                return validator.apply(value);
            } catch (ValidationException e) {
                System.out.println("Некорректное значение: " + e.getMessage() + " Повторите ввод.");
            }
        }
    }
}
