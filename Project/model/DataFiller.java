package model;

import java.util.List;

//Нужен ли этот интерфейс
public interface DataFiller<T> {
    List<T> fill (int size);
}
