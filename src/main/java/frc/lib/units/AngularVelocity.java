package frc.lib.units;

public class AngularVelocity extends Quantity<AngularVelocity, AngularVelocityUnit> {
    AngularVelocity(double value) {
        super(value, "rad/s");
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
}
