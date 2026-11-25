package main;

import model.Bus;
import model.BusComparators;
import model.DataFiller;
import model.EntityInput;
import model.EntityParser;
import model.FileFiller;
import model.ManualFiller;
import model.RandomFiller;
import model.Student;
import model.StudentComparators;
import model.User;
import model.UserComparators;
import parallel.CustomArrayList;
import parallel.ParallelCounter;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);

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
                        new RandomFiller<>(),
                        new ManualFiller<>(SCANNER, EntityInput::readStudent),
                        new FileFiller<>(Path.of("input.txt"), EntityParser::parseStudent),
                        List.of(
                                new NamedComparator<>("группа", StudentComparators.BY_GROUP),
                                new NamedComparator<>("средний балл", StudentComparators.BY_GRADE),
                                new NamedComparator<>("зачётка", StudentComparators.BY_BOOK)),
                        OddEvenOrdering.byAverageGrade(),
                        "Введите номер группы для подсчёта кол-ва студентов: ",
                        Student::getGroupNumber,
                        Integer::parseInt);
                case "2" -> runEntityMenu(
                        new RandomFiller<>(),
                        new ManualFiller<>(SCANNER, EntityInput::readBus),
                        new FileFiller<>(Path.of("input_bus.txt"), EntityParser::parseBus),
                        List.of(
                                new NamedComparator<>("номер", BusComparators.BY_NUMBER),
                                new NamedComparator<>("модель", BusComparators.BY_MODEL),
                                new NamedComparator<>("пробег", BusComparators.BY_MILEAGE)),
                        null,
                        "Введите модель автобуса для подсчёта кол-ва: ",
                        Bus::getModel,
                        String::trim);
                case "3" -> runEntityMenu(
                        new RandomFiller<>(),
                        new ManualFiller<>(SCANNER, EntityInput::readUser),
                        new FileFiller<>(Path.of("input_user.txt"), EntityParser::parseUser),
                        List.of(
                                new NamedComparator<>("имя", UserComparators.BY_NAME),
                                new NamedComparator<>("почта", UserComparators.BY_EMAIL)),
                        null,
                        "Введите почту для подсчёта кол-ва пользователей: ",
                        User::getEmail,
                        String::trim);
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
            DataFiller<T> randomFiller,
            DataFiller<T> manualFiller,
            DataFiller<T> fileFiller,
            List<NamedComparator<T>> comparators,
            Comparator<T> oddEvenOrdering,   // null — если доп-1 не применимо
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
                        sorter.setStrategy(new MergeSortStrategy<>());
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

    private static <T> List<T> fillFromMenu(DataFiller<T> randomFiller,
                                            DataFiller<T> manualFiller,
                                            DataFiller<T> fileFiller) {
        try {
            System.out.print("Длина массива: ");
            int size = DataValidator.validatePositiveInt(SCANNER.nextLine(), "Длина массива");
            System.out.println("1 - случайно, 2 - вручную, 3 - из файла");
            DataFiller<T> filler = switch (SCANNER.nextLine().trim()) {
                case "1" -> randomFiller;
                case "2" -> manualFiller;
                default -> fileFiller;
            };
            return filler.fill(size);
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

    // Именованный компаратор: название пункта меню + сам компаратор
    public record NamedComparator<T>(String name, Comparator<T> comparator) {
    }
}