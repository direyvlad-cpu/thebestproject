package Project.model;

import java.util.*;

class RandomCreate implements DataFiller
{
    @Override
    public List<Student> getStudents(int size) 
    {
        List<Student> students = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < size; i++)
        {
           int groupNumber = random.nextInt(900) + 100; 
           double averageGrade = 2.0 + (random.nextDouble() * 3.0); 
           long grageBookNumber = 100000L + (Math.abs(random.nextLong()) % 900000L);
           
           Student student = Student.builder()
                   .groupNumber(groupNumber)
                   .averageGrade(averageGrade)
                   .gradeBookNumber(grageBookNumber)
                   .build();
            students.add(student);
        }
        return students;
    }
}
