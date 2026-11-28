package Project.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Доп-2: записывает сущности в текстовый файл.
 * Каждая запись добавляется в конец файла (append), одна сущность — одна строка.
 */
public class EntityFileWriter<T> {

    private final Path path;
    private static final DateTimeFormatter TIMESTAMP =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EntityFileWriter(Path path) {
        this.path = path;
    }

    /**
     * Дописывает все элементы списка в файл.
     * Ничего не выбрасывает наружу: ошибки записи выводятся в консоль,
     * чтобы не падать в основном цикле меню.
     */
    public void append(List<T> items) {
        if (items == null || items.isEmpty()) {
            System.out.println("Нечего записывать: список пуст.");
            return;
        }

        try (var writer = Files.newBufferedWriter(
                path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {

            writer.write("=== Запись от " + LocalDateTime.now().format(TIMESTAMP) + " ===");
            writer.newLine();

            for (T item : items) {
                writer.write(item.toString());
                writer.newLine();
            }

            System.out.println("Записано объектов: " + items.size() + " (файл: " + path + ")");
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл " + path + ": " + e.getMessage());
        }
    }
}
