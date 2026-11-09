package frc.lib.units;

public class AngularAcceleration extends Quantity<AngularAcceleration, AngularAccelerationUnit> {
    AngularAcceleration(double value) {
        super(value, "rad/s²");
    }

    public AngularAcceleration max(AngularAcceleration other) {
        if (value < other.value) {
            return other;
        } else {
            return this;
        }
    }

    public AngularAcceleration min(AngularAcceleration other) {
        if (value > other.value) {
            return other;
        } else {
            return this;
        }
    }

    public AngularAcceleration plus(AngularAcceleration other) {
        return new AngularAcceleration(value + other.value);
    }

    public AngularAcceleration minus(AngularAcceleration other) {
        return new AngularAcceleration(value - other.value);
    }

    public AngularAcceleration times(double other) {
        return new AngularAcceleration(value * other);
    }

    public AngularAcceleration div(double other) {
        return new AngularAcceleration(value / other);
    }

    public AngularAcceleration negative() {
        return new AngularAcceleration(-value);
    }

    public AngularAcceleration ceil() {
        return new AngularAcceleration(Math.ceil(value));
    }

    public AngularAcceleration floor() {
        return new AngularAcceleration(Math.floor(value));
    }

    public Torque times(MomentOfInertia other) {
        return new Torque(value * other.value);
    }

    public AngularVelocity times(Time other) {
        return new AngularVelocity(value * other.value);
    }
}
