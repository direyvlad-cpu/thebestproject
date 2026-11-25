package model;

import java.nio.file.Path;
import java.util.List;

/** Совместимая с прежним именем обёртка для чтения студентов из students.txt. */
class FileCreate implements DataFiller<Student> {
    @Override
    public List<Student> fill(int size) {
        return new FileFiller<>(Path.of("students.txt"), EntityParser::parseStudent).fill(size);
    }
}
