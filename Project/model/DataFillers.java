package model;

import java.util.List;

//Нужен ли этот интерфейс
public interface DataFillers<T> {
    List<T> fill (int size);
}
