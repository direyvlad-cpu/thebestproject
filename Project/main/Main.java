package main;

import model.*;
import sorting.*;
import validation.*;
import parallel.*;
import test.*;
import model.FileCreate;


import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.Random;
import java.util.ArrayList;

/**
 * Главный класс приложения управления сущностями (Студенты, Автобусы, Пользователи).
 * Обеспечивает циклическое меню, выбор стратегий заполнения и выполнение кастомных сортировок.
 */
public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        while (true) {
            System.out.println("""
                    Выберите класс для работы:
                    1 - Студенты
                    2 - Автобусы
                    3 - Пользователи
                    0 - выход""");
            System.out.print("Ваш выбор: ");

            switch (SCANNER.nextLine().trim()) {
                case "1" -> runEntityMenu(
                        new RandomFiller<>(() -> Student.builder()
                                .groupNumber(RANDOM.nextInt(900) + 100)
                                .averageGrade(2.0 + RANDOM.nextDouble() * 3.0)
                                .gradeBookNumber(100000L + RANDOM.nextInt(900000))
                                .build()),
                        new CoutFiller<>(() -> {
                            System.out.print("  Введите номер группы (целое число): ");
                            int group = readInt();
                            System.out.print("  Введите средний балл (дробь): ");
                            double grade = readDouble();
                            System.out.print("  Введите зачетку (целое число): ");
                            long id = readLong();
                            return Student.builder().groupNumber(group).averageGrade(grade).gradeBookNumber(id).build();
                        }),
                        new FileCreates<>(Path.of("input.txt"), (line) -> {
                            String[] parts = line.split(",");
                            return Student.builder()
                                    .groupNumber(Integer.parseInt(parts[0].trim()))
                                    .averageGrade(Double.parseDouble(parts[1].trim()))
                                    .gradeBookNumber(Long.parseLong(parts[2].trim()))
                                    .build();
                        }),
                        List.of(
                                new NamedComparator<>("группа", (s1, s2) -> Integer.compare(s1.getGroupNumber(), s2.getGroupNumber())),
                                new NamedComparator<>("средний балл", (s1, s2) -> Double.compare(s1.getAverageGrade(), s2.getAverageGrade())),
                                new NamedComparator<>("зачётка", (s1, s2) -> Long.compare(s1.getGradeBookNumber(), s2.getGradeBookNumber()))
                        ),
                        (s1, s2) -> {
                            long g1 = s1.getGroupNumber();
                            long g2 = s2.getGroupNumber();
                            if (g1 % 2 == 0 && g2 % 2 == 0) return Integer.compare(s1.getGroupNumber(), s2.getGroupNumber());
                            return 0;
                        },
                        "Введите номер группы для подсчёта кол-ва студентов: ",
                        Student::getGroupNumber,
                        Integer::parseInt
                );
                case "2" -> runEntityMenu(
                        new RandomFiller<>(() -> Bus.builder()
                                .number(String.valueOf(RANDOM.nextInt(900) + 100))
                                .model("Volvo_" + RANDOM.nextInt(10))
                                .mileage(RANDOM.nextInt(500000))
                                .build()),
                        new CoutFiller<>(() -> {
                            System.out.print("  Введите номер автобуса: ");
                            int num = readInt();
                            System.out.print("  Введите модель: ");
                            String model = SCANNER.nextLine().trim();
                            System.out.print("  Введите пробег: ");
                            int mileage = readInt();
                            return Bus.builder().number(String.valueOf(num)).model(model).mileage(mileage).build();
                        }),
                        new FileCreates<>(Path.of("buses.txt"), (line) -> {
                            String[] parts = line.split(",");
                            return Bus.builder()
                                    .number(String.valueOf((parts[0].trim())))
                                    .model(parts[1].trim())
                                    .mileage(Integer.parseInt(parts[2].trim()))
                                    .build();
                        }),
                         List.of(
                                // Сравниваем номера как строки через .compareTo()
                                new NamedComparator<>("номер", (b1, b2) -> b1.getNumber().compareTo(b2.getNumber())),
                                // Сравниваем модели как строки
                                new NamedComparator<>("модель", (b1, b2) -> b1.getModel().compareTo(b2.getModel())),
                                // Сравниваем пробег как строки через .compareTo()
                                new NamedComparator<>("пробег", (b1, b2) -> Double.compare(b1.getMileage(), b2.getMileage()))

                            ),

                        null,
                        "Введите модель автобуса для подсчёта кол-ва: ",
                        Bus::getModel,
                        String::trim
                );
                case "3" -> runEntityMenu(
                        new RandomFiller<>(() -> User.builder()
                                .name("User_" + RANDOM.nextInt(1000))
                                .password("pass" + RANDOM.nextInt(9999))
                                .email("user" + RANDOM.nextInt(100) + "@mail.ru")
                                .build()),
                        new CoutFiller<>(() -> {
                            System.out.print("  Введите имя пользователя: ");
                            String name = SCANNER.nextLine().trim();
                            System.out.print("  Введите пароль: ");
                            String pass = SCANNER.nextLine().trim();
                            System.out.print("  Введите почту: ");
                            String email = SCANNER.nextLine().trim();
                            return User.builder().name(name).password(pass).email(email).build();
                        }),
                        new FileCreates<>(Path.of("users.txt"), (line) -> {
                            String[] parts = line.split(",");
                            return User.builder()
                                    .name(parts[0].trim())
                                    .password(parts[1].trim())
                                    .email(parts[2].trim())
                                    .build();
                        }),
                        List.of(
                                new NamedComparator<>("имя", (u1, u2) -> u1.getName().compareTo(u2.getName())),
                                new NamedComparator<>("почта", (u1, u2) -> u1.getEmail().compareTo(u2.getEmail()))
                        ),
                        null,
                        "Введите почту для подсчёта кол-ва пользователей: ",
                        User::getEmail,
                        String::trim
                );
                case "0" -> {
                    System.out.println("Работа завершена.");
                    return;
                }
                default -> System.out.println("Нет такой команды, попробуйте снова.");
            }
        }
    }

    // ---------- Общий цикл меню для любого класса ----------

    private static <T, V> void runEntityMenu(
            DataFillers<T> randomFiller,
            DataFillers<T> manualFiller,
            DataFillers<T> fileFiller,
            List<NamedComparator<T>> comparators,
            Comparator<T> oddEvenOrdering,   
            String countPrompt,
            Function<T, V> getField,
            Function<String, V> valueParser) {

        CustomArrayList<T> items = new CustomArrayList<>();
        Sorter<T> sorter = new Sorter<>();
        EntityFileWriter<T> writer = new EntityFileWriter<>(Path.of("output.txt"));
        ParallelCounter<T> counter = new ParallelCounter<>();

        while (true) {
            System.out.println("""
                    1 - заполнить массив
                    2 - отсортировать
                    3 - сортировка чёт/нечёт (доп-1)
                    4 - записать результат в файл (доп-2)
                    5 - подсчитать вхождения элемента (доп-4)
                    0 - выход в выбор класса""");
            System.out.print("Ваш выбор: ");
            switch (SCANNER.nextLine().trim()) {
                case "1" -> items = fillFromMenu(randomFiller, manualFiller, fileFiller);
                case "2" -> {
                    sorter.setStrategy(chooseStrategy());
                    sorter.sort(items, chooseComparator(comparators));
                    items.forEach(System.out::println);
                }
                case "3" -> {
                    if (oddEvenOrdering == null) {
                        System.out.println("Сортировка чёт/нечёт для этого типа не поддерживается.");
                    } else {
                        System.out.println("Выберите алгоритм для сортировки чёт/нечёт:");
                        sorter.setStrategy(chooseStrategy());
                        sorter.sort(items, oddEvenOrdering);
                        items.forEach(System.out::println);
                    }
                }
                case "4" -> writer.append(items);
                case "5" -> {
                    System.out.print(countPrompt);
                    try {
                        String rawInput = SCANNER.nextLine().trim();
                        V value = valueParser.apply(rawInput);
                        long count = counter.countByField(items, getField, value);
                        System.out.println("Найдено вхождений: " + count);
                    } catch (ValidationException | IllegalStateException | IllegalArgumentException e) {
                        System.out.println("Ошибка: " + e.getMessage());
                    }
                }
                case "0" -> {
                    return;
                }
                default -> System.out.println("Нет такой команды, попробуйте снова.");
            }
        }
    }

    private static <T> CustomArrayList<T> fillFromMenu(DataFillers<T> randomFiller,
                                            DataFillers<T> manualFiller,
                                            DataFillers<T> fileFiller) {
        try {
            System.out.print("Длина массива: ");
            int size = DataValidator.validatePositiveInt(SCANNER.nextLine(), "Длина массива");
            System.out.println("1 - случайно, 2 - вручную, 3 - из файла");
            DataFillers<T> filler = switch (SCANNER.nextLine().trim()) {
                case "1" -> randomFiller;
                case "2" -> manualFiller;
                default -> fileFiller;
            };
            
            List<T> rawList = filler.fill(size);
            CustomArrayList<T> customList = new CustomArrayList<>();
            if (rawList != null) {
                for (T item : rawList) {
                    customList.add(item);
                }
            }
            return customList;
        } catch (ValidationException e) {
            System.out.println("Ошибка: " + e.getMessage());
            return new CustomArrayList<>();
        }
    }

    private static <T> SortStrategy<T> chooseStrategy() {
        System.out.println("1 - пузырьковая, 2 - вставками, 3 - слиянием");
        return switch (SCANNER.nextLine().trim()) {
            case "1" -> new BubbleSortStrategy<>();
            case "2" -> new InsertionSortStrategy<>();
            default -> new MergeSortStrategy<>();
        };
    }

    private static <T> Comparator<T> chooseComparator(List<NamedComparator<T>> options) {
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + " - " + options.get(i).name());
        }
        System.out.print("Поле: ");
        int index;
        try {
            index = Integer.parseInt(SCANNER.nextLine().trim()) - 1;
        } catch (NumberFormatException e) {
            index = options.size() - 1;
        }
        if (index < 0 || index >= options.size()) {
            index = options.size() - 1;
        }
        return options.get(index).comparator();
    }

    public record NamedComparator<T>(String name, Comparator<T> comparator) {
    }

    private static int readInt() {
        while (!SCANNER.hasNextInt()) {
            System.out.print("  Ошибка! Введите целое число: ");
            SCANNER.next();
        }
        int val = SCANNER.nextInt();
        SCANNER.nextLine();
        return val;
    }

    private static double readDouble() {
        while (!SCANNER.hasNextDouble()) {
            System.out.print("  Ошибка! Введите дробное число (через запятую/точку): ");
            SCANNER.next();
        }
        double val = SCANNER.nextDouble();
        SCANNER.nextLine();
        return val;
    }

    private static long readLong() {
        while (!SCANNER.hasNextLong()) {
            System.out.print("  Ошибка! Введите целое число: ");
            SCANNER.next();
        }
        long val = SCANNER.nextLong();
        SCANNER.nextLine();
        return val;
    }
}
