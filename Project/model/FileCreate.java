package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;




class FileCreate implements DataFiller
{
    @Override
    public List<Student> getStudents(int size) 
    {
        List<Student> students = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("students.txt"))) 
        {
            for (int i = 0; i < size && scanner.hasNextLine(); i++) 
            {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 3) 
                {
                    int groupNumber = Integer.parseInt(parts[0].trim());
                    double averageGrade = Double.parseDouble(parts[1].trim());
                    long gradeBookNumber = Long.parseLong(parts[2].trim());

                    Student student = Student.builder()
                            .groupNumber(groupNumber)
                            .averageGrade(averageGrade)
                            .gradeBookNumber(gradeBookNumber)
                            .build();
                    students.add(student);
                }
            }
        } 
        catch (FileNotFoundException e) 
        {
            System.out.println("File not found: " + e.getMessage());
        }
        return students;
    }
}
