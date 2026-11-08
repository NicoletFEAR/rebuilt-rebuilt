package frc.lib.units;

public class Velocity extends Quantity<Velocity, VelocityUnit> {
    Velocity(double value) {
        super(value);
    }

    public Velocity plus(Angle other) {
        return new Velocity(value + other.value);
    }

    public Velocity minus(Angle other) {
        return new Velocity(value - other.value);
    }

    public Velocity times(double other) {
        return new Velocity(value * other);
    }

    public Velocity div(double other) {
        return new Velocity(value / other);
    }

    public Velocity negative() {
        return new Velocity(-value);
    }
}
