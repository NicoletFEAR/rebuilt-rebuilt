package frc.lib.units;

public class Time extends Quantity<Time, TimeUnit> {
    Time(double value) {
        super(value);
    }

    public Time plus(Time other) {
        return new Time(value + other.value);
    }

    public Time minus(Time other) {
        return new Time(value - other.value);
    }

    public Time times(double other) {
        return new Time(value * other);
    }

    public Time div(double other) {
        return new Time(value / other);
    }

    public Time negative() {
        return new Time(-value);
    }

    public Frequency reciprocal() {
        return new Frequency(1.0 / value);
    }
}
