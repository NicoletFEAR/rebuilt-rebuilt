package frc.lib.units;

public class Resistance extends Quantity<Resistance, ResistanceUnit> {
    Resistance(double value) {
        super(value, "Ω");
    }

    public Resistance max(Resistance other) {
        if (value < other.value) {
            return other;
        } else {
            return this;
        }
    }

    public Resistance min(Resistance other) {
        if (value > other.value) {
            return other;
        } else {
            return this;
        }
    }

    public Resistance clamp(Resistance low, Resistance high) {
        return max(low).min(high);
    }

    public Resistance plus(Resistance other) {
        return new Resistance(value + other.value);
    }

    public Resistance minus(Resistance other) {
        return new Resistance(value - other.value);
    }

    public Resistance times(double other) {
        return new Resistance(value * other);
    }

    public Resistance div(double other) {
        return new Resistance(value / other);
    }

    public Resistance negative() {
        return new Resistance(-value);
    }

    public Resistance ceil() {
        return new Resistance(Math.ceil(value));
    }

    public Resistance floor() {
        return new Resistance(Math.floor(value));
    }

    public Voltage times(Current other) {
        return new Voltage(value * other.value);
    }
}
