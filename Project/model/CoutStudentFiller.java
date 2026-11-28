package Project.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


 interface DataFiller
{
    List<Student>  getStudents(int size);
}


class CoutCreate implements DataFiller
{
    @Override
    public List<Student> getStudents(int size) 
    {
        List<Student> students = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < size; i++)
        {
            System.out.println("Enter group number for student " + (i + 1) + ":");
            int groupNumber = scanner.hasNextInt() ? scanner.nextInt() : 0;
            System.out.println("Enter average grade for student " + (i + 1) + ":");
            double averageGrade = scanner.hasNextDouble() ? scanner.nextDouble() : 0.0;
            System.out.println("Enter grade book number for student " + (i + 1) + ":");
            long gradeBookNumber = scanner.hasNextLong() ? scanner.nextLong() : 0L;

            Student student = Student.builder()
                    .groupNumber(groupNumber)
                    .averageGrade(averageGrade)
                    .gradeBookNumber(gradeBookNumber)
                    .build();
            students.add(student);
        }
        return students;
    }
}

