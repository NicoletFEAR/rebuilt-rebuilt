package frc.lib.units;

public class Current extends Quantity<Current, CurrentUnit> {
    Current(double value) {
        super(value, "A");
    }

    public Current max(Current other) {
        if (value < other.value) {
            return other;
        } else {
            return this;
        }
    }

    public Current min(Current other) {
        if (value > other.value) {
            return other;
        } else {
            return this;
        }
    }

    public Current plus(Current other) {
        return new Current(value + other.value);
    }

    public Current minus(Current other) {
        return new Current(value - other.value);
    }

    public Current times(double other) {
        return new Current(value * other);
    }

    public Current div(double other) {
        return new Current(value / other);
    }

    public Current negative() {
        return new Current(-value);
    }

    public Current ceil() {
        return new Current(Math.ceil(value));
    }

    public Current floor() {
        return new Current(Math.floor(value));
    }

    public Torque times(MagneticFlux other) {
        return new Torque(value * other.value);
    }

    public Voltage times(Resistance other) {
        return new Voltage(value * other.value);
    }
}
