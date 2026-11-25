package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/** Читает заданное количество проверенных записей из UTF-8-файла. */
public class FileFiller<T> implements DataFiller<T> {
    private final Path path;
    private final Function<String, T> parser;

    public FileFiller(Path path, Function<String, T> parser) {
        if (path == null) {
            throw new ValidationException("Путь к файлу не должен быть null.");
        }
        if (parser == null) {
            throw new ValidationException("Парсер файла не должен быть null.");
        }
        this.path = path;
        this.parser = parser;
    }

    @Override
    public List<T> fill(int size) {
        DataValidator.validatePositiveInt(size, "Размер коллекции");
        if (!Files.exists(path)) {
            throw new ValidationException("Файл не найден: " + path.toAbsolutePath());
        }
        if (!Files.isRegularFile(path) || !Files.isReadable(path)) {
            throw new ValidationException("Файл недоступен для чтения: " + path.toAbsolutePath());
        }

        List<T> result = new ArrayList<>(size);
        int lineNumber = 0;
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while (result.size() < size && (line = reader.readLine()) != null) {
                lineNumber++;
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }
                try {
                    T item = parser.apply(trimmed);
                    if (item == null) {
                        throw new ValidationException("Парсер вернул null вместо объекта.");
                    }
                    result.add(item);
                } catch (RuntimeException e) {
                    throw new ValidationException(
                            "Ошибка в файле " + path + ", строка " + lineNumber + ": " + e.getMessage(), e);
                }
            }
        } catch (IOException e) {
            throw new ValidationException("Не удалось прочитать файл " + path + ": " + e.getMessage(), e);
        }

        if (result.size() < size) {
            throw new ValidationException("В файле " + path + " недостаточно корректных записей: ожидалось "
                    + size + ", найдено " + result.size() + ".");
        }
        return result;
    }
}
