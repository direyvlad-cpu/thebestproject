package Project.model;

import java.util.Comparator;

public final class BusComparators {

    private BusComparators() { }

    public static final Comparator<Bus> BY_NUMBER =
            Comparator.comparing(Bus::getNumber);

    public static final Comparator<Bus> BY_MODEL =
            Comparator.comparing(Bus::getModel);

    public static final Comparator<Bus> BY_MILEAGE =
            Comparator.comparingDouble(Bus::getMileage);
}
