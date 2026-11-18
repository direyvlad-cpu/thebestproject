package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ParallelCounter {

    //Многопоточный подсчет студентов по номеру группы через ExecutorService.
    public static long countStudentsByGroup (CustomArrayList<Student> students, int groupNumber) {
        if (students == null || students.isEmpty()) { //переписать проверку?
            System.out.println("Студенты отсутствуют в группе: " + groupNumber); //надо ли?
            return 0;
        }

        int countThreads = calculateThreadCount(students.size());
        ExecutorService executor = Executors.newFixedThreadPool(countThreads);

        try {
            List <Future<Long>> futures = submitTasks(executor, students, groupNumber, countThreads);
            long totalCount = aggregateResults(futures);

            printResult(groupNumber, totalCount);

            return totalCount;
        } finally {
            executor.shutdown(); // Обязательное закрытие пула потоков
        }
    }

    //Расчет оптимального количества потоков (не больше, чем ядер процессора или элементов в списке)
    private static int calculateThreadCount(int countStudents) {
        int countProcessors = Runtime.getRuntime().availableProcessors();
        return Math.min(countProcessors, countStudents);
    }

    //Метод распределения задач
    private static List<Future<Long>> submitTasks (ExecutorService executor, CustomArrayList<Student> students,
                                                   int groupNumber, int countThreads) {
        //список для хранения ссылок на будущие результаты выполнения потоков
        List<Future<Long>> futures = new ArrayList<>();
        int chunkSize = calculateChunkSize(students.size(), countThreads);

        //цикл создает ровно столько задач, сколько у нас запланировано потоков.
        for (int i = 0; i < countThreads; i++ ) {
            final int start = i * chunkSize;
            final int end = Math.min(start + chunkSize, students.size());

            if (start >= students.size()) break;

            // Каждая задача считает студентов с нужным номером группы на своем отрезке
            Callable<Long> task = () -> countInRange(students, start, end, groupNumber);
            //отправляем задачу на выполнение в пул потоков и сохраняем объект Future в список.
            futures.add(executor.submit(task));
        }
        return futures;
    }

    //Делим общий размер коллекции на количество потоков с округлением вверх
    private static int calculateChunkSize (int listSize, int countThreads) {
        return (int) Math.ceil((double) listSize / countThreads);
    }

    //Метод локального подсчета кол-ва студентов в одном потоке
    private static long countInRange(CustomArrayList<Student> students,
                                     int start, int end, int groupNumber) {
        long localCount = 0;
        for (int i = start; i < end; i++) {
            Student student = students.get(i);
            // Проверяем, что студент существует и его группа совпадает с искомой
            if (student != null && student.getGroupNumber() == groupNumber) {
                localCount++;
            }
        }
        return localCount;
    }

    private static long aggregateResults(List<Future<Long>> futures) {
        long totalCount = 0;
        try {
            //проходим по всем сохраненным задачам Future.
            for (Future<Long> future : futures) {
                //метод future.get() заставляет главный поток ждать,
                //пока соответствующий дочерний поток завершит свою работу и вернет свой результат (localCount).
                totalCount += future.get(); // Ожидание завершения потока и получение результата
            }
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            System.out.println("Ошибка при многопоточных вычислениях: " + e.getMessage());
        }

        return totalCount;
    }

    private static void printResult(int groupNumber, long totalCount) {
        System.out.println("Количество студентов в группе " + groupNumber + ": " + totalCount);
    }
}
