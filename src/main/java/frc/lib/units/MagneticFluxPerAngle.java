package frc.lib.units;

public class MagneticFluxPerAngle extends Quantity<MagneticFluxPerAngle, MagneticFluxPerAngleUnit> {
    MagneticFluxPerAngle(double value) {
        super(value);
    }

    public MagneticFluxPerAngle plus(MagneticFluxPerAngle other) {
        return new MagneticFluxPerAngle(value + other.value);
    }

    public MagneticFluxPerAngle minus(MagneticFluxPerAngle other) {
        return new MagneticFluxPerAngle(value - other.value);
    }

    public MagneticFluxPerAngle times(double other) {
        return new MagneticFluxPerAngle(value * other);
    }

    public MagneticFluxPerAngle div(double other) {
        return new MagneticFluxPerAngle(value / other);
    }

    public MagneticFluxPerAngle negative() {
        return new MagneticFluxPerAngle(-value);
    }
}
