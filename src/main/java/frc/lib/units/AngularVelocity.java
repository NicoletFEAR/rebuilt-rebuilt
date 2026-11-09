package frc.lib.units;

public class AngularVelocity extends Quantity<AngularVelocity, AngularVelocityUnit> {
    AngularVelocity(double value) {
        super(value, "rad/s");
    }

    public AngularVelocity max(AngularVelocity other) {
        if (value < other.value) {
            return other;
        } else {
            return this;
        }
    }

    public AngularVelocity min(AngularVelocity other) {
        if (value > other.value) {
            return other;
        } else {
            return this;
        }
    }

    public AngularVelocity plus(AngularVelocity other) {
        return new AngularVelocity(value + other.value);
    }

    public AngularVelocity minus(AngularVelocity other) {
        return new AngularVelocity(value - other.value);
    }

    public AngularVelocity times(double other) {
        return new AngularVelocity(value * other);
    }

    public AngularVelocity div(double other) {
        return new AngularVelocity(value / other);
    }

    public AngularVelocity negative() {
        return new AngularVelocity(-value);
    }

    public AngularVelocity ceil() {
        return new AngularVelocity(Math.ceil(value));
    }

    public AngularVelocity floor() {
        return new AngularVelocity(Math.floor(value));
    }

    public Voltage times(MagneticFluxPerAngle other) {
        return new Voltage(value * other.value);
    }

    public Torque times(ActionPerAngle other) {
        return new Torque(value * other.value);
    }

    public AngularAcceleration div(Time other) {
        return new AngularAcceleration(value / other.value);
    }

    public Time div(AngularAcceleration other) {
        return new Time(value / other.value);
    }

    public Angle times(Time other) {
        return new Angle(value * other.value);
    }
}
