package Project.validation;

/** Ошибка проверки пользовательских или загружаемых данных. */
public class ValidationException extends IllegalArgumentException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
