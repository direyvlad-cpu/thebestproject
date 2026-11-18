package model;

import java.util.Random;
import java.util.stream.Stream;

//Здесь указала конкретный тип Student вместо T - надо?
public class RandomStudentFiller implements DataFiller<Student> {
    private final Random random = new Random();

    @Override
    public CustomArrayList<Student> fill (int size) {
        // Заполняем коллекцию через Stream.generate и наш кастомный коллектор
    return Stream.generate(() -> new Student.Builder()
                    .groupNumber(100 + random.nextInt(3)) // Группы 100, 101, 102
                    .averageGrade((double) Math.round((2.0 + random.nextDouble() * 3) * 10) / 10) // Балл от 2.0 до 5.0
                    .gradeBookNumber(3000 + random.nextInt(9000)) // Уникальная зачётка
                    .build())
            .limit(size)
            .collect(CustomArrayList.toCustomList());
    }

}
