package frc.lib.units;

public class Anglement extends Quantity<Anglement, AnglementUnit> {
    Anglement(double value) {
        super(value, "rad");
    }

    public Anglement max(Anglement other) {
        if (value < other.value) {
            return other;
        } else {
            return this;
        }
    }

    public Anglement min(Anglement other) {
        if (value > other.value) {
            return other;
        } else {
            return this;
        }
    }

    public Anglement clamp(Anglement low, Anglement high) {
        return max(low).min(high);
    }

    public Anglement plus(Anglement other) {
        return new Anglement(value + other.value);
    }

    public Anglement minus(Anglement other) {
        return new Anglement(value - other.value);
    }

    public Anglement times(double other) {
        return new Anglement(value * other);
    }

    public Anglement div(double other) {
        return new Anglement(value / other);
    }

    public Anglement negative() {
        return new Anglement(-value);
    }

    public Anglement ceil() {
        return new Anglement(Math.ceil(value));
    }

    public Anglement floor() {
        return new Anglement(Math.floor(value));
    }

    public Angle div(Time other) {
        return new Angle(value / other.value);
    }

    public Time div(Angle other) {
        return new Time(value / other.value);
    }
}
