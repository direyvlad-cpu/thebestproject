package main;

import model.DataFiller;
import model.DataValidator;
import model.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

/** Универсальное заполнение коллекции с консоли. Ошибочный ввод повторяется. */
public class ManualFiller<T> implements DataFiller<T> {
    private final Scanner scanner;
    private final Function<Scanner, T> entityReader;

    public ManualFiller(Scanner scanner, Function<Scanner, T> entityReader) {
        if (scanner == null || entityReader == null) {
            throw new ValidationException("Scanner и обработчик ввода не должны быть null.");
        }
        this.scanner = scanner;
        this.entityReader = entityReader;
    }

    @Override
    public List<T> fill(int size) {
        DataValidator.validatePositiveInt(size, "Размер коллекции");
        List<T> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            System.out.println("\nЗапись " + (i + 1) + " из " + size + ":");
            T item = entityReader.apply(scanner);
            if (item == null) {
                throw new ValidationException("Обработчик ручного ввода вернул null.");
            }
            result.add(item);
        }
        return result;
    }
}
