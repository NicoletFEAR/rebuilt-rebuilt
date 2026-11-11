package frc.lib.units;

public class Frequency extends Quantity<Frequency, FrequencyUnit> {
    Frequency(double value) {
        super(value, "Hz");
    }

    public Frequency max(Frequency other) {
        if (value < other.value) {
            return other;
        } else {
            return this;
        }
    }

    public Frequency min(Frequency other) {
        if (value > other.value) {
            return other;
        } else {
            return this;
        }
    }

    public Frequency clamp(Frequency low, Frequency high) {
        return max(low).min(high);
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

    public Frequency ceil() {
        return new Frequency(Math.ceil(value));
    }

    public Frequency floor() {
        return new Frequency(Math.floor(value));
    }

    public Time reciprocal() {
        return new Time(1.0 / value);
    }
}
