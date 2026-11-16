package main;

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {

        //поля инициализации

        while (true) {
            System.out.println("""
                    
                    1 - заполнить массив
                    2 - отсортировать
                    3 - сортировка чёт/нечёт (доп-1)
                    4 - записать результат в файл (доп-2)
                    5 - подсчитать вхождения элемента (доп-4)
                    0 - выход""");
            System.out.print("Ваш выбор: ");
            switch (SCANNER.nextLine().trim()) {
                case "1" -> students = fillFromMenu();
                case "2" -> {
                    sorter.setStrategy(chooseStrategy());
                    sorter.sort(students, chooseField());
                    students.forEach(System.out::println);
                }
                case "3" -> {
                    sorter.setStrategy(new MergeSortStrategy<>());
                    sorter.sort(students, OddEvenOrdering.byAverageGrade());
                    students.forEach(System.out::println);
                }
                case "4" -> writer.append(students);
                case "5" -> {
                    System.out.print("Введите номер зачётки для поиска: ");
                    long target = DataValidator.validateGradeBookNumber(SCANNER.nextLine());
                    counter.count(students,
                            new Student.Builder().gradeBookNumber(target)
                                    .groupNumber(1).averageGrade(0).build(),
                            (a, b) -> a.getGradeBookNumber() == b.getGradeBookNumber(),
                            4);
                }
                case "0" -> {
                    System.out.println("Работа завершена.");
                    return;
                }
                default -> System.out.println("Нет такой команды, попробуйте снова.");
            }
        }
    }

    private static List<Student> fillFromMenu() {
        System.out.print("Длина массива: ");
        int size = DataValidator.validatePositiveInt(SCANNER.nextLine(), "Длина массива");
        System.out.println("1 - случайно, 2 - вручную, 3 - из файла");
        DataFiller<Student> filler = switch (SCANNER.nextLine().trim()) {
            case "1" -> new RandomStudentFiller();
            case "2" -> new ManualStudentFiller();
            default -> new StudentFileFiller(Path.of("input.txt"));
        };
        try {
            return filler.fill(size);
        } catch (ValidationException e) {
            System.out.println("Ошибка: " + e.getMessage());
            return new CustomArrayList<>();
        }
    }

    private static SortStrategy<Student> chooseStrategy() {
        System.out.println("1 - пузырьковая, 2 - вставками, 3 - слиянием");
        return switch (SCANNER.nextLine().trim()) {
            case "1" -> new BubbleSortStrategy<>();
            case "2" -> new InsertionSortStrategy<>();
            default -> new MergeSortStrategy<>();
        };
    }

    private static Comparator<Student> chooseField() {
        System.out.println("Поле: 1 - группа, 2 - средний балл, 3 - зачётка");
        return switch (SCANNER.nextLine().trim()) {
            case "1" -> StudentComparators.BY_GROUP;
            case "2" -> StudentComparators.BY_GRADE;
            default -> StudentComparators.BY_BOOK;
        };
    }
}
