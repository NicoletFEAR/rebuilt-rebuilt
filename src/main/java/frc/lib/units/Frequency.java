package frc.lib.units;

public class Frequency extends Quantity<Frequency, FrequencyUnit> {
    Frequency(double value) {
        super(value);
    }

    public Frequency plus(Frequency other) {
        return new Frequency(value + other.value);
    }

    public Frequency minus(Frequency other) {
        return new Frequency(value - other.value);
    }

    public Frequency times(double other) {
        return new Frequency(value * other);
    }

    public Frequency div(double other) {
        return new Frequency(value / other);
    }

    public Frequency negative() {
        return new Frequency(-value);
    }

    public Time reciprocal() {
        return new Time(1.0 / value);
    }
}
