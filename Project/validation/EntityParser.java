package model;

/** Преобразует одну строку файла в объект предметной области. Формат полей разделён символом ';'. */
public final class EntityParser {
    private EntityParser() {
    }

    public static Student parseStudent(String line) {
        String[] fields = split(line, 3, "Студент: группа;средний балл;номер зачётки");
        return Student.builder()
                .groupNumber(DataValidator.parsePositiveInt(fields[0], "Номер группы"))
                .averageGrade(DataValidator.parseAverageGrade(fields[1]))
                .gradeBookNumber(DataValidator.parsePositiveLong(fields[2], "Номер зачётки"))
                .build();
    }

    public static Bus parseBus(String line) {
        String[] fields = split(line, 3, "Автобус: номер;модель;пробег");
        return Bus.builder()
                .number(DataValidator.validateBusNumber(fields[0]))
                .model(DataValidator.validateBusModel(fields[1]))
                .mileage(DataValidator.parseNonNegativeDouble(fields[2], "Пробег"))
                .build();
    }

    /**
     * Входной формат пользователя: имя;пароль;почта.
     * Файл с паролями храните вне публичного репозитория и ограничьте к нему доступ.
     */
    public static User parseUser(String line) {
        String[] fields = split(line, 3, "Пользователь: имя;пароль;почта");
        return User.builder()
                .name(fields[0])
                .password(fields[1])
                .email(fields[2])
                .build();
    }

    private static String[] split(String line, int expectedCount, String format) {
        if (line == null || line.isBlank()) {
            throw new ValidationException("Строка файла не должна быть пустой.");
        }
        String[] fields = line.split(";", -1);
        if (fields.length != expectedCount) {
            throw new ValidationException("Неверный формат записи. Ожидается: " + format + ".");
        }
        return fields;
    }
}
