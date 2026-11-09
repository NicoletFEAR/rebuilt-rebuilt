package frc.lib.units;

public class Torque extends Quantity<Torque, TorqueUnit> {
    Torque(double value) {
        super(value, "N⋅m");
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

    public ActionPerAngle div(AngularVelocity other) {
        return new ActionPerAngle(value / other.value);
    }
}
