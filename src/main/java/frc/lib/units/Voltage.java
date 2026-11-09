package frc.lib.units;

public class Voltage extends Quantity<Voltage, VoltageUnit> {
    Voltage(double value) {
        super(value, "V");
    }

    public Voltage max(Voltage other) {
        if (value < other.value) {
            return other;
        } else {
            return this;
        }
    }

    public Voltage min(Voltage other) {
        if (value > other.value) {
            return other;
        } else {
            return this;
        }
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

    public Voltage ceil() {
        return new Voltage(Math.ceil(value));
    }

    public Voltage floor() {
        return new Voltage(Math.floor(value));
    }

    public Resistance div(Current other) {
        return new Resistance(value / other.value);
    }

    public Current div(Resistance other) {
        return new Current(value / other.value);
    }

    public MagneticFluxPerAngle div(AngularVelocity other) {
        return new MagneticFluxPerAngle(value / other.value);
    }

    public AngularVelocity div(MagneticFluxPerAngle other) {
        return new AngularVelocity(value / other.value);
    }
}
