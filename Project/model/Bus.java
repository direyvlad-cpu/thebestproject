package model;

public class Bus implements Comparable<Bus> {

    private final String number;
    private final String model;
    private final double mileage;

    private Bus(Builder builder) {
        this.number = builder.number;
        this.model = builder.model;
        this.mileage = builder.mileage;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getNumber() {
        return number;
    }

    public String getModel() {
        return model;
    }

    public double getMileage() {
        return mileage;
    }

    @Override
    public int compareTo(Bus other) {
        int modelComparison = this.model.compareTo(other.model);
        if (modelComparison != 0) {
            return modelComparison;
        }
        // Если модели одинаковые — сравниваем по пробегу
        return Double.compare(this.mileage, other.mileage);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bus bus = (Bus) o;
        return Double.compare(bus.mileage, mileage) == 0
                && number.equals(bus.number)
                && model.equals(bus.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, model, mileage);
    }

    @Override
    public String toString() {
        return "Bus{номер=" + number
                + ", модель=" + model
                + ", пробег=" + mileage + "}";
    }

    public String toFileString() {
        return number + ";" + model + ";" + mileage;
    }

    public static class Builder {
        private String number;
        private String model;
        private double mileage = -1;

        private Builder() {
        }

        public Builder number(String value) {
            this.number = value;
            return this;
        }

        public Builder model(String value) {
            this.model = value;
            return this;
        }

        public Builder mileage(double value) {
            this.mileage = value;
            return this;
        }

        public Bus build() {
            if (number == null || number.isBlank()
                    || model == null || model.isBlank()
                    || mileage < 0) {
                throw new IllegalStateException(
                        "Не заполнены обязательные поля: номер, модель и пробег");
            }
            return new Bus(this);
        }
    }
}
