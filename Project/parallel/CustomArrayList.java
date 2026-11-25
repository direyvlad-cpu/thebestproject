package parallel;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;

public class CustomArrayList<T> extends AbstractList <T> {
    private static final Logger LOGGER = Logger.getLogger(CustomArrayList.class.getSimpleName());
    private static final int DEFAULT_CAPACITY = 10; //размер по умолчанию

    private Object[] elements;
    private int size;

    public CustomArrayList() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        //точно левел FINE нужен?
        LOGGER.log(Level.FINE, "Создана коллекция с ёмкостью {0}", DEFAULT_CAPACITY);
    }

    public CustomArrayList(int initialCapacity) {
        if (initialCapacity <= 0) {
            LOGGER.log(Level.WARNING,
                    "Передана отрицательная или нулевая начальная ёмкость: {0}", initialCapacity);
            throw new IllegalArgumentException(
                    "Начальный размер коллекции должен быть больше нуля: " + initialCapacity);
        }
        int capacity = Math.max(initialCapacity, DEFAULT_CAPACITY);
        this.elements = new Object[capacity];
        this.size = 0;
        LOGGER.log(Level.FINE, "Создана коллекция с размером {0}", capacity);

    }

    @Override
    public boolean add(T element) {
        if (element == null) {
            LOGGER.log(Level.WARNING, "Попытка добавить null-элемент в коллекцию");
        }
        // Автоматическое динамическое расширение массива при заполнении
        if(size == elements.length) {
            elements = Arrays.copyOf(elements, elements.length * 2);
            LOGGER.log(Level.FINE, "Массив расширен до размера {0}", elements.length);
        }
        elements[size++] = element; //тут увеличивается размер size
        return true;
    }

    @Override
    //аннотация, чтобы сказать компилятору, что мы проконтролировали возвращаемые типы
    //если её не будет - return будет гореть жёлтым
    @SuppressWarnings("unchecked")
    public T set(int index, T element) {
        checkIndex(index);
        T oldValue = (T) elements[index];
        elements[index] = element;
        LOGGER.log(Level.FINE, "Обновлён элемент по индексу {0}", index);
        //Возвращать oldValue требует сам контракт интерфейса java.util.List!
        return oldValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T remove(int index) {
        checkIndex(index);
        T oldValue = (T) elements[index]; //сохраняет удаляемый элемент в переменную oldValue
        int numMoved = size - index - 1; // сколько элементов сдвигаем влево
        //numMoved > 0 - Проверяет, есть ли вообще элементы после index.
        //Если удаляли последний элемент (index == size - 1), то numMoved будет 0, и сдвигать ничего не нужно
        if (numMoved > 0) { //освободившемуся последнему индексу присваивается null
            System.arraycopy(elements,index + 1,elements, index, numMoved);
        }
        elements[--size] = null;
        LOGGER.log(Level.FINE,"Удалён элемент по индексу {0}", index);
        //Возвращать oldValue требует сам контракт интерфейса java.util.List
        return oldValue;
    }

    @Override
    //аннотация, чтобы сказать компилятору, что мы проконтролировали возвращаемые типы
    //если её не будет - return будет гореть жёлтым
    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndex(index);
        return (T) elements[index];
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            LOGGER.log(Level.WARNING, "Некорректный индекс: {0}", index);
            throw new IndexOutOfBoundsException("Индекс " + index + ", Размер: " + size);
        }
    }

    @Override
    public int size() {
        return size;
    }

    // Кастомный коллектор для интеграции со Stream API:
    // метод позволяет собирать элементы стрима в кастомную коллекцию
    // Т - здесь тип входящих элементов стрима
    // ? - сам CustomArrayList как wild Card
    // CustomArrayList<T> - итоговый результат сборки
    // return Collector.of(...) — создаёт кастомный Collector из переданных функций
    public static <T> Collector<T, ?, CustomArrayList<T>> toCustomList() {
        return Collector.of(() -> new CustomArrayList<T>(), //заменить через ссылку на конструктор
                //supplier(поставщик): лямбда, кот создаёт новый пустой CustomArrayList<T> для каждого потока/контейнера при сборе элементов.
                (container, element) -> container.add(element), //заменить ссылкой добавление элементов в список
                (left, right) -> { //лямбда, которая склеивает два списка left и right
                    for (int i = 0; i < right.size; i++) {
                        left.add(right.get(i));
                    }
                    LOGGER.log(Level.FINE, "Объединены два списка, размер {0}", left.size);
                    return left;
                });
    }
}
