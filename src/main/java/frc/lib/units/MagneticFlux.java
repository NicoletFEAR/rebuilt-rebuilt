package frc.lib.units;

public class MagneticFlux extends Quantity<MagneticFlux, MagneticFluxUnit> {
    MagneticFlux(double value) {
        super(value, "Wb");
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

    public Torque times(Current other) {
        return new Torque(value / other.value);
    }
}
