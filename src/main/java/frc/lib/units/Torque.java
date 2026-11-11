package frc.lib.units;

public class Torque extends Quantity<Torque, TorqueUnit> {
    Torque(double value) {
        super(value, "N⋅m");
    }

    public Torque max(Torque other) {
        if (value < other.value) {
            return other;
        } else {
            return this;
        }
    }

    public Torque min(Torque other) {
        if (value > other.value) {
            return other;
        } else {
            return this;
        }
    }

    public Torque clamp(Torque low, Torque high) {
        return max(low).min(high);
    }

    public Torque plus(Torque other) {
        return new Torque(value + other.value);
    }

    public Torque minus(Torque other) {
        return new Torque(value - other.value);
    }

    public Torque times(double other) {
        return new Torque(value * other);
    }

    public Torque div(double other) {
        return new Torque(value / other);
    }

    public Torque negative() {
        return new Torque(-value);
    }

    public Torque ceil() {
        return new Torque(Math.ceil(value));
    }

    public Torque floor() {
        return new Torque(Math.floor(value));
    }

    public ActionPerAngle div(AngularVelocity other) {
        return new ActionPerAngle(value / other.value);
    }

    public AngularVelocity div(ActionPerAngle other) {
        return new AngularVelocity(value / other.value);
    }

    public Current div(MagneticFlux other) {
        return new Current(value / other.value);
    }

    public MagneticFlux div(Current other) {
        return new MagneticFlux(value / other.value);
    }

    public AngularAcceleration div(MomentOfInertia other) {
        return new AngularAcceleration(value / other.value);
    }

    public MomentOfInertia div(AngularAcceleration other) {
        return new MomentOfInertia(value / other.value);
    }
}
