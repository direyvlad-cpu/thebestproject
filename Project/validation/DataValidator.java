package Project.validation;

import java.util.regex.Pattern;

/** Общие правила проверки данных проекта. */
public final class DataValidator {
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_PASSWORD_LENGTH = 128;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_BUS_NUMBER_LENGTH = 20;
    private static final int MAX_BUS_MODEL_LENGTH = 80;

    // Не допускаем пробелы и управляющие символы внутри адреса.
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9.!#$%&'*+/=?^_`{|}~-]+@"
                    + "[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?"
                    + "(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?)+$"
    );

    private DataValidator() {
    }

    public static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Поле «" + fieldName + "» не должно быть пустым.");
        }
        String trimmed = value.trim();
        if (containsControlCharacters(trimmed)) {
            throw new ValidationException("Поле «" + fieldName + "» содержит недопустимые символы.");
        }
        return trimmed;
    }

    public static int validatePositiveInt(int value, String fieldName) {
        if (value <= 0) {
            throw new ValidationException("Поле «" + fieldName + "» должно быть больше нуля.");
        }
        return value;
    }

    public static long validatePositiveLong(long value, String fieldName) {
        if (value <= 0L) {
            throw new ValidationException("Поле «" + fieldName + "» должно быть больше нуля.");
        }
        return value;
    }

    public static double validateAverageGrade(double value) {
        if (!Double.isFinite(value) || value < 0.0 || value > 5.0) {
            throw new ValidationException("Средний балл должен быть числом от 0 до 5 включительно.");
        }
        return value;
    }

    public static double validateNonNegativeDouble(double value, String fieldName) {
        if (!Double.isFinite(value) || value < 0.0) {
            throw new ValidationException("Поле «" + fieldName + "» должно быть конечным числом не меньше нуля.");
        }
        return value;
    }

    public static int parsePositiveInt(String value, String fieldName) {
        String text = requireText(value, fieldName);
        try {
            return validatePositiveInt(Integer.parseInt(text), fieldName);
        } catch (NumberFormatException e) {
            throw new ValidationException("Поле «" + fieldName + "» должно быть целым числом.", e);
        }
    }

    public static long parsePositiveLong(String value, String fieldName) {
        String text = requireText(value, fieldName);
        try {
            return validatePositiveLong(Long.parseLong(text), fieldName);
        } catch (NumberFormatException e) {
            throw new ValidationException("Поле «" + fieldName + "» должно быть целым числом.", e);
        }
    }

    public static double parseAverageGrade(String value) {
        return validateAverageGrade(parseDouble(value, "Средний балл"));
    }

    public static double parseNonNegativeDouble(String value, String fieldName) {
        return validateNonNegativeDouble(parseDouble(value, fieldName), fieldName);
    }

    private static double parseDouble(String value, String fieldName) {
        String text = requireText(value, fieldName);
        try {
            double parsed = Double.parseDouble(text.replace(',', '.'));
            if (!Double.isFinite(parsed)) {
                throw new ValidationException("Поле «" + fieldName + "» должно быть конечным числом.");
            }
            return parsed;
        } catch (NumberFormatException e) {
            throw new ValidationException("Поле «" + fieldName + "» должно быть числом.", e);
        }
    }

    public static String validateBusNumber(String value) {
        String number = requireText(value, "Номер автобуса");
        if (number.length() > MAX_BUS_NUMBER_LENGTH) {
            throw new ValidationException("Номер автобуса не должен быть длиннее " + MAX_BUS_NUMBER_LENGTH + " символов.");
        }
        // Формат намеренно не привязан к конкретной стране или региону.
        if (!number.matches("[\\p{L}\\p{N} .-]+")) {
            throw new ValidationException("Номер автобуса содержит недопустимые символы.");
        }
        return number;
    }

    public static String validateBusModel(String value) {
        String model = requireText(value, "Модель автобуса");
        if (model.length() > MAX_BUS_MODEL_LENGTH) {
            throw new ValidationException("Модель автобуса не должна быть длиннее " + MAX_BUS_MODEL_LENGTH + " символов.");
        }
        return model;
    }

    public static String validateUserName(String value) {
        String name = requireText(value, "Имя пользователя");
        if (name.length() > MAX_NAME_LENGTH) {
            throw new ValidationException("Имя не должно быть длиннее " + MAX_NAME_LENGTH + " символов.");
        }
        return name;
    }

    public static String validatePassword(String value) {
        if (value == null || value.isBlank()) {
            throw new ValidationException("Пароль не должен быть пустым.");
        }
        if (value.length() < MIN_PASSWORD_LENGTH) {
            throw new ValidationException("Пароль должен содержать не менее " + MIN_PASSWORD_LENGTH + " символов.");
        }
        if (value.length() > MAX_PASSWORD_LENGTH) {
            throw new ValidationException("Пароль не должен быть длиннее " + MAX_PASSWORD_LENGTH + " символов.");
        }
        if (containsControlCharacters(value)) {
            throw new ValidationException("Пароль содержит недопустимые управляющие символы.");
        }
        return value; // Пароль не trim(): пробелы могут быть частью пароля.
    }

    public static String validateEmail(String value) {
        String email = requireText(value, "Электронная почта");
        if (email.length() > 254 || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Указан некорректный адрес электронной почты.");
        }
        return email.toLowerCase();
    }

    public static int validatePositiveInt(String value, String fieldName) {
        return parsePositiveInt(value, fieldName);
    }

    private static boolean containsControlCharacters(String value) {
        for (int i = 0; i < value.length(); i++) {
            if (Character.isISOControl(value.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}
