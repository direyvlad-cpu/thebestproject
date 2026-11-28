package Project.model;

import java.util.*;
import java.util.function.Supplier;

public class RandomFiller<Y> implements DataFillers<Y>
{
    private final Supplier<Y> supplier;

    public RandomFiller(Supplier<Y> supplier) 
    {
        this.supplier = supplier;
    }

    @Override
    public List<Y> fill(int size) 
    {
        List<Y> students = new ArrayList<>();
        
       
        for (int i = 0; i < size; i++)
        {
            Y object = supplier.get();
            students.add(object);
        }
        
        return students;
    }
}
