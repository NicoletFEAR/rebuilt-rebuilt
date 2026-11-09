package frc.lib.units;

public class Voltage extends Quantity<Voltage, VoltageUnit> {
    Voltage(double value) {
        super(value, "V");
    }

    public Voltage plus(Voltage other) {
        return new Voltage(value + other.value);
    }

    public Voltage minus(Voltage other) {
        return new Voltage(value - other.value);
    }

    public Voltage times(double other) {
        return new Voltage(value * other);
    }

    public Voltage div(double other) {
        return new Voltage(value / other);
    }

    public Voltage negative() {
        return new Voltage(-value);
    }

    public Resistance div(Current other) {
        return new Resistance(value / other.value);
    }
}
