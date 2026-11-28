package model;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Универсальная стратегия заполнения коллекции данными из файла с помощью Стримов.
 * Реализует паттерн "Стратегия" (DataFillers) и Доп. задание №3 (Стримы).
 */
public class FileCreates<Y> implements DataFillers<Y> {
    
    // Изменили тип со String на Path, чтобы соответствовать Main.java
    private final Path filePath;
    private final Function<String, Y> lineParser;

    /**
     * @param filePath Путь к файлу с данными в виде объекта Path
     * @param lineParser Инструкция-лямбда, парсящая строку в объект Y
     */
    public FileCreates(Path filePath, Function<String, Y> lineParser) {
        this.filePath = filePath;
        this.lineParser = lineParser;
    }

    @Override
    public List<Y> fill(int size) {
        // Теперь Path.of() делать не нужно, так как filePath уже является объектом Path
        try (Stream<String> lines = Files.lines(filePath)) {
            
            // Магия Стримов (Доп. задание 3):
            return lines
                .limit(size)                  // 1. Берем из файла ровно столько строк, сколько просил пользователь
                .map(lineParser)              // 2. Превращаем каждую строчку в объект Y по инструкции из Main
                .filter(obj -> obj != null)   // 3. Отфильтровываем битые строки (если парсер вернул null)
                .collect(Collectors.toList());// 4. Собираем всё в готовый список и возвращаем
                
        } catch (Exception e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
            return new ArrayList<>(); 
        }
    }
}
