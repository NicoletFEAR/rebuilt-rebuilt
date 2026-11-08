package frc.lib.units;

public class Current extends Quantity<Current, CurrentUnit> {
    Current(double value) {
        super(value);
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

    public Torque times(MagneticFlux other) {
        return new Torque(value * other.value);
    }
}
