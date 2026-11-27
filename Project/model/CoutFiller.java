package model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Универсальная стратегия заполнения коллекции через ручной ввод.
 * Реализует паттерн "Стратегия" (DataFiller) на дженериках.
 */
public class CoutFiller<Y> implements DataFillers<Y> {
    
    // Поставщик (рецепт), который умеет запрашивать данные в консоли 
    // и возвращать готовый объект (Студента, Автобус или Юзера)
    private final Supplier<Y> consoleSupplier;

    /**
     * Конструктор стратегии ручного ввода.
     * 
     * @param consoleSupplier Лямбда-выражение, внутри которого написан Scanner и валидация
     */
    public CoutFiller(Supplier<Y> consoleSupplier) {
        this.consoleSupplier = consoleSupplier;
    }

    @Override
    public List<Y> fill(int size) {
        List<Y> list = new ArrayList<>();
        System.out.println("\n--- Запуск ручного ввода объектов ---");
        
        for (int i = 0; i < size; i++) {
            System.out.println("Ввод данных для объекта " + (i + 1) + " из " + size + ":");
            
            // Вызываем инструкцию ввода. Программа перейдет в Main, 
            // считает данные, проверит их и вернет сюда готовый объект Y.
            Y object = consoleSupplier.get();
            
            list.add(object);
        }
        
        return list;
    }
}
