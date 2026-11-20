package main;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

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
                case "1" -> runStudents();
                case "2" -> runBuses();
                case "3" -> runUsers();
                case "0" -> {
                    System.out.println("Работа завершена.");
                    return;
                }
                default -> System.out.println("Нет такой команды, попробуйте снова.");
            }
        }
    }

    // ---------- Студенты ----------

    private static void runStudents() {
        runEntityMenu(
                new RandomStudentFiller(),
                new ManualStudentFiller(),
                new StudentFileFiller(Path.of("input.txt")),
                List.of(
                        new NamedComparator<>("группа", StudentComparators.BY_GROUP),
                        new NamedComparator<>("средний балл", StudentComparators.BY_GRADE),
                        new NamedComparator<>("зачётка", StudentComparators.BY_BOOK)),
                list -> {
                    Sorter<Student> sorter = new Sorter<>();
                    sorter.setStrategy(new MergeSortStrategy<>());
                    sorter.sort(list, OddEvenOrdering.byAverageGrade());
                    list.forEach(System.out::println);
                },
                list -> {
                    System.out.print("Введите номер группы для подсчёта кол-ва студентов: ");
                    long target = DataValidator.validateGradeBookNumber(SCANNER.nextLine());
                    Student probe = new Student.Builder()
                            .gradeBookNumber(target)
                            .groupNumber(1)
                            .averageGrade(0)
                            .build();
                    new Counter<Student>().count(list, probe,
                            (a, b) -> a.getGradeBookNumber() == b.getGradeBookNumber(), 4);
                });
    }

    // ---------- Автобусы ----------

    private static void runBuses() {
        runEntityMenu(
                new RandomBusFiller(),
                new ManualBusFiller(),
                new BusFileFiller(Path.of("input_bus.txt")),
                List.of(
                        new NamedComparator<>("номер", BusComparators.BY_NUMBER),
                        new NamedComparator<>("модель", BusComparators.BY_MODEL),
                        new NamedComparator<>("пробег", BusComparators.BY_MILEAGE)),
                null,
                list -> {
                    System.out.print("Введите номер автобуса для подсчёта кол-ва: ");
                    String target = SCANNER.nextLine().trim();
                    try {
                        Bus probe = Bus.builder()
                                .number(target)
                                .model("probe")
                                .mileage(0)
                                .build();
                        new Counter<Bus>().count(list, probe,
                                (a, b) -> a.getNumber().equals(b.getNumber()), 4);
                    } catch (IllegalStateException e) {
                        System.out.println("Ошибка: " + e.getMessage());
                    }
                });
    }

    // ---------- Пользователи ----------

    private static void runUsers() {
        runEntityMenu(
                new RandomUserFiller(),
                new ManualUserFiller(),
                new UserFileFiller(Path.of("input_user.txt")),
                List.of(
                        new NamedComparator<>("имя", UserComparators.BY_NAME),
                        new NamedComparator<>("почта", UserComparators.BY_EMAIL)),
                null,
                list -> {
                    System.out.print("Введите почту для подсчёта кол-ва пользователей: ");
                    String email = SCANNER.nextLine().trim();
                    try {
                        User probe = User.builder()
                                .name("probe")
                                .password("probe")
                                .email(email)
                                .build();
                        new Counter<User>().count(list, probe,
                                (a, b) -> a.getEmail().equals(b.getEmail()), 4);
                    } catch (IllegalStateException e) {
                        System.out.println("Ошибка: " + e.getMessage());
                    }
                });
    }

    // ---------- Общий цикл меню ----------

    private static <T> void runEntityMenu(
            DataFiller<T> randomFiller,
            DataFiller<T> manualFiller,
            DataFiller<T> fileFiller,
            List<NamedComparator<T>> comparators,
            Consumer<List<T>> oddEvenAction,   // null — если доп-1 не применимо
            Consumer<List<T>> countAction) {

        List<T> items = new CustomArrayList<>();
        Sorter<T> sorter = new Sorter<>();
        EntityFileWriter<T> writer = new EntityFileWriter<>(Path.of("output.txt"));
        Counter<T> counter = new Counter<>();

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
                    if (oddEvenAction == null) {
                        System.out.println("Сортировка чёт/нечёт доступна только для студентов.");
                    } else {
                        oddEvenAction.accept(items);
                    }
                }
                case "4" -> writer.append(items);
                case "5" -> countAction.accept(items);
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
        System.out.print("Длина массива: ");
        int size = DataValidator.validatePositiveInt(SCANNER.nextLine(), "Длина массива");
        System.out.println("1 - случайно, 2 - вручную, 3 - из файла");
        DataFiller<T> filler = switch (SCANNER.nextLine().trim()) {
            case "1" -> randomFiller;
            case "2" -> manualFiller;
            default -> fileFiller;
        };
        try {
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
