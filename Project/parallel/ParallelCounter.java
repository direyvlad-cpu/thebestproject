package parallel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParallelCounter {
    //МОЖЕТ СДЕЛАТЬ ЛОГИРОВАНИЕ В INFO???
    private static final Logger LOGGER = Logger.getLogger(ParallelCounter.class.getSimpleName());
    //Многопоточный подсчет кол-ва вхождений N элемента в список
    public static <T> long calculateAmountByObjects(CustomArrayList<T> elements, T object) {
        if (elements == null ) { //переписать проверку?
            LOGGER.log(Level.WARNING, "Передан null вместо списка для подсчёта вхождений");
            throw new IllegalArgumentException("Список не должен быть null");
        }

        if (elements.isEmpty()) {
            LOGGER.log(Level.FINE, "Список пуст, количество вхождений: 0");
            return 0L;
        }

        int countThreads = calculateThreadCount(elements.size());

        LOGGER.log(Level.FINE, "Запуск подсчёта вхождений");
        ExecutorService executor = Executors.newFixedThreadPool(countThreads);

        try {
            List <Future<Long>> futures = submitTasks(executor, elements, object, countThreads);
            long totalCount = aggregateResults(futures);

            //Этот лог делаем уровня INFO, чтобы выводился на консоль
            LOGGER.log(Level.INFO, "Найдено вхождений объекта {0}: {1}",
                    new Object[]{object, totalCount});

            return totalCount;
        } finally {
            executor.shutdown(); // Обязательное закрытие пула потоков
        }
    }

    //Расчет оптимального количества потоков (не больше, чем ядер процессора или элементов в списке)
    private static int calculateThreadCount(int size) {
        int countProcessors = Runtime.getRuntime().availableProcessors();
        return Math.min(countProcessors, size);
    }

    //Метод распределения задач
    private static <T> List<Future<Long>> submitTasks (ExecutorService executor, CustomArrayList<T> elements,
                                                   T object, int countThreads) {
        //список для хранения ссылок на будущие результаты выполнения потоков
        List<Future<Long>> futures = new ArrayList<>();
        int chunkSize = calculateChunkSize(elements.size(), countThreads);

        //цикл создает ровно столько задач, сколько у нас запланировано потоков.
        for (int i = 0; i < countThreads; i++ ) {
            final int start = i * chunkSize;
            final int end = Math.min(start + chunkSize, elements.size());

            if (start >= elements.size()) break;

            // Каждая задача считает кол-во вхождений по заданному параметру на своем отрезке
            Callable<Long> task = () -> countInRange(elements, start, end, object);
            //отправляем задачу на выполнение в пул потоков и сохраняем объект Future в список.
            futures.add(executor.submit(task));
        }
        return futures;
    }

    //Делим общий размер коллекции на количество потоков с округлением вверх
    private static int calculateChunkSize (int listSize, int countThreads) {
        return (int) Math.ceil((double) listSize / countThreads);
    }

    //Метод локального подсчета кол-ва совпадений по объектам в одном потоке
    private static <T> long countInRange(CustomArrayList<T> elements,
                                     int start, int end, T object) {
        long localCount = 0;
        for (int i = start; i < end; i++) {
            T current = elements.get(i);
            // Проверяем, что объекты совпадают
            if (Objects.equals(current, object)) {
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
            LOGGER.log(Level.WARNING, "Ошибка при многопоточных вычислениях или поток был прерван", e);
            throw new IllegalStateException("Ошибка при многопоточных вычислениях или поток был прерван", e);
        }
        return totalCount;
    }
}
