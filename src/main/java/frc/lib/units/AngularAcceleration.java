package frc.lib.units;

public class AngularAcceleration extends Quantity<AngularAcceleration, AngularAccelerationUnit> {
    AngularAcceleration(double value) {
        super(value, "rad/s²");
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
}
