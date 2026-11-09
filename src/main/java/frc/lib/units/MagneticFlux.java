package frc.lib.units;

public class MagneticFlux extends Quantity<MagneticFlux, MagneticFluxUnit> {
    MagneticFlux(double value) {
        super(value, "Wb");
    }

    public MagneticFlux max(MagneticFlux other) {
        if (value < other.value) {
            return other;
        } else {
            return this;
        }
    }

    public MagneticFlux min(MagneticFlux other) {
        if (value > other.value) {
            return other;
        } else {
            return this;
        }
    }

    public MagneticFlux plus(MagneticFlux other) {
        return new MagneticFlux(value + other.value);
    }

    public MagneticFlux minus(MagneticFlux other) {
        return new MagneticFlux(value - other.value);
    }

    public MagneticFlux times(double other) {
        return new MagneticFlux(value * other);
    }

    public MagneticFlux div(double other) {
        return new MagneticFlux(value / other);
    }

    public MagneticFlux negative() {
        return new MagneticFlux(-value);
    }

    public MagneticFlux ceil() {
        return new MagneticFlux(Math.ceil(value));
    }

    public MagneticFlux floor() {
        return new MagneticFlux(Math.floor(value));
    }

    public Torque times(Current other) {
        return new Torque(value * other.value);
    }
}
