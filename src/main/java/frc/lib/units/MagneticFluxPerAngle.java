package frc.lib.units;

public class MagneticFluxPerAngle extends Quantity<MagneticFluxPerAngle, MagneticFluxPerAngleUnit> {
    MagneticFluxPerAngle(double value) {
        super(value, "Wb/rad");
    }

    public MagneticFluxPerAngle max(MagneticFluxPerAngle other) {
        if (value < other.value) {
            return other;
        } else {
            return this;
        }
    }

    public MagneticFluxPerAngle min(MagneticFluxPerAngle other) {
        if (value > other.value) {
            return other;
        } else {
            return this;
        }
    }

    public MagneticFluxPerAngle clamp(MagneticFluxPerAngle low, MagneticFluxPerAngle high) {
        return max(low).min(high);
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

    public MagneticFluxPerAngle ceil() {
        return new MagneticFluxPerAngle(Math.ceil(value));
    }

    public MagneticFluxPerAngle floor() {
        return new MagneticFluxPerAngle(Math.floor(value));
    }

    public Voltage times(AngularVelocity other) {
        return new Voltage(value * other.value);
    }
}
