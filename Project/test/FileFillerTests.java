package tests;

import model.EntityParser;
import model.FileFiller;
import model.Student;
import model.ValidationException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileFillerTests {
    public static void main(String[] args) throws IOException {
        Path validFile = Files.createTempFile("students-valid-", ".txt");
        Path invalidFile = Files.createTempFile("students-invalid-", ".txt");
        Path shortFile = Files.createTempFile("students-short-", ".txt");
        try {
            Files.writeString(validFile,
                    "# тестовый файл\n\n101;4.5;123456\n102;3.8;123457\n",
                    StandardCharsets.UTF_8);
            Files.writeString(invalidFile,
                    "101;4.5;123456\nневерно;5.5;0\n",
                    StandardCharsets.UTF_8);
            Files.writeString(shortFile, "101;4.5;123456\n", StandardCharsets.UTF_8);

            TestSupport.test("Чтение нужного числа записей из файла", () -> {
                List<Student> students = new FileFiller<>(validFile, EntityParser::parseStudent).fill(2);
                TestSupport.assertEquals(2, students.size(), "Количество студентов");
                TestSupport.assertEquals(102, students.get(1).getGroupNumber(), "Данные второй записи");
            });
            TestSupport.test("Повреждённая строка файла вызывает понятную ошибку", () -> {
                try {
                    new FileFiller<>(invalidFile, EntityParser::parseStudent).fill(2);
                    throw new AssertionError("Ожидалась ошибка разбора файла");
                } catch (ValidationException e) {
                    TestSupport.assertTrue(e.getMessage().contains("строка 2"), "В сообщении должен быть номер строки");
                }
            });
            TestSupport.test("Недостаточное число записей отклоняется", () ->
                    TestSupport.assertThrows(ValidationException.class,
                            () -> new FileFiller<>(shortFile, EntityParser::parseStudent).fill(2),
                            "Не хватает записей"));
            TestSupport.test("Отсутствующий файл отклоняется", () ->
                    TestSupport.assertThrows(ValidationException.class,
                            () -> new FileFiller<>(validFile.resolveSibling("not-existing-file.txt"),
                                    EntityParser::parseStudent).fill(1),
                            "Файл не найден"));
            TestSupport.test("Формат с лишним полем отклоняется", () ->
                    TestSupport.assertThrows(ValidationException.class,
                            () -> EntityParser.parseStudent("101;4.5;123456;лишнее"),
                            "Лишнее поле"));

            TestSupport.printSummary();
        } finally {
            Files.deleteIfExists(validFile);
            Files.deleteIfExists(invalidFile);
            Files.deleteIfExists(shortFile);
        }
    }
}
